package viserrys.common;

import java.util.List;

public class Constants 
{
    public class Security {
        public static final String LOGIN_URL = "/login";
        public static final String LOGOUT_URL = "/logout";
        public static final String REGISTER_URL = "/register";
        public static final String LOGIN_FAIL_URL = LOGIN_URL + "?error";
        public static final String DEFAULT_SUCCESS_URL = "/me";
        public static final String ROOT_URL = "/";
        public static final String[] MVC_ENDPOINTS_WHITELIST = {
                ROOT_URL,
                LOGIN_URL,
                DEFAULT_SUCCESS_URL,
                REGISTER_URL,
        };

        public static final String IMAGES_URL = "/img/*";
        public static final String STYLESHEETS_URL = "/css/**";
        public static final String H2CONSOLE_URL = "/h2-console/**";
        public static final String FAVICON_URL = "/favicon.ico";
        public static final String[] ANT_ENDPOINTS_WHITELIST = {
                H2CONSOLE_URL,
                STYLESHEETS_URL,
                IMAGES_URL,
                FAVICON_URL,
        };
    }

    public static final int MB = 1024 * 1024;

    public class Paging {
        public static final int PAGEABLE_DEFAULT_SIZE = 5;
        public static final String PAGEABLE_DEFAULT_SORT = "timestamp";
        public static final List<Integer> PAGE_SIZES = List.of(5, 10, 25, 50, 100);
    }

    

}
