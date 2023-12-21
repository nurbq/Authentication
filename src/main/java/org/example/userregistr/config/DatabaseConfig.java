package org.example.userregistr.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

//    @Value("${auth.service.primary-db-url:default }")
//    private String url;
//
//    @Value("${auth.service.primary-db-username}")
//    private String username;
//
//    @Value("${auth.service.primary-db-password}")
//    private String password;


    @Bean(name = "dataSource")
    public DataSource postgresDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/login-api-db");
        dataSource.setUsername("api-user");
        dataSource.setPassword("secretpass");

        return dataSource;
    }

    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("dataSource")DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
