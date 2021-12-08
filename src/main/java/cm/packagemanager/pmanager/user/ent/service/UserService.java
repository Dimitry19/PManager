package cm.packagemanager.pmanager.user.ent.service;

import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.review.ent.bo.ReviewsSummaryBO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewDetailsVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.review.ReviewDTO;
import cm.packagemanager.pmanager.ws.requests.review.UpdateReviewDTO;
import cm.packagemanager.pmanager.ws.requests.users.*;
import com.sendgrid.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


public interface UserService {

    UserVO login(LoginDTO lr) throws Exception;

    UserVO update(UserVO user) throws UserException;

    boolean delete(UserVO user) throws UserException;

    boolean editPassword(Long userId, String oldPassword, String newPassword) throws UserException;

    UserVO manageNotification(Long userId, boolean enableNotification) throws UserException;

    void remove(UserVO user) throws UserException;

    List<UserVO> getAllUsers(PageBy pageBy) throws Exception;

    List<UserVO> getAllUsers() throws Exception;

    List<UserVO> getAllUsersToConfirm() throws Exception;

    UserVO getUser(Long id) throws UserException;

    UserVO register(RegisterDTO register) throws UserException;

    UserVO updateUser(UpdateUserDTO userDTO) throws Exception;

    UserVO updateImage(Long userId, MultipartFile multipartFile) throws UserException, IOException;

    Response managePassword(String email) throws Exception;

    Response sendMail(MailDTO mr, boolean active) throws Exception;

    boolean deleteUser(Long id) throws UserException;

    UserVO findByEmail(String email) throws Exception;

    boolean setRoleToUser(RoleToUserDTO roleToUser) throws Exception;

    UserVO findByUsername(String username, boolean isReg) throws Exception;

    UserVO findByToken(String token) throws Exception;

    Response buildAndSendMail(HttpServletRequest request, UserVO user) throws UserException;

    boolean checkLogin(LoginDTO lr) throws Exception;

    int count(PageBy pageBy) throws Exception;


    void subscribe(SubscribeDTO subscribe) throws UserException;

    void unsubscribe(SubscribeDTO subscribe) throws UserException;

    List<UserVO> subscriptions(Long userId) throws UserException;

    List<UserVO> subscribers(Long userId) throws UserException;


    /***** RATING USER ***/
    Page<ReviewVO> getReviews(UserVO user, Pageable pageable) throws Exception;

    ReviewVO getReview(UserVO user, int index) throws Exception;

    ReviewVO addReview(UserVO user, ReviewDetailsVO details) throws Exception;

    ReviewVO addReview(ReviewDTO review) throws Exception;

    ReviewVO updateReview(long id, UpdateReviewDTO review) throws Exception;

    boolean deleteReview(Long reviewId) throws Exception;

    ReviewsSummaryBO getReviewSummary(UserVO user);
}
