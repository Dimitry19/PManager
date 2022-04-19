package cm.travelpost.tp.announce.ent.dao;

/**
 * Dans la methode addMessage() La recherche entre user et announce a été faite dans cet ordre pour eviter le
 * fait que l'attrbut utisateur ne soit pas dechiffré dans l'entité announce
 * et aussi eviter HibernateException: Found two representations of same collection
 */

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.airline.ent.dao.AirlineDAO;
import cm.travelpost.tp.announce.ent.vo.*;
import cm.travelpost.tp.common.Constants;
import cm.travelpost.tp.common.ent.vo.PageBy;
import cm.travelpost.tp.common.enums.AnnounceType;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.enums.TransportEnum;
import cm.travelpost.tp.common.enums.ValidateEnum;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.RecordNotFoundException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.utils.*;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.notification.enums.NotificationType;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.announces.AnnounceDTO;
import cm.travelpost.tp.ws.requests.announces.AnnounceSearchDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateAnnounceDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.QueryException;
import org.hibernate.QueryParameterException;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cm.travelpost.tp.notification.enums.NotificationType.*;


@Repository
public class AnnounceDAOImpl extends Generic implements AnnounceDAO {

    private static Logger logger = LoggerFactory.getLogger(AnnounceDAOImpl.class);

    @Autowired
    protected AirlineDAO airlineDAO;

    @Autowired
    protected UserDAO userDAO;

    @Autowired
    protected CategoryDAO categoryDAO;

    @Autowired
    protected Constants constants;

    @Autowired
    protected QueryUtils queryUtils;

    private String retrieveReservationError="Erreur durant la recuperation des reservations liées à l'annonce id={}";


    @Override
    @Transactional(readOnly = true)
    public int count(Object o,PageBy pageBy) throws AnnounceException,Exception {
        logger.info(" Announce - count");

        if(o == null) {
            return count(AnnounceVO.class, pageBy);
        }

        if (o instanceof Long) {

            Long userId = (Long) o;
            return countByNameQuery(AnnounceVO.FINDBYUSER,AnnounceVO.class,userId,USER_PARAM,pageBy);
        }

        if (o instanceof AnnounceType){
            AnnounceType type = (AnnounceType) o;
            return countByNameQuery(AnnounceVO.FINDBYTYPE,AnnounceVO.class,type,TYPE_PARAM,pageBy);
        }
        if (o instanceof TransportEnum){
            TransportEnum transport = (TransportEnum) o;
            return countByNameQuery(AnnounceVO.FINDBYTRANSPORT,AnnounceVO.class,transport,TRANSPORT_PARAM,pageBy);
        }

        if(o instanceof AnnounceSearchDTO){
            AnnounceSearchDTO announceSearch =(AnnounceSearchDTO) o;
            return CollectionsUtils.size(commonSearchAnnounce(announceSearch,null));
        }
       return 0;

    }

