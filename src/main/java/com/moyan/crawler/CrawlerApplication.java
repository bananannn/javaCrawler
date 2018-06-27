package com.moyan.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/*@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class })
@Configuration
@ComponentScan(basePackages = { "com.moyan.crawler" })*/
@SpringBootApplication
public class CrawlerApplication {
    //private static final Logger logger = LoggerFactory.getLogger(CrawlerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);
    }
}
