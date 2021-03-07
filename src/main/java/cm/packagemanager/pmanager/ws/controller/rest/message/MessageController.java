package cm.packagemanager.pmanager.ws.controller.rest.message;


import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.message.ent.service.MessageService;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.messages.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.messages.UpdateMessageDTO;
import cm.packagemanager.pmanager.ws.responses.PaginateResponse;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

import static cm.packagemanager.pmanager.ws.controller.rest.CommonController.MESSAGE_WS;

@RestController
@RequestMapping(MESSAGE_WS)
@Api(value="messages-service", description="Messages Operations")
public class MessageController extends CommonController  {

		protected final Log logger = LogFactory.getLog(MessageController.class);

		@Autowired
		protected MessageService messageService;


		@PostMapping(value = MESSAGE_WS_UPDATE)
		MessageVO  update(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid UpdateMessageDTO umr) throws Exception {

		logger.info("Update message requestin");
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			createOpentracingSpan("MessageController -update");
			MessageVO message=messageService.update(umr);
			if (message!=null){
				message.setRetCode(WebServiceResponseCode.OK_CODE);
				message.setRetDescription(WebServiceResponseCode.UPDATED_MESSAGE_LABEL);
			}else{
				message=new MessageVO();
				message.setRetCode(WebServiceResponseCode.NOK_CODE);
				message.setRetDescription(WebServiceResponseCode.ERROR_UPDATE_MESSAGE_CODE_LABEL);
			}
			return message;
		}catch (Exception e){
			response.getWriter().write(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}

		return null;
	}


		@PostMapping(value = WS_ADD_MESSAGE)
		public ResponseEntity<MessageVO> addMessage(HttpServletRequest request ,HttpServletResponse response,@RequestBody @Valid MessageDTO mdto) throws Exception{
		HttpHeaders headers = new HttpHeaders();

			response.setHeader("Access-Control-Allow-Origin", "*");
			logger.info("add message to announce request in");

			try {
				createOpentracingSpan("MessageController -addMessage");
				if (mdto!=null){
					MessageVO message = messageService.addMessage(mdto);

					if(message!=null){
						message.setRetCode(WebServiceResponseCode.OK_CODE);
						message.setRetDescription(WebServiceResponseCode.MESSAGE_CREATE_LABEL);
						return new ResponseEntity<MessageVO>(message, headers, HttpStatus.OK);
					}else{
						response.getWriter().write("Message non ajouté!");
					}
				}
			}catch (Exception e){
				throw  e;
			}
			finally {
				finishOpentracingSpan();
			}
		return null;
	}

		/**
		 * Cette methode recherche un message en fonction des paramètres de recherche
		 * @param response
		 * @param request
		 * @param asdto
		 * @param page
		 * @param size
		 * @return
		 * @throws Exception
		 */

		/*@RequestMapping(value =MESSAGE_WS_FIND,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
		public @ResponseBody List<MessageVO> find(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid MessageSearchDTO asdto, @RequestParam(required = false, defaultValue = "0")  @Valid int page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception{

		logger.info("find  announces request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();
		List<MessageVO> messages=null;

		try{
			if (asdto!=null){
				PageBy pageBy= new PageBy(page,size);
				//messages=messageService.find(asdto,pageBy);
			}
		}
		catch (Exception e){
			response.getWriter().write(e.getMessage());
		}
		return announces;
	}*/

		/**
		 *  Cette methode recherche toutes les annonces d'un utilisateur
		 * @param response
		 * @param request
		 * @param userId
		 * @param page
		 * @param size
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value =MESSAGE_WS_BY_USER,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
		public ResponseEntity<PaginateResponse> messagesByUser(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long userId,
		                                      @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre un nombre positif") int page,
		                                      @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception{

		    response.setHeader("Access-Control-Allow-Origin", "*");
			logger.info("get all users request in");
			HttpHeaders headers = new HttpHeaders();
			PageBy pageBy = new PageBy(page,size);
			PaginateResponse paginateResponse = new PaginateResponse();

		try{
			logger.info("find message by user request in");
			createOpentracingSpan("MessageController -messagesByUser");
			if (userId!=null){
				int count =messageService.count(pageBy);
				if(count ==0){
					headers.add(HEADER_TOTAL, Long.toString(count));
				}else{
					List<MessageVO>  messages=messageService.messagesByUser(userId,pageBy);
					headers.add(HEADER_TOTAL, Long.toString(messages.size()));
				}
			}
		}
		catch (Exception e){
			response.getWriter().write(e.getMessage());
		}
		finally {
			finishOpentracingSpan();
		}
		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}

		/**
		 * Cette methode elimine un commentaire d'une annonce dont on a son Id
		 * @param response
		 * @param request
		 * @param id
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value =MESSAGE_WS_DELETE,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
		public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try{
			createOpentracingSpan("MessageController -delete");
			logger.info("delete message request in");
			if (id!=null){
				if(messageService.delete(id)){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.CANCELLED_MESSAGE_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_DELETE_MESSAGE_CODE_LABEL);
				}
			}
		}
		catch (Exception e){
			//response.getWriter().write(e.getMessage());
			pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			pmResponse.setRetDescription(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}
		return pmResponse;
	}

		/**
		 * Cette methode permet de recuperer tous les messages avec pagination
		 * @param page
		 * @param size
		 * @return
		 * @throws Exception
		 */
		@GetMapping(MESSAGES_WS)
		public ResponseEntity<PaginateResponse> messages(@Valid @Positive(message = "Page number should be a positive number") @RequestParam int page,
		@Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse=new PaginateResponse();
		logger.info("retrieve  messages request in");
		PageBy pageBy= new PageBy(page,size);

		try {
			createOpentracingSpan("MessageController -messages");
			int count = messageService.count(pageBy);
			if(count==0){
				headers.add(HEADER_TOTAL, Long.toString(count));
			}else{
				List<MessageVO> messages = messageService.messages(pageBy);
				paginateResponse.setCount(count);
				paginateResponse.setResults(messages);
				headers.add(HEADER_TOTAL, Long.toString(messages.size()));
			}
		}catch (Exception e){
			throw e;
		}finally {
			finishOpentracingSpan();
		}

		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}
}
