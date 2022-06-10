package cm.travelpost.tp.ws.controller.rest.image;

import cm.travelpost.tp.common.enums.UploadImageType;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.image.ent.bo.ImageMultipart;
import cm.travelpost.tp.image.ent.service.ImageService;
import cm.travelpost.tp.image.ent.vo.ImageVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.core.MediaType;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;

import static cm.travelpost.tp.constant.WSConstants.IMG_WS;

/*1-https://dzone.com/articles/upload-and-retrieve-filesimages-using-spring-boot*/
@RestController
@RequestMapping(IMG_WS)
public class ImageController extends CommonController {

    protected final Log logger = LogFactory.getLog(ImageController.class);

    @Autowired
    ImageService imageService;



    @ApiOperation(value = "Upload user or announce image ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully Image uploaded"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Image uploaded",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @PostMapping(value = UPLOAD, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<ImageVO> uploadImage(HttpServletRequest request, HttpServletResponse response,
                                               @RequestParam("id") @Valid Long id,
                                               @RequestParam("type") @Valid UploadImageType type,
                                               @RequestParam("imageFile") MultipartFile file
    ) throws Exception {
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);


        logger.info(" upload user image request in");
        HttpHeaders headers = new HttpHeaders();
        try {
            createOpentracingSpan("ImageController - upload user or announce image");
            logger.debug("Original Image Byte Size :" + file.getBytes().length);

            ImageVO image = imageService.save(file, id, type);
            if (!imageCheck(image)) return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);

            return ResponseEntity.status(HttpStatus.OK).body(image);
        } catch (Exception e) {
            logger.error("Erreur durant l'upload de l'image", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = "Delete user or announce image ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully image delete"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Image deleted",
                    response = ResponseEntity.class, responseContainer = "Object")})
    @DeleteMapping(value = DELETE, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<Response> deleteImage(HttpServletRequest request, HttpServletResponse response,
												@RequestParam("id") @Valid Long id,
												@RequestParam("imageName") @Valid String filename) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);


        logger.info(" delete user image request in");
        try {
            createOpentracingSpan("ImageController - delete user or announce image");
            Response tpResponse = new Response();
            if (imageService.delete(filename, id)) {
                tpResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                tpResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.CANCELLED_LABEL, " L'image"));
                return new ResponseEntity<>(tpResponse, HttpStatus.OK);
            }
            tpResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
            tpResponse.setMessage(MessageFormat.format(WebServiceResponseCode.ERROR_DELETE_LABEL, " L'image"));
            return new ResponseEntity<>(tpResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur durant l'upload de l'image", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }

    @ApiOperation(value = "get image ", response = ImageVO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Image retrieve successfull",
                    response = ImageVO.class, responseContainer = "Object")})
    @GetMapping(value = IMAGE, produces = MediaType.APPLICATION_JSON, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<ImageVO> getImage(HttpServletRequest request, HttpServletResponse response,
                                            @PathVariable("imageName") String imageName) throws Exception {

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);


        HttpHeaders headers = new HttpHeaders();
        logger.info(" get image request in");
        try {
            createOpentracingSpan("ImageController - get image");
            final ImageVO image = imageService.findByName(imageName);

            if (!imageCheck(image)) return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);

            return ResponseEntity.status(HttpStatus.OK).body(image);
        } catch (Exception e) {
            logger.error("Erreur durant la recuperation de l'image", e);
            throw e;
        } finally {
            finishOpentracingSpan();
        }
    }



    @PostMapping("/uploadFile")
    public ResponseEntity<Object> uploadFile(@RequestParam("File") MultipartFile file)  {

        return new ResponseEntity<>("The File Uploaded Successfully.", HttpStatus.OK);
    }

    @PostMapping(value = "image/upload/file/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void uploadJpgImageFile(
            @PathVariable("name") String name,
            @RequestPart(value = "file") MultipartFile multipartFile) {
        imageService.compress(new ImageMultipart(multipartFile), name,"jpeg");
    }

    @PostMapping(value = "image/upload/url/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void uploadJpgImageUrl(
            @PathVariable("name") String name,
            @RequestBody String urlAsString) {
        URL url;

        try {
            url = new URL(urlAsString);
            url.toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //imageService.compress(new ImageUrl(url), name,"jpeg");
    }
}