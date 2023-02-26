package com.bonsai.configurations;

import com.bonsai.core.RepositoryFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableAsync
@EnableScheduling
public class BonsaiConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource getDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    public RepositoryFactory getRepositoryFactory(DataSource dataSource){
        return new RepositoryFactory(dataSource);
    }
}
