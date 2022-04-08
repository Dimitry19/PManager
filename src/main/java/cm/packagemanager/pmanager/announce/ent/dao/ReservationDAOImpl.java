package cm.packagemanager.pmanager.announce.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.packagemanager.pmanager.announce.ent.vo.*;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.ReservationType;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.enums.ValidateEnum;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.common.utils.BigDecimalUtils;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.notification.enums.NotificationType;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserInfo;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ValidateReservationDTO;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


@Repository("reservationDAO")
public class ReservationDAOImpl extends Generic implements ReservationDAO {

    private static Logger logger = LoggerFactory.getLogger(ReservationDAOImpl.class);

    @Autowired
    private AnnounceDAO announceDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    CategoryDAO categoryDAO;


    @Transactional
    public int count(String queryname, Long id, String paramName, PageBy pageBy) throws Exception {

        return countByNameQuery(queryname, ReservationVO.class, id, paramName, pageBy);
    }

    /**
     * Ajoute une reservation a une annonce
     * @param reservationDTO
     * @return
     * @throws Exception
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, UserNotFoundException.class})
    public ReservationVO addReservation(ReservationDTO reservationDTO) throws Exception {
        logger.info("add reservation ");

        UserVO user = userDAO.findById(reservationDTO.getUserId());
        if (user == null)
            throw new UserNotFoundException("Utilisateur de la reservation non trouve");

        AnnounceVO announce = announceDAO.announce(reservationDTO.getAnnounceId());
        if (announce == null) {
            logger.error("add reservation {},{} non trouvé ou {} non trouvée", "La reservation n'a pas été ajoutée", "Utilisateur avec id=" + reservationDTO.getUserId(), " Annonce avec id=" + reservationDTO.getAnnounceId());
            throw new Exception("Announce non trouve");
        }
        List userAnnounces = announceDAO.announcesByUser(user);

        checkUserReservation(announce, userAnnounces);
        checkRemainWeight(announce, reservationDTO.getWeight());

        List<ReservationVO> reservations = findByUserNameQuery(ReservationVO.SQL_FIND_BY_USER, ReservationVO.class, user.getId(),null);
        if (CollectionsUtils.isNotEmpty(reservations)) {

            List<ReservationVO> anReservations = Optional.ofNullable(reservations
                    .stream()
                    .filter(res -> res.getValidate().equals(ValidateEnum.INSERTED) && res.getAnnounce().getId().equals(announce.getId()))
                    .collect(Collectors.toList())).get();

            if (CollectionsUtils.isNotEmpty(anReservations)) {

                ReservationVO rsv = (ReservationVO) CollectionsUtils.getFirst(anReservations);
                rsv.getAnnounce().setRemainWeight(announce.getRemainWeight().subtract(reservationDTO.getWeight()));
                rsv.setWeight(rsv.getWeight().add(reservationDTO.getWeight()));

                handleCategories(rsv, reservationDTO.getCategories());

                StringBuilder noteBuilder = new StringBuilder();
                noteBuilder.append(StringUtils.isNotEmpty(rsv.getDescription()) ? rsv.getDescription() + "\n" : "")
                        .append(StringUtils.isNotEmpty(reservationDTO.getDescription()) ? reservationDTO.getDescription() + "\n" : "");

                rsv.setDescription(noteBuilder.toString());

                return (ReservationVO)merge(rsv);
            }
        }

        announce.setRemainWeight(announce.getRemainWeight().subtract(reservationDTO.getWeight()));
        ReservationVO reservation = new ReservationVO();
        reservation.setUser(user);
        reservation.setAnnounce(announce);
        handleCategories(reservation, reservationDTO.getCategories());
        reservation.setWeight(reservationDTO.getWeight());
        reservation.setDescription(reservationDTO.getDescription());
        reservation.setValidate(ValidateEnum.INSERTED);
        reservation.setStatus(StatusEnum.VALID);
        save(reservation);
        String message= MessageFormat.format(notificationMessagePattern,user.getUsername(),
                " a fait une reservation de ["+reservation.getWeight()+" Kg ] sur votre annonce "+announce.getDeparture() +"/"+announce.getArrival(),
                " du " + DateUtils.getDateStandard(announce.getStartDate())
                        + " au "+ DateUtils.getDateStandard(announce.getEndDate()));
        generateEvent(announce,message);
        return reservation;
    }

    /**
     *  Ajourne une reservation
     * @param reservationDTO
     * @return
     * @throws Exception
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public ReservationVO updateReservation(UpdateReservationDTO reservationDTO) throws Exception {

        UserVO user = userDAO.findById(reservationDTO.getUserId());
        if (user == null) {
            throw new RecordNotFoundException("Aucun utilisateur trouvé");
        }

        ReservationVO reservation = (ReservationVO) findById(ReservationVO.class, reservationDTO.getId());
        if (reservation == null) {
            throw new Exception("Reservation non trouve");
        }
        AnnounceVO announce = reservation.getAnnounce();

        BigDecimal oldWeight=reservation.getWeight();
        if (BigDecimalUtils.lessThan(reservation.getWeight(), reservationDTO.getWeight())) {
            announce.setRemainWeight(announce.getRemainWeight().subtract(reservationDTO.getWeight().subtract(reservation.getWeight())));
        } else {
            announce.setRemainWeight(announce.getRemainWeight().add(reservation.getWeight().subtract(reservationDTO.getWeight())));
        }

        handleCategories(reservation, reservationDTO.getCategories());
        reservation.setWeight(reservationDTO.getWeight());
        if (StringUtils.isNotEmpty(reservationDTO.getDescription())) {
            reservation.setDescription(reservationDTO.getDescription());
        }
        update(reservation);
        String message= MessageFormat.format(notificationMessagePattern,user.getUsername(),
                " a modifié une reservation sur votre annonce "+announce.getDeparture() +"/"+announce.getArrival(),
                " du " + DateUtils.getDateStandard(announce.getStartDate())
                        + " et retour le "+ DateUtils.getDateStandard(announce.getEndDate())+" la reservation est passée de ["+oldWeight+" Kg ] a ["+reservationDTO.getWeight()+" Kg ] ");
        generateEvent(announce,message);
        return reservation;
    }

    /**
     * Supprime une reservation
     * @param id id de la reservation
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public boolean deleteReservation(Long id) throws Exception {

        ReservationVO reservation = (ReservationVO) findById(ReservationVO.class, id);
        if (reservation == null) {
            throw new Exception("Reservation non trouve");
        }

        if (!reservation.getValidate().equals(ValidateEnum.INSERTED)) {
            throw new Exception("Impossible d'eliminer cette reservation car déjà validée");
        }

        AnnounceVO announce = reservation.getAnnounce();
        announce.setRemainWeight(announce.getRemainWeight().add(reservation.getWeight()));
        update(announce);
        //delete(ReservationVO.class,id,true);
        String message= MessageFormat.format(notificationMessagePattern,reservation.getUser().getUsername(),
                " a supprimé une reservation de ["+reservation.getWeight() +" Kg ] sur votre annonce "+ announce.getDeparture() +"/"+announce.getArrival(),
                " de la date du " + DateUtils.getDateStandard(announce.getStartDate())
                        + " et retour le "+ DateUtils.getDateStandard(announce.getEndDate()));
        generateEvent(announce,message);
        return updateDelete(reservation);

    }

    /**
     *  Valide une reservation
     *
     * @param reservationDTO données de la reservation à valider
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ReservationVO validate(ValidateReservationDTO reservationDTO) throws Exception {

        ReservationVO reservation = (ReservationVO) findById(ReservationVO.class, reservationDTO.getId());
        if (reservation == null) {
            throw new Exception("Reservation non trouvee");
        }
        //reservation.setCancelled(!reservationDTO.isValidate());
        reservation.getAnnounce().setRemainWeight(!reservationDTO.isValidate() ? reservation.getAnnounce().getRemainWeight().add(reservation.getWeight())
                : reservation.getAnnounce().getRemainWeight());


        reservation.setValidate(reservationDTO.isValidate() ? ValidateEnum.ACCEPTED : ValidateEnum.REFUSED);
        update(reservation);

        String validate= reservationDTO.isValidate() ?  ValidateEnum.ACCEPTED.toValue():ValidateEnum.REFUSED.toValue();

        String message= MessageFormat.format(notificationMessagePattern,
                reservation.getUser().getUsername(),
                " a " +validate +" votre reservation  de [" +reservation.getWeight() +" kg ] sur l' annonce "+ reservation.getAnnounce().getDeparture() +"/"+reservation.getAnnounce().getArrival(),
                " de la date du " + DateUtils.getDateStandard(reservation.getAnnounce().getStartDate())
                        + " et retour le "+ DateUtils.getDateStandard(reservation.getAnnounce().getEndDate()));
        generateEvent(reservation,message);

        return reservation.getId() != null ? reservation : null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ReservationVO getReservation(long id) throws Exception {
        ReservationVO reservation = (ReservationVO) findById(ReservationVO.class, id);
        return reservation;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ReservationVO> otherReservations(long id, PageBy pageBy) throws Exception {
        List<ReservationVO> reservations = findBy(ReservationVO.FIND_ANNOUNCE_USER, ReservationVO.class, id, USER_PARAM, pageBy);
        handleReservationInfos(reservations);
        return reservations;
    }

    @Override
    public List<ReservationVO> reservationByAnnounce(Long announceId, PageBy pageBy) throws Exception {
        List<ReservationVO> reservations = findBy(ReservationVO.FINDBYANNOUNCE, ReservationVO.class, announceId, ANNOUNCE_PARAM, pageBy);
        handleReservationInfos(reservations);
        return reservations;

    }

    @Override
    public List reservationByUser(Long userId, ReservationType type, PageBy pageBy) throws Exception {
        List<ReservationUserVO> reservations = null;

        switch (type) {
            case RECEIVED:
                reservations = findByUserId(ReservationReceivedUserVO.class, userId, pageBy);
                break;
            case CREATED:
                reservations = findByUserId(ReservationUserVO.class, userId, pageBy);
                break;
        }
        handleReservationInfos(reservations);
        return reservations;
    }


    private void handleReservationInfos(List reservations) {
        if (CollectionsUtils.isNotEmpty(reservations)) {
            reservations.stream().forEach(r -> {

                if (r instanceof ReservationUserVO) {
                    ReservationUserVO res = (ReservationUserVO) r;
                    AnnounceInfo ai = new AnnounceInfo(res.getAnnounce());
                    res.setAnnounceInfo(ai);
                }
                if (r instanceof ReservationVO) {
                    ReservationVO res = (ReservationVO) r;
                    AnnounceInfo ai = new AnnounceInfo(res.getAnnounce());
                    res.setAnnounceInfo(ai);
                    UserInfo ui = new UserInfo(res.getUser());
                    res.setUserInfo(ui);
                }
            });
        }
    }

    public boolean updateDelete(ReservationVO reservation) throws BusinessResourceException, UserException {
        boolean result = false;
        try {
            if (reservation != null) {
                reservation.cancel();
                merge(reservation);
                reservation = (ReservationVO) get(ReservationVO.class, reservation.getId());
                result = (reservation != null) && (reservation.isCancelled());

            }
        } catch (Exception e) {
            throw new BusinessResourceException(e.getMessage());
        }
        return result;
    }


    @Override
    public boolean updateDelete(Object o) throws BusinessResourceException, UserException {
        return false;
    }

    @Override
    public String composeQuery(Object o, String alias) throws Exception {
        return null;
    }

    @Override
    public void composeQueryParameters(Object o, Query query) throws Exception {

    }


    @Override
    public void generateEvent(Object obj , String message) throws Exception {


        Set subscribers=new HashSet();
        Long id =null;
        UserVO user=null;
        NotificationType type=null;

        if(obj instanceof AnnounceVO){

            AnnounceVO announce= (AnnounceVO) obj;
            user= announce.getUser();
            id=announce.getId();
            type=NotificationType.ANNOUNCE;
        }

        if (obj instanceof ReservationVO){

            ReservationVO reservation= (ReservationVO)obj;
            user= reservation.getUser();
            id=reservation.getId();
            type=NotificationType.RESERVATION;
        }

        subscribers.add(user);

        if (CollectionsUtils.isNotEmpty(subscribers)){
            fillProps(props,id,message, user.getId(),subscribers);
            generateEvent(type);
        }
    }

    /**
     * Permet de controler dans le cas d'une reservation sur une annonce du type BUYER
     * si l'utilisateur qui fait la reservation a une annonce de voyage de la destination souhaitée
     *
     * @param announce
     * @param userAnnounces
     * @throws Exception
     */
    private void checkUserReservation(AnnounceVO announce, List<AnnounceVO> userAnnounces) throws Exception {

        switch (announce.getAnnounceType()) {
            case BUYER:
                if (CollectionsUtils.isEmpty(userAnnounces)) {
                    throw new Exception("Impossible pour  de faire " +
                            "une reservation car cet utilisateur  ne propose pas de voyage");
                }
                List check = Optional.ofNullable(userAnnounces.stream().filter(ua -> ua.getStatus() != StatusEnum.COMPLETED
                        && !ua.isCancelled() &&
                        StringUtils.equals(ua.getDeparture(), announce.getDeparture())
                        && StringUtils.equals(ua.getArrival(), announce.getArrival())
                        && ua.getStartDate().equals(announce.getStartDate()))
                        .collect(Collectors.toList()))
                        .orElseGet(Collections::emptyList);

                if (CollectionsUtils.isEmpty(check)) {
                    throw new Exception("Impossible pde faire " +
                            "une reservation car cet utilisateur  ne propose pas de voyage");
                }
                break;
            case SELLER:
                break;
            default:
                break;
        }

    }

    private void checkRemainWeight(AnnounceVO announce, BigDecimal weight) {

        if (BigDecimalUtils.lessThan(announce.getRemainWeight(), weight) || BigDecimalUtils.lessThan(announce.getWeight(), weight)) {
            throw new BusinessResourceException("Impossible de reserver une quantité superieure [" + weight + " kg] à la quantité disponible [" + announce.getRemainWeight() + " kg]");
        }
    }

    private void handleCategories(ReservationVO reservation, List<String> rcategories) {
        Set<CategoryVO> categories = new HashSet<>();

        reservation.getCategories().clear();
        if (CollectionsUtils.isNotEmpty(rcategories)) {
            rcategories.stream().filter(x -> StringUtils.isNotEmpty(x)).forEach(x -> {
                CategoryVO category = categoryDAO.findByCode(x);
                fillCategories(category, categories);
            });
        } else {
            CategoryVO category = categoryDAO.findByCode(FieldConstants.DEFAULT_CATEGORY);
            fillCategories(category, categories);
        }
        reservation.setCategories(categories);
    }

    private void fillCategories(CategoryVO category, Set<CategoryVO> categories) {
        categories.add(category);
    }


}
