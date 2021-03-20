package cm.packagemanager.pmanager.ws.controller.rest.announce;


import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.announce.service.AnnounceService;
import cm.packagemanager.pmanager.announce.service.ReservationService;
import cm.packagemanager.pmanager.announce.service.ServiceAnnounce;
import cm.packagemanager.pmanager.common.ent.dto.ResponseDTO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

import static cm.packagemanager.pmanager.ws.controller.rest.CommonController.ANNOUNCE_WS;

@RestController
@RequestMapping(ANNOUNCE_WS)
@Api(value="announces-service", description="Announces Operations")
public class AnnounceController extends CommonController {

	protected final Log logger = LogFactory.getLog(AnnounceController.class);


	@Autowired
	protected AnnounceService announceService;

	@Autowired
	protected ReservationService reservationService;

	@Autowired
	ServiceAnnounce announceTester;

	/**
	 * Cette methode cree unae annonce
	 * @param response
	 * @param request
	 * @param ar
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Create/add an announce ",response = AnnounceVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = AnnounceVO.class, responseContainer = "Object") })
	@RequestMapping(value = ANNOUNCE_WS_CREATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	AnnounceVO  create(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid AnnounceDTO ar) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		AnnounceVO announce = null;
		Response res=new Response();

		try{
			createOpentracingSpan("AnnounceController -create");

			logger.info("create announce request in");
			if (ar!=null){
				announce= announceService.create(ar);

				if(announce!=null){
					announce.setRetDescription(WebServiceResponseCode.ANNOUNCE_CREATE_LABEL);
					announce.setRetCode(WebServiceResponseCode.OK_CODE);
				}else{
					announce=new AnnounceVO();
					announce.setRetCode(WebServiceResponseCode.NOK_CODE);
					announce.setRetDescription(WebServiceResponseCode.ERROR_ANNOUNCE_CREATE_LABEL);
				}
			}
		}
		catch (Exception e)	{
			logger.error("Errore eseguendo add announce: ", e);
			response.setStatus(500);
			response.getWriter().write(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}

		return  announce;
	}

	@ApiOperation(value = "Update an announce ",response = AnnounceVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = AnnounceVO.class, responseContainer = "Object") })
	@PostMapping(value = ANNOUNCE_WS_UPDATE)
	AnnounceVO  update(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid UpdateAnnounceDTO uar) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			createOpentracingSpan("AnnounceController -update");

			if (uar==null)	return null;

			AnnounceVO announce=announceService.update(uar);
			if (announce!=null){
				announce.setRetCode(WebServiceResponseCode.OK_CODE);
				announce.setRetDescription(WebServiceResponseCode.UPDATED_ANNOUNCE_LABEL);
			}else{
				announce=new AnnounceVO();
				announce.setRetCode(WebServiceResponseCode.NOK_CODE);
				announce.setRetDescription(WebServiceResponseCode.ERROR_UPDATE_ANNOUNCE_CODE_LABEL);
			}
			return announce;
		}catch (Exception e){
			response.getWriter().write(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}

		return null;
	}

	/**
	 * Cette methode recherche une annonce en fonction des paramretres de recherche
	 * @param response
	 * @param request
	 * @param asdto
	 * @param page
	 * @param size
	 * @return
	 * @throws Exception
	 */

