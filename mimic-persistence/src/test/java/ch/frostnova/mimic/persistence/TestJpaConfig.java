package ch.frostnova.mimic.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Test JPA configuration
 *
 * @author pwalser
 * @since 11.03.2018
 */
@Configuration
@EnableJpaRepositories
@EnableTransactionManagement
@EnableJpaAuditing
public class TestJpaConfig {

    private final String driverClassName = "org.hsqldb.jdbcDriver";
    private final String jdbcURL = "jdbc:hsqldb:file:build/test-db/testDatabase";
    private final String hibernateDialect = "org.hibernate.dialect.HSQLDialect";
    private final String defaultSchema = "MIMIC";
    private final String dbUser = "test";
    private final String dbPassword = "test";
    private final boolean showSQL = true;
    private final String jpaScanPackage = "ch.frostnova.mimic";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(jdbcURL);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(jpaScanPackage);
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", hibernateDialect);
        hibernateProperties.setProperty("hibernate.show_sql", String.valueOf(showSQL));
        hibernateProperties.setProperty("hibernate.default_schema", defaultSchema);
        return hibernateProperties;
    }
}
