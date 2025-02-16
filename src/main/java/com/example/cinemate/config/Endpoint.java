package com.example.cinemate.config;

import java.util.ArrayList;
import java.util.List;

public class Endpoint {
    // version
    public static final String API_V1 = "/api/v1";

    // root
    public static final String AUTH = "/auth";
    public static final String USERS = "/users";

    // all
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String GOOGLE = "/google";

    // auth users
    public static final String GET_ME = "/me";
    public static final String LOGOUT = "/logout";

    // admin
    public static final String GET_PHOTO = "/secure/photo";

    static public List<String> getEndpointForAllUsers() {
        List<String> endpoints = new ArrayList<>();
        endpoints.add(API_V1 + AUTH + LOGIN);
        endpoints.add(API_V1 + AUTH + REGISTER);
        endpoints.add(API_V1 + AUTH + GOOGLE);
        return endpoints;
    }

    static public List<String> getEndpointForAuthUsers() {
        List<String> endpoints = new ArrayList<>();
        endpoints.add(API_V1 + USERS + GET_ME);
        endpoints.add(API_V1 + AUTH + LOGOUT);
        return endpoints;
    }

    static public List<String> getEndpointForAdmin() {
        List<String> endpoints = new ArrayList<>();
        endpoints.add(API_V1 + USERS + GET_PHOTO);
        return endpoints;
    }
}
