package cm.travelpost.tp.user.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.announce.ent.vo.AnnounceInfo;
import cm.travelpost.tp.authentication.ent.vo.AuthenticationVO;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.review.ent.bo.ReviewsSummaryBO;
import cm.travelpost.tp.review.ent.vo.ReviewDetailsVO;
import cm.travelpost.tp.review.ent.vo.ReviewVO;
import cm.travelpost.tp.user.ent.vo.UserInfo;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.review.ReviewDTO;
import cm.travelpost.tp.ws.requests.review.UpdateReviewDTO;
import cm.travelpost.tp.ws.requests.users.*;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;


public interface UserService {

    AuthenticationVO checkAuthenticationAttempt(String username) throws Exception;

    AuthenticationVO checkAttempt(String username) throws Exception;

    void resetUserAuthentication(String username) throws Exception;

    UserInfo enableMFA(LoginDTO lr) throws Exception;

    UserInfo checkMFA(LoginDTO lr) throws Exception;

    UserVO login(LoginDTO lr) throws Exception;

    UserVO update(UserVO user) throws UserException;

    boolean delete(UserVO user) throws UserException;

    boolean editPassword(Long userId, String oldPassword, String newPassword) throws UserException;

    UserVO manageMfa(Long userId, boolean enableNotification) throws UserException;

    UserVO manageNotification(Long userId, boolean enableNotification) throws UserException;

    void remove(UserVO user) throws UserException;

    List<UserVO> getAllUsers(PageBy pageBy) throws Exception;

    List<UserVO> getAllUsers() throws Exception;

    List<UserVO> getAllUsersToConfirm() throws Exception;

    UserVO getUser(Long id) throws UserException;

    UserVO register(RegisterDTO register) throws UserException;

    UserVO updateUser(UpdateUserDTO userDTO) throws Exception;

   // UserVO updateImage(Long userId, MultipartFile multipartFile) throws UserException, IOException;

    boolean managePassword(String email) throws Exception, UserException, IOException, MailjetSocketTimeoutException, MailjetException, MessagingException;

    boolean deleteUser(Long id) throws UserException;

    UserVO findByEmail(String email) throws Exception;

    boolean setRoleToUser(RoleToUserDTO roleToUser) throws Exception;

    UserVO findByUsername(String username, boolean isReg) throws Exception;

    UserVO findByUsername(String username) throws Exception;

    UserVO findByToken(String token) throws Exception;

    boolean checkLogin(LoginDTO lr) throws Exception;

    int count(Object o,Long id,PageBy pageBy) throws Exception;


    void subscribe(SubscribeDTO subscribe) throws Exception;

    void unsubscribe(SubscribeDTO subscribe) throws Exception;

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

    String verify(String username, String code) throws Exception;

    boolean AddAnnounceFavorites(UsersAnnounceFavoriteDTO userAnnounceFavoriteDTO) throws UserException;

    boolean removeAnnounceFavorites(UsersAnnounceFavoriteDTO userAnnounceFavoriteDTO) throws UserException;

    List<AnnounceInfo> listAnnounceFavoriteByUser(long idUser)throws UserException;
}
