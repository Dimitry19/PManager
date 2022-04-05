package cm.packagemanager.pmanager.ws.controller.rest.message;


import cm.framework.ds.hibernate.enums.FindByType;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.constant.WSConstants;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.List;

import static cm.packagemanager.pmanager.constant.WSConstants.MESSAGE_WS;

@RestController
@RequestMapping(MESSAGE_WS)
@Api(value = "messages-service", description = "Messages Operations")
public class MessageController extends CommonController {

    protected final Log logger = LogFactory.getLog(MessageController.class);


    @PostMapping(value = ADD, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<MessageVO> addMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid MessageDTO mdto) throws Exception {
        HttpHeaders headers = new HttpHeaders();

        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("add message to announce request in");

        try {
            createOpentracingSpan("MessageController -addMessage");
            if (mdto != null) {
                MessageVO message = messageService.addMessage(mdto);

                if (message != null) {
                    message.setRetCode(WebServiceResponseCode.OK_CODE);
                    message.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, "Commentaire"));
                    return new ResponseEntity<MessageVO>(message, headers, HttpStatus.OK);
                } else {
                    return new ResponseEntity<MessageVO>(message, headers, HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }

    @PutMapping(value = MESSAGE_WS_UPDATE, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    MessageVO update(HttpServletResponse response, HttpServletRequest request, @PathVariable long id,
                     @RequestBody @Valid UpdateMessageDTO umr) throws Exception {

        logger.info("Update message requestin");
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            createOpentracingSpan("MessageController -update");
            umr.setId(id);
            MessageVO message = messageService.update(umr);
            if (message != null) {
                message.setRetCode(WebServiceResponseCode.OK_CODE);
                message.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, "Le commentaire"));
            } else {
                message = new MessageVO();
                message.setRetCode(WebServiceResponseCode.NOK_CODE);
                message.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, "Le commentaire"));
            }
            return message;
        } catch (Exception e) {
            logger.error(" Error {}", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }
    /**
     * Cette methode recherche toutes les messages d'une annonce
     *
     * @param response
     * @param request
     * @param announceId
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @RequestMapping(value = BY_ANNOUNCE, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> messagesByAnnounce(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long announceId,
                                                           @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre un nombre positif") int page,
                                                           @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("get all users request in");
        HttpHeaders headers = new HttpHeaders();
        PageBy pageBy = new PageBy(page, size);
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            logger.info("find message by user request in");
            createOpentracingSpan("MessageController -messagesByAnnounce");
            if (announceId != null) {
                int count = messageService.count(pageBy);
                List<MessageVO> messages = messageService.messagesBy(announceId, FindByType.ANNOUNCE, pageBy);
                return getPaginateResponseResponseEntity(headers,paginateResponse,count,messages);
            }else{
                paginateResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                paginateResponse.setRetDescription(WebServiceResponseCode.ERROR_PAGINATE_RESPONSE_LABEL);
            }
        } catch (Exception e) {
            logger.error("MessageController- error {}", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
    }

    /**
     * Cette methode recherche toutes les messages d'un utilisateur
     *
     * @param response
     * @param request
     * @param userId
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @RequestMapping(value = BY_USER, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> messagesByUser(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long userId,
                                                           @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre un nombre positif") int page,
                                                           @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        logger.info("get all users request in");
        HttpHeaders headers = new HttpHeaders();
        PageBy pageBy = new PageBy(page, size);
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            logger.info("find message by user request in");
            createOpentracingSpan("MessageController -messagesByUser");
            if (userId != null) {
                int count = messageService.count(pageBy);
                List<MessageVO> messages = messageService.messagesBy(userId,FindByType.USER, pageBy);
                return getPaginateResponseResponseEntity(headers,paginateResponse,count,messages);
            }else{
                paginateResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                paginateResponse.setRetDescription(WebServiceResponseCode.ERROR_PAGINATE_RESPONSE_LABEL);
            }
        } catch (Exception e) {
            logger.error("MessageController- error {}", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
    }

    /**
     * Cette methode elimine un commentaire d'une annonce dont on a son Id
     *
     * @param response
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = DELETE, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Response> delete(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        Response pmResponse = new Response();

        try {
            createOpentracingSpan("MessageController -delete");
            logger.info("delete message request in");
            if (id != null) {
                if (messageService.delete(id)) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, "Commentaire"));

                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);
                } else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, "Commentaire"));

                }
            }
            return new ResponseEntity<>(pmResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur durant la suppression du commentaire");
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    /**
     * Cette methode permet de recuperer tous les messages avec pagination
     *
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @GetMapping(MESSAGES_WS)
    public ResponseEntity<PaginateResponse> messages(@Valid @Positive(message = "Page number should be a positive number") @RequestParam int page,
                                                     @Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();
        logger.info("retrieve  messages request in");
        PageBy pageBy = new PageBy(page, size);

        try {
            createOpentracingSpan("MessageController -messages");
            int count = messageService.count(pageBy);
            List<MessageVO> messages = messageService.messages(pageBy);

            return getPaginateResponseResponseEntity(  headers,   paginateResponse,   count,  messages);
        } catch (Exception e) {
            throw e;
        } finally {
            finishOpentracingSpan();
        }

    }
}
