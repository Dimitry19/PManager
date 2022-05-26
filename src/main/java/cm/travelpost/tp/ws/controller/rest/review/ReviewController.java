package cm.travelpost.tp.ws.controller.rest.review;


import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.review.ent.vo.ReviewVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.review.ReviewDTO;
import cm.travelpost.tp.ws.requests.review.UpdateReviewDTO;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
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
import java.text.MessageFormat;

import static cm.travelpost.tp.constant.WSConstants.*;


@RestController
@RequestMapping(REVIEW_WS)
@Api(value = "reviews-service", description = "Reviews Operations")
public class ReviewController extends CommonController {


    private static final String REVIEW_LABEL = "L'avis";
    protected final Log logger = LogFactory.getLog(ReviewController.class);

    @PostMapping(value = REVIEW_WS_ADD, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<ReviewVO> add(@RequestBody @Valid ReviewDTO reviewDTO) throws Exception {
        try {
            logger.info("add  review  request in");
            createOpentracingSpan("ReviewController -add");

            ReviewVO review = userService.addReview(reviewDTO);
            if (review != null) {
                review.setRetCode(WebServiceResponseCode.OK_CODE);
                review.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, REVIEW_LABEL));
                return new ResponseEntity<>(review, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(review, HttpStatus.EXPECTATION_FAILED);
        } catch (Exception e) {
            logger.error("add  review  error ", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }


    @PutMapping(value = REVIEW_WS_UPDATE, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ReviewVO update(HttpServletResponse response, HttpServletRequest request, @PathVariable long id,
                    @RequestBody @Valid UpdateReviewDTO reviewDTO) throws Exception {

        try {
            logger.info("update  review  request in");
            createOpentracingSpan("ReviewController -update");

            ReviewVO review = userService.updateReview(id, reviewDTO);

            if (review != null) {
                review.setRetCode(WebServiceResponseCode.OK_CODE);
                review.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_LABEL, REVIEW_LABEL));

            } else {
                review = new ReviewVO();
                review.setRetCode(WebServiceResponseCode.NOK_CODE);
                review.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, REVIEW_LABEL));

            }
            return review;
        } catch (Exception e) {
            logger.error("update  review  error ", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }


    /**
     * Cette methode elimine un commentaire d'une annonce dont on a son Id
     *
     * @param response
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = REVIEW_WS_DELETE, headers = WSConstants.HEADER_ACCEPT)
    public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        Response pmResponse = new Response();

        try {
            createOpentracingSpan("ReviewController -delete");
            logger.info("delete review request in");
            if (id != null) {
                if (userService.deleteReview(id)) {
                    pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, REVIEW_LABEL));

                } else {
                    pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                    pmResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, REVIEW_LABEL));
                }
            }
        } catch (Exception e) {
            pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            pmResponse.setMessage(e.getMessage());
            logger.error("delete  review  error ", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
        return pmResponse;
    }
}
