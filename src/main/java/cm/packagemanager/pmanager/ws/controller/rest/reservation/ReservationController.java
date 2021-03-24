package cm.packagemanager.pmanager.ws.controller.rest.reservation;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.announce.ent.service.ReservationService;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ValidateReservationDTO;
import cm.packagemanager.pmanager.ws.responses.PaginateResponse;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import javax.ws.rs.core.MediaType;
import java.util.List;

import static cm.packagemanager.pmanager.constant.WSConstants.*;

@RestController
@RequestMapping(RESERVATION_WS)
@Api(value="reservations-service", description="Reservation Operations")
public class ReservationController extends CommonController {


	protected final Log logger = LogFactory.getLog(ReservationController.class);

	@Autowired
	protected ReservationService reservationService;



	@ApiOperation(value = "create an reservation ",response = ReservationVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ReservationVO.class, responseContainer = "Object") })
	@RequestMapping(value =ADD,method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	ReservationVO reservation(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ReservationDTO reservationDTO) throws Exception{

		try{
			response.setHeader("Access-Control-Allow-Origin", "*");
			logger.info("add reservation request in");

			createOpentracingSpan("ReservationController - reservation");
			ReservationVO reservation =reservationService.addReservation(reservationDTO);
			if(reservation==null){
				reservation=new ReservationVO();
				reservation.setRetDescription(WebServiceResponseCode.ERROR_RESERV_CREATE_LABEL);
				reservation.setRetCode(WebServiceResponseCode.NOK_CODE);
				response.setStatus(org.apache.http.HttpStatus.SC_EXPECTATION_FAILED);
			}else{
				reservation.setRetDescription(WebServiceResponseCode.RESERV_CREATE_LABEL);
				reservation.setRetCode(WebServiceResponseCode.OK_CODE);
				response.setStatus(org.apache.http.HttpStatus.SC_CREATED);
			}

			return reservation;
		}catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}finally {
			finishOpentracingSpan();
		}
	}


	@ApiOperation(value = "Update an reservation ",response = ReservationVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ReservationVO.class, responseContainer = "Object") })
	@PostMapping(value = UPDATE)
	ReservationVO  updateReservation(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid UpdateReservationDTO urr) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			createOpentracingSpan("ReservationController -update");

			if (urr==null)	return null;

			ReservationVO reservation=reservationService.updateReservation(urr);
			if (reservation!=null){
				reservation.setRetCode(WebServiceResponseCode.OK_CODE);
				reservation.setRetDescription(WebServiceResponseCode.UPDATED_RESERV_LABEL);
			}else{
				reservation=new ReservationVO();
				reservation.setRetCode(WebServiceResponseCode.NOK_CODE);
				reservation.setRetDescription(WebServiceResponseCode.ERROR_UPDATE_RESERV_CODE_LABEL);
			}
			return reservation;
		}catch (Exception e){
			response.getWriter().write(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}

		return null;
	}

	@ApiOperation(value = "Delete reservation",response = Response.class)
	@RequestMapping(value =DELETE,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public Response deleteReservation(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();
		;

		try{
			logger.info("delete reservation request in");
			createOpentracingSpan("ReservationController - delete reservation");

			if (reservationService.deleteReservation(id)){
				pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.CANCELLED_RESERV_OK_LABEL);
			}else{
				pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.ERROR_DELETE_RESERV_CODE_LABEL);
			}
		}
		catch (Exception e){
			//response.getWriter().write(e.getMessage());
			logger.info(" ReservationController - delete reservation:Exception occurred while fetching the response from the database.", e);
			throw e;
		}
		finally {
			finishOpentracingSpan();
		}
		return pmResponse;
	}


	@ApiOperation(value = "Validate an reservation ",response = AnnounceVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful validate",
					response = Response.class, responseContainer = "Object") })
	@PostMapping(value = VALIDATE)
	Response  validate(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid ValidateReservationDTO urr) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			createOpentracingSpan("ReservationController - validate");
			Response pmResponse=new Response();
			if (urr==null)	return null;

			if (reservationService.validate(urr)){
				response.setStatus(200);
				pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.UPDATED_RESERV_LABEL);
			}else{
				response.setStatus(404);
				pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				pmResponse.setRetDescription(WebServiceResponseCode.ERROR_UPDATE_RESERV_CODE_LABEL);
			}
			return pmResponse;
		}catch (Exception e){
			response.getWriter().write(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}

		return null;
	}

	/**
	 *  Cette methode recherche toutes les reservations d'un utilisateur
	 * @param response
	 * @param request
	 * @param userId
	 * @param page
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Retrieve reservations by user with an ID",response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ResponseEntity.class, responseContainer = "List") })
	@RequestMapping(value =BY_USER,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public ResponseEntity<PaginateResponse> reservationsByUser(HttpServletResponse response, HttpServletRequest request,
	                                                           @RequestParam @Valid Long userId,
	                                                           @RequestParam (required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
	                                                           @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");
		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse=new PaginateResponse();
		logger.info("find reservation by user request in");
		PageBy pageBy= new PageBy(page,size);

		List<ReservationVO> reservations=null;

		try{
			createOpentracingSpan("ReservationController -reservationsByUser");

			if (userId!=null){

				int count = reservationService.count(userId,pageBy,true);
				switch (count) {
					case 0:
						headers.add(HEADER_TOTAL, Long.toString(count));
						break;
					default:
						reservations=reservationService.findByUser(userId,pageBy);
						if(CollectionsUtils.isNotEmpty(reservations)){
							paginateResponse.setCount(count);
						}
						paginateResponse.setResults(reservations);
						headers.add(HEADER_TOTAL, Long.toString(reservations.size()));
						break;
				}
			}else response.getWriter().write("Utilisateur non existant");
		}
		catch (Exception e){
			//response.getWriter().write(e.getMessage());
			response.getWriter().write("Utilisateur non existant");
			logger.info(" ReservationController - reservationsByUser:Exception occurred while fetching the response from the database.", e);
			return new ResponseEntity<>(HttpStatus.OK);
		}finally {
			finishOpentracingSpan();
		}
		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}

	/**
	 *  Cette methode recherche toutes les reservations d'une annonce
	 * @param response
	 * @param request
	 * @param announceId
	 * @param page
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Retrieve reservations by an announce with an ID",response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ResponseEntity.class, responseContainer = "List") })
	@RequestMapping(value =RESERVATION_WS_BY_ANNOUNCE,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public ResponseEntity<PaginateResponse> reservationsByAnnounce(HttpServletResponse response, HttpServletRequest request,
	                                                           @RequestParam @Valid Long announceId,
	                                                           @RequestParam (required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
	                                                           @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");
		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse=new PaginateResponse();
		logger.info("find reservation by announce request in");
		PageBy pageBy= new PageBy(page,size);

		List<ReservationVO> reservations=null;

		try{
			createOpentracingSpan("ReservationController -reservations by announce");

			if (announceId!=null){
				int count = reservationService.count(announceId,pageBy,false);
				if(count==0){
					headers.add(HEADER_TOTAL, Long.toString(count));
				}else{
					reservations=reservationService.findByAnnounce(announceId,pageBy);
					if(CollectionsUtils.isNotEmpty(reservations)){
						paginateResponse.setCount(count);
					}
					paginateResponse.setResults(reservations);
					headers.add(HEADER_TOTAL, Long.toString(reservations.size()));
				}
			}else response.getWriter().write("Utilisateur non existant");
		}
		catch (Exception e){
			//response.getWriter().write(e.getMessage());
			response.getWriter().write("Annonce non existante");
			logger.info(" ReservationController - reservationsByAnnounce:Exception occurred while fetching the response from the database.", e);
			return new ResponseEntity<>(HttpStatus.OK);
		}finally {
			finishOpentracingSpan();
		}
		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}

}
