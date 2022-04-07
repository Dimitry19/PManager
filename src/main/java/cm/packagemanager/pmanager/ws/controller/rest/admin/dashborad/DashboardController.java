package cm.packagemanager.pmanager.ws.controller.rest.admin.dashborad;

import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.common.exception.DashboardException;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.airplane.AirlineDTO;
import cm.packagemanager.pmanager.ws.requests.airplane.UpdateAirlineDTO;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;

import static cm.packagemanager.pmanager.constant.WSConstants.DASHBOARD_WS;

@RestController
@RequestMapping(DASHBOARD_WS)
@Api(value = "Dashboard-service", description = "Dashboard admin Operations")
public class DashboardController extends CommonController {
	//Implementer le controle tel que seul l'admin peut operer ici


	/**
	 * Cette methode cree une compagnie aerienne
	 *
	 * @param response
	 * @param request
	 * @param ac
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Add a company airlines ", response = AnnounceVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful Added Company Airline",
					response = AirlineVO.class, responseContainer = "Object")})
	@PostMapping(value = AIRLINE_CREATE, consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	ResponseEntity<Object> addCompany(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid AirlineDTO ac) throws DashboardException,Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");


		try {
			createOpentracingSpan("DashboardController - AddCompany");

			logger.info("Add Company ");
			if (ac != null) {
				AirlineVO airline  = airplaneService.add(ac.getCode(), ac.getName());

				if (airline != null) {
					airline.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, "La compagnie aerienne"));
					airline.setRetCode(WebServiceResponseCode.OK_CODE);

					return  new ResponseEntity<>(airline, HttpStatus.CREATED);
				}
			}
		} catch (DashboardException e) {
			logger.error("Erreur durant l'execution de  add Company: ", e);
			throw e;
		}  finally {
			finishOpentracingSpan();
		}
		WSCommonResponseVO commonResponse= new WSCommonResponseVO();
		commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
		commonResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_CREATE_LABEL, "La compagnie aerienne"));
		return new ResponseEntity<>(commonResponse, HttpStatus.NOT_ACCEPTABLE);
	}

	@ApiOperation(value = "Update an airline ", response = AnnounceVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful Update Airline",
					response = AirlineVO.class, responseContainer = "Object")})
	@PutMapping(value = AIRLINE_UPD, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	ResponseEntity<Object> update(HttpServletResponse response, HttpServletRequest request, @PathVariable("code") @Valid String code,
								  @RequestBody @Valid UpdateAirlineDTO ua) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			createOpentracingSpan("AirlineController - update");

			if (ua == null) return null;
			ua.setCode(code);

			AirlineVO airline = airplaneService.update(ua);
			if (airline != null) {
				airline.setRetCode(WebServiceResponseCode.OK_CODE);
				airline.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, "La compagnie aerienne"));
				return new ResponseEntity<>(airline, HttpStatus.OK);
			} else {
				WSCommonResponseVO  commonResponse = new WSCommonResponseVO();
				commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				commonResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, "La compagnie aerienne"));
				return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
			}
		} catch (DashboardException e) {
			logger.error("Erreur durant l'ajournement de la compagnie " + ua.toString() + "{ }", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}

	/**
	 * Cette methode elimine une compagnie dont on a son Id
	 *
	 * @param response
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Delete a company with an ID", response = Response.class)
	@DeleteMapping(value = DELETE, headers = WSConstants.HEADER_ACCEPT)
	public ResponseEntity<Response> delete(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") @Valid Long id) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try {
			logger.info("delete request in");
			createOpentracingSpan("AirlineController - delete");

			if (id != null) {
				if (airplaneService.delete(id)) {
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, "La compagnie aerienne"));

					return new ResponseEntity<>(pmResponse, HttpStatus.OK);

				} else {
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, "La compagnie aerienne"));

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

}
