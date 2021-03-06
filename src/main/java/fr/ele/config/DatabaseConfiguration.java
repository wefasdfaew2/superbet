package fr.ele.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import fr.ele.core.ApplicationProfiles;

@Configuration
@EnableJpaRepositories("fr.ele.services.repositories")
@EnableTransactionManagement
public class DatabaseConfiguration implements EnvironmentAware {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    private RelaxedPropertyResolver propertyResolver;

    @Inject
    private Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        propertyResolver = new RelaxedPropertyResolver(environment, "spring.datasource.");
    }

    @Bean
    public DataSource dataSource() {
        log.info("Configuring Datasource");
        if (env.acceptsProfiles(ApplicationProfiles.MEM_DB)) {
            log.info("Embedded DB");
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        }
        String url = propertyResolver.getProperty("url");
        if (url == null && propertyResolver.getProperty("databaseName") == null) {
            log.error("Your database connection pool configuration is incorrect! The application"
                    + "cannot start. Please check your Spring profile, current profiles are: {}", (Object[]) env.getActiveProfiles());

            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        log.info("Pooled datasource");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDataSourceClassName(propertyResolver.getProperty("dataSourceClassName"));
        if (StringUtils.isNotEmpty(url)) {
            dataSource.addDataSourceProperty("url", url);
        } else {
            dataSource.addDataSourceProperty("databaseName", propertyResolver.getProperty("databaseName"));
            dataSource.addDataSourceProperty("serverName", propertyResolver.getProperty("serverName"));
        }
        dataSource.addDataSourceProperty("user", propertyResolver.getProperty("username"));
        dataSource.addDataSourceProperty("password", propertyResolver.getProperty("password"));
        return dataSource;

    }

    @Bean(name = {"org.springframework.boot.autoconfigure.AutoConfigurationUtils.basePackages"})
    public List<String> getBasePackages() {
        List<String> basePackages = new ArrayList<>();
        basePackages.add("fr.ele.model");
        return basePackages;
    }

}
