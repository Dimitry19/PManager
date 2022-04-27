package cm.travelpost.tp.common.sms.configuration;


import org.springframework.context.annotation.Configuration;

@Configuration
public class RandomOtpGenerator {

	int min = 100000;
	int max = 999999;


	public int generate(){
		return (int)(Math.random()*(max-min+1)+min);
	}
}
