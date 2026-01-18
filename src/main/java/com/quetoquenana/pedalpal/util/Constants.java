package com.quetoquenana.pedalpal.util;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Constants {

    public static class BikeComponents {
        // Component-related categories/codes
        public static final String COMPONENT_TYPE = "COMPONENT_TYPE";
        public static final String COMPONENT_STATUS = "COMPONENT_STATUS";
        public static final String COMPONENT_STATUS_ACTIVE = "ACTIVE";
    }

    public static class Dates {
        public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
        public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT);
    }

    public static class JWTClaims {
        public static final String KEY_FACTORY_ALGORITHM = "RS256";
        public static final String KEY_NAME = "name";
        public static final String KEY_ROLES = "roles";
        public static final String KEY_TYPE = "type";
        public static final String KEY_USER_ID = "userId";

        public static final String KEY_SUB = "sub";

        public static final String ROLE_SUFFIX = "ROLE_";

        public static final String TYPE_AUTH = "auth";
        public static final String TYPE_REFRESH = "refresh";
    }

    public static class Headers {
        public static final String LOCATION = "Location";
    }

    public static class MessageSource {
        public static final String BASE_NAME = "classpath:messages";
        public static final String DEFAULT = "UTF-8";
    }

    public static class Pagination {
        public static final String PAGE = "0";
        public static final String PAGE_SIZE = "10";
    }

    public static class ResponseValues{
        public static final String SUCCESS = "Success";
        public static final Integer DEFAULT_ERROR_CODE = 0;
    }

    public static class Roles {
        public static final String ROLE_PREFIX = "ROLE_";
        public static final String ROLE_SYSTEM = "ROLE_SYSTEM";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
    }

    public static class SystemCodes {
        public static final String BIKE_TYPE = "BIKE_TYPE";
        public static final String BIKE_STATUS = "BIKE_STATUS";
        public static final String BIKE_STATUS_ACTIVE = "ACTIVE";
        public static final String BIKE_STATUS_INACTIVE = "INACTIVE";
        public static final String BIKE_STATUS_STOLEN = "STOLEN";
        public static final String BIKE_STATUS_SOLD = "SOLD";
        public static final String BIKE_STATUS_DELETED = "DELETED";

        public static final List<String> BIKE_ALL_STATUSES = List.of(
                BIKE_STATUS_ACTIVE,
                BIKE_STATUS_INACTIVE,
                BIKE_STATUS_STOLEN,
                BIKE_STATUS_SOLD
        );
    }

}