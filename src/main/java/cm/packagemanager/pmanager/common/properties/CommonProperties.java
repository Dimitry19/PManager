package cm.packagemanager.pmanager.common.properties;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.ManagedBean;

@ManagedBean
public class CommonProperties {

	@Value("${server.servlet.context-path}")
	protected String contextRoot;
}
