package cm.packagemanager.pmanager.user.service;

import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.mail.MailSender;
import cm.packagemanager.pmanager.common.mail.MailType;
import cm.packagemanager.pmanager.common.utils.HTMLEntities;
import cm.packagemanager.pmanager.rating.ent.vo.RatingCountVO;
import cm.packagemanager.pmanager.rating.enums.Rating;
import cm.packagemanager.pmanager.review.ent.bo.ReviewsSummaryBO;
import cm.packagemanager.pmanager.review.ent.dao.ReviewDAO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewIdVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewDetailsVO;
import cm.packagemanager.pmanager.security.PasswordGenerator;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import cm.packagemanager.pmanager.ws.requests.review.ReviewDTO;
import cm.packagemanager.pmanager.ws.requests.review.UpdateReviewDTO;
import cm.packagemanager.pmanager.ws.requests.users.*;
import com.sendgrid.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cm.packagemanager.pmanager.constant.FieldConstants.FACEBOOK_PROVIDER;
import static cm.packagemanager.pmanager.constant.FieldConstants.GOOGLE_PROVIDER;


/*
Le fait d’avoir des singletons a un impact en environnement multi-threadé
○ Les variables de classe sont partagées entre les threads
○ Les beans doivent donc être thread-safe (d'où le transactional)
*
* */
@Service
public class UserServiceImpl  implements  UserService{

	public static final String USER_WS="/ws/user";

	@Autowired
	UserDAO userDAO;

	@Autowired
	ReviewDAO reviewDAO;

	@Autowired
	MailSender mailSender;


	@PostConstruct
	public void init() {
		System.out.println("User service starts...." );
	}


	public boolean checkLogin(LoginDTO lr) throws BusinessResourceException, UserException {
		return userDAO.checkLogin(lr);
	}

	@Override
	public int count(PageBy pageBy) throws Exception {
		return userDAO.count(pageBy);
	}

	@Override
	public void subscribe(SubscribeDTO subscribe) throws UserException {
		userDAO.subscribe(subscribe);
	}


