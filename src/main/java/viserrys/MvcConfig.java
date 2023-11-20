package viserrys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    @Autowired
    protected void configureThymeleafSpringTemplateEngine(SpringTemplateEngine templateEngine) {
        templateEngine.setEnableSpringELCompiler(true);
    }

}
