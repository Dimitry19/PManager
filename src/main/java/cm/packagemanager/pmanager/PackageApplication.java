package cm.packagemanager.pmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@ComponentScan
//@Configuration
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
//@EnableJpaRepositories(basePackages = "cm.packagemanager.pmanager")
@SpringBootApplication(scanBasePackages = "cm.packagemanager.pmanager")
public class PackageApplication {

	private static Logger logger = LoggerFactory.getLogger(PackageApplication.class);

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(PackageApplication.class);
		application.run(args);
	}
/*

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		// See: application.properties
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));

		System.out.println("## getDataSource: " + dataSource);

		return dataSource;
	}

	@Autowired
	@Bean(name = "sessionFactory")
	public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
		Properties properties = new Properties();

		// See: application.properties
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
		properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		properties.put("current_session_context_class", //
				env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));


		// Fix Postgres JPA Error:
		// Method org.postgresql.jdbc.PgConnection.createClob() is not yet implemented.
		// properties.put("hibernate.temp.use_jdbc_metadata_defaults",false);

		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

		// Package contain entity classes
		factoryBean.setPackagesToScan("cm.packagemanager.pmanager");
		factoryBean.setDataSource(dataSource);
		factoryBean.setHibernateProperties(properties);
		factoryBean.afterPropertiesSet();
		//
		SessionFactory sf = factoryBean.getObject();
		System.out.println("## getSessionFactory: " + sf);
		return sf;
	}

	@Autowired
	@Bean(name = "transactionManager")
	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

		return transactionManager;
	}
	*/
}
