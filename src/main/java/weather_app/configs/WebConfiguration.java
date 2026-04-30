package weather_app.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import weather_app.interceptors.SessionInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("weather_app")
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//TODO эта конфигурация ломает отдачу статики
// когда пользователь не авторизован, в частности, не отдаются CSS  стили

//                registry.addInterceptor(myInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login","/register");
//TODO добавить интерцептор, который редиректит с логина и регистрации на главную, если авторизован пользователь
        registry.addInterceptor(myInterceptor)
                .addPathPatterns("/", "search-location").order(1);
//                .excludePathPatterns("/login","/register");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    }

    @Bean
    public RestClient getRestClient() {
        return RestClient.create();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}