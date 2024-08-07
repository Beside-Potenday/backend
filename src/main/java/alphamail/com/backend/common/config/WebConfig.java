package alphamail.com.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://alphamails.vercel.app/", "https://alpamail-frontend-git-seungbeom-seung365s-projects.vercel.app/")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization", "Content-Type","job")
                //.exposedHeaders("Custom-Header")
                .allowCredentials(true)
                .maxAge(3600);
    }
}