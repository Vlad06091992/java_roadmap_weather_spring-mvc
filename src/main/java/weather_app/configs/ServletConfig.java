package weather_app.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import weather_app.interceptors.LoginInterceptor;
import weather_app.interceptors.SessionInterceptor;

@Configuration
@EnableWebMvc
@ComponentScan("weather_app")
@RequiredArgsConstructor
public class ServletConfig implements WebMvcConfigurer {

    private final SessionInterceptor sessionInterceptor;
    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //TODO эта конфигурация ломает отдачу статики
        // когда пользователь не авторизован, в частности, не отдаются CSS  стили

        //                registry.addInterceptor(myInterceptor)
        //                .addPathPatterns("/**")
        //                .excludePathPatterns("/login","/register");
        //TODO добавить интерцептор, который редиректит с логина и регистрации на главную, если авторизован пользователь
        registry
                .addInterceptor(sessionInterceptor)
                //                .addPathPatterns("/", "/search-location","/add-location","/delete-location","/logout");
                .excludePathPatterns("/login", "/register", "/images/*","/css/*").order(1);

        registry.addInterceptor(loginInterceptor).addPathPatterns("/login", "/register").order(2);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").addResourceLocations("/images/");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    }


    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

}