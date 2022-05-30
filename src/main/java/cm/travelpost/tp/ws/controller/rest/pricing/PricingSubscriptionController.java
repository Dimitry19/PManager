package cm.travelpost.tp.ws.controller.rest.pricing;

import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.pricing.ent.service.PricingService;
import cm.travelpost.tp.pricing.ent.service.SubscriptionService;
import cm.travelpost.tp.pricing.ent.vo.PricingVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.text.MessageFormat;

@RestController
@RequestMapping( WSConstants.PRICING_WS)
@Api(value = "Pricing and Subscription -service", description = "Pricing and Subscription Operations",tags ="Pricing and Subscription" )
public class PricingSubscriptionController extends CommonController {

	protected final Logger logger = LoggerFactory.getLogger(PricingSubscriptionController.class);

	@Autowired
	PricingService pricingService;

	@Autowired
	SubscriptionService subscriptionService;

	@ApiOperation(value = " Create pricing ", response = PricingVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful update",
					response = PricingVO.class, responseContainer = "Object")})
	@PostMapping(value = CREATE, produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody ResponseEntity<PricingVO> createPricing(HttpServletRequest request,
																 HttpServletResponse response, @RequestParam(value = "amount", required = true) BigDecimal amount) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Create pricing request in");
		try {
			createOpentracingSpan("PricingSubscriptionController - Create pricing");
			PricingVO pricing = pricingService.create(amount);

			pricing.setRetCode(WebServiceResponseCode.OK_CODE);
			pricing.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, "Pricing"));
			return new ResponseEntity<>(pricing, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Erreur durant la creation du pricing", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = " Update pricing ", response = PricingVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful update",
					response = PricingVO.class, responseContainer = "Object")})
	@PutMapping(value = WSConstants.PRICING_WS_UPDATE_PRICING_CODE, produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody ResponseEntity<PricingVO>  updatePricing(HttpServletRequest request,
																  HttpServletResponse response, @PathVariable @Valid String pricingCode, @PathVariable @Valid String token,@RequestParam(value = "amount", required = true) BigDecimal amount) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Update pricing request in");
		try {
			createOpentracingSpan("PricingSubscriptionController - Update pricing");
			PricingVO pricing = pricingService.update(pricingCode,token,amount);

			pricing.setRetCode(WebServiceResponseCode.OK_CODE);
			pricing.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, "Pricing"));
			return new ResponseEntity<>(pricing, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Erreur durant l'ajournement du pricing", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = "Delete pricing ", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful deleted",
					response = Response.class, responseContainer = "Object")})
	@DeleteMapping(value = WSConstants.DELETE, headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody Response deletePricing(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "code", required = true) String code, @RequestParam(value = "token", required = true) String token) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Delete pricing request in");
		Response tpResponse = new Response();
		try {
			createOpentracingSpan("PricingSubscriptionController - Delete pricing");
			boolean deleted= pricingService.delete(code,token);

			if(BooleanUtils.isTrue(deleted)){
				tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				tpResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, "Pricing"));
			} else {
				tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				tpResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, "Pricing"));
			}
		} catch (Exception e) {
			tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			tpResponse.setMessage(e.getMessage());
			logger.error("Erreur durant l'ajournement du pricing {} - code {} - token {}", e, code, token);
			e.printStackTrace();
			throw e;
		} finally {
			finishOpentracingSpan();
		}
		return tpResponse;
	}

	@GetMapping
	public @ResponseBody ResponseEntity<Response>   pricing(HttpServletResponse response, HttpServletRequest request,  @RequestParam(value = "code", required = true) String code,@RequestParam(value = "token", required = true) String token){

		return null;
	}

//	@PostMapping
//	public @ResponseBody ResponseEntity<Response> createSubscription(){
//
//		return null;
//	}
//
//	@PutMapping(value = WSConstants.PRICING_WS_UPDATE_SUBSCRIPTION_CODE, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
//	public @ResponseBody ResponseEntity<Response>  updateSubscription(@PathVariable String subscriptionCode, @PathVariable String token){
//
//		return null;
//	}
//
//	@DeleteMapping
//	public @ResponseBody ResponseEntity<Response>  deleteSubscription(){
//
//		return null;
//	}
//
//	@GetMapping
//	public @ResponseBody ResponseEntity<Response>   subscription(  @RequestParam(value = "code", required = true) String code,@RequestParam(value = "token", required = true) String token){
//
//		return null;
//	}

}