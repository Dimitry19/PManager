package cm.packagemanager.pmanager.ws.controller.rest.announce;


import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.dto.ResponseDTO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import cm.packagemanager.pmanager.ws.responses.PaginateResponse;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import static cm.packagemanager.pmanager.constant.WSConstants.*;

@RestController
@RequestMapping(ANNOUNCE_WS)
@Api(value = "Announce-service", description = "Announces Operations")
public class AnnounceController extends CommonController {

    protected final Log logger = LogFactory.getLog(AnnounceController.class);


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
    ResponseEntity<AnnounceVO> create(HttpServletResponse response, HttpServletRequest request,
                                      @RequestBody @Valid AnnounceDTO ar) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        AnnounceVO announce = null;

        try {
            createOpentracingSpan("AnnounceController -create");

            logger.info("create announce request in");
            if (ar != null) {
                announce = announceService.create(ar);

                if (announce != null) {
                    announce.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, "L'annonce"));
                    announce.setRetCode(WebServiceResponseCode.OK_CODE);
                    return new ResponseEntity<>(announce, HttpStatus.CREATED);
                } else {
                    announce = new AnnounceVO();
                    announce.setRetCode(WebServiceResponseCode.NOK_CODE);
                    announce.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_CREATE_LABEL, "L'annonce"));
                    return new ResponseEntity<>(announce, HttpStatus.NOT_ACCEPTABLE);
                }
            }
        } catch (Exception e) {
            logger.error("Errore eseguendo add announce: ", e);
            response.setStatus(500);
            response.getWriter().write(e.getMessage());
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return null;
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
    ResponseEntity<AnnounceVO> update(HttpServletResponse response, HttpServletRequest request, @PathVariable("id") @Valid long id,
                                      @RequestBody @Valid UpdateAnnounceDTO uar) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            createOpentracingSpan("AnnounceController -update");

            if (uar == null) return null;
            uar.setId(id);

            AnnounceVO announce = announceService.update(uar);
            if (announce != null) {
                announce.setRetCode(WebServiceResponseCode.OK_CODE);
                announce.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, "L'annonce"));
                return new ResponseEntity<>(announce, HttpStatus.OK);
            } else {
                announce = new AnnounceVO();
                announce.setRetCode(WebServiceResponseCode.NOK_CODE);
                announce.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, "L'annonce"));
            }
            return new ResponseEntity<>(announce, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur durant l'ajournement de l'announce " + uar.toString() + "{ }", e);
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
     * @param asdto
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
    ResponseEntity<PaginateResponse> find(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid AnnounceSearchDTO asdto,
                                          @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                          @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {


        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();

        logger.info("find  announces request in");
        PageBy pageBy = new PageBy(page, size);

        List<AnnounceVO> announces = null;

        try {
            createOpentracingSpan("AnnounceController -find");

            if (asdto != null) {
                int count = announceService.count(asdto, pageBy);
                if (count == 0) {
                    headers.add(HEADER_TOTAL, Long.toString(count));
                } else {
                    announces = announceService.find(asdto, pageBy);
                    paginateResponse.setCount(count);
                    paginateResponse.setResults(announces);
                    headers.add(HEADER_TOTAL, Long.toString(announces.size()));
                }
            }
        } catch (Exception e) {
            logger.info(" AnnounceController -find:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
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
                                                            @RequestParam @Valid Long userId,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();
        logger.info("find announce by user request in");
        PageBy pageBy = new PageBy(page, size);

        List<AnnounceVO> announces = null;

        try {
            createOpentracingSpan("AnnounceController -announcesByUser");

            if (userId != null) {
                int count = announceService.count(null, pageBy);
                switch (count) {
                    case 0:
                        headers.add(HEADER_TOTAL, Long.toString(count));
                        break;
                    default:
                        announces = announceService.announcesByUser(userId, pageBy);
                        if (CollectionsUtils.isNotEmpty(announces)) {
                            paginateResponse.setCount(count);
                        }
                        paginateResponse.setResults(announces);
                        headers.add(HEADER_TOTAL, Long.toString(announces.size()));
                        break;
                }
            } else response.getWriter().write("Utilisateur non existant");
        } catch (Exception e) {
            logger.info(" AnnounceController -announcesByUser:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
    }


    /**
     * Cette methode recherche toutes les annonces d'un utilisateur
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
    @RequestMapping(value = ANNOUNCE_WS_BY_TYPE, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> announcesByType(HttpServletResponse response, HttpServletRequest request,
                                                            @RequestParam @Valid AnnounceType type,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                            @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();
        logger.info("find announce by type request in");
        PageBy pageBy = new PageBy(page, size);

        List<AnnounceVO> announces = null;


        try {
            createOpentracingSpan("AnnounceController - announcesByType");

            int count = announceService.count(null, pageBy);
            if (count == 0) {
                headers.add(HEADER_TOTAL, Long.toString(count));
            } else {
                announces = announceService.announcesByType(type, pageBy);
                if (CollectionsUtils.isNotEmpty(announces)) {
                    paginateResponse.setCount(count);
                }
                paginateResponse.setResults(announces);
                headers.add(HEADER_TOTAL, Long.toString(announces.size()));
            }
        } catch (Exception e) {
            logger.info(" AnnounceController -announcesByType:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
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
    public ResponseEntity<Response> delete(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") @Valid Long id) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        Response pmResponse = new Response();

        try {
            logger.info("delete request in");
            createOpentracingSpan("AnnounceController -delete");

            if (id != null) {
                if (announceService.delete(id)) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, "L'annonce"));

                    return new ResponseEntity<>(pmResponse, HttpStatus.OK);

                } else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, "L'annonce"));

                }
            }
            return new ResponseEntity<>(pmResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur durant l'elimination de l'annonce {}", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = "Retrieve an announce with an ID", response = AnnounceVO.class)
    @GetMapping(value = ANNOUNCE_WS_BY_ID, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<AnnounceVO> getAnnounce(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            logger.info("retrieve announce request in");
            createOpentracingSpan("AnnounceController -get announce");
            AnnounceVO announce = null;
            if (id != null) {
                announce = announceService.announce(id);
                if (announce == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(announce, HttpStatus.OK);
        } catch (UserException e) {
            logger.info(" AnnounceController -get announce:Exception occurred while fetching the response from the database.", e);
            throw e;
        } catch (Exception e) {
            logger.info(" AnnounceController -get announce:Exception occurred while fetching the response from the database.", e);
            throw e;

        } finally {
            finishOpentracingSpan();
        }
    }

    /**
     * Cette methode permet de recuperer toutes les annonces avec pagination
     *
     * @param page
     * @param size
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
    public ResponseEntity<PaginateResponse> announces(@RequestParam @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                      @RequestParam(required = false, defaultValue = DEFAULT_SIZE)
                                                      @Valid @Positive(message = "Page size should be a positive number") int size) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        PaginateResponse paginateResponse = new PaginateResponse();
        logger.info("retrieve  announces request in");
        PageBy pageBy = new PageBy(page, size);

        try {

            createOpentracingSpan("AnnounceController -announces");

            int count = announceService.count(null, pageBy);
            if (count == 0) {
                headers.add(HEADER_TOTAL, Long.toString(count));
            } else {
                List<AnnounceVO> announces = announceService.announces(pageBy);
                paginateResponse.setCount(count);
                paginateResponse.setResults(announces);
                headers.add(HEADER_TOTAL, Long.toString(announces.size()));
            }
            return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.info(" AnnounceController -announces:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    //@RequestMapping(ANNOUNCE_WS_USER_ID_PAGE_NO)
    @ResponseBody
    public List<AnnounceVO> getAllPosts(@PathVariable("page") int pageno, @PageableDefault(value = 10, page = 0) Pageable pageable) throws Exception {

        logger.info("retrieve  announces request in");
        Page page = announceService.announces(pageable);
        return page.getContent();
        //return announceService.announces(pageno,size);

    }


    //https://examples.javacodegeeks.com/spring-boot-pagination-tutorial/
    //@GetMapping(value = ANNOUNCE_WS_ALL, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<ResponseDTO> getAllMovies(
            @RequestParam(name = "pageNumber", defaultValue = "0") final int pageNumber,    // In spring the default page number starts with '0'.
            @RequestParam(name = "pageSize", defaultValue = "2") final int pageSize) {
		/*logger.info("Getting all the movies from the database for page-number= {} and page-size= {}.", pageNumber,
				pageSize);*/
        final ResponseEntity<ResponseDTO> responseEntity;
        try {
            final Pageable pageable = PageRequest.of(pageNumber, pageSize);
            final Page<AnnounceVO> announces = announceTester.getAllMovies(pageable);
            responseEntity = createResponseDto(announces);
        } catch (final Exception e) {
            logger.info("Exception occurred while fetching the response from the database.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    private ResponseEntity<ResponseDTO> createResponseDto(final Page<AnnounceVO> moviesPage) {
        final List<AnnounceVO> movies = moviesPage.getContent();
        final ResponseEntity<ResponseDTO> responseEntity;
        if (CollectionUtils.isEmpty(movies)) {
            logger.info("Returning an empty list as no movies are fetched from the database.");
            responseEntity = new ResponseEntity<>(ResponseDTO.create(Collections.emptyList(), 0, 0, 0, null, null),
                    HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>(ResponseDTO.create(movies, (int) moviesPage.getTotalElements(),
                    moviesPage.getTotalPages(), moviesPage.getNumber(), moviesPage.isFirst(),
                    moviesPage.isLast()), HttpStatus.OK);
        }
        return responseEntity;
    }

}
