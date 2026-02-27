package com.quetoquenana.pedalpal.common.util;

public class Constants {

    public static class BikeComponents {
        public static final String COMPONENT_TYPE = "COMPONENT_TYPE";
    }

    public static class JWTClaims {
        public static final String KEY_FACTORY_ALGORITHM = "RS256";
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
        public static final String SYSTEM = "SYSTEM";
    }

    public static class SystemCodes {
        public static final String SUGGESTION_TYPE_IA = "SUGGESTION_TYPE_IA";
    }
    public static class Services {
        public static final String TYPE_PRODUCT = "product";
        public static final String TYPE_PACKAGE = "package";

    }
}