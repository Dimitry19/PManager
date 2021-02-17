package cm.packagemanager.pmanager.ws.controller.rest.announce;


import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnouncesVO;
import cm.packagemanager.pmanager.announce.service.AnnounceService;
import cm.packagemanager.pmanager.announce.service.AnnounceServiceImpl;
import cm.packagemanager.pmanager.announce.service.ServiceAnnounce;
import cm.packagemanager.pmanager.common.ent.dto.ResponseDTO;
import cm.packagemanager.pmanager.common.ent.vo.Movie;
import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import cm.packagemanager.pmanager.ws.responses.PaginateResponse;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cm.packagemanager.pmanager.ws.controller.rest.CommonController.ANNOUNCE_WS;

@RestController
@RequestMapping(ANNOUNCE_WS)
public class AnnounceController extends CommonController {

	protected final Log logger = LogFactory.getLog(AnnounceController.class);


	@Autowired
	protected AnnounceService announceService;


	@Autowired
	ServiceAnnounce announceTester;


	@RequestMapping(value = ANNOUNCE_WS_CREATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON,headers = WSConstants.HEADER_ACCEPT)
	public @ResponseBody
	Response  create(HttpServletResponse response, HttpServletRequest request, @RequestBody AnnounceDTO ar) throws Exception {

		response.setHeader("Access-Control-Allow-Origin", "*");
		AnnounceVO announce = null;
		Response res=new Response();

		try
		{
			logger.info("create request out");
			if (ar!=null){
				announce= announceService.create(ar);

				if(announce!=null){
					res.setRetCode(WebServiceResponseCode.OK_CODE);
					res.setRetDescription(WebServiceResponseCode.ANNOUNCE_CREATE_LABEL);
				}else{
					res.setRetCode(WebServiceResponseCode.NOK_CODE);
					res.setRetDescription(WebServiceResponseCode.ERROR_ANNOUNCE_CREATE_LABEL);
				}
			}
		}
		catch (Exception e)	{
			logger.error("Errore eseguendo add announce: ", e);
			response.setStatus(500);
			response.getWriter().write(e.getMessage());
		}

		return  res;
	}

	@PostMapping(value = ANNOUNCE_WS_UPDATE)
	AnnounceVO  update(HttpServletResponse response, HttpServletRequest request, @RequestBody @Valid UpdateAnnounceDTO uar) throws Exception {
		try {
			if (uar==null)
				return null;
			return announceService.update(uar);
		}catch (Exception e){

			response.getWriter().write(e.getMessage());

		}

		return null;
	}


	@PostMapping(value = ANNOUNCE_WS_ADD_MESSAGE)
	public  ResponseEntity<MessageVO> addMessage(HttpServletRequest request ,HttpServletResponse response,@RequestBody @Valid MessageDTO mdto) throws Exception{
		HttpHeaders headers = new HttpHeaders();


		if (mdto!=null){
			MessageVO message = announceService.addMessage(mdto);

			if(message!=null){
				return new ResponseEntity<MessageVO>(message, headers, HttpStatus.OK);
			}
		}
		return null;
	}



	@RequestMapping(value =ANNOUNCE_WS_DELETE,method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
	public Response delete(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception{
		logger.info("delete request in");
		response.setHeader("Access-Control-Allow-Origin", "*");
		Response pmResponse = new Response();

		try{
			logger.info("delete request out");
			if (id!=null){
				if(announceService.delete(id)){
					pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.CANCELLED_ANNOUNCE_LABEL);
				}else{
					pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
					pmResponse.setRetDescription(WebServiceResponseCode.ERROR_DELETE_ANNOUNCE_CODE_LABEL);
				}
			}
		}
		catch (Exception e){
			response.getWriter().write(e.getMessage());
			pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
			pmResponse.setRetDescription(e.getMessage());
		}

		return pmResponse;
	}




	@GetMapping(ANNOUNCES_WS)
	public ResponseEntity<PaginateResponse> announces(
			@Valid @Positive(message = "Page number should be a positive number") @RequestParam int page,
			@Valid @Positive(message = "Page size should be a positive number") @RequestParam(required = false, defaultValue = "12") int size) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		PaginateResponse paginateResponse=new PaginateResponse();

		int count = announceService.count(page,size);
		if(count==0){
			paginateResponse.setCount(count);
			paginateResponse.setResults(new ArrayList());
			headers.add("X-Users-Total", Long.toString(count));


		}else{
			List<AnnounceVO> announces = announceService.announces(page,size);
			paginateResponse.setCount(count);
			paginateResponse.setResults(announces);
			headers.add("X-Users-Total", Long.toString(announces.size()));
		}

		return new ResponseEntity<PaginateResponse>(paginateResponse, headers, HttpStatus.OK);
	}

	@RequestMapping(ANNOUNCE_WS_USER_ID_PAGE_NO)
	@ResponseBody
	public List<AnnounceVO> getAllPosts(@PathVariable int pageno,@PageableDefault(value=10, page=0) Pageable pageable) throws Exception {

		Page page = announceService.announces(pageable);
		return page.getContent();
		//return announceService.announces(pageno,size);

	}

//https://examples.javacodegeeks.com/spring-boot-pagination-tutorial/
	@GetMapping(value = ANNOUNCE_WS_ALL, produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<ResponseDTO> getAllMovies(
			@RequestParam(name = "pageNumber", defaultValue = "0") final int pageNumber,    // In spring the default page number starts with '0'.
			@RequestParam(name = "pageSize", defaultValue = "2") final int pageSize) {
		/*logger.info("Getting all the movies from the database for page-number= {} and page-size= {}.", pageNumber,
				pageSize);*/
		final ResponseEntity<ResponseDTO> responseEntity;
		try {
			final Pageable pageable = PageRequest.of(pageNumber, pageSize);
			final Page<AnnounceVO> announces = announceTester.getAllMovies(pageable);
			responseEntity = createResponseDto(announces);
		} catch (final Exception e) {
			logger.info("Exception occurred while fetching the response from the database.", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}


	private ResponseEntity<ResponseDTO> createResponseDto(final Page<AnnounceVO> moviesPage) {
		final List<AnnounceVO> movies = moviesPage.getContent();
		final ResponseEntity<ResponseDTO> responseEntity;
		if (CollectionUtils.isEmpty(movies)) {
			logger.info("Returning an empty list as no movies are fetched from the database.");
			responseEntity = new ResponseEntity<>(ResponseDTO.create(Collections.emptyList(), 0, 0, 0, null, null),
					HttpStatus.OK);
		} else {
			responseEntity = new ResponseEntity<>(ResponseDTO.create(movies, (int) moviesPage.getTotalElements(),
					moviesPage.getTotalPages(), moviesPage.getNumber(), moviesPage.isFirst(),
					moviesPage.isLast()), HttpStatus.OK);
		}
		return responseEntity;
	}


}
