package com.quetoquenana.pedalpal.common.util;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Constants {

    public static class BikeComponents {
        public static final String COMPONENT_TYPE = "COMPONENT_TYPE";
        public static final String COMPONENT_STATUS = "COMPONENT_STATUS";
        public static final String COMPONENT_STATUS_ACTIVE = "ACTIVE";
        public static final String COMPONENT_STATUS_COMPLETED = "COMPLETED";
    }

    public static class Dates {
        public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
        public static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT);
    }

    public static class JWTClaims {
        public static final String KEY_FACTORY_ALGORITHM = "RS256";
        public static final String KEY_NAME = "name";
        public static final String KEY_ROLES = "roles";
        public static final String KEY_USER_ID = "userId";
    }

    public static class Headers {
        public static final String LOCATION = "Location";
    }

    public static class MessageSource {
        public static final String BASE_NAME = "classpath:messages";
        public static final String DEFAULT_ENCODING = "UTF-8";
    }

    public static class Pagination {
        public static final String PAGE = "0";
        public static final String PAGE_SIZE = "10";
    }

    public static class ResponseValues{
        public static final String SUCCESS = "Success";
        public static final Integer DEFAULT_ERROR_CODE = 0;
    }

    public static class Default{
        public static final Integer MAINTENANCE_SUGGESTIONS_HISTORY_SIZE = 5;
    }

    public static class Roles {
        public static final String ROLE_PREFIX = "ROLE_";
        public static final String ROLE_SYSTEM = "ROLE_SYSTEM";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ADMIN = "ADMIN";
        public static final String SYSTEM = "SYSTEM";
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

        // General status used for products and other entities
        public static final String GENERAL_STATUS = "GENERAL_STATUS";
        public static final String GENERAL_STATUS_ACTIVE = "ACTIVE";
        public static final String GENERAL_STATUS_INACTIVE = "INACTIVE";

        public static final String SUGGESTION_ITEM_PRIORITY = "SUGGESTION_PRIORITY";
        public static final String SUGGESTION_ITEM_URGENCY = "SUGGESTION_URGENCY";
        public static final String SUGGESTION_STATUS = "SUGGESTION_STATUS";
        public static final String SUGGESTION_STATUS_CREATED = "CREATED";
        public static final String SUGGESTION_TYPE = "SUGGESTION_TYPE";
        public static final String SUGGESTION_TYPE_IA = "SUGGESTION_TYPE_IA";
        public static final String SUGGESTION_TYPE_MANUAL = "SUGGESTION_TYPE_MANUAL";
    }

}