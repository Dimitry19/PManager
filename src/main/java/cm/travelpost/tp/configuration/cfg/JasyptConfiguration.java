package cm.travelpost.tp.configuration.cfg;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

//@Configuration
//@PropertySource("classpath:encrypted.properties")
public class JasyptConfiguration {

	//common method
	//used in classes - JasyptConfig.java and EncryptDecryptPwd.java
	public static SimpleStringPBEConfig getSimpleStringPBEConfig() {
		final SimpleStringPBEConfig pbeConfig = new SimpleStringPBEConfig();
		//can be picked via the environment variablee
		//TODO - hardcoding to be removed
		pbeConfig.setPassword("tr@velP0StSerViC3s");  //encryptor private key
		pbeConfig.setAlgorithm("PBEWithMD5AndDES");
		pbeConfig.setKeyObtentionIterations("1000");
		pbeConfig.setPoolSize("1");
		pbeConfig.setProviderName("SunJCE");
		pbeConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		pbeConfig.setStringOutputType("base64");

		return pbeConfig;
	}

	//@Bean(name = "jasyptStringEncryptor")
	public StringEncryptor encryptor() {
		final PooledPBEStringEncryptor pbeStringEncryptor = new PooledPBEStringEncryptor();
		pbeStringEncryptor.setConfig(getSimpleStringPBEConfig());

		return pbeStringEncryptor;
	}
}
