package com.quetoquenana.pedalpal.common.util;

public class Constants {

    public static class BikeComponents {
        public static final String COMPONENT_TYPE = "COMPONENT_TYPE";
    }

    public static class JWTClaims {
        public static final String KEY_NAME = "name";
        public static final String KEY_ROLES = "roles";
        public static final String KEY_USER_ID = "userId";
    }

    public static class MessageSource {
        public static final String BASE_NAME = "classpath:messages";
        public static final String DEFAULT_ENCODING = "UTF-8";
    }

    public static class ResponseValues{
        public static final String SUCCESS = "Success";
        public static final Integer DEFAULT_ERROR_CODE = 0;
    }

    public static class Roles {
        public static final String ROLE_PREFIX = "ROLE_";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_TECHNICIAN = "ROLE_TECHNICIAN";
        public static final String SYSTEM = "SYSTEM";
    }

    public static class Strava {
        public static final String AUTH_URL_PATH = "/authorize";
        public static final String AUTH_PARAM_CLIENT_ID = "client_id";
        public static final String AUTH_PARAM_REDIRECT_URI = "redirect_uri";
        public static final String AUTH_PARAM_RESPONSE_TYPE = "response_type";
        public static final String AUTH_PARAM_APPROVAL_PROMPT = "approval_prompt";
        public static final String AUTH_PARAM_SCOPE = "scope";
        public static final String AUTH_PARAM_STATE = "state";
        public static final String AUTH_PARAM_RESPONSE_TYPE_CODE = "code";
        public static final String AUTH_PARAM_APPROVAL_PROMPT_AUTO = "auto";

    }

    public static class SystemCodes {
        public static final String SUGGESTION_TYPE_IA = "SUGGESTION_TYPE_IA";
    }
}