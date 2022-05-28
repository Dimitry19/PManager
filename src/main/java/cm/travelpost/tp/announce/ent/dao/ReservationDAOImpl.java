package cm.travelpost.tp.announce.ent.dao;

import cm.framework.ds.common.ent.vo.KeyValue;
import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.dao.Generic;

import cm.travelpost.tp.announce.ent.vo.*;
import cm.travelpost.tp.common.enums.AnnounceType;
import cm.travelpost.tp.common.enums.ReservationType;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.enums.ValidateEnum;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.RecordNotFoundException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.exception.UserNotFoundException;
import cm.travelpost.tp.common.utils.BigDecimalUtils;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.notification.enums.NotificationType;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserInfo;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.announces.ReservationDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateReservationDTO;
import cm.travelpost.tp.ws.requests.announces.ValidateReservationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cm.travelpost.tp.common.enums.ReservationType.RECEIVED;
import static cm.travelpost.tp.notification.enums.NotificationType.*;


@Repository("reservationDAO")
public class ReservationDAOImpl extends Generic implements ReservationDAO<ReservationVO> {

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
     * @param dto
     * @return
     * @throws Exception
     */

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class, UserNotFoundException.class})
    public ReservationVO addReservation(ReservationDTO dto) throws Exception {
        logger.info("add reservation ");

        UserVO user = userDAO.findById(dto.getUserId());
        if (user == null)
            throw new UserNotFoundException("Utilisateur de la reservation non trouve");

        AnnounceVO announce = announceDAO.announce(dto.getAnnounceId());
        if (announce == null) {
            logger.error("add reservation {},{} non trouvé ou {} non trouvée", "La reservation n'a pas été ajoutée", "Utilisateur avec id=" + dto.getUserId(), " Annonce avec id=" + dto.getAnnounceId());
            throw new Exception("Announce non trouve");
        }

        checkRemainWeight(announce, dto.getWeight());

        if(announce.getAnnounceType() == AnnounceType.BUYER){
            List userAnnounces = announceDAO.announcesByUser(user);
            checkUserReservation(announce, userAnnounces);
        }

        List<ReservationVO> reservations = reservationByAnnounceAndUser(user.getId(),announce.getId(), ValidateEnum.INSERTED, null);

        if (CollectionsUtils.isNotEmpty(reservations)) {
            ReservationVO rsv = (ReservationVO) CollectionsUtils.getFirst(reservations);
            rsv.getAnnounce().setRemainWeight(announce.getRemainWeight().subtract(dto.getWeight()));
            rsv.setWeight(rsv.getWeight().add(dto.getWeight()));

            handleCategories(rsv, dto.getCategories());

            StringBuilder noteBuilder = new StringBuilder();
            noteBuilder.append(StringUtils.isNotEmpty(rsv.getDescription()) ? rsv.getDescription() + "\n" : "")
                    .append(StringUtils.isNotEmpty(dto.getDescription()) ? dto.getDescription() + "\n" : "");

            rsv.setDescription(noteBuilder.toString());

            return (ReservationVO)merge(rsv);
        }

        announce.setRemainWeight(announce.getRemainWeight().subtract(dto.getWeight()));
        ReservationVO reservation = new ReservationVO();
        reservation.setUser(user);
        reservation.setAnnounce(announce);
        handleCategories(reservation, dto.getCategories());
        reservation.setWeight(dto.getWeight());
        reservation.setDescription(dto.getDescription());
        reservation.setValidate(ValidateEnum.INSERTED);
        reservation.setStatus(StatusEnum.VALID);
        reservation.setEstimateValue(dto.getEstimateValue());
        save(reservation);

        BigDecimal sumQtyRes=announceDAO.checkQtyReservations(announce.getId(),false);

        announceDAO.warning(reservation, announce.getEndDate(),announce.getWeight(),sumQtyRes);

        String message=buildNotificationMessage(RESERVATION,reservation.getUser().getUsername(),
                reservation.getAnnounce().getDeparture(),
                reservation.getAnnounce().getArrival(),DateUtils.getDateStandard(reservation.getAnnounce().getStartDate()),
                DateUtils.getDateStandard(reservation.getAnnounce().getEndDate()),String.valueOf(reservation.getWeight()));


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
        AnnounceMasterVO announce = reservation.getAnnounce();

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
        reservation.setEstimateValue(reservationDTO.getEstimateValue());
        update(reservation);

        BigDecimal sumQtyRes=announceDAO.checkQtyReservations(announce.getId(),false);

        announceDAO.warning(reservation, announce.getEndDate(),announce.getWeight(),sumQtyRes);

        String kg=" la reservation est passée de ["+oldWeight+" Kg ] a ["+reservationDTO.getWeight()+" Kg  ";

        String message=buildNotificationMessage(RESERVATION_UPD,reservation.getUser().getUsername(),
                reservation.getAnnounce().getDeparture(),
                reservation.getAnnounce().getArrival(),DateUtils.getDateStandard(reservation.getAnnounce().getStartDate()),
                DateUtils.getDateStandard(reservation.getAnnounce().getEndDate()),kg);

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

        AnnounceMasterVO announce = reservation.getAnnounce();
        announce.setRemainWeight(announce.getRemainWeight().add(reservation.getWeight()));
        update(announce);

        String message=buildNotificationMessage(RESERVATION_DEL,reservation.getUser().getUsername(),
                reservation.getAnnounce().getDeparture(),
                reservation.getAnnounce().getArrival(),DateUtils.getDateStandard(reservation.getAnnounce().getStartDate()),
                DateUtils.getDateStandard(reservation.getAnnounce().getEndDate()),String.valueOf(reservation.getWeight()));

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
            throw new Exception("Reservation non trouvée");
        }

        if(reservation.getValidate()!=ValidateEnum.INSERTED){
            return reservation;
        }

        reservation.getAnnounce().setRemainWeight(!reservationDTO.isValidate() ? reservation.getAnnounce().getRemainWeight().add(reservation.getWeight())
                : reservation.getAnnounce().getRemainWeight());


        reservation.setValidate(reservationDTO.isValidate() ? ValidateEnum.ACCEPTED : ValidateEnum.REFUSED);
        update(reservation);

        String message=buildNotificationMessage(((reservationDTO.isValidate())?RESERVATION_VALIDATE:RESERVATION_UNVALIDATE),reservation.getUser().getUsername(),
                reservation.getAnnounce().getDeparture(),
                reservation.getAnnounce().getArrival(),DateUtils.getDateStandard(reservation.getAnnounce().getStartDate()),
                DateUtils.getDateStandard(reservation.getAnnounce().getEndDate()),String.valueOf(reservation.getWeight()));


        generateEvent(reservation,message);

        return reservation.getId() != null ? reservation : null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ReservationVO getReservation(long id) throws Exception {
        ReservationVO reservation= (ReservationVO) findById(ReservationVO.class, id);

        if(reservation!=null){
            BigDecimal sumQtyRes=announceDAO.checkQtyReservations(reservation.getAnnounce().getId(),false);
            announceDAO.warning(reservation, reservation.getAnnounce().getEndDate(),reservation.getAnnounce().getWeight(),sumQtyRes);
        }
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
        List<ReservationVO> reservations = findBy(ReservationVO.FIND_BY_ANNOUNCE, ReservationVO.class, announceId, ANNOUNCE_PARAM, pageBy);
        handleReservationInfos(reservations);
        return reservations;

    }

    @Override
    public List reservationByUser(Long userId, ReservationType type, PageBy pageBy) throws Exception {
        List<ReservationUserVO> reservations = null;

        if (type == RECEIVED) {
            reservations = findByJoinUserId(ReservationReceivedUserVO.class, userId, pageBy);
        }else {
             reservations = findByUserId(ReservationUserVO.class, userId, pageBy);
        }
        handleReservationInfos(reservations);
        return reservations;
    }

    @Override
    public List reservationByAnnounceAndUser(Long userId, Long announceId, ValidateEnum validateEnum,PageBy pageBy) throws Exception {
        setMap(new KeyValue(USER_PARAM, userId),new KeyValue(ANNOUNCE_PARAM, announceId),new KeyValue(VALIDATE_PARAM, validateEnum));
        return findBy(ReservationVO.FIND_BY_ANNOUNCE_AND_USER_AND_VALIDATE, ReservationVO.class, getMap(), null, emptyFilters());
    }

    private void handleReservationInfos(List reservations) throws Exception{
        if (CollectionsUtils.isNotEmpty(reservations)) {
            reservations.stream().forEach(r -> {
                try {
                    reservationInfo(r);

                } catch (Exception e) {
                    logger.error("Erreur durant la gestion des reservations {}",e);
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
    public void generateEvent(Object obj , String message) throws Exception {


        Set subscribers=new HashSet();
        Long id =null;
        UserVO user=null;

        if(obj instanceof AnnounceVO || obj instanceof AnnounceMasterVO){

            AnnounceMasterVO announce= (AnnounceMasterVO) obj;
            user= announce.getUser();
            id=announce.getId();
        }

        if (obj instanceof ReservationVO){

            ReservationVO reservation= (ReservationVO)obj;
            user= reservation.getUser();
            id=reservation.getAnnounce().getId();
        }
        if(user!=null){
            subscribers.add(user);

            if (CollectionsUtils.isNotEmpty(subscribers)){
                fillProps(props,id,message, user.getId(),subscribers);
                generateEvent(NotificationType.ANNOUNCE);
            }
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
        if (CollectionsUtils.isEmpty(userAnnounces)) {
            throw new Exception("Impossible pour  de faire une reservation car cet utilisateur  ne propose pas de voyage");
        }
        List check = Optional.ofNullable(
                 userAnnounces
                .stream()
                .filter(a->filterUserAnnounces(a, announce))
                .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);

        if (CollectionsUtils.isEmpty(check)) {
            throw new Exception("Impossible pde faire une reservation car cet utilisateur  ne propose pas de voyage");
        }
    }

    private boolean filterUserAnnounces(AnnounceVO ua, AnnounceVO announce){
        return ua.getStatus() != StatusEnum.COMPLETED
                && StringUtils.equals(ua.getDeparture(), announce.getDeparture())
                && StringUtils.equals(ua.getArrival(), announce.getArrival())
                && ua.getStartDate().equals(announce.getStartDate());
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
            rcategories.stream().filter(StringUtils::isNotEmpty)
                    .forEach(x -> findAndFillCategories(x,categories));
        } else {
            CategoryVO category = categoryDAO.findByCode(FieldConstants.DEFAULT_CATEGORY);
            fillCategories(category, categories);
        }
        reservation.setCategories(categories);
    }

    private void findAndFillCategories(String code, Set<CategoryVO> categories) {
        CategoryVO category = categoryDAO.findByCode(code);
        fillCategories(category, categories);
    }
    private void fillCategories(CategoryVO category, Set<CategoryVO> categories) {
        categories.add(category);
    }

    private void reservationInfo(Object r) throws Exception{

        if (r instanceof ReservationUserVO) {
            ReservationUserVO res = (ReservationUserVO) r;
            AnnounceInfo ai = new AnnounceInfo(res.getAnnounce());
            res.setAnnounceInfo(ai);

            BigDecimal sumQtyRes = announceDAO.checkQtyReservations(res.getAnnounce().getId(), false);
            announceDAO.warning(res, res.getAnnounce().getEndDate(), res.getAnnounce().getWeight(), sumQtyRes);
        }
        if (r instanceof ReservationReceivedUserVO) {
            ReservationReceivedUserVO res = (ReservationReceivedUserVO) r;
            AnnounceInfo ai = new AnnounceInfo(res.getAnnounce());
            res.setAnnounceInfo(ai);
            UserInfo ui = new UserInfo(res.getUserReservation());
            res.setUserInfo(ui);
            BigDecimal sumQtyRes = announceDAO.checkQtyReservations(res.getAnnounce().getId(), false);
            announceDAO.warning(res, res.getAnnounce().getEndDate(), res.getAnnounce().getWeight(), sumQtyRes);
        }

        if (r instanceof ReservationVO) {
            ReservationVO res = (ReservationVO) r;
            AnnounceInfo ai = new AnnounceInfo(res.getAnnounce());
            res.setAnnounceInfo(ai);
            UserInfo ui = new UserInfo(res.getUser());
            res.setUserInfo(ui);
            BigDecimal sumQtyRes = announceDAO.checkQtyReservations(res.getAnnounce().getId(), false);
            announceDAO.warning(res, res.getAnnounce().getEndDate(), res.getAnnounce().getWeight(), sumQtyRes);
        }
    }
}