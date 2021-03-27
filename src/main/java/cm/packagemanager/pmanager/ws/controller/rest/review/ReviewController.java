package cm.packagemanager.pmanager.ws.controller.rest.review;


import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.service.UserService;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.review.ReviewDTO;
import cm.packagemanager.pmanager.ws.requests.review.UpdateReviewDTO;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static cm.packagemanager.pmanager.constant.WSConstants.*;


@RestController
@RequestMapping(REVIEW_WS)
@Api(value="reviews-service", description="Reviews Operations")
public class ReviewController extends CommonController {
	@Autowired
	UserService userService;

	@PostMapping(value = REVIEW_WS_ADD)
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
			throw e;
		}finally {
			finishOpentracingSpan();
		}
 	}


	@PostMapping(value = REVIEW_WS_UPDATE)
	ReviewVO update(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid UpdateReviewDTO reviewDTO) throws Exception {

			try{
			logger.info("update  review  request in");
			createOpentracingSpan("ReviewController -update");

			ReviewVO review = userService.updateReview(reviewDTO);

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
	@RequestMapping(value =REVIEW_WS_DELETE,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
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
			//response.getWriter().write(e.getMessage());
			pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			pmResponse.setRetDescription(e.getMessage());
		}finally {
			finishOpentracingSpan();
		}
		return pmResponse;
	}
}
