package cm.travelpost.tp.ws.controller.rest.admin.dashboard;

import cm.framework.ds.common.ent.vo.WSCommonResponseVO;
import cm.travelpost.tp.administrator.ent.enums.DashBoardObjectType;
import cm.travelpost.tp.airline.ent.vo.AirlineIdVO;
import cm.travelpost.tp.airline.ent.vo.AirlineVO;
import cm.travelpost.tp.city.ent.vo.CityVO;
import cm.travelpost.tp.common.Constants;
import cm.travelpost.tp.common.exception.DashboardException;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.CommonDTO;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.Set;

import static cm.travelpost.tp.administrator.ent.enums.DashBoardObjectType.AIRLINE;

@RestController
@RequestMapping(WSConstants.DASHBOARD_WS)
@Api(value = "Dashboard-service", description = "Dashboard admin Operations")
public class DashboardController extends CommonController {

	protected final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	 public final static String LA_COMPAGNIE_AERIENNE ="La compagnie aerienne";
	 public final static String LA_VILLE ="La ville";
	/**
	 * Cette methode cree une compagnie aerienne ou une ville
	 *
	 * @param response
	 * @param request
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Add a company airlines or city ", response = AirlineVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful Added Company Airline/ City",
					response = AirlineVO.class, responseContainer = "Object")})
	@PostMapping(value = WSConstants.CREATE, consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	ResponseEntity<Object> createCompanyOrCity(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid CommonDTO dto) throws DashboardException,Exception{

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		try {
			createOpentracingSpan("DashboardController - createCompanyOrCity");

			logger.info("createCompanyOrCity Operation ");
			AirlineVO airline=null;
			CityVO city=null;
			Object o=null;
			if (dto != null) {

				if (dto.getObjectType() == AIRLINE) {
						o = airlineService.add(dto.getCode(), dto.getName());
				}else{
						o  = cityService.create(dto);
				}
				if (o instanceof AirlineVO) {

					airline = (AirlineVO)o;
					airline.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, "L'element"));
					airline.setRetCode(WebServiceResponseCode.OK_CODE);
					return  new ResponseEntity<>(airline, HttpStatus.CREATED);
				}

				if (o instanceof CityVO) {
					city = (CityVO)o;
					return  new ResponseEntity<>(city, HttpStatus.CREATED);
				}
			}
		} catch (DashboardException e) {
			logger.error("Erreur durant l'execution de  createCompanyOrCity: ", e);
			throw e;
		}  finally {
			finishOpentracingSpan();
		}
		WSCommonResponseVO wsCommonResponse = new WSCommonResponseVO();
		wsCommonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
		wsCommonResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_CREATE_LABEL, "L'element"));
		return  new ResponseEntity<>(wsCommonResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ApiOperation(value = "Update an airline / city ", response = AirlineVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful Update Object",
					response = AirlineVO.class, responseContainer = "Object")})
	@PutMapping(value = WSConstants.UPDATE, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	public @ResponseBody ResponseEntity<Object> updateCompanyOrCity(HttpServletResponse response, HttpServletRequest request, @PathVariable("code") @Valid String code,
								  @RequestBody @Valid CommonDTO dto) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

		try {
			createOpentracingSpan("DashboardController - updateCompanyOrCity");

			logger.info("Update Operation ");
			if (dto == null) {
				return null;
			}
			dto.setCode(code);

			AirlineVO airline=null;
			CityVO city=null;

			if (dto.getObjectType() == AIRLINE) {
				airline = airlineService.update(dto);
			}else{
				city = cityService.update(dto);
			}

			if (airline != null) {
				airline.setRetCode(WebServiceResponseCode.OK_CODE);
				airline.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, LA_COMPAGNIE_AERIENNE));
				return new ResponseEntity<>(airline, HttpStatus.OK);
			}

			if (city != null) {
				return new ResponseEntity<>(city, HttpStatus.OK);
			}else {
				WSCommonResponseVO  commonResponse = new WSCommonResponseVO();
				commonResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				commonResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, getMessage(dto.getObjectType())));
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
	@DeleteMapping(value = WSConstants.DELETE, headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody ResponseEntity<Response> delete(HttpServletResponse response, HttpServletRequest request, @RequestParam("id") @Valid String o) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		Response tpResponse = new Response();

		try {
			createOpentracingSpan("DashboardController - delete");

			logger.info("delete Operation ");

			if(o.contains("_"+Constants.DEFAULT_TOKEN)){

				String code = o.substring(0,o.lastIndexOf("_"+Constants.DEFAULT_TOKEN));
				AirlineIdVO id = new AirlineIdVO();
				id.setToken(Constants.DEFAULT_TOKEN);
				id.setCode(code);
				if (airlineService.delete(id)) {
					tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					tpResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, LA_COMPAGNIE_AERIENNE));
					return new ResponseEntity<>(tpResponse, HttpStatus.OK);
				}
			}else{
				    cityService.delete(o);
					tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					tpResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, "La ville"));
					return new ResponseEntity<>(tpResponse, HttpStatus.OK);
			}
			tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			tpResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, o));
			return new ResponseEntity<>(tpResponse, HttpStatus.METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			logger.error("Erreur durant l'elimination de element avec id {} - {}", o, e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}
	@ApiOperation(value = "All countries ", response = Set.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful countries retrieved",
					response = Set.class, responseContainer = "Object")})
	@GetMapping(value = WSConstants.DASHBOARD_COUNTRIES,produces = MediaType.APPLICATION_JSON)
	public @ResponseBody ResponseEntity<?>  countries(HttpServletRequest request, HttpServletResponse response) throws Exception { client.countriesAndCities();

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		Response tpResponse = new Response();

		try {
			createOpentracingSpan("DashboardController - countries");
			logger.info("countries ");
			return new ResponseEntity<>(client.countriesAndCities(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Erreur durant les pays {}", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}
	@ApiOperation(value = "All countries infos ", response = Set.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful countries infos retrieved",
					response = Set.class, responseContainer = "Object")})
	@GetMapping(value = WSConstants.DASHBOARD_COUNTRIES_AND_CITIES_INFOS,produces = MediaType.APPLICATION_JSON)
	public @ResponseBody ResponseEntity<?>  countriesInfos(HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		Response tpResponse = new Response();

		try {
			createOpentracingSpan("DashboardController - countriesInfos");
			logger.info("countriesInfos ");
			return new ResponseEntity<>(client.countriesInfos(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Erreur durant les pays {}", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = "All cities of a country ", response = Set.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieved all cities of a country ",
					response = Set.class, responseContainer = "Object")})
	@GetMapping(value = WSConstants.DASHBOARD_CITIES_OF_COUNTRY,produces = MediaType.APPLICATION_JSON)
	public @ResponseBody ResponseEntity<?>  cities(HttpServletRequest request, HttpServletResponse response,
													@PathVariable(name = "country",required = true) @Valid @NotNull(message = "valorisez le nom du pays") String country) throws Exception { client.countriesAndCities();

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		Response tpResponse = new Response();

		try {
			createOpentracingSpan("DashboardController - cities");
			logger.info("cities of a country ");
			return new ResponseEntity<>(client.citiesFromCountry(country), HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Erreur durant la recuperation des villes du pays {} - {}",country, e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}
	private String getMessage(DashBoardObjectType objectType){
		return (objectType == AIRLINE)? LA_COMPAGNIE_AERIENNE :LA_VILLE;
	}
}
