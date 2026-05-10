package weather_app.configs;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {
        "weather_app.services",
        "weather_app.interceptors",
        "weather_app.components",
        "weather_app.dao",
        "weather_app.networkAdapter"})

@Import(RootConfig.class)
public class TestConfig {
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        System.out.println(">>> CREATING TEST LIQUIBASE BEAN");
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/test-master.xml");
        return liquibase;
    }
}
