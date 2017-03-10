package com.socialnetwork.dao;

import com.socialnetwork.common.ServiceConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@ComponentScan("com.socialnetwork")
@EnableTransactionManagement
@PropertySource("classpath:usertweetservice.properties")
@EnableJpaRepositories(basePackages = {"com.socialnetwork.model", "com.socialnetwork.dao"})
public class JPAPersistenceContext {


    @Bean(destroyMethod = "close")
    DataSource dataSource(Environment env) {

        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(env.getRequiredProperty(ServiceConstants.DB_DRIVER_KEY));
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty(ServiceConstants.DB_URL_KEY));
        dataSourceConfig.setUsername(env.getRequiredProperty(ServiceConstants.DB_USERNAME_KEY));
        dataSourceConfig.setPassword(env.getRequiredProperty(ServiceConstants.DB_PASSWORD_KEY));

        return new HikariDataSource(dataSourceConfig);

    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
                                                                Environment env) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan("com.socialnetwork.model");

        Properties jpaProperties = new Properties();
        jpaProperties.put(ServiceConstants.HIBERNATE_DIALECT,
                env.getRequiredProperty(ServiceConstants.HIBERNATE_DIALECT));
        jpaProperties.put(ServiceConstants.HIBERNATE_NAMING_STRATEGY,
                env.getRequiredProperty(ServiceConstants.HIBERNATE_NAMING_STRATEGY));
        jpaProperties.put(ServiceConstants.HIBERNATE_SHOW_SQL,
                env.getRequiredProperty(ServiceConstants.HIBERNATE_SHOW_SQL));
        jpaProperties.put(ServiceConstants.HIBERNATE_FORMAT_SQL,
                env.getRequiredProperty(ServiceConstants.HIBERNATE_FORMAT_SQL));

        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean
    @Primary
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}
