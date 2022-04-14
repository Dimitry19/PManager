package cm.travelpost.tp.ws.controller.rest.city;

import cm.travelpost.tp.city.ent.vo.CityVO;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static cm.travelpost.tp.constant.WSConstants.CITY_WS;


@RestController
@RequestMapping(name = CITY_WS)
@Api(value = "City-service", description = " Read only city  Operations")
public class CityController  extends CommonController {

    protected final Log logger = LogFactory.getLog(CityController.class);



    @ApiOperation(value = "Retrieve a cities", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful search a city",
                    response = CityVO.class, responseContainer = "List")})
    @GetMapping(value = AUTOCOMPLETE, headers = WSConstants.HEADER_ACCEPT,produces = MediaType.APPLICATION_JSON)
    public @ResponseBody ResponseEntity<List> autocomplete(HttpServletResponse response, HttpServletRequest request, @RequestParam ("search") @Valid String search) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

        try {
            logger.info("city autocomplete request in");
            createOpentracingSpan("CityController - autocomplete");

                List<CityVO> cities = cityService.autoComplete(search,true);

            return new ResponseEntity<>(cities, HttpStatus.OK);
        } catch (Exception e) {
            logger.info(" CityController - autocomplete :Exception occurred while fetching the response from the database.", e);
            throw e;

        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = "Retrieve all cities", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful search a city",
                    response = List.class, responseContainer = "List")})
    @GetMapping(value = CITIES_WS, headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public @ResponseBody ResponseEntity<List> cities(HttpServletResponse response,HttpServletRequest request) throws Exception{

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

        try{
            logger.info("cities request in");
            createOpentracingSpan("CityController - cities");

            List<CityVO> cities = cityService.cities(null);

            return new ResponseEntity<>(cities, HttpStatus.OK);

        }catch (Exception e){
            logger.info(" CityController - cities :Exception occurred while fetching the response from the database.", e);

            throw e;

        }finally {
            finishOpentracingSpan();
        }
    }
}
