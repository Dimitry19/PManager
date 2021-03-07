package cm.packagemanager.pmanager.user.service;

import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.review.ent.bo.ReviewsSummaryBO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewDetailsVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.review.ReviewDTO;
import cm.packagemanager.pmanager.ws.requests.review.UpdateReviewDTO;
import cm.packagemanager.pmanager.ws.requests.users.LoginDTO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.users.RegisterDTO;
import cm.packagemanager.pmanager.ws.requests.users.RoleToUserDTO;
import cm.packagemanager.pmanager.ws.requests.users.UpdateUserDTO;
import com.sendgrid.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService{

	public UserVO login(LoginDTO lr ) throws UserException;

	public UserVO update(UserVO user) throws UserException ;

	public boolean delete(UserVO user) throws UserException ;

	public void remove(UserVO user) throws UserException ;

	public List<UserVO> getAllUsers(PageBy pageBy) throws UserException ;

	public List<UserVO> getAllUsers() throws UserException;

	public List<UserVO> getAllUsersToConfirm() throws UserException;

	public UserVO getUser(Long id) throws UserException;

	public UserVO register(RegisterDTO register) throws UserException;

	public UserVO updateUser(UpdateUserDTO userDTO) throws UserException;

	public Response managePassword(String email) throws UserException;

	public Response sendMail(MailDTO mr, boolean active) throws UserException;

	public boolean deleteUser(Long id)throws UserException;

	public UserVO findByEmail(String email)throws UserException;

	public boolean setRoleToUser(RoleToUserDTO roleToUser)  throws UserException;

	public UserVO findByUsername(String username, boolean isReg) throws UserException;

	public UserVO findByToken(String token) throws UserException;

	public Response buildAndSendMail(HttpServletRequest request , UserVO user) throws UserException;

	public boolean checkLogin(LoginDTO lr ) throws BusinessResourceException, UserException;

	public int  count(PageBy pageBy)  throws Exception;


	/***** RATING USER ***/
	Page<ReviewVO> getReviews(UserVO user, Pageable pageable) throws Exception;

	ReviewVO getReview(UserVO user, int index) throws Exception;

	ReviewVO addReview(UserVO user, ReviewDetailsVO details) throws Exception;

	ReviewVO addReview(ReviewDTO review) throws Exception;

	ReviewVO updateReview(UpdateReviewDTO review) throws Exception;


	boolean deleteReview(Long reviewId) throws Exception;


	ReviewsSummaryBO getReviewSummary(UserVO user);
}
