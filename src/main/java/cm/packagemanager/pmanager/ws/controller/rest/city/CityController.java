package cm.packagemanager.pmanager.ws.controller.rest.city;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.city.ent.vo.CityVO;
import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.common.exception.AnnounceException;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.controller.rest.announce.AnnounceController;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.List;

import static cm.packagemanager.pmanager.constant.WSConstants.CITY_WS;


@RestController
@RequestMapping(name = CITY_WS)
@Api(value = "City-service", description = " Read only city  Operations")
public class CityController  extends CommonController {

    protected final Log logger = LogFactory.getLog(AnnounceController.class);



    @ApiOperation(value = "Retrieve an announce with an ID and Source", response = AnnounceVO.class)
    @GetMapping(value = AUTOCOMPLETE, headers = WSConstants.HEADER_ACCEPT,produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Object> autocomplete(HttpServletResponse response, HttpServletRequest request, @RequestParam ("search") @Valid String search) throws AnnounceException {

        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            logger.info("city autocomplete request in");
            createOpentracingSpan("CityController - autocomplete");

                List<CityVO> cities = cityService.autoComplete(search,true);

            return new ResponseEntity<>(cities, HttpStatus.OK);
        } catch (AnnounceException e) {
            logger.info(" CityController - autocomplete :Exception occurred while fetching the response from the database.", e);
            WSCommonResponseVO wsResponse = new WSCommonResponseVO();
            wsResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            wsResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_INEXIST_CODE_LABEL, "L'annonce"));
            return new ResponseEntity<>(wsResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }
}