	@ApiOperation(value = "Search announce function ",response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ResponseEntity.class, responseContainer = "List") })
	@RequestMapping(value =ANNOUNCE_WS_FIND,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody ResponseEntity<PaginateResponse> find(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid AnnounceSearchDTO asdto,
	                                                           @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
	                                                           @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception{


		response.setHeader("Access-Control-Allow-Origin", "*");
		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse=new PaginateResponse();

		logger.info("find  announces request in");
		PageBy pageBy= new PageBy(page,size);

		List<AnnounceVO> announces=null;

		try{
			createOpentracingSpan("AnnounceController -find");

			if (asdto!=null){
				int count = announceService.count(asdto, pageBy);
				if(count==0){
					headers.add(HEADER_TOTAL, Long.toString(count));
				}else{
					announces=announceService.find(asdto,pageBy);
					paginateResponse.setCount(count);
					paginateResponse.setResults(announces);
					headers.add(HEADER_TOTAL, Long.toString(announces.size()));
				}
			}
		}
		catch (Exception e){
			response.getWriter().write(e.getMessage());
			logger.info(" AnnounceController -find:Exception occurred while fetching the response from the database.", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			finishOpentracingSpan();
		}
		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}

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
	@ApiOperation(value = "Retrieve announces by user with an ID",response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ResponseEntity.class, responseContainer = "List") })
	@RequestMapping(value =ANNOUNCE_WS_BY_USER,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public ResponseEntity<PaginateResponse> announcesByUser(HttpServletResponse response, HttpServletRequest request,
	                                                        @RequestParam @Valid Long userId,
	                                                        @RequestParam (required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
	                                                        @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");
		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse=new PaginateResponse();
		logger.info("find announce by user request in");
		PageBy pageBy= new PageBy(page,size);

		List<AnnounceVO> announces=null;


		try{
			createOpentracingSpan("AnnounceController -announcesByUser");

			if (userId!=null){
				int count = announceService.count(null,pageBy);
				if(count==0){
					headers.add(HEADER_TOTAL, Long.toString(count));
				}else{
					announces=announceService.findByUser(userId,pageBy);
					if(CollectionsUtils.isNotEmpty(announces)){
						paginateResponse.setCount(count);
					}
					paginateResponse.setResults(announces);
					headers.add(HEADER_TOTAL, Long.toString(announces.size()));
				}
			}else response.getWriter().write("Utilisateur non existant");
		}
		catch (Exception e){
			//response.getWriter().write(e.getMessage());
			response.getWriter().write("Utilisateur non existant");
			logger.info(" AnnounceController -announcesByUser:Exception occurred while fetching the response from the database.", e);
			return new ResponseEntity<>(HttpStatus.OK);
		}finally {
			finishOpentracingSpan();
		}
		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}


	/**
	 *  Cette methode recherche toutes les annonces d'un utilisateur
	 * @param response
	 * @param request
	 * @param type
	 * @param page
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Retrieve announces by Type",response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ResponseEntity.class, responseContainer = "List") })
	@RequestMapping(value =ANNOUNCE_WS_BY_TYPE,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public ResponseEntity<PaginateResponse> announcesByType(HttpServletResponse response, HttpServletRequest request,
	                                                        @RequestParam @Valid AnnounceType type,
	                                                        @RequestParam (required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
	                                                        @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");
		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse=new PaginateResponse();
		logger.info("find announce by type request in");
		PageBy pageBy= new PageBy(page,size);

		List<AnnounceVO> announces=null;


		try{
			createOpentracingSpan("AnnounceController - announcesByType");

			if (type!=null){
				int count = announceService.count(null,pageBy);
				if(count==0){
					headers.add(HEADER_TOTAL, Long.toString(count));
				}else{
					announces=announceService.findByType(type,pageBy);
					if(CollectionsUtils.isNotEmpty(announces)){
						paginateResponse.setCount(count);
					}
					paginateResponse.setResults(announces);
					headers.add(HEADER_TOTAL, Long.toString(announces.size()));
				}
			}else response.getWriter().write("Utilisateur non existant");
		}
		catch (Exception e){
			//response.getWriter().write(e.getMessage());
			response.getWriter().write("Utilisateur non existant");
			logger.info(" AnnounceController -announcesByUser:Exception occurred while fetching the response from the database.", e);
			return new ResponseEntity<>(HttpStatus.OK);
		}finally {
			finishOpentracingSpan();
		}
		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}

	/**
	 * Cette methode elimine une announce dont on a son Id
	 * @param response
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Delete an announce with an ID",response = Response.class)
	@RequestMapping(value =ANNOUNCE_WS_DELETE,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try{
			logger.info("delete request in");
			createOpentracingSpan("AnnounceController -delete");

			if (id!=null){
				if(announceService.delete(id)){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.CANCELLED_ANNOUNCE_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_DELETE_ANNOUNCE_CODE_LABEL);
				}
			}
		}
		catch (Exception e){
			//response.getWriter().write(e.getMessage());
			pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			pmResponse.setRetDescription(e.getMessage());
		}
		finally {
			finishOpentracingSpan();
		}
		return pmResponse;
	}

	@ApiOperation(value = "Retrieve an announce with an ID",response = AnnounceVO.class)
	@RequestMapping(value =ANNOUNCE_WS_BY_ID,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public AnnounceVO getAnnounce(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws UserException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		AnnounceVO announce=null;

		try{
			logger.info("retrieve announce request in");
			createOpentracingSpan("AnnounceController -get announce");

			if (id!=null){
				announce=announceService.findById(id);
			}
		}
		catch (UserException e){
			//response.getWriter().write(e.getMessage());
			logger.info(" AnnounceController -get announce:Exception occurred while fetching the response from the database.", e);
            throw e;
		}
		finally {
			finishOpentracingSpan();
		}
		return announce;
	}

	/**
	 * Cette methode permet de recuperer toutes les annonces avec pagination
	 * @param page
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Retrieve all announces ", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ResponseEntity.class, responseContainer = "List") })
	@GetMapping(ANNOUNCES_WS)
	public ResponseEntity<PaginateResponse> announces(@RequestParam @Valid @Positive(message = "la page doit etre nombre positif")  int page,
	                                                  @RequestParam (required = false, defaultValue = DEFAULT_SIZE)
	                                                  @Valid @Positive(message = "Page size should be a positive number")  int size) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse=new PaginateResponse();
		logger.info("retrieve  announces request in");
		PageBy pageBy= new PageBy(page,size);

		try {

			createOpentracingSpan("AnnounceController -announces");

			int count = announceService.count(null,pageBy);
			if(count==0){
				headers.add(HEADER_TOTAL, Long.toString(count));
			}else{
				List<AnnounceVO> announces = announceService.announces(pageBy);
				paginateResponse.setCount(count);
				paginateResponse.setResults(announces);
				headers.add(HEADER_TOTAL, Long.toString(announces.size()));
			}
			return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
		}catch (Exception e){
			logger.info(" AnnounceController -announces:Exception occurred while fetching the response from the database.", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			finishOpentracingSpan();
		}
	}

	//@RequestMapping(ANNOUNCE_WS_USER_ID_PAGE_NO)
	@ResponseBody public List<AnnounceVO> getAllPosts(@PathVariable int pageno,@PageableDefault(value=10, page=0) Pageable pageable) throws Exception {

		logger.info("retrieve  announces request in");
		Page page = announceService.announces(pageable);
		return page.getContent();
		//return announceService.announces(pageno,size);

	}


	@RequestMapping(value =ANNOUNCE_WS_RESERVATION,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	ReservationVO reservation(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ReservationDTO reservation) throws Exception{

		try{
			response.setHeader("Access-Control-Allow-Origin", "*");
			logger.info("add reservation request in");

			createOpentracingSpan("AnnounceController - reservation");
			ReservationVO reservationVO =reservationService.addReservation(reservation);
			if(reservationVO==null){
				response.setStatus(org.apache.http.HttpStatus.SC_EXPECTATION_FAILED);
			}else{
				response.setStatus(org.apache.http.HttpStatus.SC_CREATED);

			}
			return reservationVO;

		}catch (Exception e){
				logger.error(e.getMessage());
				throw e;
		}finally {
			finishOpentracingSpan();
		}
	}


	@ApiOperation(value = "Delete reservation",response = Response.class)
	@RequestMapping(value =ANNOUNCE_WS_DELETE_RESERVATION_BY_ID,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public Response deleteReservation(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = null;

		try{
			logger.info("delete reservation request in");
			createOpentracingSpan("AnnounceController -get deleteReservation");

			if (reservationService.deleteReservation(id)){
				pmResponse=new Response();
				pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.RESERV_OK_LABEL);
			}
		}
		catch (UserException e){
			//response.getWriter().write(e.getMessage());
			logger.info(" AnnounceController -get announce:Exception occurred while fetching the response from the database.", e);
			throw e;
		}
		finally {
			finishOpentracingSpan();
		}
		return pmResponse;
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
