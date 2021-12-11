package cm.packagemanager.pmanager.configuration.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

//@Configuration
//@EnableTransactionManagement
public class ConfigTestToDelete {

    @Value("${db.driver}")
    private String DRIVER;

    @Value("${db.password}")
    private String PASSWORD;

    @Value("${db.url}")
    private String URL;

    @Value("${db.username}")
    private String USERNAME;

    @Value("${hibernate.dialect}")
    private String DIALECT;

    @Value("${hibernate.show_sql}")
    private String SHOW_SQL;

    @Value("${hibernate.hbm2ddl.auto}")
    private String HBM2DDL_AUTO;

    @Value("${entitymanager.packagesToScan}")
    private String PACKAGES_TO_SCAN;

    @Value("${hibernate.default_schema}")
    private String SCHEMA;


    @Value("${transaction.factory_class}")
    private String TRANSACTION_FACTORY_CLASS;

    @Value("${current_session_context_class}")
    private String CURRENT_SESSION_CONEXT_CLASS;

    @Value("${spring.jpa.properties.hibernate.enable_lazy_load_no_trans}")
    private boolean ENABLE_LAZY_TRANS;


    @Value("${connection.pool_size}")
    private int POOL_SIZE;

    //@Autowired
    private DataSource dataSource;


    //@Bean
    public DataSource dataSource() {
        System.out.println("IN DATASOURCE");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;

    }

    //@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        //factory.setPackagesToScan(getClass().getPackage().getName());

        factory.setPackagesToScan(PACKAGES_TO_SCAN);
        factory.setDataSource(dataSource());
        factory.setJpaProperties(jpaProperties());

        return factory;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();

        properties.put("hibernate.dialect", DIALECT);
        properties.put("hibernate.show_sql", SHOW_SQL);
        properties.put("hibernate.show_sql", SHOW_SQL);
        properties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
        // Ajouer par moi
        properties.put("hibernate.default_schema", SCHEMA);
        properties.put("spring.jpa.properties.hibernate.enable_lazy_load_no_trans", ENABLE_LAZY_TRANS);
        properties.put("connection.pool_size", POOL_SIZE);
        properties.put("transaction.factory_class", TRANSACTION_FACTORY_CLASS);
        properties.put("current_session_context_class", CURRENT_SESSION_CONEXT_CLASS);
        return properties;
    }

    //@Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

}
