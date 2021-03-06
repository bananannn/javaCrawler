package com.moyan.crawler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
public class DSConfig {

    /*@Configuration
    @EnableTransactionManagement
    @EnableJpaRepositories(basePackages="com.moyan.crawler.service")
    @Profile({"SqlService"})
    public class DataScourceConfig {

        @Autowired
        private Environment env;

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder
                    .create()
                    .url(env.getProperty("spring.datasource.url"))
                    .username(env.getProperty("spring.datasource.username"))
                    .password(env.getProperty("spring.datasource.password"))
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }

        @Bean
        public PlatformTransactionManager transactionManager()
        {
            EntityManagerFactory factory = entityManagerFactory().getObject();
            return new JpaTransactionManager(factory);
        }

        @Bean
        public HibernateExceptionTranslator hibernateExceptionTranslator()
        {
            return new HibernateExceptionTranslator();
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

            vendorAdapter.setGenerateDdl(Boolean.TRUE);

            factoryBean.setDataSource(dataSource());
            factoryBean.setPackagesToScan("com.moyan.crawler");

            factoryBean.setJpaVendorAdapter(vendorAdapter);
            factoryBean.setJpaProperties(additionalProperties());

            return factoryBean;
        }

        Properties additionalProperties() {
            Properties properties = new Properties();
            properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
            return properties;
        }
    }*/

}
