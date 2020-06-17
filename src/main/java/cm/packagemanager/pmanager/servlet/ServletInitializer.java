package cm.packagemanager.pmanager.servlet;

import cm.packagemanager.pmanager.PackageApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**La classe ServletInitializer permet l'initialisation de l'application.
	Elle étend la classe SpringBootServletInitializer qui
 est à l'origine de cette initialisation et remplace ainsi l'ancien fichier web.xml.**/

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PackageApplication.class);
	}
}