    @Override
    @Transactional(readOnly = true)
    public int count(Object o,StatusEnum status,PageBy pageBy) throws AnnounceException,Exception {
        logger.info(" Announce - count");

        if(o == null) {
            return count(AnnounceVO.class,  pageBy);
        }

        if (o instanceof Long) {

            Long userId = (Long) o;

            filters = new String[1];
            if(status == StatusEnum.COMPLETED){
                return countByNameQuery(AnnounceCompletedVO.FINDBYUSER,AnnounceMasterVO.class,userId,USER_PARAM,pageBy, null);
            }
            return countByNameQuery(AnnounceVO.FINDBYUSER,AnnounceVO.class,userId,USER_PARAM,pageBy, null);
        }

        return 0;

    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> announces(PageBy pageBy) throws AnnounceException,Exception {

        return allAndOrderBy(AnnounceVO.class, START_DATE_PARAM, true, pageBy);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> announces(int page, int size) throws AnnounceException,Exception {

        PageBy pageBy = new PageBy(page, size);
        return allAndOrderBy(AnnounceVO.class, START_DATE_PARAM, true, pageBy);
    }

    /**
     * Retourne les annonces d'un utilisateur dont on a passé en parametre l'id
     *
     * @param userId id de l'user
     * @param pageBy optionnel si on veut un resultat paginé
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws AnnounceException,Exception {

        UserVO user = userDAO.findById(userId);
        if (user == null) {
            throw new UserException("Aucun utilisateur trouvé avec cet id " + userId);
        }
        return findByUserNameQuery(AnnounceVO.SQL_FIND_BY_USER, AnnounceVO.class, userId, pageBy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<?> announcesByUser(Long userId, StatusEnum status, PageBy pageBy) throws AnnounceException,Exception {

        UserVO user = userDAO.findById(userId);
        if (user == null) {
            throw new UserException("Aucun utilisateur trouvé avec cet id " + userId);
        }

        filters = new String[1];
        if(status == StatusEnum.COMPLETED){
            return findBySqlQuery(AnnounceCompletedVO.SQL_FIND_BY_USER, AnnounceMasterVO.class, userId,USER_PARAM, pageBy,null);
        }

        return findBySqlQuery(AnnounceVO.SQL_FIND_BY_USER, AnnounceVO.class, userId,USER_PARAM, pageBy,null);
    }


    /**
     * Retourne les annonces ayant un certain type (BUYER,SELLER)dont on a passé en parametre l'id
     *
     * @param o objet qui definit le type de recherche à effectuer sur l'annonce
     *  @param pageBy optionnel si on veut un resultat paginé
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> announcesBy(Object o,  PageBy pageBy) throws AnnounceException,Exception {

        List<AnnounceVO> announces = null;

        if(o==null) return new ArrayList<>();

        if(o instanceof AnnounceType){

            AnnounceType  type=(AnnounceType) o;
            announces=findBy(AnnounceVO.FINDBYTYPE, AnnounceVO.class, type, TYPE_PARAM, pageBy);
        }
        if(o instanceof TransportEnum){
            TransportEnum transport=(TransportEnum) o;
            announces=findBy(AnnounceVO.FINDBYTRANSPORT, AnnounceVO.class, transport, TRANSPORT_PARAM, pageBy);
        }
        return announces;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    public AnnounceVO announce(Long id) throws Exception {

        AnnounceVO announce = (AnnounceVO) findById(AnnounceVO.class, id);
        if (announce == null) return null;
        double rating = calcolateAverage(announce.getUser());
        announce.getUserInfo().setRating(rating);

        BigDecimal sumQtyRes= checkQtyReservations(id,false);
        warning( announce , announce.getEndDate(), announce.getWeight(),  sumQtyRes);
        return announce;
    }

    /**
     * Cette methode permet de creer une annonce
     * @param adto  ddonnees de l'annonce à creer
     * @return AnnonceVO
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,Exception.class})
    public AnnounceVO create(AnnounceDTO adto) throws AnnounceException,Exception {

        if (adto != null) {

            UserVO user = userDAO.findById(adto.getUserId());

            if (user == null) {
                throw new UserException("Aucun utilisateur trouvé avec cet id " + adto.getUserId());
            }

            AnnounceVO announce = new AnnounceVO();
            setAnnounce(announce, user, adto,true);

            announce.setStatus(StatusEnum.VALID);
            announce.setAnnounceId(new AnnounceIdVO(Constants.DEFAULT_TOKEN));
            announce.setMessages(null);
            announce.setCancelled(false);
            save(announce);
            addAnnounceToUser(announce);



            String message=buildNotificationMessage(ANNOUNCE,announce.getUser().getUsername(),announce.getDeparture(),
                    announce.getArrival(),DateUtils.getDateStandard(announce.getStartDate()),
                    DateUtils.getDateStandard(announce.getEndDate()),null);


            generateEvent(announce,message);

            return announce;
        }
        return null;
    }

    /**
     * Cette methode permet d'ajourner une annonce
     * @param dto  ddonnees de l'annonce à ajourner
     * @return AnnonceVO
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,Exception.class})
    public AnnounceVO update(UpdateAnnounceDTO dto) throws AnnounceException,Exception {

        UserVO user = userDAO.findById(dto.getUserId());

        if (user == null) {
            throw new RecordNotFoundException("Aucun utilisateur trouvé");
        }

        AnnounceVO announce = announce(dto.getId());
        if (announce == null) {
            throw new RecordNotFoundException("Aucune annonce  trouvée");
        }
        if (CollectionsUtils.isEmpty(dto.getCategories())) {
            dto.setCategories(new ArrayList<>());
            dto.getCategories().add(constants.defaultCategorie);
        }
        double rating = calcolateAverage(user);
        announce.getUserInfo().setRating(rating);
        setAnnounce(announce, user, dto,false);
        update(announce);



        String message=buildNotificationMessage(ANNOUNCE_UPD,announce.getUser().getUsername(),announce.getDeparture(),
                announce.getArrival(),DateUtils.getDateStandard(announce.getStartDate()),
                DateUtils.getDateStandard(announce.getEndDate()),null);

        generateEvent(announce,message);
        return announce;

    }

    /**
     * Recherche annonces
     *
     * @param announceSearch
     * @param pageBy
     * @return
     * @throws Exception
     */

    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> search(AnnounceSearchDTO announceSearch, PageBy pageBy) throws AnnounceException,Exception {

          return commonSearchAnnounce(announceSearch,pageBy);
    }



    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,Exception.class})
    public void announcesStatus() throws AnnounceException,Exception {
        List<AnnounceVO> announces = Optional.ofNullable(allAndOrderBy(AnnounceVO.class, START_DATE_PARAM, true, null)).orElseGet(Collections::emptyList);
        if (CollectionsUtils.isNotEmpty(announces)) {
            announces.stream().filter(ann -> !ann.isCancelled() && DateUtils.isDifferenceDay(ann.getEndDate(), DateUtils.currentDate(),7,true))
                    .forEach(a -> {
                        a.setStatus(StatusEnum.COMPLETED);
                        update(a);
                        try {
                            updateReservationsStatus(a.getId(), StatusEnum.COMPLETED);
                            generateEvent(a,"l'annonce de " +partOneMessage(a.getDeparture(),a.getArrival()) + partTwoMessage(" et ayant", DateUtils.dateToString(a.getStartDate()),DateUtils.dateToString(a.getEndDate()))+" n'est plus disponible", true);

                        } catch (Exception e) {
                            logger.error("Erreur durant l''execution du Batch d''ajournement de status de l'annonce");
                            e.printStackTrace();
                        }
                });
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,Exception.class})
    public void announcesToNotificate(int numberDays) throws AnnounceException,Exception {
        List<AnnounceVO> announces = Optional.ofNullable(allAndOrderBy(AnnounceVO.class, START_DATE_PARAM, true, null)).orElseGet(Collections::emptyList);
        if (CollectionsUtils.isNotEmpty(announces)) {
            announces.stream().filter(ann -> !ann.isCancelled() && DateUtils.isDifferenceDay(ann.getEndDate(), DateUtils.currentDate(), numberDays,false))
                    .forEach(a -> {
                        try {
                            generateEvent(a,"Dans " + numberDays + " jours l'annonce de " +partOneMessage(a.getDeparture(),a.getArrival()) + partTwoMessage(" et ayant", DateUtils.dateToString(a.getStartDate()),DateUtils.dateToString(a.getEndDate()))+" ne sera plus disponible");
                        } catch (Exception e) {
                            logger.error("Erreur durant l''execution du Batch de notifation des annonceurs");
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,BusinessResourceException.class,AnnounceException.class})
    public boolean delete(Long id) throws Exception {
        logger.info("Announce: delete");

        try {
            AnnounceVO announce = announce(id);

            BigDecimal sumQtyRes=checkQtyReservations(announce.getId(), false);

            if (sumQtyRes.compareTo(BigDecimal.ZERO)>0) {
                throw new AnnounceException("Impossible de supprimer cette annonce ," +
                        " car il existe des résevations pour une quantité ["+sumQtyRes+"] Kg");
            }

            String message= buildNotificationMessage(ANNOUNCE_DEL,announce.getUser().getUsername(),announce.getDeparture(),
                    announce.getArrival(),DateUtils.getDateStandard(announce.getStartDate()),
                    DateUtils.getDateStandard(announce.getEndDate()),null);
            generateEvent(announce,message,true);
        } catch (AnnounceException e) {
            logger.info("Erreur durant la suppression de l'annonce");
            throw e;
        }
        return updateDelete(id);
    }


    @Transactional
    public UserVO addAnnounceToUser(AnnounceVO announce) throws BusinessResourceException {

        UserVO user = announce.getUser();

        if (user != null) {
            user.addAnnounce(announce);
             update(user);
            return (UserVO) get(UserVO.class, user.getId());
        } else {
            return null;
        }
    }


    private void setAnnounce(AnnounceVO announce, UserVO user, AnnounceDTO adto,boolean isCreate) throws AnnounceException,Exception {

        BigDecimal price = priceByAnnounceType(adto.getAnnounceType(),adto.getPrice());
        BigDecimal preniumPrice = priceByAnnounceType(adto.getAnnounceType(),adto.getPreniumPrice());
        BigDecimal goldenPrice = priceByAnnounceType(adto.getAnnounceType(),adto.getGoldPrice());
        BigDecimal weight = adto.getWeight();

        if(BooleanUtils.isFalse(DateUtils.validLongValue(adto.getStartDate())) || BooleanUtils.isFalse(DateUtils.validLongValue(adto.getEndDate()))){

            throw new AnnounceException("Une des dates n'est pas valide");

        }

        Date startDate=DateUtils.milliSecondToDate(adto.getStartDate());
        Date endDate = DateUtils.milliSecondToDate(adto.getEndDate());


        if (DateUtils.isAfter(startDate, endDate)) {
            throw new AnnounceException("La date de depart ne peut pas etre superieure à celle de retour");
        }

        if (BooleanUtils.isTrue(isCreate) &&DateUtils.isBefore(startDate, DateUtils.currentDate())) {
            throw new AnnounceException("La date de depart n'est pas valide, elle doit etre minimum la date d'aujourd\'hui");
        }

        BigDecimal sumQtyRes=BigDecimal.ZERO;

        if (BooleanUtils.isFalse(isCreate)){

            sumQtyRes=checkQtyReservations(announce.getId(),false);
            // Il y a deja des reservations acceptées  sur l'annonce

            if(sumQtyRes.compareTo(BigDecimal.ZERO)>0 && BigDecimalUtils.lessThan(weight, sumQtyRes)){
                throw new AnnounceException("Impossible de reduire la quantité , car il existe des resevations pour une quantité ["+sumQtyRes+"] Kg");
            }

            warning( announce , endDate, weight,  sumQtyRes);
        }

        announce.setPrice(price);
        announce.setPreniumPrice(preniumPrice);
        announce.setGoldPrice(goldenPrice);
        announce.setWeight(weight);
        announce.setRemainWeight(weight.subtract(sumQtyRes));
        announce.setUser(user);
        announce.setStartDate(startDate);
        announce.setEndDate(endDate);
        announce.setArrival(adto.getArrival());
        announce.setDeparture(adto.getDeparture());
        announce.setDescription(adto.getDescription());
        announce.setAnnounceType(adto.getAnnounceType());
        handleCategories(announce, adto.getCategories());
        announce.setTransport(adto.getTransport());
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReservations(Long id) throws Exception {

        try {

            List<ReservationVO> reservations = findReservations(id);
            if (CollectionsUtils.isNotEmpty(reservations)){
                reservations.stream().distinct().forEach(r->{
                    r.cancel();
                    update(r);
                });
            }
        } catch (Exception e) {
            logger.error(retrieveReservationError, id);
            throw e;
        }
        return true;
    }

    @Override
    public AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException {
        return null;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<AnnounceVO> announcesByUser(UserVO user) throws Exception {
        return findByUserNameQuery(AnnounceVO.SQL_FIND_BY_USER, AnnounceVO.class, user.getId(), null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public AnnounceVO update(Integer id) throws BusinessResourceException {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    public boolean updateDelete(Object o) throws BusinessResourceException {
        boolean result = false;

        try {

            Long id=(Long)o;
            AnnounceVO announce = announce(id);
            if (announce != null) {
                announce.updateDeleteChildrens();
                announce.cancel();
                merge(announce);
                announce = (AnnounceVO) get(AnnounceVO.class, id);
                result = (announce != null) && (announce.isCancelled()) && deleteReservations(id);
            }
        } catch (Exception e) {
            throw new BusinessResourceException(e.getMessage());
        }
        return result;
    }


    private TransportEnum getTransport(String transport) {

        for (TransportEnum t : TransportEnum.values()) {
            if (StringUtils.equals(transport, t.toString())) {
                return t;
            }
        }
        return null;
    }


    private AnnounceType getAnnounceType(String announceType) {

        for (AnnounceType a : AnnounceType.values()) {
            if (StringUtils.equals(announceType, a.toString())) {
                return a;
            }
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    public List<ReservationVO> findReservations(Long id) throws AnnounceException,Exception {

        try {
            return findBy(ReservationVO.FIND_BY_ANNOUNCE, ReservationVO.class, id, ANNOUNCE_PARAM, null);
        } catch (AnnounceException e) {
            logger.error(retrieveReservationError, id);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    public BigDecimal checkQtyReservations(Long id, boolean onlyRefused) throws AnnounceException,Exception {

        try {

            List<ReservationVO> reservations=findReservations(id);
            if(CollectionsUtils.isEmpty(reservations)){
                return BigDecimal.ZERO;
            }

            if(onlyRefused){
                return  reservations.stream().filter(r->r.getValidate().equals(ValidateEnum.REFUSED))
                        .map(ReservationVO::getWeight).reduce(BigDecimal.ZERO,BigDecimal::add);

            }
           return reservations.stream().filter(r->!r.getValidate().equals(ValidateEnum.REFUSED)
                    &&!r.getValidate().equals(ValidateEnum.INSERTED))
                    .map(ReservationVO::getWeight).reduce(BigDecimal.ZERO,BigDecimal::add);

        } catch (AnnounceException e) {
            logger.error(retrieveReservationError, id);
            throw e;
        }
    }


    private List commonSearchAnnounce(AnnounceSearchDTO announceSearch,PageBy pageBy) throws AnnounceException {
        filters= new String[2];
        filters[0]= FilterConstants.CANCELLED;
        filters[1]= FilterConstants.NOT_COMPLETED;
        String where = composeQuery(announceSearch, ANNOUNCE_TABLE_ALIAS);
        Query query = search(AnnounceVO.ANNOUNCE_SEARCH , where,filters);
        composeQueryParameters(announceSearch, query);
        pageBy(query,pageBy);
        return query.list();
    }

    @Override
    public String composeQuery(Object obj, String alias) throws QueryException {

        AnnounceSearchDTO announceSearch = (AnnounceSearchDTO) obj;

        StringBuilder hql = new StringBuilder();

        boolean joinCategory=StringUtils.isNotEmpty(announceSearch.getCategory());

        hql.append(WHERE);


        try {
            boolean andOrOr = announceSearch.isAnd();
            boolean addCondition = false;

            if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
                hql.append(alias + TRANSPORT_PARAM+"=:"+TRANSPORT_PARAM);
            }

            addCondition = addCondition(hql.toString());

            if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(alias + ANNOUNCE_TYPE_PARAM+"=:"+ANNOUNCE_TYPE_PARAM);
            }

            addCondition = addCondition(hql.toString());

            if (ObjectUtils.isCallable(announceSearch, PRICE_PARAM) && announceSearch.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                    buildAndOr(hql, addCondition, andOrOr);
                    hql.append(" ( " + alias  +GOLD_PRICE_PARAM +"<=:"+GOLD_PRICE_PARAM +OR + alias +PRICE_PARAM+"<=:"+PRICE_PARAM+" " +
                            OR + alias + PRENIUM_PRICE_PARAM +"<=:"+PRENIUM_PRICE_PARAM+") ");
            }
            addCondition = addCondition(hql.toString());

            if (ObjectUtils.isCallable(announceSearch, START_DATE_PARAM) && announceSearch.getStartDate() > 0) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(alias + START_DATE_PARAM +"=:" +START_DATE_PARAM+"");
            }

            addCondition = addCondition(hql.toString());

            if (ObjectUtils.isCallable(announceSearch, END_DATE_PARAM) && announceSearch.getEndDate() > 0) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(alias + END_DATE_PARAM +"=:" +END_DATE_PARAM+"");
            }

            addCondition = addCondition(hql.toString());

            if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append("("+alias + DEPARTURE_PARAM+ LIKE +":"+DEPARTURE_PARAM+OR+ alias +DEPARTURE_PARAM + LIKE+":"+DEPARTURE_PARAM+")");
            }
            addCondition = addCondition(hql.toString());

            if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
                buildAndOr(hql, addCondition, andOrOr);

                hql.append("("+alias + ARRIVAL_PARAM+LIKE +":"+ARRIVAL_PARAM +OR+ alias +ARRIVAL_PARAM+LIKE+":"+ARRIVAL_PARAM+")");
            }


            addCondition = addCondition(hql.toString());
            if (joinCategory) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(" c.code =:"+CATEGORY_PARAM);
            }
            addCondition = addCondition(hql.toString());
            if (!addCondition) {
                hql = new StringBuilder();
            }
            hql.append(GROUP_BY + alias + ID_PARAM);
            hql.append(ORDER_BY + alias +START_DATE_PARAM +DESC);
        } catch (QueryException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new QueryException("Erreur dans la fonction " + AnnounceDAO.class.getName() + " composeQuery");

        }
        return hql.toString();
    }

    @Override
    public void composeQueryParameters(Object obj, Query query) throws QueryParameterException {

        AnnounceSearchDTO announceSearch = (AnnounceSearchDTO) obj;

        try {

            if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
                TransportEnum transport = getTransport(announceSearch.getTransport());
                query.setParameter(TRANSPORT_PARAM, transport);
            }

            if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
                AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
                query.setParameter(ANNOUNCE_TYPE_PARAM, announceType);
            }

            if (ObjectUtils.isCallable(announceSearch, PRICE_PARAM)) {

                BigDecimal price = announceSearch.getPrice();
                if (price.compareTo(BigDecimal.ZERO) > 0) {
                    query.setParameter(GOLD_PRICE_PARAM, price);
                    query.setParameter(PRICE_PARAM, price);
                    query.setParameter(PRENIUM_PRICE_PARAM, price);
                }
            }
            if (ObjectUtils.isCallable(announceSearch, START_DATE_PARAM) && announceSearch.getStartDate() > 0) {
                Date startDate = DateUtils.milliSecondToDate(announceSearch.getStartDate());
                query.setParameter(START_DATE_PARAM, startDate);

            }
            if (ObjectUtils.isCallable(announceSearch, END_DATE_PARAM) && announceSearch.getEndDate() > 0) {
                Date endDate = DateUtils.milliSecondToDate(announceSearch.getEndDate());
                query.setParameter(END_DATE_PARAM, endDate);
            }

            if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
                query.setParameter(DEPARTURE_PARAM, "%" + announceSearch.getDeparture() + "%");
                query.setParameter(DEPARTURE_PARAM, "%" + announceSearch.getDeparture().toUpperCase() + "%");
            }

            if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
                query.setParameter(ARRIVAL_PARAM, "%" + announceSearch.getArrival() + "%");
                query.setParameter(ARRIVAL_PARAM, "%" + announceSearch.getArrival().trim().toUpperCase() + "%");
            }

            if (StringUtils.isNotEmpty(announceSearch.getCategory())) {
                query.setParameter(CATEGORY_PARAM, announceSearch.getCategory());
            }
        } catch (QueryParameterException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new QueryParameterException("Erreur dans la fonction " + AnnounceDAO.class.getName() + " composeQueryParameters");
        }
    }


    private boolean addCondition(String val){
        return  StringUtils.isNotEmpty(val) && !StringUtils.equals(val,  WHERE);
    }

    private void handleCategories(AnnounceVO announce, List<String> categories) throws AnnounceException {

        announce.getCategories().clear();
        if (CollectionsUtils.isNotEmpty(categories)) {
            categories.stream().filter(StringUtils::isNotEmpty).forEach(x -> {
                try {
                    CategoryVO category = categoryDAO.findByCode(x);
                    if (category == null) {
                        throw new AnnounceException("Valoriser la categorie");
                    }
                    announce.addCategory(category);
                } catch (AnnounceException e) {
                    logger.error("Erreur durant la recuperation des categories", e);
                    try {
                        throw e;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }
    }
    private void updateReservationsStatus(Long id,StatusEnum status) throws Exception {

        List<ReservationVO> reservations = findReservations(id);
        if(CollectionsUtils.isNotEmpty(reservations)){
            reservations.stream().forEach(r -> {
                r.setStatus(status);
                update(r);
            });
        }
    }

    BigDecimal priceByAnnounceType(AnnounceType type, BigDecimal price){

        if (type == AnnounceType.SELLER){
            return price;
        }
        return BigDecimal.ZERO;

    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    public void generateEvent(AnnounceVO announce , String message, boolean delete)throws Exception {


        UserVO user= announce.getUser();

        Set subscribers=fillSubscribers(user,announce);

        if (CollectionsUtils.isNotEmpty(subscribers)){
            fillProps(props,1L,message, user.getId(),subscribers);
            generateEvent(USER);
        }
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    public void generateEvent(Object obj , String message) throws Exception {

        AnnounceVO announce= (AnnounceVO) obj;
        UserVO user= announce.getUser();

        Set subscribers=fillSubscribers(user,announce);

        if (CollectionsUtils.isNotEmpty(subscribers)){
            fillProps(props,announce.getId(),message, user.getId(),subscribers);
            generateEvent(NotificationType.ANNOUNCE);
        }

    }

    private Set fillSubscribers(UserVO user, AnnounceVO announce) throws Exception{

        Set subscribers=new HashSet();
        if(CollectionsUtils.isNotEmpty(user.getSubscribers())){
            subscribers.addAll(user.getSubscribers().stream().filter(UserVO ::isEnableNotification).collect(Collectors.toSet()));
        }
        List<ReservationVO> reservations=findReservations(announce.getId());
        if(CollectionsUtils.isNotEmpty(reservations)){
            reservations.stream().filter(r->r.getUser()!=user && r.getUser().isEnableNotification()).forEach(r-> subscribers.add(r.getUser()));
        }

        if(CollectionsUtils.isNotEmpty(announce.getMessages())) {
            announce.getMessages().stream().filter(m->m.getUser()!=user && m.getUser().isEnableNotification()).forEach(m->subscribers.add(m.getUser()));
        }

        return subscribers;

    }

    @Override
    public void warning(Object o, Date endDate, BigDecimal weight, BigDecimal sumQtyRes){


        boolean closeDate= DateUtils.isBefore(endDate, DateUtils.currentDate()) || DateUtils.isSame(endDate, DateUtils.currentDate());
        boolean allReserved=(weight != null && sumQtyRes != null) && weight.compareTo(sumQtyRes)==0;

        //Si la modification se fait à la date courante ou bien si  tous les kg ont été reservés -> Completed
        if(BooleanUtils.isTrue(allReserved) || BooleanUtils.isTrue(closeDate)){

            StringBuilder sb = new StringBuilder("Bientot l'annonce et les reservations associées ne seront plus disponibles car ");
            sb.append(BooleanUtils.isTrue(allReserved) ?"la disponibilité des Kg est terminée tout a déjà été reservé ":"la date d'arrivée de l'annonce est déjà atteinte");


            if(o instanceof  AnnounceVO){
                AnnounceVO announce = (AnnounceVO) o;
                 announce.setRetDescription(sb.toString());
                 announce.setWarning(sb.toString());
            }

            if(o instanceof  ReservationVO){
                ReservationVO reservation = (ReservationVO) o;
                reservation.setWarning(sb.toString());
            }

            if(o instanceof  ReservationUserVO){
                ReservationUserVO reservation = (ReservationUserVO) o;
                reservation.setWarning(sb.toString());
            }

            if(o instanceof  ReservationReceivedUserVO){
                ReservationReceivedUserVO reservation = (ReservationReceivedUserVO) o;
                reservation.setWarning(sb.toString());
            }



        }

    }
}