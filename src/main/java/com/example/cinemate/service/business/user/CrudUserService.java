package com.example.cinemate.service.business.user;

import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.user.*;
import com.example.cinemate.dto.user.file.UserFilesDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.user.UserFileMapper;
import com.example.cinemate.mapper.user.UserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business.common.UploadFilesAsyncService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.service.redis.UserProviderStorage;
import com.example.cinemate.utils.PaginationUtil;
import com.example.cinemate.validate.common.CommonDataValidate;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class CrudUserService {

    private final UploadFilesAsyncService uploadFilesAsyncService;
    private final AppUserService appUserService;
    private final UserRoleService userRoleService;
    private final UpdateUserService updateUserService;
    private final SaveUserService saveUserService;
    private final UserProviderStorage userProviderStorage;
    private final UserDataValidate userDataValidate;
    private final CommonDataValidate commonDataValidate;
    private final UserMapper userMapper;
    private final UserFileMapper userFileMapper;

    public CrudUserService(UploadFilesAsyncService uploadFilesAsyncService, AppUserService appUserService, UserRoleService userRoleService, UpdateUserService updateUserService, SaveUserService saveUserService, UserProviderStorage userProviderStorage, UserDataValidate userDataValidate, CommonDataValidate commonDataValidate, UserMapper userMapper, UserFileMapper userFileMapper) {
        this.uploadFilesAsyncService = uploadFilesAsyncService;
        this.appUserService = appUserService;
        this.userRoleService = userRoleService;
        this.updateUserService = updateUserService;
        this.saveUserService = saveUserService;
        this.userProviderStorage = userProviderStorage;
        this.userDataValidate = userDataValidate;
        this.commonDataValidate = commonDataValidate;
        this.userMapper = userMapper;
        this.userFileMapper = userFileMapper;
    }

    public PagedResponse<UserAdminDto> getUsers(final UserSearchParamsDto userSearchParamsDto) {
        String validSortBy = commonDataValidate.getIsFieldExists(
                userSearchParamsDto.getSortBy(),
                AppUser.class.getDeclaredFields()
        );

        userSearchParamsDto.setPage(userSearchParamsDto.getPage() - 1);
        userSearchParamsDto.setSortBy(validSortBy);

        Page<AppUser> pageUsers = appUserService.getUsers(userSearchParamsDto);

        List<Integer> userIds = pageUsers.get().map(AppUser::getId).toList();
        Map<Integer, String> providers = userProviderStorage.getProviders(userIds);  // bulk-запрос
        List<UserAdminDto> usersAdminDto = pageUsers.get()
                .map(user -> userMapper.toUserAdminDto(
                                user,
                                providers.get(user.getId()),
                                userRoleService.getRoleNames(user.getId())
                            ))
                .toList();

        return PaginationUtil.getPagedResponse(usersAdminDto, pageUsers);
    }

    public UserAdminDto getById(final Integer id) {
        AppUser appUser = appUserService.findByIdWithoutIsActive(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        String provider = userProviderStorage.getProvider(id.toString());

        List<String> roles = userRoleService.getRoleNames(appUser.getId());

        return userMapper.toUserAdminDto(appUser, provider, roles);
    }

    @Transactional
    public void updateById(final Integer id, final UserUpdateAdminDto userUpdateAdminDto) {
        Logger.info("User update id{" + id + "} - " + userUpdateAdminDto);

        // найти пользователя в БД
        AppUser appUser = appUserService.findByIdWithoutIsActive(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        // валидация email (иначе исключение)
        boolean isUserDetailsCacheDel =
                userDataValidate.validateEmailForUpdate(appUser.getEmail(), userUpdateAdminDto.getEmail(), appUser.getEncPassword().isEmpty());

        // проверяем и изменяем password у user (если необходимо)
        updateUserService.updateUserPassword(appUser, userUpdateAdminDto.getPassword());

        boolean isAllCacheUserDel =
                updateUserService.updateUserRole(appUser, userUpdateAdminDto.getRoles())  // изменены ли роли
                || (appUser.getIsActive() != userUpdateAdminDto.isActive() && !userUpdateAdminDto.isActive());  // если заблокировали

        // проверяем и изменяем username у user (если необходимо)
        userUpdateAdminDto.setUsername(
                userDataValidate.normalizeUsername(userUpdateAdminDto.getUsername(), appUser.getEmail())
        );

        // удаляем все access, refresh токены и UserDetails этого пользователя
        if (isAllCacheUserDel) {
            updateUserService.deleteAllUserCache(appUser.getId().toString());
        }
        else if (isUserDetailsCacheDel) {  // удаляем UserDetails этого пользователя
            updateUserService.deleteUserDetailsCache(appUser.getId().toString());
        }

        // обновляем данные
        updateUserService.saveUserData(appUser, userUpdateAdminDto);
        appUser.setIsActive(userUpdateAdminDto.isActive());

        appUserService.save(appUser);
    }

    @Transactional
    public void add(final UserAddDto userAddDto, final UserFilesDto userFilesDto) {
        // валидация (если ошибка, то исключение)
        userDataValidate.validateUserExistence(userAddDto.getEmail());

        // добавление пользователя
        AppUser newUser = userMapper.toAppUser(userAddDto);
        AppUser savedUser = saveUserService.createUser(newUser);
        saveUserService.createUserRoles(newUser, userAddDto.getRoles());

        // в отдельном потоке загружаем аватарку
        var userFilesBufferDto = userFileMapper.toUserFilesBufferDto(userFilesDto);
        uploadFilesAsyncService.uploadUserFilesAndUpdate(savedUser, userFilesBufferDto);
    }

    @Transactional
    public void delete(final Integer id) {
        // найти пользователя в БД
        AppUser appUser = appUserService.findByIdWithoutIsActive(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        // делаем неактивным
        if (appUser.getIsActive()) {
            appUser.setIsActive(false);
            appUserService.save(appUser);
        }

        // удалить все токены и кэш
        updateUserService.deleteAllUserCache(id.toString());
    }
}