	@Override
	public void unsubscribe(SubscribeDTO subscribe) throws UserException {
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

	public UserVO login(LoginDTO lr ) throws UserException {
		UserVO user=null;
		if(lr.getUsername()!=null){
			user= userDAO.login(lr.getUsername(), lr.getPassword());
		}

		if(user== null){
			if(lr.getEmail()!=null){
				user=userDAO.findByEmail(lr.getEmail());
			}

			if(lr.getProvider()!=null){

				if(lr.getProvider().equals(GOOGLE_PROVIDER)){
					user=userDAO.findByGoogleId(lr.getSocialId());
				}

				if(lr.getProvider().equals(FACEBOOK_PROVIDER)){
					user=userDAO.findByFacebookId(lr.getSocialId());
				}
			}
		}
		return user;
	}

	public UserVO update(UserVO user) throws UserException {
		return userDAO.update(user);
	}

	public boolean delete(UserVO user) throws UserException {
		return userDAO.deleteUser(user);
	}

	public void remove(UserVO user) throws UserException {
		 userDAO.remove(user);
	}


	public List<UserVO> getAllUsers(PageBy pageBy)throws UserException {
		return userDAO.getAllUsers(pageBy);
	}

	public List<UserVO> getAllUsers()throws UserException {
		return userDAO.getAllUsers();
	}

	public List<UserVO> getAllUsersToConfirm()throws UserException {
		return userDAO.getAllUsersToConfirm();
	}

	public UserVO getUser(Long id) throws UserException{
		return userDAO.getUser(id);
	}


	public UserVO register(RegisterDTO register) throws UserException{

		return userDAO.register(register);
	}

	@Transactional(readOnly = true)
	public List<UserVO> find(UserSeachDTO userSeachDTO, PageBy pageBy) throws Exception {
		return userDAO.find(userSeachDTO, pageBy);
	}

	@Transactional(rollbackFor = UserException.class)
	public UserVO updateUser(UpdateUserDTO userDTO) throws UserException{
		return userDAO.updateUser(userDTO);
	}

	public Response managePassword(String email) {
		UserVO user=findByEmail(email);


		if(user==null)
			throw new UserException( "Aucun utilisateur trouvé avec cette adresse email :" +email);

			String decrypt=PasswordGenerator.decrypt(user.getPassword());

			List<String> labels=new ArrayList<String>();
			List<String> emails=new ArrayList<String>();

			labels.add(MailType.PASSWORD_KEY);
			labels.add(MailType.USERNAME_KEY);

			emails.add(email);

			return mailSender.sendMailMessage(MailType.PASSWORD_TEMPLATE,MailType.PASSWORD_TEMPLATE_TITLE,	MailSender.replace(user,labels,null,decrypt),
					emails,null,null, null,user.getUsername(),null,false);
	}

	@Transactional(rollbackFor = UserException.class)
	public boolean deleteUser(Long id) throws UserException{
		return userDAO.deleteUser(id);
	}

	public UserVO findByEmail(String email) {
		return userDAO.findByEmail(email);
	}


	public boolean setRoleToUser(RoleToUserDTO roleToUser) {

		return userDAO.setRole(roleToUser.getEmail(),roleToUser.getRole());
	}

	@Transactional(readOnly = true)
	public UserVO findByUsername(String username, boolean isReg) {
		return userDAO.findByOnlyUsername(username,isReg);
	}

	@Transactional(readOnly = true)
	public UserVO findByToken(String token) {
		return userDAO.findByToken(token);
	}


	public Response sendMail(MailDTO mr, boolean active) {

		UserVO user=findByEmail(mr.getFrom());

		if(user!=null){

			List<String> labels=new ArrayList<String>();

			labels.add(MailType.BODY_KEY);

			return mailSender.sendMailMessage(MailType.SEND_MAIL_TEMPLATE,mr.getSubject(),MailSender.replace(user,labels,mr.getBody(),null),mr.getTo(),mr.getCc(),mr.getBcc(), mr.getFrom(),user.getUsername(),null,true);
		}
		return  null;
	}

	@Transactional(rollbackFor = UserException.class)
	public Response buildAndSendMail(HttpServletRequest request , UserVO user) throws UserException{

		String appUrl=HTMLEntities.buildUrl(request,USER_WS);
		StringBuilder sblink= new StringBuilder("<a href=");
		sblink.append(appUrl);
		sblink.append("/confirm?token=");
		sblink.append(user.getConfirmationToken());
		sblink.append(">Confirmation</a>");;

		String body=MailType.CONFIRM_TEMPLATE_BODY +sblink.toString();
		List<String> labels=new ArrayList<String>();
		List<String> emails=new ArrayList<String>();

		labels.add(MailType.BODY_KEY);

		emails.add(user.getEmail());

		Response sent= mailSender.sendMailMessage(MailType.CONFIRM_TEMPLATE,MailType.CONFIRM_TEMPLATE_TITLE,	MailSender.replace(user,labels,body,null),
				emails,null,null, null,user.getUsername(),null,false);

		return sent;
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
		return reviewDAO.save(review);
	}

	@Override
	public ReviewVO addReview(ReviewDTO reviewDTO) throws Exception{
		UserVO user =getUser(reviewDTO.getUserId());
		UserVO ratingUser =getUser(reviewDTO.getRatingUserId());

		if (ratingUser.equals(user)){
			throw new UserException("Un utilisateur ne peut pas s'evaluer");
		}
		ReviewDetailsVO details= new ReviewDetailsVO(reviewDTO.getRating(), reviewDTO.getTitle(),reviewDTO.getDetails());
		ReviewVO review = new ReviewVO(user, 1, details);
		ReviewIdVO id= new ReviewIdVO();
		id.setToken(Constants.DEFAULT_TOKEN);
		review.setReviewId(id);
		return reviewDAO.save(review);
	}

	@Override
	public ReviewVO updateReview(UpdateReviewDTO reviewDTO) throws Exception{
		UserVO user = getUser(reviewDTO.getUserId());
		UserVO ratingUser =getUser(reviewDTO.getRatingUserId());

		if (ratingUser.equals(user)){
			throw new UserException("Un utilisateur ne peut donner un avis sur lui même");
		}

		ReviewVO review = reviewDAO.findById(reviewDTO.getId());
		if (review ==null){
			throw new Exception("Avis non existant");
		}
		return reviewDAO.update(review);
	}

	@Override
	public boolean deleteReview(Long id) throws Exception {
		 return reviewDAO.delete(id);
	}

	@Override
	public ReviewsSummaryBO getReviewSummary(UserVO user) {
		List<RatingCountVO> ratingCounts = userDAO.findRatingCounts(user);
		return new ReviewsSummaryBOImpl(ratingCounts);
	}

	private static class ReviewsSummaryBOImpl implements ReviewsSummaryBO {

		private final Map<Rating, Long> ratingCount;

		public ReviewsSummaryBOImpl(List<RatingCountVO> ratingCounts) {
			this.ratingCount = new HashMap<Rating, Long>();
			for (RatingCountVO ratingCount : ratingCounts) {
				this.ratingCount.put(ratingCount.getRating(), ratingCount.getCount());
			}
		}

		@Override
		public long getNumberOfReviewsWithRating(Rating rating) {
			Long count = this.ratingCount.get(rating);
			return count == null ? 0 : count;
		}
	}
}

