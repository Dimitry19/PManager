package cm.travelpost.tp.ws.controller.rest.reservation;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.enums.FindBy;
import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.announce.ent.vo.ReservationVO;
import cm.travelpost.tp.common.enums.ReservationType;
import cm.travelpost.tp.common.enums.ValidateEnum;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.announces.ReservationDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateReservationDTO;
import cm.travelpost.tp.ws.requests.announces.ValidateReservationDTO;
import cm.travelpost.tp.ws.responses.PaginateResponse;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import static cm.travelpost.tp.constant.WSConstants.*;

@RestController
@RequestMapping(RESERVATION_WS)
@Api(value = "reservations-service", description = "Reservation Operations")
public class ReservationController extends CommonController {


    private static final String RESERVATION_LABEL = "La reservation";
    protected final Log logger = LogFactory.getLog(ReservationController.class);

    @ApiOperation(value = "create an reservation ", response = ReservationVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful create reservation",
                    response = ReservationVO.class, responseContainer = "Object")})
    @PostMapping(value = ADD, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public @ResponseBody
    ResponseEntity<ReservationVO> addReservation(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ReservationDTO reservationDTO) throws Exception {

        try {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
            logger.info("add reservation request in");

            createOpentracingSpan("ReservationController - add reservation");
            ReservationVO reservation = reservationService.addReservation(reservationDTO);

            if (reservation == null) {

                reservation = new ReservationVO();
                reservation.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_CREATE_LABEL, RESERVATION_LABEL));
                reservation.setRetCode(WebServiceResponseCode.NOK_CODE);

                return new ResponseEntity<>(reservation, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                reservation.setRetDescription(MessageFormat.format(WebServiceResponseCode.CREATE_LABEL, RESERVATION_LABEL));
                reservation.setRetCode(WebServiceResponseCode.OK_CODE);
            }

            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur durant la creation d'ue reservation", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }


    @ApiOperation(value = "Update an reservation ", response = ReservationVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval",
                    response = ReservationVO.class, responseContainer = "Object")})

    @PutMapping(value = RESERVATION_WS_UPDATE_ID, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    ResponseEntity<ReservationVO> updateReservation(HttpServletResponse response, HttpServletRequest request, @PathVariable @Valid long id, @RequestBody @Valid UpdateReservationDTO urr) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        try {
            createOpentracingSpan("ReservationController -update");

            if (urr == null) return null;
            urr.setId(id);

            ReservationVO reservation = reservationService.updateReservation(urr);

            if (reservation != null) {
                reservation.setRetCode(WebServiceResponseCode.OK_CODE);
                reservation.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_RESERV_LABEL, "modifiée"));

            } else {

                reservation = new ReservationVO();
                reservation.setRetCode(WebServiceResponseCode.NOK_CODE);
                reservation.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, RESERVATION_LABEL));

                return new ResponseEntity<>(reservation, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(reservation, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("{}", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = "Delete reservation", response = Response.class)
    @DeleteMapping(value = DELETE, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<Response> deleteReservation(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        Response tpResponse = new Response();


        try {
            logger.info("delete reservation request in");
            createOpentracingSpan("ReservationController - delete reservation");

            if (reservationService.deleteReservation(id)) {

                tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                tpResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, RESERVATION_LABEL));

                return new ResponseEntity<>(tpResponse, HttpStatus.OK);

            } else {

                tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                tpResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, RESERVATION_LABEL));

            }
            return new ResponseEntity<>(tpResponse, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error(" ReservationController - delete reservation:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = "retrieve  an reservation ", response = ReservationVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Retrieve reservation",
                    response = ReservationVO.class, responseContainer = "Object")})
    @GetMapping(RESERVATION_WS_BY_ID)
    public ResponseEntity<ReservationVO> getReservation(HttpServletRequest request,
                                                        HttpServletResponse response, @RequestParam("id") long id) throws Exception {
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        try {
            createOpentracingSpan("ReservationController - retrieve reservation");


            ReservationVO reservation = reservationService.getReservation(id);

            if (reservation != null) {

                reservation.setRetCode(WebServiceResponseCode.OK_CODE);

                return new ResponseEntity<>(reservation, HttpStatus.OK);
            }
            reservation = new ReservationVO();
            reservation.setRetCode(WebServiceResponseCode.NOK_CODE);
            reservation.setMessage(WebServiceResponseCode.ERROR_GET_RESERV_CODE_LABEL);

            return new ResponseEntity<>(reservation, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            finishOpentracingSpan();
        }

    }

    @ApiOperation(value = "Validate an reservation ", response = AnnounceVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful validate",
                    response = Response.class, responseContainer = "Object")})
    @PostMapping(value = VALIDATE, produces = MediaType.APPLICATION_JSON)
    ResponseEntity<ReservationVO> validate(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid ValidateReservationDTO vr) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        try {

            createOpentracingSpan("ReservationController - validate");


            if (vr == null) return null;

            ReservationVO reservation = reservationService.validate(vr);
            if (reservation != null) {
                response.setStatus(200);
                String mes = (vr.isValidate()) ? ValidateEnum.ACCEPTED.toValue() : ValidateEnum.REFUSED.toValue();
                reservation.setRetCode(WebServiceResponseCode.OK_CODE);
                reservation.setRetDescription(MessageFormat.format(WebServiceResponseCode.UPDATED_RESERV_LABEL, mes));
                return new ResponseEntity<>(reservation, HttpStatus.OK);

            } else {
                reservation = new ReservationVO();
                response.setStatus(org.apache.http.HttpStatus.SC_NOT_FOUND);
                reservation.setRetCode(WebServiceResponseCode.NOK_CODE);
                reservation.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_UPDATE_LABEL, RESERVATION_LABEL));

            }
            return new ResponseEntity<>(reservation, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    /**
     * Cette methode recherche toutes les reservations créees par un utilisateur
     *
     * @param response
     * @param request
     * @param userId
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Retrieve user reservations", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval",
                    response = ResponseEntity.class, responseContainer = "List")})
    @GetMapping(value = BY_USER,  headers = WSConstants.HEADER_ACCEPT, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<PaginateResponse> reservationsByUser(HttpServletResponse response, HttpServletRequest request,
                                                               @RequestParam @Valid long userId,
                                                               @RequestParam @Valid ReservationType type,
                                                               @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                               @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
        HttpHeaders headers = new HttpHeaders();

        logger.info("find reservation by user request in");
        PageBy pageBy = new PageBy(page, size);

        List reservations = null;

        try {
            createOpentracingSpan("ReservationController -reservationsByUser");
            PaginateResponse res = new PaginateResponse();
            int count = reservationService.count(userId, null, FindBy.USER,type);
            reservations =reservationService.reservationsByUser(userId, type, pageBy);

            if (CollectionsUtils.isNotEmpty(reservations)) {
                res.setCount(CollectionsUtils.size(reservations));
                res.setResults(reservations);
            }
            headers.add(HEADER_TOTAL, Long.toString(count));

            return new ResponseEntity<>(res, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.info(" ReservationController - reservationsByUser:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    /**
     * Cette methode recherche toutes les reservations d'une annonce
     *
     * @param response
     * @param request
     * @param announceId
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "Retrieve reservations by an announce with an ID", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Successful retrieval",
                    response = ResponseEntity.class, responseContainer = "List")})
    @GetMapping(value = RESERVATION_WS_BY_ANNOUNCE, produces = MediaType.APPLICATION_JSON, headers = HEADER_ACCEPT)
    public ResponseEntity<PaginateResponse> reservationsByAnnounce(HttpServletResponse response, HttpServletRequest request,
                                                                   @RequestParam @Valid long announceId,
                                                                   @RequestParam(required = false, defaultValue = DEFAULT_PAGE) @Valid @Positive(message = "la page doit etre nombre positif") int page,
                                                                   @RequestParam(required = false, defaultValue = DEFAULT_SIZE) int size) throws Exception {

        try {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);
            HttpHeaders headers = new HttpHeaders();
            PaginateResponse paginateResponse = new PaginateResponse();
            logger.info("find reservation by announce request in");

            createOpentracingSpan("ReservationController -reservations by announce");

            PageBy pageBy = new PageBy(page, size);
            int count = reservationService.count(announceId, null, FindBy.ANNOUNCE, null);
            if (count == 0) {
                headers.add(HEADER_TOTAL, Long.toString(count));
            } else {
                List<ReservationVO> reservations = reservationService.reservationsByAnnounce(announceId, pageBy);
                paginateResponse.setCount(CollectionsUtils.size(reservations));
                paginateResponse.setResults(reservations);
                headers.add(HEADER_TOTAL, Long.toString(reservations.size()));
            }

            return new ResponseEntity<>(paginateResponse, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.info(" ReservationController - reservationsByAnnounce:Exception occurred while fetching the response from the database.", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }
}
