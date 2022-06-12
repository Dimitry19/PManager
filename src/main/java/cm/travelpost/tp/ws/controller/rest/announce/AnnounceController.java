package cm.travelpost.tp.ws.controller.rest.announce;


import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.common.ent.vo.WSCommonResponseVO;
import cm.travelpost.tp.announce.ent.vo.AnnounceCompletedVO;
import cm.travelpost.tp.announce.ent.vo.AnnounceMasterVO;
import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.announce.enums.Source;
import cm.travelpost.tp.common.enums.AnnounceType;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.enums.TransportEnum;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.announces.AnnounceDTO;
import cm.travelpost.tp.ws.requests.announces.AnnounceSearchDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateAnnounceDTO;
import cm.travelpost.tp.ws.requests.users.UsersAnnounceFavoriteDTO;
import cm.travelpost.tp.ws.responses.PaginateResponse;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cm.travelpost.tp.constant.WSConstants.ANNOUNCE_WS;

@RestController
@RequestMapping(ANNOUNCE_WS)
@Api(value = "Announce-service", description = "Announces Operations")
public class AnnounceController extends CommonController {

   private static Logger logger = LoggerFactory.getLogger(AnnounceController.class);

    private static  final String ANNOUNCE_LABEL ="L'annonce";

    /**
     * Cette methode cree une annonce
     *
     * @param response
     * @param request
     * @param ar
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Create/add an announce ", response = AnnounceVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful Create Announce",
                    response = AnnounceVO.class, responseContainer = "Object")})
    @PostMapping(value = CREATE, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Object> create(HttpServletResponse response, HttpServletRequest request,
                                  @RequestBody @Valid AnnounceDTO ar) throws AnnounceException {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        AnnounceMasterVO announce = null;

        try {
            createOpentracingSpan("AnnounceController -create");

            logger.info("create announce request in");
            announce = announceService.create(ar);

            if (announce != null) {
                announce.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, ANNOUNCE_LABEL));
                announce.setRetCode(WebServiceResponseCode.OK_CODE);

                return new ResponseEntity<>(announce, HttpStatus.CREATED);
            }
        } catch ( Exception  e) {
            logger.error("Erreur durant l'execution de  add announce: ", e);
            throw new AnnounceException(e.getMessage());
        } finally {
            finishOpentracingSpan();
        }
        WSCommonResponseVO  commonResponse= new WSCommonResponseVO();
        commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
        commonResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_CREATE_LABEL, ANNOUNCE_LABEL));
        return new ResponseEntity<>(commonResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ApiOperation(value = "Update an announce ", response = AnnounceVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful Update Announce",
                    response = AnnounceVO.class, responseContainer = "Object")})
    @PutMapping(value = UPDATE_ID, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
    ResponseEntity<Object> update(HttpServletResponse response, HttpServletRequest request, @PathVariable("id") @Valid long id,
                                  @RequestBody @Valid UpdateAnnounceDTO uar) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        try {
            createOpentracingSpan("AnnounceController -update");

            if (uar == null) return null;
            uar.setId(id);

            AnnounceVO announce = announceService.update(uar);
            if (announce != null) {
                announce.setRetCode(WebServiceResponseCode.OK_CODE);
                if(StringUtils.isEmpty(announce.getRetDescription())){
                    announce.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, ANNOUNCE_LABEL));
                }
                return new ResponseEntity<>(announce, HttpStatus.OK);
            } else {
                WSCommonResponseVO  commonResponse = new WSCommonResponseVO();
                commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                commonResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, ANNOUNCE_LABEL));
                return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
            }
        } catch (AnnounceException e) {
            logger.error("Erreur durant l'ajournement de l'announce  {} - { }", uar.toString() ,e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    /**
     * Cette methode recherche une annonce en fonction des paramretres de recherche
     *
     * @param response
     * @param request
     * @param dto
     * @param page
     * @param size
     * @return
     * @throws Exception
     */

