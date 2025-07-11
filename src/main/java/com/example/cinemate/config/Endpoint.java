package com.example.cinemate.config;

import java.util.List;

public class Endpoint {
    // version
    public static final String API_V1 = "/api/v1";

    // prefix
    public static final String ADMIN = "/admin";

    // frontend
    public static final String OAUTH_SUCCESS_TOKENS = "/oauth-success?state=";
    public static final String OAUTH_SUCCESS_ERROR = "/oauth-success?error=";
    public static final String FRONT_RESET_PASSWORD = "/reset-password?token=";

    // root
    public static final String AUTH = "/auth";
    public static final String USER = "/user";
    public static final String USERS = "/users";
    public static final String ROLES = "/roles";
    public static final String PROVIDERS = "/providers";
    public static final String CONTENT_TYPES = "/content-types";
    public static final String WARNINGS = "/warnings";
    public static final String CONTENTS = "/contents";
    public static final String EPISODES = "/episodes";
    public static final String ACTORS = "/actors";
    public static final String GENRES = "/genres";
    public static final String CONTENT_VIEWS = "/content-views";
    public static final String WISHLISTS = "/wishlists";

    // all
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String FORGOT_PASSWORD = "/forgot-password";
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String TOKENS = "/tokens";
    public static final String RANDOM = "/random";
    public static final String BY_GENRE = "/genre";
    public static final String BY_CONTENT_TYPE = "/type";
    public static final String PRIVACY_POLICY = "/privacy-policy";
    public static final String ALL = "/all";

    // auth users
    public static final String ME = "/me";
    public static final String UPDATE_ACCESS_TOKEN = "/update-access-token";
    public static final String BY_RECOMMENDATIONS_TEST = "/rec-test";
    public static final String BY_RECOMMENDATIONS = "/recommendations";
    public static final String BY_CONTENT_ID = "/{contentId}";
    public static final String LOGOUT = "/logout";

    // admin
    public static final String BY_ID = "/{id}";
    public static final String ADD_USER = "/add";

    static public List<String> getEndpointForAllUsers() {
        return List.of(
                API_V1 + AUTH + LOGIN,
                API_V1 + AUTH + REGISTER,
                API_V1 + AUTH + FORGOT_PASSWORD,
                API_V1 + AUTH + RESET_PASSWORD,
                API_V1 + AUTH + TOKENS,
                API_V1 + PROVIDERS,
                API_V1 + CONTENTS + RANDOM,
                API_V1 + CONTENTS + BY_GENRE,
                API_V1 + CONTENTS + BY_CONTENT_TYPE,
                API_V1 + GENRES + ALL,
                API_V1 + CONTENT_TYPES + ALL,
                API_V1 + WARNINGS + ALL,
                API_V1 + ACTORS + ALL,
                API_V1 + EPISODES + BY_CONTENT_ID,
                API_V1 + PRIVACY_POLICY
        );
    }

    static public List<String> getEndpointForAuthUsers() {
        return List.of(
                API_V1 + USER + ME,
                API_V1 + CONTENTS,
                API_V1 + CONTENTS + BY_RECOMMENDATIONS,
                API_V1 + CONTENT_VIEWS,
                API_V1 + WISHLISTS,
                API_V1 + GENRES + BY_RECOMMENDATIONS_TEST
        );
    }

    static public List<String> getEndpointForAdmin() {
        return List.of(
                API_V1 + ROLES,
                API_V1 + USERS,
                API_V1 + USERS + BY_ID,
                API_V1 + USERS + ADD_USER,
                API_V1 + CONTENT_TYPES,
                API_V1 + WARNINGS,
                API_V1 + ADMIN + CONTENTS,
                API_V1 + ADMIN + CONTENTS + BY_ID,
                API_V1 + ADMIN + EPISODES,
                API_V1 + ACTORS,
                API_V1 + GENRES
        );
    }

    static public List<String> getEndpointForAuthUsersAndAdmin() {
        return List.of(
                API_V1 + AUTH + UPDATE_ACCESS_TOKEN,
                API_V1 + AUTH + LOGOUT
        );
    }
}
