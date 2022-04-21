package cm.travelpost.tp.ws.controller.rest.message;


import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.enums.FindBy;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.message.ent.vo.MessageVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.messages.MessageDTO;
import cm.travelpost.tp.ws.requests.messages.UpdateMessageDTO;
import cm.travelpost.tp.ws.responses.PaginateResponse;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
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

import static cm.travelpost.tp.constant.WSConstants.MESSAGE_WS;

@RestController
@RequestMapping(MESSAGE_WS)
@Api(value = "messages-service", description = "Messages Operations")
public class MessageController extends CommonController {

    private static final String COMMENT_LABEL = "Commentaire";
    protected final Log logger = LogFactory.getLog(MessageController.class);


    @PostMapping(value = ADD, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public ResponseEntity<MessageVO> addMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid MessageDTO mdto) throws Exception {
        HttpHeaders headers = new HttpHeaders();

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        logger.info("add message to announce request in");

        try {
            createOpentracingSpan("MessageController -addMessage");
            if (mdto != null) {
                MessageVO message = messageService.addMessage(mdto);

                if (message != null) {
                    message.setRetCode(WebServiceResponseCode.OK_CODE);
                    message.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, COMMENT_LABEL));
                    return new ResponseEntity<>(message, headers, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            logger.info("add message to announce error");
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
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
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
     * Cette methode recherche toutes les messages d'un utilisateur
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

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        logger.info("get all users request in");
        HttpHeaders headers = new HttpHeaders();
        PageBy pageBy = new PageBy(page, size);
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            logger.info("find message by user request in");
            createOpentracingSpan("MessageController - messagesByAnnounce");
            if (announceId != null) {
                int count = messageService.count(pageBy);
                List<MessageVO> messages = messageService.messagesBy(announceId, FindBy.ANNOUNCE, pageBy);
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
        return new ResponseEntity<>(paginateResponse, headers, HttpStatus.OK);
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

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        logger.info("get all users request in");
        HttpHeaders headers = new HttpHeaders();
        PageBy pageBy = new PageBy(page, size);
        PaginateResponse paginateResponse = new PaginateResponse();

        try {
            logger.info("find message by user request in");
            createOpentracingSpan("MessageController -messagesByUser");
            if (userId != null) {
                int count = messageService.count(pageBy);
                List<MessageVO> messages = messageService.messagesBy(userId, FindBy.USER, pageBy);
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
        return new ResponseEntity<>(paginateResponse, headers, HttpStatus.OK);
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

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        Response pmResponse = new Response();

        try {
            createOpentracingSpan("MessageController -delete");
            logger.info("delete message request in");
            return getResponseDeleteResponseEntity(id, pmResponse, messageService.delete(id), COMMENT_LABEL);
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

            return getPaginateResponseResponseEntity(headers,paginateResponse, count,  messages);
        } catch (Exception e) {
            logger.info("retrieve messages call error");
            throw e;
        } finally {
            finishOpentracingSpan();
        }

    }
}