    @ApiOperation(value = "Search announce function ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval announces",
                    response = ResponseEntity.class, responseContainer = "List")})
    @PostMapping(value = FIND, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<PaginateResponse> search(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid AnnounceSearchDTO dto,
                                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                            @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) throws Exception {


        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();

        logger.info("Search  announces request in");

        try {
            createOpentracingSpan("AnnounceController -search");

            if (dto != null) {

                PageBy pageBy =new PageBy(dto.getPage()!=null ? dto.getPage():page, size);

                int count = announceService.count(dto,pageBy);
                List<AnnounceVO> announces = announceService.search(dto, pageBy);
                return getPaginateResponseSearchResponseEntity(headers,paginateResponse,count,announces,pageBy);
            }
        } catch (Exception e) {
            logger.info(" AnnounceController -find:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return new ResponseEntity<>(paginateResponse, headers, HttpStatus.PRECONDITION_FAILED);
    }

    /**
     * Cette methode recherche toutes les annonces d'un utilisateur
     *
     * @param response
     * @param request
     * @param userId
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Retrieve announces by user with an ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval user announces",
                    response = ResponseEntity.class, responseContainer = "List")})
    @GetMapping(value = BY_USER, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> announcesByUser(HttpServletResponse response, HttpServletRequest request,
                                                            @RequestParam @Valid Long userId, @RequestParam StatusEnum status,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE)@Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) throws AnnounceException, Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();
        PageBy pageBy = new PageBy(page, size);

        logger.info("find announce by user request in");


        try {
            createOpentracingSpan("AnnounceController -announcesByUser");

            if(status ==null){
                status =StatusEnum.VALID;
            }

            int count = announceService.count(userId,status,pageBy);
            List announces = announceService.announcesByUser(userId,status,pageBy);
            return getPaginateResponseResponseEntity(  headers,count,  announces);
        } catch (AnnounceException e) {
            logger.info(" AnnounceController -announcesByUser:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }


    /**
     * Cette methode recherche toutes les annonces par type
     *
     * @param response
     * @param request
     * @param type
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Retrieve announces by Type", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval announces by type",
                    response = ResponseEntity.class, responseContainer = "List")})
    @GetMapping(value = ANNOUNCE_WS_BY_TYPE, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> announcesByType(HttpServletResponse response, HttpServletRequest request,
                                                            @RequestParam @Valid AnnounceType type,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) throws AnnounceException,Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        HttpHeaders headers = new HttpHeaders();
        PageBy pageBy = new PageBy(page, size);
        logger.info("find announce by type request in");

        try {
            createOpentracingSpan("AnnounceController - announcesByType");

            int count = announceService.count(type, null);
            List<AnnounceVO> announces = announceService.announcesBy(type, pageBy);
            return getPaginateResponseResponseEntity(headers,count,  announces);

        } catch (AnnounceException e) {
            logger.info(" AnnounceController -announcesByType:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }


    /**
     * Cette methode recherche toutes les annonces par type
     *
     * @param response
     * @param request
     * @param transport
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Retrieve announces by Transport Mode", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval announces by transport",
                    response = ResponseEntity.class, responseContainer = "List")})
    @GetMapping(value = ANNOUNCE_WS_BY_TRANSPORT,   headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> announcesByTransport(HttpServletResponse response, HttpServletRequest request,
                                                                 @RequestParam @Valid TransportEnum transport,
                                                                 @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                                 @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) throws AnnounceException,Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        HttpHeaders headers = new HttpHeaders();
        PageBy pageBy = new PageBy(page, size);
        logger.info("find announce by transport request in");


        try {
            createOpentracingSpan("AnnounceController - announcesByTransport");

            int count = announceService.count(transport, null);
            List<AnnounceVO> announces = announceService.announcesBy(transport,  pageBy);
            return getPaginateResponseResponseEntity(headers,count,announces);

        } catch (AnnounceException e) {
            logger.info(" AnnounceController -announcesByType:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    /**
     * Cette methode elimine une announce dont on a son Id
     *
     * @param response
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Delete an announce with an ID", response = Response.class)
    @DeleteMapping(value = DELETE, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<Response> delete(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") @Valid Long id) throws AnnounceException {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        Response tpResponse = new Response();

        try {
            logger.info("delete request in");
            createOpentracingSpan("AnnounceController -delete");

            return getResponseDeleteResponseEntity(id, tpResponse, announceService.delete(id), ANNOUNCE_LABEL);
        } catch (AnnounceException e) {
            logger.error("Erreur durant l'elimination de l'annonce {}", e);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }

    @ApiOperation(value = "Retrieve an announce completed with an ID ", response = AnnounceVO.class)
    @GetMapping(value = ANNOUNCE_WS_COMPLETED_BY_ID, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<Object> getAnnounceCompleted(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id ) throws AnnounceException {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

        try {
            logger.info("retrieve announce request in");
            createOpentracingSpan("AnnounceController -get announce completed");

            AnnounceCompletedVO announce = null;
            if (id != null) {
                announce = announceService.announceCompleted(id);
                if (announce == null) {
                    WSCommonResponseVO wsResponse = new WSCommonResponseVO();
                    wsResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    wsResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_INEXIST_CODE_LABEL, ANNOUNCE_LABEL));
                    return new ResponseEntity<>(wsResponse, HttpStatus.NOT_FOUND);
                }

            }
            return new ResponseEntity<>(announce, HttpStatus.OK);
        } catch (AnnounceException e) {
            logger.info(" AnnounceController -get announce Completed:Exception occurred while fetching the response from the database.", e);

            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }
    @ApiOperation(value = "Retrieve an announce with an ID and Source", response = AnnounceVO.class)
    @GetMapping(value = ANNOUNCE_WS_BY_ID_AND_SOURCE, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<Object> getAnnounce(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid @NotNull(message = "Valoriser l'id de l'annonce")  Long id , @RequestParam @Valid Source source ) throws AnnounceException {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

        try {
            logger.info("retrieve announce request in");
            createOpentracingSpan("AnnounceController -get announce");


            AnnounceVO announce = announceService.announce(id);

            if (announce == null) {
                WSCommonResponseVO wsResponse = new WSCommonResponseVO();
                wsResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                wsResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_INEXIST_CODE_LABEL, ANNOUNCE_LABEL));
                return new ResponseEntity<>(wsResponse, HttpStatus.NOT_FOUND);
            }
            announce.setRetCode(source== Source.SHARE ? WebServiceResponseCode.SHARE:WebServiceResponseCode.OK_CODE);


            return new ResponseEntity<>(announce, HttpStatus.OK);
        } catch (AnnounceException e) {
            logger.info(" AnnounceController -get announce:Exception occurred while fetching the response from the database.", e);
            if(source.equals(Source.NOTIFICATION)){
                WSCommonResponseVO wsResponse = new WSCommonResponseVO();
                wsResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                wsResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_INEXIST_CODE_LABEL, ANNOUNCE_LABEL));
                return new ResponseEntity<>(wsResponse, HttpStatus.NOT_FOUND);
            }
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }

    /**
     * Cette methode permet de recuperer toutes les annonces avec pagination
     *
     * @param page
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Retrieve all announces ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval",
                    response = ResponseEntity.class, responseContainer = "List")})
    @GetMapping(value = ANNOUNCES_WS, produces = MediaType.APPLICATION_JSON, headers = HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> announces(@RequestParam @Valid @Positive(message = "la page doit etre nombre positif") int page
            ,@RequestParam  Source source) throws AnnounceException,Exception {

        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();
        logger.info("retrieve  announces request in");
        PageBy pageBy = new PageBy(page, Integer.valueOf(DEFAULT_SIZE));

        try {

            createOpentracingSpan("AnnounceController -announces");
            List<AnnounceVO> announces = new ArrayList();
            int count = 0;

            if(source == null){
                source = Source.OTHER;
            }

            if(source== Source.HOME){

                for (TransportEnum t : Arrays.asList(TransportEnum.values())) {
                    add(announces, announceService.announcesBy(t, new PageBy(0, 4)));
                }
                count = CollectionsUtils.size(announces);

            }else{
                count = announceService.count(null, null);
                announces = announceService.announces(pageBy);
            }

            return getPaginateResponseSearchResponseEntity(  headers, paginateResponse,   count,  announces,pageBy);

        } catch (AnnounceException e) {
            logger.info(" AnnounceController -announces:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = " Add Announce Favoris ", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful create announce favorites",
                    response = String.class, responseContainer = "Object")})
    @RequestMapping(value = WSConstants.USER_ADD_ANNOUNCE_FAVORITE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Response> addAnnounceFavorites(HttpServletRequest request, HttpServletResponse response,
                                                  @RequestBody @Valid UsersAnnounceFavoriteDTO userAnnounceFavoriteDTO) throws Exception {

        logger.info("add a favorite announce into this users" + userAnnounceFavoriteDTO.getUserId());
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        try {
            createOpentracingSpan("UserController - new announce favorite");
            boolean result = announceService.addAnnounceFavorites(userAnnounceFavoriteDTO);
            Response tpResponse = new Response();
            if (result) {
                tpResponse.setRetCode(0);
                tpResponse.setRetDescription(WebServiceResponseCode.ANNOUNCE_FAVORITE_ADD_OK);
                return new ResponseEntity<>(tpResponse, HttpStatus.OK);
            }

            tpResponse.setMessage(WebServiceResponseCode.ANNOUNCE_FAVORITE_ALREADY_EXIST);
            return new ResponseEntity<>(tpResponse, HttpStatus.CONFLICT);

        } catch (UserException e) {
            e.printStackTrace();
        } finally {
            finishOpentracingSpan();
        }

        return null;
    }


    @ApiOperation(value = " Remove Announce Favoris ", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful delete announce favorite By user",
                    response = String.class, responseContainer = "Object")})
    @RequestMapping(value = WSConstants.DELETE_ANNOUNCE_FAVORITE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<Response> deleteAnnounceFavoriteByUser(HttpServletRequest request, HttpServletResponse response,
                                                          @RequestBody @Valid UsersAnnounceFavoriteDTO userAnnounceFavoriteDTO) throws Exception {

        logger.info("remove a favorite announce into this users" + userAnnounceFavoriteDTO.getUserId());
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        try {
            createOpentracingSpan("UserController - remove announce favorite");
            boolean result = announceService.removeAnnounceFavorites(userAnnounceFavoriteDTO);
            Response tpResponse = new Response();
            if (result) {
                tpResponse.setRetCode(0);
                tpResponse.setRetDescription(WebServiceResponseCode.REMOVE_FAVORITE_ANNOUNCE);
                return new ResponseEntity<>(tpResponse, HttpStatus.OK);
            }

            tpResponse.setMessage(WebServiceResponseCode.REMOVE_NOT_OK);
            return new ResponseEntity<>(tpResponse, HttpStatus.OK);

        } catch (UserException e) {
            e.printStackTrace();
        } finally {
            finishOpentracingSpan();
        }

        return null;
    }

    @ApiOperation(value = " list All Announce Favoris By Users ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval announces favorites by user",
                    response = ResponseEntity.class, responseContainer = "List")})
    @GetMapping(value =WSConstants.ANNOUNCES_FAVORITE_BY_USER, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> announcesFavoriteByUser(HttpServletResponse response, HttpServletRequest request,
                                                                         @RequestParam @Valid Long userId,
                                                                         @RequestParam(required = false, defaultValue = DEFAULT_PAGE)@Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                                         @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        HttpHeaders headers = new HttpHeaders();

        PageBy pageBy = new PageBy(page, size);
        logger.info("get announces favorites by user request in");

        try {
            createOpentracingSpan("AnnounceController -announcesFavoriteByUser");
            int count = announceService.count(null, null,null);
            List announces = announceService.announcesFavoritesByUser(userId);

            logger.info("find announces favorites by user request out");
            return getPaginateResponseResponseEntity(  headers,      count,  announces);
        }catch (UserException e) {
            logger.error("Erreur pour recuperer les annonces favoris de l'utilisateur "+ userId);
             e.printStackTrace();
        }finally {
            finishOpentracingSpan();
        }

        return null;
    }

    private void add(List announces, List announce){

        if(CollectionsUtils.isNotEmpty(announce)){
            announces.addAll(announce);
        }
    }
}
