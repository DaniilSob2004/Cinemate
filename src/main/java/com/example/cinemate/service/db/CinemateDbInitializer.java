package com.example.cinemate.service.db;

import com.example.cinemate.dto.user.UserSearchParamsDto;
import com.example.cinemate.model.db.*;
import com.example.cinemate.service.business_db.actorservice.ActorService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.authproviderservice.AuthProviderService;
import com.example.cinemate.service.business_db.contenttypeservice.ContentTypeService;
import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import com.example.cinemate.service.business_db.genreservice.GenreService;
import com.example.cinemate.service.business_db.roleservice.RoleService;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.service.business_db.warningservice.WarningService;
import com.example.cinemate.utils.GenerateUtil;
import com.example.cinemate.utils.TextFileReaderUtil;
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
    private static List<String> ContentTypes;
    private static List<String> Warnings;
    private static List<String> Actors;
    private static List<String> Genres;
    private static List<String> DeleteTablesLines;

    @Value("${db_data.surname}")
    private String dataSurname;

    @Value("${db_data.username}")
    private String dataUsername;

    @Value("${db_data.content_types}")
    private String dataContentTypes;

    @Value("${db_data.warnings}")
    private String dataWarnings;

    @Value("${db_data.actors}")
    private String dataActors;

    @Value("${db_data.genres}")
    private String dataGenres;

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

    private final AppUserService appUserService;
    private final AuthProviderService authProviderService;
    private final ExternalAuthService externalAuthService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final ContentTypeService contentTypeService;
    private final WarningService warningService;
    private final ActorService actorService;
    private final GenreService genreService;
    private final JdbcTemplate jdbcTemplate;

    public CinemateDbInitializer(AppUserService appUserService, AuthProviderService authProviderService, ExternalAuthService externalAuthService, RoleService roleService, UserRoleService userRoleService, ContentTypeService contentTypeService, WarningService warningService, ActorService actorService, GenreService genreService, JdbcTemplate jdbcTemplate) {
        this.appUserService = appUserService;
        this.authProviderService = authProviderService;
        this.externalAuthService = externalAuthService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.contentTypeService = contentTypeService;
        this.warningService = warningService;
        this.actorService = actorService;
        this.genreService = genreService;
        this.jdbcTemplate = jdbcTemplate;
    }


    @PostConstruct
    public void init() {
        Surnames = TextFileReaderUtil.ReadTextFile(dataSurname);
        Usernames = TextFileReaderUtil.ReadTextFile(dataUsername);
        ContentTypes = TextFileReaderUtil.ReadTextFile(dataContentTypes);
        Warnings = TextFileReaderUtil.ReadTextFile(dataWarnings);
        Actors = TextFileReaderUtil.ReadTextFile(dataActors);
        Genres = TextFileReaderUtil.ReadTextFile(dataGenres);
        DeleteTablesLines = TextFileReaderUtil.ReadTextFile(deleteTablesSql);
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

        contentTypeService.deleteAll();
        warningService.deleteAll();
        actorService.deleteAll();
        genreService.deleteAll();

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
        var userSearchParamsDto = new UserSearchParamsDto(0, 20, "id", true, "");
        List<AppUser> allUsers = appUserService.getUsers(userSearchParamsDto).getContent();
        List<UserRole> userRoles = new ArrayList<>();
        allUsers.forEach(user -> userRoles.add(new UserRole(null, user, userRole)));
        userRoleService.saveUserRolesList(userRoles);

        Logger.info("AppUsers and UserRoles created successfully...");
    }

    public void createExternalAuth() {
        List<ExternalAuth> externalAuths = new ArrayList<>();

        var userSearchParamsDto = new UserSearchParamsDto(0, 20, "id", true, "");
        List<AppUser> allUsers = appUserService.getUsers(userSearchParamsDto).getContent();

        int countAuths = GenerateUtil.getRandomInteger(1, 3);

        AuthProvider googleProvider = authProviderService.findByName("google")
                .orElseThrow(() -> new RuntimeException("Auth provider 'google' not found..."));

        for (int i = 0; i < countAuths; i++) {
            externalAuths.add(CreateExternalAuth(allUsers, googleProvider));
        }
        externalAuthService.saveExternalAuthsList(externalAuths);

        Logger.info("ExternalAuths created successfully...");
    }

    public void createContentTypes() {
        List<ContentType> contentTypes = new ArrayList<>();
        ContentTypes.forEach(contentType -> {
            contentTypes.add(new ContentType(
                    null,
                    contentType,
                    "",
                    ""
            ));
        });
        contentTypeService.saveContentTypesList(contentTypes);

        Logger.info("ContentTypes created successfully...");
    }

    public void createWarnings() {
        List<Warning> warnings = new ArrayList<>();
        Warnings.forEach(warning -> {
            warnings.add(new Warning(
                    null,
                    warning
            ));
        });
        warningService.saveWarningsList(warnings);

        Logger.info("Warnings created successfully...");
    }

    public void createActors() {
        List<Actor> actors = new ArrayList<>();
        Actors.forEach(actor -> {
            var nameSurname = actor.split(" ");
            if (nameSurname.length == 2) {
                actors.add(new Actor(
                        null,
                        nameSurname[0],
                        nameSurname[1],
                        "Biography for " + nameSurname[0] + " " + nameSurname[1]
                ));
            }
        });
        actorService.saveActorsList(actors);

        Logger.info("Actors created successfully...");
    }

    public void createGenres() {
        List<Genre> genres = new ArrayList<>();
        Genres.forEach(genre -> {
            genres.add(new Genre(
                   null,
                   genre,
                   "Description for " + genre,
                   ""
            ));
        });
        genreService.saveGenresList(genres);

        Logger.info("Genres created successfully...");
    }


    private AppUser CreateAdmin() {
        return new AppUser(
                null,
                "ADMIN",
                "Admin",
                "SuperAdmin",
                "admin@gmail.com",
                GenerateUtil.getRandomNumTel(),
                adminPassword,
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
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
                true
        );
    }

    private ExternalAuth CreateExternalAuth(final List<AppUser> allUsers, final AuthProvider googleProvider) {
        return new ExternalAuth(
                null,
                allUsers.get(GenerateUtil.getRandomInteger(0, allUsers.size() - 1)),
                googleProvider,
                GenerateUtil.getRandomNumberString(),
                LocalDateTime.now(),
                GenerateUtil.getRandomString()
        );
    }
}
