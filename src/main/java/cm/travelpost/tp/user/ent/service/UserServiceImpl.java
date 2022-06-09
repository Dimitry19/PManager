package cm.travelpost.tp.user.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.announce.ent.dao.AnnounceDAO;
import cm.travelpost.tp.authentication.ent.dao.AuthenticationDAO;
import cm.travelpost.tp.authentication.ent.vo.AuthenticationVO;
import cm.travelpost.tp.common.Constants;
import cm.travelpost.tp.common.enums.RoleEnum;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.mail.MailType;
import cm.travelpost.tp.common.mail.TravelPostMailSender;
import cm.travelpost.tp.common.mail.ent.service.IGoogleMailSenderService;
import cm.travelpost.tp.common.mail.sendgrid.MailSenderSendGrid;
import cm.travelpost.tp.common.sms.ent.service.TotpService;
import cm.travelpost.tp.common.utils.MailUtils;
import cm.travelpost.tp.rating.ent.vo.RatingCountVO;
import cm.travelpost.tp.rating.enums.Rating;
import cm.travelpost.tp.review.ent.bo.ReviewsSummaryBO;
import cm.travelpost.tp.review.ent.dao.ReviewDAO;
import cm.travelpost.tp.review.ent.vo.ReviewDetailsVO;
import cm.travelpost.tp.review.ent.vo.ReviewIdVO;
import cm.travelpost.tp.review.ent.vo.ReviewVO;
import cm.travelpost.tp.security.PasswordGenerator;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserInfo;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.review.ReviewDTO;
import cm.travelpost.tp.ws.requests.review.UpdateReviewDTO;
import cm.travelpost.tp.ws.requests.users.*;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.ws.rs.BadRequestException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/*
Le fait d’avoir des singletons a un impact en environnement multi-threadé
○ Les variables de classe sont partagées entre les threads
○ Les beans doivent donc être thread-safe (d'où le transactional)
*
* */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    protected final Log logger = LogFactory.getLog(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private MailSenderSendGrid mailSenderSendGrid;

    @Autowired
    private TravelPostMailSender personalMailSender;

    @Autowired
    private IGoogleMailSenderService googleMailSenderService;

    @Autowired
    TotpService totpService;

    @Autowired
    private AuthenticationDAO authenticationDAO;

    @Value("${tp.travelpost.authentication.attempt}")
    private int attemptLimit;

    @Autowired
    private AnnounceDAO announceDAO;


    @PostConstruct
    public void init() {
        logger.info("User service starts....");
    }


    public boolean checkLogin(LoginDTO lr) throws Exception {
        return userDAO.checkLogin(lr);
    }

    @Override
    public int count(Object o, Long id, PageBy pageBy) throws Exception {
        return userDAO.count(o, id, pageBy);
    }

    @Override
    public void subscribe(SubscribeDTO subscribe) throws Exception {
        userDAO.subscribe(subscribe);
    }


    @Override
    public void unsubscribe(SubscribeDTO subscribe) throws Exception {
        userDAO.unsubscribe(subscribe);
    }

    @Override
    public List<UserVO> subscriptions(Long userId) throws UserException {

        return userDAO.subscriptions(userId);
    }

    @Override
    public List<UserVO> subscribers(Long userId) throws UserException {
        return userDAO.subscribers(userId);
    }

    @Override
    public AuthenticationVO checkAuthenticationAttempt(String username) throws Exception {

        int attempt = 0;
        UserVO user = userDAO.findByUsername(username);
        if (user != null) {
            AuthenticationVO authentication = user.getAuthentication();
            if (authentication == null) {
                authentication = new AuthenticationVO();
                authentication.setAttempt(++attempt);
                authentication.setUser(user);
                Long id = (Long) authenticationDAO.save(authentication);
                AuthenticationVO auth = (AuthenticationVO) authenticationDAO.findById(AuthenticationVO.class, id);
                user.setAuthentication(auth);
            } else {
                attempt = user.getAuthentication().getAttempt();
                user.getAuthentication().setAttempt(++attempt);
                user.getAuthentication().setDesactivate(attempt >= attemptLimit);
            }
            update(user);
            return user.getAuthentication();
        }
        return null;
    }

    @Override
    public AuthenticationVO checkAttempt(String username) throws Exception {

        UserVO user = userDAO.findByUsername(username);
        AuthenticationVO authentication = null;
        if (user != null) {
            authentication = user.getAuthentication();
            if (user.getAuthentication() == null) {
                authentication = new AuthenticationVO();
            }
        }
        return authentication;
    }

    @Override
    public void resetUserAuthentication(String username) throws Exception {

        UserVO user = findByUsername(username);
        if (user.getAuthentication() != null) {
            user.getAuthentication().setAttempt(0);
            user.getAuthentication().setDesactivate(Boolean.FALSE);
            update(user);
        }
    }

    public UserInfo enableMFA(LoginDTO lr) throws Exception {

        UserVO user = login(lr);
        if (user == null) {
            throw new UserException("Utilisateur non trouvé");
        }

        userDAO.generateSecret(user);
        user = userDAO.findById(user.getId());
        return new UserInfo(user.getEmail(), user.getSecret(), false);
    }

    public UserInfo checkMFA(LoginDTO lr) throws Exception {

        UserVO user = login(lr);
        if (user == null) {
            throw new UserException("Utilisateur non trouvé");
        }
        return new UserInfo(user.getEmail(), user.getSecret(), user.isMultipleFactorAuthentication());
    }

    public UserVO login(LoginDTO lr) throws Exception {

        UserVO user = checkLoginAdmin(lr);
        if (user == null) {
            user = userDAO.login(lr.getUsername(), lr.getPassword());
        }
        return user;
    }

    public UserVO update(UserVO user) throws UserException {
        return (UserVO) userDAO.merge(user);
    }

    public boolean delete(UserVO user) throws UserException {
        return userDAO.deleteUser(user);
    }


    public UserVO checkLoginAdmin(LoginDTO login) throws Exception {
        AtomicBoolean found = new AtomicBoolean(false);

        UserVO admin = userDAO.findByUsername(login.getUsername());
        if (admin != null) {
            admin.getRoles().forEach(x -> {
                if (RoleEnum.ADMIN.equals(RoleEnum.fromValue(x.getDescription().name()))) {
                    found.set(true);
                }
            });
            return found.get() ? admin : null;
        }

        return null;
    }

    @Override
    public boolean editPassword(Long userId, String oldPassword, String newPassword) throws UserException {

        return userDAO.editPassword(userId, oldPassword, newPassword);
    }

    @Override
    public UserVO manageMfa(Long userId, boolean mfa) throws UserException {
        return userDAO.manageMfa(userId, mfa);
    }

    @Override
    public UserVO manageNotification(Long userId, boolean enableNotification) throws UserException {
        return userDAO.manageNotification(userId, enableNotification);
    }

    public void remove(UserVO user) throws UserException {
        userDAO.remove(user);
    }


    public List<UserVO> getAllUsers(PageBy pageBy) throws Exception {
        return userDAO.getAllUsers(pageBy);
    }

    public List<UserVO> getAllUsers() throws Exception {
        return userDAO.getAllUsers();
    }

    public List<UserVO> getAllUsersToConfirm() throws Exception {
        return userDAO.getAllUsersToConfirm();
    }

    public UserVO getUser(Long id) throws UserException {
        return userDAO.getUser(id);
    }


    public UserVO register(RegisterDTO register) throws UserException {

        return userDAO.register(register);
    }

    @Transactional(readOnly = true)
    public List<UserVO> search(UserSeachDTO userSeachDTO, PageBy pageBy) throws UserException {
        return userDAO.search(userSeachDTO, pageBy);
    }

    @Transactional(rollbackFor = UserException.class)
    public UserVO updateUser(UpdateUserDTO userDTO) throws Exception {
        return userDAO.updateUser(userDTO);
    }

//    @Override
//    @Transactional(rollbackFor = IOException.class)
//    public UserVO updateImage(Long userId, MultipartFile multipartFile) throws UserException, IOException {
//        return userDAO.updateImage(userId, multipartFile);
//    }

    public boolean managePassword(String email) throws Exception, UserException, MailjetSocketTimeoutException, MailjetException, MessagingException {
        UserVO user = findByEmail(email);


        if (user == null)
            throw new UserException("Aucun utilisateur trouvé avec cette adresse email :" + email);

        String decrypt = PasswordGenerator.decrypt(user.getPassword());

        List<String> labels = new ArrayList<>();
        List<String> emails = new ArrayList<>();

        labels.add(MailType.PASSWORD_KEY);
        labels.add(MailType.USERNAME_KEY);

        emails.add(email);
        Map<String, String> emailTo = new HashMap();
        emailTo.put(user.getUsername(), user.getEmail());

        String title = MessageFormat.format(MailType.PASSWORD_TEMPLATE, MailType.PASSWORD_TEMPLATE_TITLE);

        googleMailSenderService.sendMail(title, emails, null, null, personalMailSender.getTravelPostPseudo(), user.getUsername(), decrypt, false);

        return personalMailSender.send(MailType.CONFIRM_TEMPLATE, title, MailUtils.replace(user, labels, null, decrypt),
                emailTo, null, null, personalMailSender.getTravelPostPseudo(), null, false, null);
    }

    @Transactional(rollbackFor = UserException.class)
    public boolean deleteUser(Long id) throws UserException {
        return userDAO.deleteUser(id);
    }

    public UserVO findByEmail(String email) throws Exception {
        return userDAO.findByEmail(email);
    }


    public boolean setRoleToUser(RoleToUserDTO roleToUser) throws Exception {

        return userDAO.setRole(roleToUser.getEmail(), roleToUser.getRole());
    }

    @Transactional(readOnly = true)
    public UserVO findByUsername(String username, boolean isReg) throws Exception {
        return userDAO.findByOnlyUsername(username, isReg);
    }

    @Transactional(readOnly = true)
    public UserVO findByUsername(String username) throws Exception {
        return userDAO.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public UserVO findByToken(String token) throws Exception {
        return userDAO.findByToken(token);
    }


    @Override
    public Page<ReviewVO> getReviews(UserVO user, Pageable pageable) throws Exception {
        Assert.notNull(user, "User must not be null");
        return this.reviewDAO.findByUser(user, pageable);
    }

    @Override
    public ReviewVO getReview(UserVO user, int reviewNumber) throws Exception {
        Assert.notNull(user, "User must not be null");
        return reviewDAO.findByUserAndIndex(user, reviewNumber);
    }

    @Override
    public ReviewVO addReview(UserVO user, ReviewDetailsVO details) throws Exception {
        ReviewVO review = new ReviewVO(user, 1, details);
        return reviewDAO.saves(review);
    }

    @Override
    public ReviewVO addReview(ReviewDTO reviewDTO) throws Exception {
        UserVO user = (UserVO) userDAO.checkAndResolve(UserVO.class, reviewDTO.getUserId());
        UserVO ratingUser = (UserVO) userDAO.checkAndResolve(UserVO.class, reviewDTO.getRatingUserId());

        if (ratingUser.equals(user)) {
            throw new UserException("Un utilisateur ne peut pas s'evaluer");
        }
        ReviewDetailsVO details = new ReviewDetailsVO(reviewDTO.getRating(), reviewDTO.getTitle(), reviewDTO.getDetails());
        ReviewVO review = new ReviewVO(user, ratingUser, 1, details);
        ReviewIdVO id = new ReviewIdVO();
        id.setToken(Constants.DEFAULT_TOKEN);
        review.setReviewId(id);
        return reviewDAO.saves(review);
    }

    @Override
    public ReviewVO updateReview(long id, UpdateReviewDTO reviewDTO) throws Exception {
        UserVO user = getUser(reviewDTO.getUserId());
        UserVO ratingUser = getUser(reviewDTO.getRatingUserId());

        if (ratingUser.equals(user)) {
            throw new UserException("Un utilisateur ne peut donner un avis sur lui même");
        }

        ReviewVO review = reviewDAO.findById(id);
        if (review == null) {
            throw new UserException("Avis non existant");
        }
        return reviewDAO.update(review);
    }

    @Override
    public boolean deleteReview(Long id) throws Exception {
        return reviewDAO.delete(id);
    }

    @Override
    public ReviewsSummaryBO getReviewSummary(UserVO user) {
        List<RatingCountVO> ratingCounts = findRatingCounts(user);
        return new ReviewsSummaryBOImpl(ratingCounts);
    }

    private List<RatingCountVO> findRatingCounts(UserVO user) {
        return userDAO.findRatingCounts(user);
    }


    private static class ReviewsSummaryBOImpl implements ReviewsSummaryBO {

        private final Map<Rating, Long> ratingCount;

        public ReviewsSummaryBOImpl(List<RatingCountVO> ratingCounts) {
            this.ratingCount = new HashMap<>();
            for (RatingCountVO rc : ratingCounts) {
                this.ratingCount.put(rc.getRating(), rc.getCount());
            }
        }

        @Override
        public long getNumberOfReviewsWithRating(Rating rating) {
            Long count = this.ratingCount.get(rating);
            return count == null ? 0 : count;
        }
    }


    @Override
    public String verify(String username, String code) throws Exception {
        UserVO user = findByUsername(username, false);

        if (!totpService.verifyCode(code, user.getSecret())) {
            throw new BadRequestException("Code is incorrect");
        }

        return "";
    }



}