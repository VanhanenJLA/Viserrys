package viserrys.common;

public class Constants 
{
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
    
    public static final int MB = 1024 * 1024;
}
