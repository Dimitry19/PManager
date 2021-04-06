package cm.packagemanager.pmanager.ws.controller.rest.review;


import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.review.ReviewDTO;
import cm.packagemanager.pmanager.ws.requests.review.UpdateReviewDTO;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;

import static cm.packagemanager.pmanager.constant.WSConstants.*;


@RestController
@RequestMapping(REVIEW_WS)
@Api(value="reviews-service", description="Reviews Operations")
public class ReviewController extends CommonController {


	protected final Log logger = LogFactory.getLog(ReviewController.class);

	@PostMapping(value = REVIEW_WS_ADD, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public ResponseEntity<ReviewVO> add(@RequestBody @Valid  ReviewDTO reviewDTO) throws Exception {
		try{
			logger.info("add  review  request in");
			createOpentracingSpan("ReviewController -add");

			ReviewVO review = userService.addReview(reviewDTO);
			if (review!=null){
				review.setRetCode(WebServiceResponseCode.OK_CODE);
				review.setRetDescription(WebServiceResponseCode.REVIEW_CREATE_LABEL);
 		        return new ResponseEntity<ReviewVO>(review, HttpStatus.CREATED);
			}
 		        return new ResponseEntity<ReviewVO>(review, HttpStatus.EXPECTATION_FAILED);
		}catch (Exception e){
			logger.error("add  review  error ",e);
			throw e;
		}finally {
			finishOpentracingSpan();
		}
 	}


	@PutMapping(value =REVIEW_WS_UPDATE, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	ReviewVO update(HttpServletResponse response, HttpServletRequest request, @PathVariable long id,
	                @RequestBody @Valid UpdateReviewDTO reviewDTO) throws Exception {

			try{
			logger.info("update  review  request in");
			createOpentracingSpan("ReviewController -update");

			ReviewVO review = userService.updateReview(id,reviewDTO);

			if (review!=null){
				review.setRetCode(WebServiceResponseCode.OK_CODE);
				review.setRetDescription(WebServiceResponseCode.UPDATED_REVIEW_LABEL);
			}else{
				review=new ReviewVO();
				review.setRetCode(WebServiceResponseCode.NOK_CODE);
				review.setRetDescription(WebServiceResponseCode.ERROR_UPDATE_REVIEW_CODE_LABEL);
			}
			return review;
		}catch (Exception e){
				logger.error("update  review  error ",e);
				throw e;
		}finally {
			finishOpentracingSpan();
		}
	}


	/**
	 * Cette methode elimine un commentaire d'une annonce dont on a son Id
	 * @param response
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping(value =REVIEW_WS_DELETE, headers = WSConstants.HEADER_ACCEPT)
	public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception{

		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try{
			createOpentracingSpan("ReviewController -delete");
			logger.info("delete review request in");
			if (id!=null){
				if(userService.deleteReview(id)){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.CANCELLED_REVIEW_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_DELETE_REVIEW_CODE_LABEL);
				}
			}
		}
		catch (Exception e){
			pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			pmResponse.setRetDescription(e.getMessage());
			logger.error("delete  review  error ",e);
			throw e;
		}finally {
			finishOpentracingSpan();
		}
		return pmResponse;
	}
}
