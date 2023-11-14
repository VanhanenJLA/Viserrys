package viserrys;


import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
//@EnableWebMvc
public class SecurityConfiguration {
    
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

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        var antMatchers = createAntPathRequestMatchers(ANT_ENDPOINTS_WHITELIST);
        var mvcMatchers = createMvcRequestMatchers(mvc, MVC_ENDPOINTS_WHITELIST);
        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(antMatchers).permitAll()
                        .requestMatchers(mvcMatchers).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(LOGIN_URL).permitAll()
                        .failureUrl(LOGIN_FAIL_URL).permitAll()
                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL, true)
                )
                .logout(logout -> logout
                        .logoutUrl(LOGOUT_URL).permitAll()
                );
        return http.build();
    }

    public static AntPathRequestMatcher[] createAntPathRequestMatchers(String... patterns) {
        return Stream.of(patterns)
                .map(AntPathRequestMatcher::new)
                .toArray(AntPathRequestMatcher[]::new);
    }

    public MvcRequestMatcher[] createMvcRequestMatchers(MvcRequestMatcher.Builder builder, String... patterns) {
        return Stream.of(patterns)
                .map(p -> builder.pattern(p))
                .toArray(MvcRequestMatcher[]::new);
    }
    
}
