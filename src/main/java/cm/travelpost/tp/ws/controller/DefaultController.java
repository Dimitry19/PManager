package cm.travelpost.tp.ws.controller;

import cm.travelpost.tp.ws.controller.rest.CommonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class DefaultController extends CommonController {
	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

	/**
	 *  Ceci permet de rediriger la context route sur la page index.html
	 * @param response
	 * @throws IOException
	 */



    @GetMapping(value = "/")
	public void  ping(HttpServletResponse response) throws Exception {
		logger.info("/ -> index.html.....");
		int totalUsers=userService.count(null, null,null);


		response.setIntHeader("totalUsers", totalUsers);
		response.sendRedirect(getRedirectPage(RedirectType.INDEX));

	}


}