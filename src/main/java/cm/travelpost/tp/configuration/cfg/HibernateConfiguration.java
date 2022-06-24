package cm.travelpost.tp.configuration.cfg;

import cm.framework.ds.common.security.CommonSecurityResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@Component("entityManagerFactory")
public class HibernateConfiguration  extends CommonSecurityResource {

    private static Logger logger = LoggerFactory.getLogger(HibernateConfiguration.class);

    @Value("${db.driver}")
    private String driverClassName;

    @Value("${db.password}")
    private String password;

    @Value("${hibernate.datasource.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${spring.jpa.database-platform}")
    private String dialect;

    @Value("${hibernate.show_sql}")
    private String showSql;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2DdlAuto;

    @Value("${entitymanager.packagesToScan}")
    private String [] packagesToScan;

    @Value("${hibernate.default_schema}")
    private String SCHEMA;


    @Value("${transaction.factory_class}")
    private String transactionFactoryClass;

    @Value("${current_session_context_class}")
    private String currentSessionContextClass;

    @Value("${hibernate.enable_lazy_load_no_trans}")
    private boolean enableLazyTrans;

    @Value("${hibernate.order_updates}")
    private boolean orderUpdates;

    @Value("${hibernate.batch_versioned_data}")
    private boolean batchVersionedData;


    @Value("${connection.pool_size}")
    private int poolSize;



    @Bean
    @ConfigurationProperties(prefix = "hibernate.datasource")
    public DataSource dataSource() {

        if(logger.isDebugEnabled()){
            logger.info("Loading datasource...");
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(encryptorBean.decrypt(username));
        dataSource.setPassword(encryptorBean.decrypt(password));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(packagesToScan);
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", dialect);
        hibernateProperties.put("hibernate.show_sql", showSql);
        hibernateProperties.put("hibernate.hbm2ddl.auto", hbm2DdlAuto);
        // Ajouer par moi
       // hibernateProperties.put("hibernate.default_schema", SCHEMA);
        hibernateProperties.put("hibernate.enable_lazy_load_no_trans", enableLazyTrans);
        hibernateProperties.put("connection.pool_size", poolSize);
        hibernateProperties.put("transaction.factory_class", transactionFactoryClass);
        hibernateProperties.put("current_session_context_class", currentSessionContextClass);


        hibernateProperties.put("hibernate.order_updates", orderUpdates);
        hibernateProperties.put("hibernate.batch_versioned_data", batchVersionedData);


        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    @Bean("transactionManager")
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }


}
