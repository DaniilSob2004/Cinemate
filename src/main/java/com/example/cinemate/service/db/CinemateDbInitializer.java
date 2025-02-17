package com.example.cinemate.service.db;

import com.example.cinemate.model.db.*;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.authproviderservice.AuthProviderService;
import com.example.cinemate.service.busines.externalauthservice.ExternalAuthService;
import com.example.cinemate.service.busines.roleservice.RoleService;
import com.example.cinemate.service.busines.userroleservice.UserRoleService;
import com.example.cinemate.utils.GenerateUtil;
import com.example.cinemate.utils.TextFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CinemateDbInitializer {

    private static List<String> Surnames;
    private static List<String> Usernames;
    private static List<String> DeleteTablesLines;

    @Value("${db_data.surname}")
    private String dataSurname;

    @Value("${db_data.username}")
    private String dataUsername;

    @Value("${db_data.delete_tables_sql}")
    private String deleteTablesSql;

    @Value("${admin_data.password}")
    private String adminPassword;

    @Value("${user_data.password}")
    private String userPassword;

    @Value("${admin_data.role}")
    private String nameAdminRole;

    @Value("${user_data.role}")
    private String nameUserRole;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AuthProviderService authProviderService;

    @Autowired
    private ExternalAuthService externalAuthService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;


    @PostConstruct
    public void init() {
        Surnames = TextFileReader.ReadTextFile(dataSurname);
        Usernames = TextFileReader.ReadTextFile(dataUsername);
        DeleteTablesLines = TextFileReader.ReadTextFile(deleteTablesSql);
    }


    public void deleteTables() {
        StringBuilder deleteTablesQuery = new StringBuilder();

        for (var currString : DeleteTablesLines) {
            deleteTablesQuery.append(currString);
            deleteTablesQuery.append("\n");
        }

        String sqlStr = deleteTablesQuery.toString();
        jdbcTemplate.execute(sqlStr);

        Logger.info("Delete all tables successfully...");
    }

    public void deleteAllRowsInDB() {
        externalAuthService.deleteAll();
        userRoleService.deleteAll();
        appUserService.deleteAll();

        Logger.info("Delete all rows successfully...");
    }

    public void createAppUsers() {
        List<AppUser> users = new ArrayList<>();

        // создаём админа
        AppUser admin = CreateAdmin();
        appUserService.save(admin);

        // записываем роль для админа
        Role adminRole = roleService.findRoleByName(nameAdminRole)
                .orElseThrow(() -> new RuntimeException(nameAdminRole + " not found..."));
        UserRole roleForAdmin = new UserRole(null, admin, adminRole);
        userRoleService.save(roleForAdmin);

        // создаём пользователей
        int countUsers = GenerateUtil.getRandomInteger(5, Math.min(Surnames.size(), Usernames.size()));
        for (int i = 0; i < countUsers; i++) {
            users.add(CreateUser());
        }

        // сохраняем пользователей в БД
        appUserService.saveUsersList(users);

        // записываем роли для обычных пользователей
        Role userRole = roleService.findRoleByName(nameUserRole)
                .orElseThrow(() -> new RuntimeException(nameUserRole + " not found..."));
        List<AppUser> allUsers = appUserService.findAll();
        List<UserRole> userRoles = new ArrayList<>();
        allUsers.forEach(user -> userRoles.add(new UserRole(null, user, userRole)));
        userRoleService.saveUserRolesList(userRoles);

        Logger.info("AppUsers and UserRoles created successfully...");
    }

    public void createExternalAuth() {
        List<ExternalAuth> externalAuths = new ArrayList<>();
        List<AppUser> allUsers = appUserService.findAll();
        int countAuths = GenerateUtil.getRandomInteger(1, 3);

        AuthProvider googleProvider = authProviderService.findByName("google")
                .orElseThrow(() -> new RuntimeException("Auth provider 'google' not found..."));

        for (int i = 0; i < countAuths; i++) {
            externalAuths.add(CreateExternalAuth(allUsers, googleProvider));
        }
        externalAuthService.saveExternalAuthsList(externalAuths);

        Logger.info("ExternalAuths created successfully...");
    }


    private AppUser CreateAdmin() {
        return new AppUser(
                null,
                "ADMIN",
                "Admin",
                "SuperAdmin",
                GenerateUtil.getEmailByName("Admin").toLowerCase(),
                GenerateUtil.getRandomNumTel(),
                adminPassword,
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    private AppUser CreateUser() {
        String randUserName = Usernames.get(GenerateUtil.getRandomInteger(0, Usernames.size() - 1));
        String randSurname = Surnames.get(GenerateUtil.getRandomInteger(0, Surnames.size() - 1));

        return new AppUser(
                null,
                randUserName.toUpperCase(),
                randUserName,
                randSurname,
                GenerateUtil.getEmailByName(randUserName).toLowerCase(),
                GenerateUtil.getRandomNumTel(),
                userPassword,
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    private ExternalAuth CreateExternalAuth(final List<AppUser> allUsers, final AuthProvider googleProvider) {
        return new ExternalAuth(
                null,
                allUsers.get(GenerateUtil.getRandomInteger(0, allUsers.size() - 1)),
                googleProvider,
                GenerateUtil.getRandomNumberString(),
                LocalDateTime.now()
        );
    }
}
