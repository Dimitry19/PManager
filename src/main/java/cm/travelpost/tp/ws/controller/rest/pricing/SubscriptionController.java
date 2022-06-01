package cm.travelpost.tp.ws.controller.rest.pricing;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.common.enums.OperationEnum;
import cm.travelpost.tp.common.exception.SubscriptionException;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.pricing.ent.service.SubscriptionService;
import cm.travelpost.tp.pricing.ent.vo.SubscriptionVO;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.pricing.CreateSubscriptionDTO;
import cm.travelpost.tp.ws.requests.pricing.ManageSubscriptionUserDTO;
import cm.travelpost.tp.ws.requests.pricing.UpdateSubscriptionDTO;
import cm.travelpost.tp.ws.responses.PaginateResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping( WSConstants.SUBSCRIPTION_PRICING_WS)
@Api(value = "Subscription -service", description = "Subscription Operations",tags ="Subscription" )
public class SubscriptionController extends CommonController {

	protected final Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

	private static String labelSubscription ="abonnement";

	@Autowired
	SubscriptionService subscriptionService;

	@ApiOperation(value = " Create subscription ", response = SubscriptionVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful update",
					response = SubscriptionVO.class, responseContainer = "Object")})
	@PostMapping(value = CREATE, consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody ResponseEntity<SubscriptionVO> create(HttpServletRequest request,
																 HttpServletResponse response, @RequestBody @Valid CreateSubscriptionDTO dto) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Create subscription request in");
		try {
			createOpentracingSpan("SubscriptionController - Create subscription");
			SubscriptionVO subscription = subscriptionService.create(dto);

			subscription.setRetCode(WebServiceResponseCode.OK_CODE);
			subscription.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, labelSubscription));
			return new ResponseEntity<>(subscription, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Erreur durant la creation du subscription", e);
			throw new SubscriptionException(e.getMessage());
		} finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = " Update subscription ", response = SubscriptionVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful update",
					response = SubscriptionVO.class, responseContainer = "Object")})
	@PutMapping(value = WSConstants.SUBSCRIPTION_WS_UPDATE_SUBSCRIPTION_CODE, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody ResponseEntity<SubscriptionVO>  update(HttpServletRequest request,
																  HttpServletResponse response, @PathVariable @Valid String code, @PathVariable @Valid String token,
																  @RequestBody @Valid UpdateSubscriptionDTO dto) throws Exception {
		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Update subscription request in");
		try {
			createOpentracingSpan("subscriptionSubscriptionController - Update subscription");
			SubscriptionVO subscription = subscriptionService.update(code,token,dto);

			subscription.setRetCode(WebServiceResponseCode.OK_CODE);
			subscription.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, labelSubscription));
			return new ResponseEntity<>(subscription, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Erreur durant l'ajournement du subscription", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = "Delete subscription ", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful deleted",
					response = Response.class, responseContainer = "Object")})
	@DeleteMapping(value = WSConstants.DELETE, headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "code", required = true) String code, @RequestParam(value = "token", required = true) String token) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Delete subscription request in");
		Response tpResponse = new Response();
		try {
			createOpentracingSpan("subscriptionSubscriptionController - Delete subscription");
			boolean deleted= subscriptionService.delete(code,token);

			if(BooleanUtils.isTrue(deleted)){
				tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				tpResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, labelSubscription));
			} else {
				tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				tpResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, labelSubscription));
			}
		} catch (Exception e) {
			tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			tpResponse.setMessage(e.getMessage());
			logger.error("Erreur durant l'ajournement du subscription {} - code {} - token {}", e, code, token);
			e.printStackTrace();
			throw e;
		} finally {
			finishOpentracingSpan();
		}
		return tpResponse;
	}

	@ApiOperation(value = "Retrieve subscription by ID", response = SubscriptionVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval announces by transport",
					response = SubscriptionVO.class, responseContainer = "Object")})
	@GetMapping(value = WSConstants.SUBSCRIPTION_WS_GET,produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody ResponseEntity<?>  subscription(HttpServletResponse response, HttpServletRequest request,  @RequestParam(value = "code", required = true) String code,@RequestParam(value = "token", required = true) String token) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Get subscription request in");
		Response tpResponse = new Response();
		try {
			createOpentracingSpan("subscriptionSubscriptionController - Get subscription");
			SubscriptionVO subscription = (SubscriptionVO) subscriptionService.object(code,token);

			if(subscription!=null){
				return new ResponseEntity<>(subscription, HttpStatus.OK);
			}
			throw  new SubscriptionException("Abonnement non trouvé");
		} catch (Exception e) {
			tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			tpResponse.setMessage(e.getMessage());
			logger.error("Erreur durant la recuperation du Abonnement {} - code {} - token {}", e, code, token);
			e.printStackTrace();
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = "Retrieve all subscriptions ", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval",
					response = ResponseEntity.class, responseContainer = "List")})
	@GetMapping(value = SUBSCRIPTION_WS_ALL, produces = MediaType.APPLICATION_JSON, headers = HEADER_ACCEPT)
	public ResponseEntity<PaginateResponse> subscriptions(@RequestParam @Valid @Positive(message = "la page doit etre nombre positif") int page) throws SubscriptionException {

		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse = new PaginateResponse();
		logger.info("retrieve  subscriptions request in");
		PageBy pageBy = new PageBy(page, Integer.valueOf(DEFAULT_SIZE));

		try {

			createOpentracingSpan("subscriptionSubscriptionController -subscriptions");
			int count = subscriptionService.count(null);
			List<SubscriptionVO> subscriptions = subscriptionService.all(pageBy);

			return getPaginateResponseSearchResponseEntity(  headers, paginateResponse,   count,  subscriptions,pageBy);

		} catch (Exception e) {
			logger.info(" subscriptionSubscriptionController -subscription:Exception occurred while fetching the response from the database.", e);
			throw new SubscriptionException(e.getMessage());
		} finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = "Retrieve subscription by type", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval announces by transport",
					response = ResponseEntity.class, responseContainer = "List")})
	@GetMapping(value = SUBSCRIPTION_WS_BY_TYPE ,  headers = WSConstants.HEADER_ACCEPT)
	public ResponseEntity<SubscriptionVO> subscriptionByType(HttpServletResponse response, HttpServletRequest request,
																 @RequestParam @Valid SubscriptionPricingType type) throws SubscriptionException,Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info("find subscription by price request in");

		try {
			createOpentracingSpan("subscriptionSubscriptionController - subscription by type");
			SubscriptionVO subscription = (SubscriptionVO) subscriptionService.byType(type);
			if(subscription!=null){
				return new ResponseEntity<>(subscription, HttpStatus.OK);
			}
			throw  new SubscriptionException("subscription avec le type ["+ type +"] non trouvé");
		} catch (Exception e) {
			logger.info(" subscriptionSubscriptionController - subscription by price:Exception occurred while fetching the response from the database.", e);
			throw new SubscriptionException(e.getMessage());
		} finally {
			finishOpentracingSpan();
		}
	}

	@ApiOperation(value = " Add/Remove subscription to user(s)", response = SubscriptionVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful update",
					response = SubscriptionVO.class, responseContainer = "Object")})
	@PostMapping(value = SUBSCRIPTION_WS_ADD_USERS, consumes = MediaType.APPLICATION_JSON,produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody Response manageSubscriptionUser(HttpServletRequest request,
																		   HttpServletResponse response, @RequestBody @Valid ManageSubscriptionUserDTO dto) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Create subscription request in");
		Response tpResponse = new Response();
		try {
			createOpentracingSpan("SubscriptionController - Create subscription");
			boolean added = subscriptionService.addOrRemoveToUser(dto);
			if(BooleanUtils.isTrue(added)){
				tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
				tpResponse.setRetDescription(dto.getOperation()== OperationEnum.ADD ? WebServiceResponseCode.ADD_SUBSCRIPTION_TO_USERS_OK_LABEL:WebServiceResponseCode.REMOVE_SUBSCRIPTION_TO_USERS_OK_LABEL);
			} else {
				tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
				tpResponse.setMessage(WebServiceResponseCode.ERROR_ADD_SUBSCRIPTION_TO_USERS_LABEL);
			}

		} catch (Exception e) {
			tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			tpResponse.setMessage(e.getMessage());
			logger.error("Erreur durant l'ajournement du subscription {} - code {} - token {}", e, dto.getCode(), dto.getToken());
			e.printStackTrace();
			throw e;
		} finally {
			finishOpentracingSpan();
		}
		return tpResponse;
	}

	@ApiOperation(value = "Retrieve users from subscription by ID", response = SubscriptionVO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful retrieval announces by transport",
					response = List.class, responseContainer = "List")})
	@GetMapping(value = WSConstants.SUBSCRIPTION_WS_GET_USERS,produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML},headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody ResponseEntity<?>  users(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid @Positive(message = "la page doit etre nombre positif") int page,
												  @RequestParam(value = "code", required = true) String code,@RequestParam(value = "token", required = true) String token) throws Exception {

		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
		logger.info(" Get users request in");
		Response tpResponse = new Response();


		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse = new PaginateResponse();
		PageBy pageBy = new PageBy(page, Integer.valueOf(DEFAULT_SIZE));

		try {
			createOpentracingSpan("subscriptionSubscriptionController - Get users");
			int count = subscriptionService.countUsers(code,token,null);
			List<UserVO> users = subscriptionService.retrieveUsers(code,token,pageBy);
			return getPaginateResponseSearchResponseEntity(  headers, paginateResponse,   count,  users,pageBy);

		} catch (Exception e) {
			tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			tpResponse.setMessage(e.getMessage());
			logger.error("Erreur durant la recuperation des utilisateurs pour l'abonnement: code {} - token {}", e, code, token);
			e.printStackTrace();
			throw e;
		} finally {
			finishOpentracingSpan();
		}
	}
}