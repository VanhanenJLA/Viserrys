package viserrys;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.stream.Stream;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static viserrys.Constants.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
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
                .headers(headers -> headers
                        .frameOptions(frameOptionsConfig -> frameOptionsConfig
                                .disable()))
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(antMatcher(H2CONSOLE_URL)))
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
        return Stream
                .of(patterns)
                .map(AntPathRequestMatcher::new)
                .toArray(AntPathRequestMatcher[]::new);
    }

    public MvcRequestMatcher[] createMvcRequestMatchers(MvcRequestMatcher.Builder builder, String... patterns) {
        return Stream
                .of(patterns)
                .map(p -> builder.pattern(p))
                .toArray(MvcRequestMatcher[]::new);
    }

}
