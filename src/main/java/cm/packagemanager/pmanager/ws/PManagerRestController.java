package cm.packagemanager.pmanager.ws;

import cm.packagemanager.pmanager.user.ent.bo.AuthUserBO;
import cm.packagemanager.pmanager.user.ent.bo.UserBO;
import cm.packagemanager.pmanager.ws.requests.LoginRequest;
import cm.packagemanager.pmanager.ws.responses.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/")
public class PManagerRestController {

	protected final Log logger = LogFactory.getLog(RestController.class);

	ServletConfig config;
	@Autowired
	ServletContext context;


	@Autowired
	UserBO userBO;

	@Autowired
	AuthUserBO authUserBO;

	@Autowired
	Environment env;


	@RequestMapping(value = "/loginHome", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody Response login(HttpServletResponse response, HttpServletRequest request, @RequestBody LoginRequest loginRequest)
			throws Exception
	{
		logger.info("login request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try
		{
			pmResponse.setRetCode(0);
			pmResponse.setRetDescription("OK");
			logger.info("login request out");

		}
		catch (Exception e)
		{
			logger.error("Errore eseguendo login: ", e);
		}

		return pmResponse;
	}



	/*
	* @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
   public ResponseEntity<Object> updateProduct(@PathVariable("id") String id, @RequestBody Product product) {
      productRepo.remove(id);
      product.setId(id);
      productRepo.put(id, product);
      return new ResponseEntity<>("Product is updated successsfully", HttpStatus.OK);
   } */

}
