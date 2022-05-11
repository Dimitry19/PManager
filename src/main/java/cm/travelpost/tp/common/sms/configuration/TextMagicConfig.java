package cm.travelpost.tp.common.sms.configuration;

import cm.travelpost.tp.common.sms.ent.vo.TxtMagicMessage;
import com.textmagic.sdk.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TextMagicConfig {

	@Value("${textmagic.api.username}")
	protected  String username;

	@Value("${textmagic.api.key}")
	protected  String apiKey;



	@Bean(name = "tmClient")
	public TxtMagicMessage client(){
		RestClient client = new RestClient(username,apiKey);
		TxtMagicMessage message = client.getResource(TxtMagicMessage.class);
		return message;
	}


	public String getUsername() {
		return username;
	}

	public String getApiKey() {
		return apiKey;
	}
}
