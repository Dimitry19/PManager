package cm.packagemanager.pmanager.ws.controller.rest.review;


import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.user.service.RoleService;
import cm.packagemanager.pmanager.user.service.UserService;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.review.ReviewDTO;
import cm.packagemanager.pmanager.ws.requests.users.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static cm.packagemanager.pmanager.ws.controller.rest.CommonController.REVIEW_WS;
import static cm.packagemanager.pmanager.ws.controller.rest.CommonController.ROLE_WS;


@RestController
@RequestMapping(REVIEW_WS)
public class ReviewController extends CommonController {

	@Autowired
	UserService userService;

	@PostMapping(value = REVIEW_WS_ADD)
	public ResponseEntity<ReviewVO> add(@RequestBody @Valid  ReviewDTO reviewDTO) throws Exception {
		try{
			logger.info("add  review  request in");
			createOpentracingSpan("ReviewController -add");

			ReviewVO review = userService.addReview(reviewDTO);
 		    return new ResponseEntity<ReviewVO>(review, HttpStatus.CREATED);
		}catch (Exception e){
			throw e;
		}finally {
			finishOpentracingSpan();
		}
 	}
}
