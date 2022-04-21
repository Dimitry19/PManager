package cm.travelpost.tp.ws.controller.rest.users.otp;

import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.user.ent.vo.otp.OPTNumber;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.users.otp.OTPNumberDTO;
import cm.travelpost.tp.ws.requests.users.otp.SmsDTO;
import cm.travelpost.tp.ws.responses.Response;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static cm.travelpost.tp.websocket.constants.WebSocketConstants.SUSCRIBE_QUEUE_ITEM_SEND;

@RestController
@RequestMapping(WSConstants.SMS_WS)
public class SmsController extends CommonController {

	protected final Log logger = LogFactory.getLog(SmsController.class);


	@ApiOperation(value = "Send OTP Code ", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful OTP Code sent",
					response = Boolean.class, responseContainer = "Boolean")})
	@RequestMapping(value = SMS_SEND, method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> smsSend(@RequestBody SmsDTO sms) {

		logger.info("smsSubmit request in");

		createOpentracingSpan("SmsController - smsSend");

		try{

			smsService.send(sms);
			socket.convertAndSend(SUSCRIBE_QUEUE_ITEM_SEND, getTimeStamp() + ": SMS has been sent!: "+sms.getTo());
			return new ResponseEntity<>(true,HttpStatus.OK);

		}
		catch(Exception e){
			logger.error("Erreur survenue durant l'envoi du code OTP {}",e);
			return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			finishOpentracingSpan();
		}
	}

	@RequestMapping(value = SMS_CALLBACK, method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void smsCallback(@RequestBody MultiValueMap<String, String> map) {
		smsService.receive(map);
		socket.convertAndSend(SUSCRIBE_QUEUE_ITEM_SEND, getTimeStamp() + ": Twilio has made a callback request! Here are the contents: "+map.toString());
	}

	@ApiOperation(value = "Manage OTP ", response = Response.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Server error"),
			@ApiResponse(code = 200, message = "Successfully retrieved list"),
			@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
			@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
			@ApiResponse(code = 200, message = "Successful OTP confirmation",
					response = Response.class, responseContainer = "Object")})
	@PostMapping(path = WSConstants.USER_WS_OTP, consumes = {javax.ws.rs.core.MediaType.APPLICATION_JSON}, produces = {javax.ws.rs.core.MediaType.APPLICATION_JSON}, headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	ResponseEntity<String> validateOTP(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid OTPNumberDTO dto) throws ValidationException, IOException {

		logger.info("validate otp  request in");
		response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);

		try {
			createOpentracingSpan("SmsController - validate OTP");

			if (dto != null) {

				int sentOtp = OPTNumber.getOtp();
				System.out.println(sentOtp + ":" + dto.getOtp());
				if (sentOtp == dto.getOtp()) {
					return new ResponseEntity<>("Success otp", HttpStatus.OK);
				}
				return new ResponseEntity<>("failure", HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			logger.error("Erreur durant l\'execution de validateOTP: ", e);
			throw e;
		} finally {
			finishOpentracingSpan();
		}
		return null;
	}
	private String getTimeStamp() {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
	}
}
