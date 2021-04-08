package cm.packagemanager.pmanager.configuration.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
public class HibernateConfiguration {

	@Value("${db.driver}")
	private String DRIVER;

	@Value("${db.password}")
	private String PASSWORD;

	@Value("${hibernate.datasource.url}")
	private String URL;

	@Value("${db.username}")
	private String USERNAME;

	@Value("${spring.jpa.database-platform}")
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

	//@Value("${spring.jpa.properties.hibernate.enable_lazy_load_no_trans}")
	@Value("${hibernate.enable_lazy_load_no_trans}")
	private boolean ENABLE_LAZY_TRANS;


	@Value("${connection.pool_size}")
	private int POOL_SIZE;

	@Bean
	@ConfigurationProperties(prefix="hibernate.datasource")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DRIVER);
		dataSource.setUrl(URL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", DIALECT);
		hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
		hibernateProperties.put("hibernate.hbm2ddl.auto", HBM2DDL_AUTO);
		// Ajouer par moi
		hibernateProperties.put("hibernate.default_schema", SCHEMA);
		hibernateProperties.put("hibernate.enable_lazy_load_no_trans", ENABLE_LAZY_TRANS);
		hibernateProperties.put("connection.pool_size", POOL_SIZE);
		hibernateProperties.put("transaction.factory_class", TRANSACTION_FACTORY_CLASS);
		hibernateProperties.put("current_session_context_class", CURRENT_SESSION_CONEXT_CLASS);

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
