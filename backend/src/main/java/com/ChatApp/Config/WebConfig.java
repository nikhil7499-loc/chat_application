package com.ChatApp.Config;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import org.springframework.beans.factory.annotation.Value;
import com.ChatApp.Security.CurrentUserArgumentResolver;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;
    @Value("${app.api-prefix:/api}")
    private String apiPrefix;

    public WebConfig(CurrentUserArgumentResolver currentUserArgumentResolver) {
        this.currentUserArgumentResolver = currentUserArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(apiPrefix, clazz ->
            clazz.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class)
            && !clazz.getPackageName().contains("com.ChatApp.Controllers.Frontend")
        );
    }
}
