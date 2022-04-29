package cm.travelpost.tp.configuration.cfg;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("classpath:config.properties")
public class JasyptConfiguration {

	private static final String secret ="tr@v3lP0st";
	private static final String algorithm ="PBEWithHmacSHA512AndAES_256";
	private static final int poolSize =4;



	@Bean(name = "jasyptStringEncryptor")
	public StringEncryptor stringEncryptor() {

		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setIvGenerator(new RandomIvGenerator());
		encryptor.setPassword(secret);
		encryptor.setAlgorithm(algorithm);
		encryptor.setPoolSize(poolSize);
		return encryptor;
	}

}
