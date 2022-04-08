package cm.packagemanager.pmanager.ws.controller.rest.admin.dashborad;

import cm.packagemanager.pmanager.administrator.ent.enums.DashBoardObjectType;
import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.city.ent.vo.CityVO;
import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.common.exception.DashboardException;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.CommonDTO;
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
	 * Cette methode cree une compagnie aerienne ou une ville
	 *
	 * @param response
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Add a company airlines or city ", response = Object.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful Added Company Airline/ City",
					response = Object.class, responseContainer = "Object")})
	@PostMapping(value = CREATE, consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	ResponseEntity<Object> createCompanyOrCity(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid CommonDTO dto) throws DashboardException,Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");


		try {
			createOpentracingSpan("DashboardController - createCompanyOrCity");

			logger.info("createCompanyOrCity Operation ");

			Object  object = null;

			if (dto != null) {

				switch (dto.getObjectType()){
					case AIRLINE:
						 object  = airplaneService.add(dto.getCode(), dto.getName());

					case CITY:
						object  = cityService.create(dto);

				}
				if (object != null) {
					WSCommonResponseVO wsr = new WSCommonResponseVO();
					wsr.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, "La compagnie aerienne"));
					wsr.setRetCode(WebServiceResponseCode.OK_CODE);
					return  new ResponseEntity<>(wsr, HttpStatus.CREATED);
				}
			}
		} catch (DashboardException e) {
			logger.error("Erreur durant l'execution de  createCompanyOrCity: ", e);
			throw e;
		}  finally {
			finishOpentracingSpan();
		}
		WSCommonResponseVO commonResponse= new WSCommonResponseVO();
		commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
		commonResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_CREATE_LABEL, "La compagnie aerienne"));
		return new ResponseEntity<>(commonResponse, HttpStatus.NOT_ACCEPTABLE);
	}

	@ApiOperation(value = "Update an airline / city ", response = Object.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful Update Object",
					response = Object.class, responseContainer = "Object")})
	@PutMapping(value = UPDATE, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	ResponseEntity<Object> updateCompanyOrCity(HttpServletResponse response, HttpServletRequest request, @PathVariable("code") @Valid String code,
								  @RequestBody @Valid CommonDTO dto) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			createOpentracingSpan("DashboardController - updateCompanyOrCity");

			logger.info("Update Operation ");
			if (dto == null) {
				return null;
			}
			dto.setCode(code);

			AirlineVO airline=null;
			CityVO city=null;

			switch (dto.getObjectType()){
				case AIRLINE:
					airline  =  airplaneService.update(dto);
				case CITY:
					city = cityService.update(dto);

			}

			if (airline != null) {
				airline.setRetCode(WebServiceResponseCode.OK_CODE);
				airline.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, "La compagnie aerienne"));
				return new ResponseEntity<>(airline, HttpStatus.OK);
			}

			if (city != null) {
				city.setRetCode(WebServiceResponseCode.OK_CODE);
				city.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, "La ville"));
				return new ResponseEntity<>(city, HttpStatus.OK);
			}else {
				WSCommonResponseVO  commonResponse = new WSCommonResponseVO();
				commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				commonResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, getMessage(dto.getObjectType())));
				return new ResponseEntity<>(commonResponse, HttpStatus.NOT_FOUND);
			}
		} catch (DashboardException e) {
			logger.error("Erreur durant updateCompanyOrCity " + dto.toString() + "{ }", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}

	/**
	 * Cette methode elimine une objet dont on a son Id
	 *
	 * @param response
	 * @param request
	 * @param o
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Delete a company or city that we get  with an ID", response = Response.class)
	@DeleteMapping(value = DELETE, headers = WSConstants.HEADER_ACCEPT)
	public ResponseEntity<Response> delete(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") @Valid Object o) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try {
			createOpentracingSpan("DashboardController - delete");

			logger.info("delete Operation ");

			if(o instanceof Long){

				Long id = (Long)o;

				if (airplaneService.delete(id)) {
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, "La compagnie aerienne"));
					return new ResponseEntity<>(pmResponse, HttpStatus.OK);

				}

			}

			if(o instanceof String){
				String id = (String) o;
				if (cityService.delete(id)) {
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, "La ville"));
					return new ResponseEntity<>(pmResponse, HttpStatus.OK);
				}
			}
			pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, (String)o));
			return new ResponseEntity<>(pmResponse, HttpStatus.METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			logger.error("Erreur durant l'elimination de l'element avec id" +o+"", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}
	private String getMessage(DashBoardObjectType objectType){
		return (objectType == DashBoardObjectType.AIRLINE)?"La compagnie aerienne":"La ville";
	}
}
