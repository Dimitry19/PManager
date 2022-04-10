package cm.travelpost.tp.common.properties;

import org.springframework.beans.factory.annotation.Value;

import javax.annotation.ManagedBean;

@ManagedBean
public class CommonProperties {

	@Value("${server.servlet.context-path}")
	protected String contextRoot;

	@Value("${tp.travelpost.app.domain}")
	protected String travelPostDomain;

	@Value("${tp.travelpost.app.email}")
	protected String travelPostEmail;

	@Value("1MB")
	protected String maxFileSize;
}
