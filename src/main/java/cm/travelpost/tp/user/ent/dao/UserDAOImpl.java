package cm.travelpost.tp.user.ent.dao;


import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.dao.Generic;
import cm.framework.ds.hibernate.enums.CountBy;
import cm.travelpost.tp.common.enums.RoleEnum;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.exception.UserNotFoundException;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.ObjectUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.communication.ent.vo.CommunicationVO;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.notification.enums.NotificationType;
import cm.travelpost.tp.security.PasswordGenerator;
import cm.travelpost.tp.user.ent.vo.RoleVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.users.*;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import dev.samstevens.totp.secret.SecretGenerator;
import org.hibernate.QueryParameterException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cm.travelpost.tp.common.Constants.TP_ACTIVATE_ACCOUNT;
import static cm.travelpost.tp.common.Constants.TP_INACTIVATE_ACCOUNT;


@Repository
public class UserDAOImpl extends Generic implements UserDAO {


    private static final String USER_NOT_FOUND = "Utilisateur non trouvé";
    private static Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);


    @Autowired
    RoleDAO roleDAO;

    @Autowired
    private SecretGenerator secretGenerator;


    @Value("${tp.travelpost.active.registration.enable}")
    protected boolean enableAutoActivateRegistration;



    public UserDAOImpl() {

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public int count(Object o,Long id,PageBy pageBy) throws BusinessResourceException {
        if(logger.isDebugEnabled()){
            logger.debug("User - count ");
        }

        if (o ==null){
            return count(UserVO.class, pageBy);
        }

        if(o instanceof CountBy && id!=null){

            CountBy cb= (CountBy)o;
            if(cb.equals(CountBy.SUBSCRIPTIONS)){
                return CollectionsUtils.size(subscriptions(id));
            }
            if(cb.equals(CountBy.SUBSCRIBERS)){
                return CollectionsUtils.size(subscribers(id));
            }
            return 0;
        }
      return 0;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserVO login(String username, String password) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("User: login");
        }

        return internalLogin(username, password);

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
    public void subscribe(SubscribeDTO subscribe) throws UserException,Exception {
        UserVO subscriber = findById(subscribe.getSubscriberId());
        UserVO subscription = findById(subscribe.getSubscriptionId());

        if (subscriber != null && subscription != null) {
            subscriber.addSubscription(subscription);
            subscription.addSubscriber(subscriber);

            update(subscriber);
            update(subscription);

           String message= buildNotificationMessage(NotificationType.SUBSCRIBE,subscriber.getUsername(),null, null,
                    null, null, null);

            generateEvent(subscription,message);
        } else throw new UserException("Une erreur survenue pendant l'abonnement, veuillez reessayer");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
    public void unsubscribe(SubscribeDTO subscribe) throws UserException,Exception {

        UserVO subscriber = findById(subscribe.getSubscriberId());
        UserVO subscription = findById(subscribe.getSubscriptionId());

        if (subscriber != null && subscription != null) {
            subscriber.removeSubscription(subscription);
            subscription.removeSubscriber(subscriber);

            update(subscriber);
            update(subscription);


            String message=buildNotificationMessage( NotificationType.UNSUBSCRIBE,subscriber.getUsername(),null, null,
                    null, null, null);
            generateEvent(subscription,message);
        } else throw new UserException("Une erreur survenue pendant la desinscription, veuillez reessayer");
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
    public List<UserVO> subscriptions(Long userId) throws UserException {

        if(logger.isDebugEnabled()){
            logger.debug("Retrieve subscriptions for the user with id {}", userId);
        }

        UserVO user = findById(userId);
        if (user == null)
            throw new UserNotFoundException(USER_NOT_FOUND);

        return new ArrayList<>(user.getSubscriptions());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
    public List<UserVO> subscribers(Long userId) throws UserException {

        if(logger.isDebugEnabled()){
            logger.info("Retrieve subscribers for the user with id {}", userId);
        }

        UserVO user = findById(userId);
        if (user == null)
            throw new UserNotFoundException(USER_NOT_FOUND);

        return new ArrayList<>(user.getSubscribers());
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserVO> getAllUsers() throws Exception {
        if(logger.isDebugEnabled()){
            logger.info("User: all users");
        }

        return all(UserVO.class, null);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserVO> getAllUsersToConfirm() throws Exception {

        if(logger.isDebugEnabled()){
            logger.info("User:  users to confirm");
        }

        return findBy(UserVO.JOB_CONFIRM, UserVO.class, 0, ACTIVE_PARAM, null);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserVO> getAllUsers(PageBy pageBy) throws Exception {

        filters = new String[2];
        filters[0] = FilterConstants.CANCELLED;
        filters[1] = FilterConstants.ACTIVE_MBR;

        logger.info("User: all users page by");
        //return  helper.allUsers();

        return all(UserVO.class, null, filters);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserVO getUser(Long id) throws UserException {

        if(logger.isDebugEnabled()){
            logger.info("User: get user with id {}", id);
        }

        try {
            UserVO user = findById(id);
            calcolateAverage(user);
            return user;
        } catch (UserException e) {
            logger.error("User: {}" , e);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
    public UserVO register(RegisterDTO register) throws UserException {
        if(logger.isDebugEnabled()){
            logger.info("User registration {}", register);
        }
        try {

            filters = new String[2];

            map.put(EMAIL_PARAM, register.getEmail());
            map.put(USERNAME_PARAM, register.getUsername());

            filters[1] = FilterConstants.ACTIVE_MBR;
            filters[0] = FilterConstants.CANCELLED;

            UserVO user = (UserVO) findByNaturalIds(UserVO.class, map, filters);
            if (user != null) {
                logger.error("User: username deja existant");
                user.setError(WebServiceResponseCode.ERROR_USERNAME_REGISTER_LABEL + " et " + WebServiceResponseCode.ERROR_EMAIL_REGISTER_LABEL);
                return user;
            }

            user = findByEmail(register.getEmail());
            if (user != null) {
                logger.error(WebServiceResponseCode.ERROR_EMAIL_REGISTER_LABEL);
                user.setError(WebServiceResponseCode.ERROR_EMAIL_REGISTER_LABEL);
                return user;
            }

            user = findByOnlyUsername(register.getUsername(), true);
            if (user != null) {
                user.setError(WebServiceResponseCode.ERROR_USERNAME_REGISTER_LABEL);
                return user;
            }
            user = new UserVO();
            user.setEmail(register.getEmail());
            user.setUsername(register.getUsername());
            user.setPassword(PasswordGenerator.encrypt(register.getPassword()));
            user.setFirstName(register.getFirstName());
            user.setLastName(register.getLastName());
            user.setPhone(register.getPhone());
            user.setActive(enableAutoActivateRegistration?TP_ACTIVATE_ACCOUNT:TP_INACTIVATE_ACCOUNT); // TODO remettre à 0 quand le service d'envoi mail sera de nouveau disponible
            user.setEnableNotification(Boolean.TRUE);
            user.setGender(register.getGender());
            user.setConfirmationToken(UUID.randomUUID().toString());
            Long id=(Long)save(user);
            user=findById(id);
            setRole(user, register.getRole());
            return user;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Une erreur est survenue durant l'enregistrement {}",e.getMessage());
            throw new UserException("Erreur durant l'inscription de l'utilisateur");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
    public UserVO updateUser(UpdateUserDTO userDTO) throws Exception {
        if(logger.isDebugEnabled()){
            logger.info("User update {}", userDTO);
        }

        UserVO user = findById(userDTO.getId());

        if (user == null) {
            user = findByEmail(userDTO.getEmail());
        }

        if (user != null) {
            user.setPhone(userDTO.getPhone());
            user.setGender(userDTO.getGender());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            //setRole(user, userDTO.getRole());
            calcolateAverage(user);
            return (UserVO) merge(user);
        } else throw new UserException(USER_NOT_FOUND);

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateUser(UserVO user) {
            update(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
    public boolean deleteUser(Long id) throws UserException {
        if(logger.isDebugEnabled()){
            logger.debug("Delete user with id {}", id);
        }
        //delete(UserVO.class,id,false);
        return updateDelete(id);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = BusinessResourceException.class)
    public void removeUser(UserVO user) throws BusinessResourceException {
        if (user != null) {
            remove(user);
        }
    }


    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserVO findByUsername(String username) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("User: find by username {}", username);
        }

        filters = new String[2];
        filters[0] = FilterConstants.CANCELLED;
        filters[1] = FilterConstants.ACTIVE_MBR;
        return (UserVO) findByUniqueResult(UserVO.USERNAME, UserVO.class, username, USERNAME_PARAM, null, filters);

    }


    @Override
    public UserVO findByOnlyUsername(String username, boolean disableFilter) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("User: find by only username {}", username);
        }
        if (!disableFilter) {
            filters = new String[2];
            filters[0] = FilterConstants.CANCELLED;
            filters[1] = FilterConstants.ACTIVE_MBR;
        }

        return (UserVO) findByUniqueResult(UserVO.USERNAME, UserVO.class, username, USERNAME_PARAM, null, filters);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserVO> search(UserSeachDTO search, PageBy pageBy) throws BusinessResourceException {

        return commonSearchUser(search, pageBy);
    }


    private List commonSearchUser(UserSeachDTO search, PageBy pageBy) throws AnnounceException {
        filters= new String[2];
        filters[0]= FilterConstants.CANCELLED;
        filters[1]= FilterConstants.ACTIVE_MBR;
        String where = composeQuery(search, USER_TABLE_ALIAS);
        Query query = search(UserVO.SEARCH , where,filters);
        composeQueryParameters(search, query);
        pageBy(query,pageBy);
        return query.list();
    }



    @Override
    public UserVO findByToken(String token) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("User: find token  {}", token);
        }

        filters = new String[1];
        filters[0] = FilterConstants.CANCELLED;

        return (UserVO) findByUniqueResult(UserVO.CONF_TOKEN, UserVO.class, token, CONFIRM_TOKEN_PARAM, null, filters);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserVO findById(Long id) throws UserException {

        try {
            if(logger.isDebugEnabled()){
                logger.info("User: find by id  {}", id);
            }

            filters = new String[2];
            filters[0] = FilterConstants.ACTIVE_MBR;
            filters[1] = FilterConstants.CANCELLED;

            return (UserVO) find(UserVO.class, id,filters);
            //Hibernate.initialize(user.getMessages()); // on l'utilise quand la fetch avec message est lazy
            //return user;
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
    }


    @Override
    public UserVO login(String username) throws UserException {

        filters = new String[2];
        filters[0] = FilterConstants.ACTIVE_MBR;
        filters[1] = FilterConstants.CANCELLED;
        return (UserVO) findById(UserVO.class, username,filters);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserVO findByEmail(String email) throws Exception {
        if(logger.isDebugEnabled()){
            logger.info("User: find by email {}", email);
        }
        return (UserVO) findByUniqueResult(UserVO.EMAIL, UserVO.class, email, EMAIL_PARAM);
    }

    @Override
    public UserVO findByFacebookId(String facebookId) throws Exception {
        if(logger.isDebugEnabled()){
            logger.debug("User: find by facebook");
        }
        filters = new String[2];
        filters[0] = FilterConstants.CANCELLED;
        filters[1] = FilterConstants.ACTIVE_MBR;
        return (UserVO) findByUniqueResult(UserVO.FACEBOOK, UserVO.class, facebookId, FACEBOOK_ID_PARAM, null, filters);
    }

    @Override
    public UserVO findByGoogleId(String googleId) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("User: find by google");
        }
        filters = new String[2];
        filters[0] = FilterConstants.CANCELLED;
        filters[1] = FilterConstants.ACTIVE_MBR;
        return (UserVO) findByUniqueResult(UserVO.FACEBOOK, UserVO.class, googleId, GOOGLE_ID_PARAM, null, filters);

    }

    @Override
    public boolean setRole(UserVO user, RoleEnum roleId) throws Exception {
        if(logger.isDebugEnabled()){
            logger.info("User:  set role");
        }

        RoleVO role = roleDAO.findByDescription(roleId.name());

        if (user != null && role != null) {
            user.addRole(role);

            return (merge(user)!=null);
        } else {
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = UserException.class)
    public boolean setRole(String email, RoleEnum roleId) throws Exception {

        UserVO user = findByEmail(email);
        return setRole(user, roleId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, UserException.class})
    public boolean deleteUser(UserVO user) throws BusinessResourceException, UserException {
        if(logger.isDebugEnabled()){
            logger.debug("User: update delete");
        }
        if (user != null) {
            return updateDelete(user.getId());
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public boolean checkLogin(LoginDTO lr) throws Exception {
        if(logger.isDebugEnabled()){
            logger.info("User: check login");
        }
        UserVO user = findByOnlyUsername(lr.getUsername(), true);
        return user != null;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public boolean updateDelete(Object o) throws BusinessResourceException, UserException {
        boolean result = false;

        try {

            Long id=(Long)o;
            UserVO user = findById(id);
            if (user != null) {
                user.updateDeleteChildrens();
                user.cancel();
                merge(user);
                user = (UserVO) get(UserVO.class, id);

                result = (user != null) && (user.isCancelled());
            }
        } catch (Exception e) {
            throw new UserException(e.getMessage());
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = {UserException.class, Exception.class})
    public UserVO manageNotification(Long userId, boolean enableNotification) throws UserException {

        UserVO user = findById(userId);
        if (user == null){
            throw new UserException("Utilisateur non trouvé");
        }
        try {

            boolean precedent = user.isEnableNotification();
            if (precedent != enableNotification) {
                user.setEnableNotification(enableNotification);
                update(user);
                return (UserVO) get(UserVO.class, userId);
            }
        } catch (UserException e) {
            logger.error("Erreur durant la modification de la gestion des notifications {}", e.getMessage());
            throw e;
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = {UserException.class, Exception.class})
    public UserVO manageMfa(Long userId, boolean mfa) throws UserException {
        UserVO user = findById(userId);
        if (user == null){
            throw new UserException("Utilisateur non trouvé");
        }
        try {

            boolean precedent = user.isEnableNotification();
            if (precedent != mfa) {
                user.setMultipleFactorAuthentication(mfa);
                update(user);
                return (UserVO) get(UserVO.class, userId);
            }
        } catch (UserException e) {
            logger.error("Erreur durant la modification de la gestion mfa {}", e.getMessage());
            throw e;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean editPassword(Long userId, String oldPassword, String newPassword) throws UserException {
        if(logger.isDebugEnabled()){
            logger.info("Modification du mot de passe");
        }
        UserVO user = findById(userId);
        if (user == null) {
            logger.error("Erreur : aucun utilisateur correspondant a l'id{}" , userId);
            throw new UserException("Aucun utilisateur trouvé avec cet identifiant" + userId);
        }
        String decryptedPassword = PasswordGenerator.decrypt(user.getPassword());
        if (!StringUtils.equals(decryptedPassword, oldPassword)) {
            logger.error("Erreur : mot de passe inexact {}-{}:", decryptedPassword, oldPassword);
            throw new UserException("Le mot de passe ne correspond pas: veuillez contrôler l'ancien mot de passe");
        }

        user.setPassword(PasswordGenerator.encrypt(newPassword));
        merge(user);
        return true;
    }

    @Override
    @Transactional
    public List<CommunicationVO> communications(Long userId) throws Exception {
        UserVO user = findById(userId);
        if (user == null) {
            logger.error("Erreur : aucun utilisateur correspondant a l'id {}" , userId);
            throw new UserException("Aucun utilisateur trouvé avec cet identifiant" + userId);
        }

        return user.getCommunications().stream().collect(Collectors.toList());
    }

    private UserVO internalLogin(String username, String password) throws BusinessResourceException {
        try {
            if(logger.isDebugEnabled()){
                logger.info("User: internal login");
            }
            String encryptedPassword = PasswordGenerator.encrypt(password);

            UserVO userFound = findByUsername(username);

            if ((encryptedPassword == null || userFound == null)) {
                throw new BusinessResourceException("UserNotFound", "Mot de passe incorrect", HttpStatus.NOT_FOUND);
            }

            if ((StringUtils.notEquals(encryptedPassword, userFound.getPassword()))) {
                throw new BusinessResourceException("UserNotFound", "Mot de passe incorrect", HttpStatus.NOT_FOUND);
            }

            if (encryptedPassword.equals(userFound.getPassword())) {
                return userFound;
            }
            throw new BusinessResourceException("UserNotFound", "Mot de passe incorrect", HttpStatus.NOT_FOUND);

        } catch (BusinessResourceException ex) {
            logger.error("Login ou mot de passe incorrect {}", ex.getMessage());
            throw new BusinessResourceException("UserNotFound", " Nom utilisateur ou mot de passe incorrect", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("Une erreur technique est survenue {}", ex.getMessage());
            throw new BusinessResourceException("TechnicalError", "Une erreur technique est survenue", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public String composeQuery(Object o, String alias) {

        UserSeachDTO search = (UserSeachDTO) o;

        StringBuilder hql = new StringBuilder(WHERE);
        try {
            boolean and = false;

            if (StringUtils.isNotEmpty(search.getUsername())) {
                hql.append(alias +USERNAME_PARAM+"=:"+USERNAME_PARAM);
            }
//            and = StringUtils.isNotEmpty(hql.toString()) && !StringUtils.equals(hql.toString(),WHERE);
//            if (StringUtils.isNotEmpty(search.getUsername())) {
//                if (and) {
//                    hql.append(AND);
//                }
//                hql.append(alias + ANNOUNCE_TYPE_PARAM +"=:" + ANNOUNCE_TYPE_PARAM);
//            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return hql.toString();
    }

    @Override
    public void composeQueryParameters(Object o, Query query) {
        try{

            UserSeachDTO search = (UserSeachDTO) o;

            if (ObjectUtils.isCallable(search,USERNAME_PARAM) && StringUtils.isNotEmpty(search.getUsername())){
				query.setParameter(USERNAME_PARAM, search.getUsername());
			}

    } catch (
         QueryParameterException e) {
        logger.error(e.getMessage());
        e.printStackTrace();
        throw new QueryParameterException("Erreur dans la fonction " + UserDAO.class.getName() + " composeQueryParameters");
        }
    }

    @Override
    public UserVO generateSecret(UserVO user) throws Exception {

        user.setMultipleFactorAuthentication(Boolean.TRUE);
        user.setSecret(secretGenerator.generate());
        update(user);

        return findById(user.getId());
    }

    @Override
    public void generateEvent(Object obj , String message)  throws UserException,Exception{

        UserVO user= (UserVO) obj;
        Set subscribers=new HashSet();

        if(user.isEnableNotification()){
            subscribers.add(user);
        }

        if (CollectionsUtils.isNotEmpty(subscribers)){
            fillProps(props,user.getId(),message, user.getId(),subscribers);
            generateEvent(NotificationType.USER);
        }
    }
}
