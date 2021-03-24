package cm.packagemanager.pmanager.user.ent.service;

import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.review.ent.bo.ReviewsSummaryBO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewDetailsVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.review.ReviewDTO;
import cm.packagemanager.pmanager.ws.requests.review.UpdateReviewDTO;
import cm.packagemanager.pmanager.ws.requests.users.*;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import com.sendgrid.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


public interface UserService{

	public UserVO login(LoginDTO lr ) throws Exception;

	public UserVO update(UserVO user) throws UserException ;

	public boolean delete(UserVO user) throws UserException ;

	boolean editPassword(Long userId, String oldPassword,String newPassword) throws UserException;

	boolean manageNotification(Long userId, boolean enableNotification) throws UserException;

	public void remove(UserVO user) throws UserException ;

	public List<UserVO> getAllUsers(PageBy pageBy) throws Exception;

	public List<UserVO> getAllUsers() throws UserException, Exception;

	public List<UserVO> getAllUsersToConfirm() throws UserException;

	public UserVO getUser(Long id) throws UserException;

	public UserVO register(RegisterDTO register) throws UserException;

	public UserVO updateUser(UpdateUserDTO userDTO) throws Exception;

	UserVO updateImage(Long userId, MultipartFile multipartFile) throws UserException, IOException;

	public Response managePassword(String email) throws Exception;

	public Response sendMail(MailDTO mr, boolean active) throws Exception;

	public boolean deleteUser(Long id)throws UserException;

	public UserVO findByEmail(String email) throws Exception;

	public boolean setRoleToUser(RoleToUserDTO roleToUser) throws Exception;

	public UserVO findByUsername(String username, boolean isReg) throws Exception;

	public UserVO findByToken(String token) throws Exception;

	public Response buildAndSendMail(HttpServletRequest request , UserVO user) throws UserException;

	public boolean checkLogin(LoginDTO lr ) throws Exception;

	public int  count(PageBy pageBy)  throws Exception;


	public  void  subscribe(SubscribeDTO subscribe) throws UserException;

	public  void  unsubscribe(SubscribeDTO subscribe) throws UserException;

	public  List<UserVO> subscriptions(Long  userId) throws UserException;

	public  List<UserVO> subscribers(Long  userId) throws UserException;



	/***** RATING USER ***/
	Page<ReviewVO> getReviews(UserVO user, Pageable pageable) throws Exception;

	ReviewVO getReview(UserVO user, int index) throws Exception;

	ReviewVO addReview(UserVO user, ReviewDetailsVO details) throws Exception;

	ReviewVO addReview(ReviewDTO review) throws Exception;

	ReviewVO updateReview(UpdateReviewDTO review) throws Exception;

	boolean deleteReview(Long reviewId) throws Exception;

	ReviewsSummaryBO getReviewSummary(UserVO user);
}
