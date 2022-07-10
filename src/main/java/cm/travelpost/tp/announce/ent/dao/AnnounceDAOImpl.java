package cm.travelpost.tp.announce.ent.dao;

/**
 * Dans la methode addMessage() La recherche entre user et announce a été faite dans cet ordre pour eviter le
 * fait que l'attrbut utisateur ne soit pas dechiffré dans l'entité announce
 * et aussi eviter HibernateException: Found two representations of same collection
 */

import cm.framework.ds.activity.enums.ActivityOperation;
import cm.framework.ds.common.ent.vo.KeyValue;
import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.common.utils.CodeGenerator;
import cm.framework.ds.hibernate.dao.Generic;
import cm.framework.ds.hibernate.utils.IQueryBuilder;
import cm.framework.ds.hibernate.utils.QueryBuilder;
import cm.travelpost.tp.airline.ent.dao.AirlineDAO;
import cm.travelpost.tp.announce.ent.vo.*;
import cm.travelpost.tp.announce.enums.AnnounceType;
import cm.travelpost.tp.announce.enums.TransportEnum;
import cm.travelpost.tp.announce.enums.ValidateEnum;
import cm.travelpost.tp.common.Constants;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.exception.*;
import cm.travelpost.tp.common.utils.*;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.image.ent.dao.ImageDAO;
import cm.travelpost.tp.image.ent.vo.ImageVO;
import cm.travelpost.tp.notification.ent.dao.NotificationDAO;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;
import cm.travelpost.tp.notification.enums.NotificationType;
import cm.travelpost.tp.user.ent.service.UserService;
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
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cm.travelpost.tp.notification.enums.NotificationType.*;


@Repository
public class AnnounceDAOImpl extends Generic implements AnnounceDAO {

    private static Logger logger = LoggerFactory.getLogger(AnnounceDAOImpl.class);

    @Autowired
    protected AirlineDAO airlineDAO;

    @Autowired
    protected CategoryDAO categoryDAO;

    @Autowired
    NotificationDAO notificationDAO;

    @Autowired
    protected Constants constants;

    @Autowired
    protected QueryUtils queryUtils;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ImageDAO imageDAO;

    private String retrieveReservationError="Erreur durant la recuperation des reservations liées à l'annonce id={}";


    @Override
    @Transactional(readOnly = true)
    public int count(Object o, PageBy pageBy) throws AnnounceException,Exception {
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
            return countByNameQuery(AnnounceVO.FINDBYTYPE,AnnounceVO.class,type,ANNOUNCE_TYPE_PARAM,pageBy);
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
            if(status == StatusEnum.COMPLETED){
                return countByNameQuery(AnnounceCompletedVO.FINDBYUSER,AnnounceCompletedVO.class,userId,USER_PARAM,pageBy, emptyFilters());
            }
            return countByNameQuery(AnnounceVO.FINDBYUSER,AnnounceVO.class,userId,USER_PARAM,pageBy, emptyFilters());
        }
        return 0;
    }

    @Override
    @Transactional
    public List<AnnounceVO> announcesFavoris(UserVO user, PageBy pageBy) throws AnnounceException, Exception {


        List<AnnounceVO> results= new ArrayList();


        setMap(new KeyValue(USER_PARAM, user.getId()));
        setFilters(FilterConstants.CANCELLED);
        List<Object[]> lst =findBySqlNativeQuery(AnnounceVO.ANNOUNCES_FAVORIS_BY_USER_NQ, getMap(),AnnounceVO.ANNOUNCE_FAVORITE_MAPPING , pageBy, getFilters());
        manageAnnouncesFavorites(lst,results);
        return results;
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

        UserVO user = userService.findById(userId);
        if (user == null) {
            throw new UserException(messageConfig.getUserExceptionNotFound() + userId);
        }
        return findByUserNameQuery(AnnounceVO.FIND_BY_USER_SQL, AnnounceVO.class, userId, pageBy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<?> announcesByUser(Long userId, StatusEnum status, PageBy pageBy) throws AnnounceException,Exception {

        UserVO user = userService.findById(userId);
        if (user == null) {
            throw new UserException(messageConfig.getUserExceptionNotFound() + userId);
        }

        if(status == StatusEnum.COMPLETED){
            return findBySqlQuery(AnnounceCompletedVO.SQL_FIND_BY_USER, AnnounceCompletedVO.class, userId,USER_PARAM, pageBy,emptyFilters());
        }

        return findBySqlQuery(AnnounceVO.FIND_BY_USER_SQL, AnnounceVO.class, userId,USER_PARAM, pageBy,emptyFilters());
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
            announces=findBy(AnnounceVO.FINDBYTYPE, AnnounceVO.class, type, ANNOUNCE_TYPE_PARAM, pageBy);
        }
        if(o instanceof TransportEnum){
            TransportEnum transport=(TransportEnum) o;
            announces=findBy(AnnounceVO.FINDBYTRANSPORT, AnnounceVO.class, transport, TRANSPORT_PARAM, pageBy);
        }
        return announces;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class, BusinessResourceException.class})
    public AnnounceVO announce(Long id) throws Exception {

        AnnounceVO announce = (AnnounceVO) findById(AnnounceVO.class, id);
        if (announce == null) {
            throw new AnnounceException("Annonce non trouvée");
        }
        double rating = calcolateAverage(announce.getUser());
        announce.getUserInfo().setRating(rating);

        checkReservations( announce ,false);
        return announce;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class, BusinessResourceException.class})
    public AnnounceVO announce(String code) throws Exception {

        AnnounceVO announce = (AnnounceVO) findByNaturalId(AnnounceVO.class, code);
        if (announce == null) {
            throw new AnnounceException("Annonce non trouvée");
        }
        double rating = calcolateAverage(announce.getUser());
        announce.getUserInfo().setRating(rating);

        checkReservations( announce ,false);
        return announce;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class, BusinessResourceException.class})
    public AnnounceCompletedVO announceCompleted(Long id) throws Exception {

        AnnounceCompletedVO announce = (AnnounceCompletedVO) findById(AnnounceCompletedVO.class, id);
        if (announce == null) {
            throw new AnnounceException("Annonce non trouvée");
        }
        double rating = calcolateAverage(announce.getUser());
        announce.getUserInfo().setRating(rating);
        return announce;
    }

    /**
     * Cette methode permet de creer une annonce
     * @param adto  ddonnees de l'annonce à creer
     * @return AnnonceVO
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class})
    public AnnounceMasterVO create(AnnounceDTO adto) throws AnnounceException, UserException, SubscriptionException, Exception {

            UserVO user = userService.findById(adto.getUserId());

            if (user == null) {
                throw new UserException(messageConfig.getUserExceptionNotFound() + adto.getUserId());
            }

            AnnounceVO announce = new AnnounceVO();
            announce.setAnnounceId(new AnnounceIdVO(Constants.DEFAULT_TOKEN));
            setAnnounce(announce, user, adto,true);

            announce.setStatus(StatusEnum.VALID);

            announce.setMessages(null);
            announce.setCancelled(false);

            save(announce);
            addAnnounceToUser(announce);

            String message=buildNotificationMessage(ANNOUNCE,announce.getUser().getUsername(),announce.getDeparture(),
                    announce.getArrival(),DateUtils.getDateStandard(announce.getStartDate()),
                    DateUtils.getDateStandard(announce.getEndDate()),null);


            generateEvent(announce,message);
            notificationForBuyers(announce);
            writer.logActivity(partOneMessage("Creation de l'annonce "+announce.getDeparture(),
                    announce.getArrival()), ActivityOperation.CREATE,adto.getUserId(), null);

            return announce;
    }

    /**
     * Cette methode permet d'ajourner une annonce
     * @param dto  ddonnees de l'annonce à ajourner
     * @return AnnonceVO
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,BusinessResourceException.class})
    public AnnounceVO update(UpdateAnnounceDTO dto) throws AnnounceException,Exception {

        UserVO user = userService.findById(dto.getUserId());

        if (user == null) {
            throw new RecordNotFoundException(messageConfig.getUserExceptionNotFound());
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
        notificationForBuyers(announce);
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
            announces.stream().filter(this::filterAnnounceToComplete)
                    .forEach(a -> {
                        a.setStatus(StatusEnum.COMPLETED);
                      //  update(a);
                        try {
                            updateReservationsStatus(a.getId(), StatusEnum.COMPLETED);
                            generateEvent(a,"L'annonce de " +partOneMessage(a.getDeparture(),a.getArrival()) + partTwoMessage(" et ayant", DateUtils.dateToString(a.getStartDate()),DateUtils.dateToString(a.getEndDate()))+" n'est plus disponible", true);
                        } catch (Exception e) {
                            logger.error("Erreur durant l''execution du Batch d''ajournement de status de l'annonce {}",e.getMessage());
                            throw  new AnnounceException(e.getMessage());
                        }
                    });
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class})
    public void announcesToNotificate(int numberDays) throws AnnounceException,Exception {

        List<AnnounceVO> announces = Optional.ofNullable(allAndOrderBy(AnnounceVO.class, START_DATE_PARAM, true, null)).orElseGet(Collections::emptyList);
        List<NotificationVO> notifications= notificationDAO.findByStatus(StatusEnum.VALID);
        AtomicInteger count= new AtomicInteger();
        if (CollectionsUtils.isNotEmpty(announces)) {
            announces.stream()
                    .filter(ann->filterAnnounceToNotificate(ann,numberDays,notifications))
                    .forEach(a -> {
                        try {
                            count.getAndIncrement();
                            generateEvent(a,"Dans " + numberDays + " jours l'annonce de " +partOneMessage(a.getDeparture(),a.getArrival()) + partTwoMessage(" et ayant ", DateUtils.dateToString(a.getStartDate()),DateUtils.dateToString(a.getEndDate()))+" ne sera plus disponible");
                        } catch (Exception e) {
                            logger.error("Erreur durant l''execution du Batch de notification des annonceurs {}", e.getMessage());
                            throw  new AnnounceException(e.getMessage());
                        }
                    });
        }
        logger.info("{} notifications envoyées pour {} announces recupérées ", count.get(), CollectionsUtils.size(announces) );

    }

    @Transactional
    public boolean filterAnnounceToNotificate(AnnounceVO ann,int numberDays, List<NotificationVO> notifications) {

        Set<Long> ids=notifications.stream().map(NotificationVO::getAnnounceId).collect(Collectors.toSet());
        return !ids.contains(ann.getId()) && !ann.isCancelled() && DateUtils.isDifferenceDay(ann.getEndDate(), DateUtils.currentDate(), numberDays,false);
    }
    @Transactional
    public boolean filterAnnounceToComplete(AnnounceVO ann) {

         return !ann.isCancelled() && DateUtils.isDifferenceDay(ann.getEndDate(),DateUtils.currentDate(),7,true);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class,AnnounceException.class})
    public boolean delete(Long id) throws Exception {
        logger.info("Announce: delete");

        try {
            AnnounceVO announce = announce(id);

            BigDecimal sumQtyRes=checkQtyReservations(announce.getId(), false);

            if (sumQtyRes.compareTo(BigDecimal.ZERO)>0) {
                throw new AnnounceException(MessageFormat.format(messageConfig.getAnnounceWarningDelete(),sumQtyRes));
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
    public UserVO addAnnounceToUser(AnnounceMasterVO announce) throws BusinessResourceException {

        UserVO user = announce.getUser();

        if (user != null) {
            user.addAnnounce(announce);
            update(user);
            return (UserVO) get(UserVO.class, user.getId());
        } else {
            return null;
        }
    }


    private void setAnnounce(AnnounceMasterVO announce, UserVO user, AnnounceDTO adto,boolean isCreate) throws AnnounceException,Exception {

        BigDecimal price = priceByAnnounceType(adto.getAnnounceType(),adto.getPrice());
        BigDecimal preniumPrice = priceByAnnounceType(adto.getAnnounceType(),adto.getPreniumPrice());
        BigDecimal goldenPrice = priceByAnnounceType(adto.getAnnounceType(),adto.getGoldPrice());
        BigDecimal weight = adto.getWeight();

        if(BooleanUtils.isFalse(DateUtils.validLongValue(adto.getStartDate())) || BooleanUtils.isFalse(DateUtils.validLongValue(adto.getEndDate()))){
            throw new AnnounceException("Une des dates n'est pas valide");
        }

        Date startDate=adto.getConvertedStartDate();
        Date endDate = adto.getConvertedEndDate();


        if (DateUtils.isAfter(startDate, endDate)) {
            throw new AnnounceException("La date de depart ne peut pas etre superieure à celle de retour");
        }

        if (BooleanUtils.isTrue(isCreate) && DateUtils.isBefore(startDate, DateUtils.currentDate())) {
            throw new AnnounceException("La date de depart n'est pas valide, elle doit etre minimum la date d'aujourd\'hui");
        }
        if (BooleanUtils.isFalse(isCreate) && DateUtils.isBefore(endDate, DateUtils.currentDate())) {
            throw new AnnounceException("Impossible de modifier  cette annonce car la date d\'arrivée est deja passée");
        }

        BigDecimal sumQtyRes=BigDecimal.ZERO;

        if (BooleanUtils.isFalse(isCreate)){

            sumQtyRes=checkQtyReservations(announce.getId(),false);
            // Il y a deja des reservations acceptées  sur l'annonce

            if(sumQtyRes.compareTo(BigDecimal.ZERO)>0 && BigDecimalUtils.lessThan(weight, sumQtyRes)){
                throw new AnnounceException(MessageFormat.format(messageConfig.getAnnounceWarningQtyReduction(),sumQtyRes));
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

        if(announce.getAnnounceType().equals(AnnounceType.BUYER)) {
            announce.setEstimateValue(adto.getEstimateValue());
        }
        if(!ObjectUtils.isCallable(announce, "code")){
            announce.setCode(CodeGenerator.generateCode(announce.getAnnounceId().getToken(),announce.getAnnounceType().name()));
        }
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
        return findByUserNameQuery(AnnounceVO.FIND_BY_USER_SQL, AnnounceVO.class, user.getId(), null);
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
    public List<ReservationVO> findReservations(Long id) throws AnnounceException,Exception {

        try {
            return findBy(ReservationVO.FIND_BY_ANNOUNCE, ReservationVO.class, id, ANNOUNCE_PARAM, null);
        } catch (AnnounceException e) {
            logger.error(retrieveReservationError, id);
            throw e;
        }
    }

    @Override
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


    protected void checkReservations(AnnounceVO announce, boolean onlyRefused) throws Exception {
        BigDecimal sumQtyRes= checkQtyReservations(announce.getId(),onlyRefused);
        warning( announce , announce.getEndDate(), announce.getWeight(),  sumQtyRes);
    }


    private List commonSearchAnnounce(AnnounceSearchDTO search,PageBy pageBy) throws AnnounceException {

        QueryBuilder hql = new QueryBuilder(AnnounceVO.ANNOUNCE_SEARCH_SINGLE,"from AnnounceVO as "+ANNOUNCE_ALIAS);
        composeQuery(hql,search);
        Query query = search(hql.createQuery(),emptyFilters());
        composeQueryParameters(hql,search, query);

        pageBy(query,pageBy);

        return query.list();
    }


    private void composeQuery(QueryBuilder hql,AnnounceSearchDTO search){

        if(StringUtils.isNotEmpty(search.getCategory())){
            hql.addJoin(IQueryBuilder.JoinEnum.INNER, false,ANNOUNCE_ALIAS,"categories",CATEGORY_TABLE_ALIAS);
            hql.and().appendProperty(hql.getWhere(),CATEGORY_TABLE_ALIAS, "code", CATEGORY_PARAM);
        }

        if (StringUtils.isNotEmpty(search.getTransport())) {
            hql.and().appendProperty(hql.getWhere(),ANNOUNCE_ALIAS,TRANSPORT_PARAM,TRANSPORT_PARAM);
        }

        if (StringUtils.isNotEmpty(search.getAnnounceType())) {

            hql.and().appendProperty(hql.getWhere(),ANNOUNCE_ALIAS,ANNOUNCE_TYPE_PARAM,ANNOUNCE_TYPE_PARAM);
        }

        if (ObjectUtils.isCallable(search, PRICE_PARAM)) {
            hql.and().openBracket().lessThanOrEqual(ANNOUNCE_ALIAS,PRICE_PARAM ,search.getPrice()).closeBracket();
        }

        if (ObjectUtils.isCallable(search, START_DATE_PARAM) && search.getStartDate() > 0) {

            hql.and().appendProperty(hql.getWhere(),ANNOUNCE_ALIAS, START_DATE_PARAM,START_DATE_PARAM);
        }

        if (ObjectUtils.isCallable(search, END_DATE_PARAM) && search.getEndDate() > 0) {

            hql.and().appendProperty(hql.getWhere(),ANNOUNCE_ALIAS, END_DATE_PARAM,END_DATE_PARAM);
        }

        if (StringUtils.isNotEmpty(search.getDeparture())) {
            hql.and().like(ANNOUNCE_ALIAS,DEPARTURE_PARAM,search.getDeparture(),Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        }

        if (StringUtils.isNotEmpty(search.getArrival())) {
            hql.and().like(ANNOUNCE_ALIAS,ARRIVAL_PARAM,search.getArrival(),Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        }

        if (StringUtils.isNotEmpty(search.getReference())) {
            hql.and().like(ANNOUNCE_ALIAS, CODE_PARAM,search.getReference(),Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        }
        hql.addGroupBy(ANNOUNCE_ALIAS,ID_PARAM);
        hql.addOrderBy(ANNOUNCE_ALIAS,START_DATE_PARAM , Boolean.FALSE);

        //return  hql.createQuery();
    }

    @Override
    public String composeQuery(Object obj, String alias) throws QueryException {
        return null;
    }
    @Override
    public void composeQueryParameters(Object o, Query query) throws QueryParameterException {}

    public void composeQueryParameters(QueryBuilder hql,Object o, Query query) throws QueryParameterException {

        AnnounceSearchDTO search = (AnnounceSearchDTO)o;

        try {

            if (StringUtils.isNotEmpty(search.getTransport())) {
                TransportEnum transport = getTransport(search.getTransport());
                query.setParameter(TRANSPORT_PARAM, transport);
            }

            if (StringUtils.isNotEmpty(search.getAnnounceType())) {
                AnnounceType announceType = getAnnounceType(search.getAnnounceType());
                query.setParameter(ANNOUNCE_TYPE_PARAM, announceType);
            }

            if (ObjectUtils.isCallable(search, PRICE_PARAM)) {

                if (search.getPrice()!=null) {
                    query.setParameter(PRICE_PARAM, search.getPrice());
                }
            }
            if (ObjectUtils.isCallable(search, START_DATE_PARAM) && search.getStartDate() > 0) {
                Date startDate = DateUtils.milliSecondToDate(search.getStartDate());
                query.setParameter(START_DATE_PARAM, startDate);

            }
            if (ObjectUtils.isCallable(search, END_DATE_PARAM) && search.getEndDate() > 0) {
                Date endDate = DateUtils.milliSecondToDate(search.getEndDate());
                query.setParameter(END_DATE_PARAM, endDate);
            }

            if (StringUtils.isNotEmpty(search.getDeparture())) {
                query.setParameter(DEPARTURE_PARAM,  hql.getParameters().get(DEPARTURE_PARAM));

            }

            if (StringUtils.isNotEmpty(search.getArrival())) {
                query.setParameter(ARRIVAL_PARAM, hql.getParameters().get(ARRIVAL_PARAM));
            }

            if (StringUtils.isNotEmpty(search.getReference())) {
                query.setParameter(CODE_PARAM,hql.getParameters().get(CODE_PARAM));
            }
            if (StringUtils.isNotEmpty(search.getCategory())) {
                query.setParameter(CATEGORY_PARAM, search.getCategory());
            }
        } catch (QueryParameterException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new QueryParameterException("Erreur dans la fonction " + AnnounceDAO.class.getName() + " composeQueryParameters");
        }
    }



    private void handleCategories(AnnounceMasterVO announce, List<String> categories) throws AnnounceException {

        announce.getCategories().clear();
        if (CollectionsUtils.isNotEmpty(categories)) {
            categories.stream().filter(StringUtils::isNotEmpty)
                    .filter(c->StringUtils.notEquals(c,Constants.CATEGORY_INDIFFERENT))
                    .forEach(x -> {
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
               // update(r);
            });
        }
    }

    BigDecimal priceByAnnounceType(AnnounceType type, BigDecimal price){

        if (type == AnnounceType.SELLER){
            return price;
        }
        return BigDecimal.ZERO;

    }

    /**
     *  Recupere les utilisateurs à notifier ayant des annonces de type BUYER qui correspondent
     *  à une annonce de type SELLER
     * @return
     */
    private Set usersNotification(AnnounceVO announce) throws Exception {

        Set subscribers=new HashSet();

        if(AnnounceType.SELLER == announce.getAnnounceType()){
            List<AnnounceVO>  announces= announcesBy(AnnounceType.BUYER,null);
            if(CollectionsUtils.isNotEmpty(announces)){
                List<AnnounceVO> check = Optional.ofNullable(
                            announces.stream()
                                .filter(this::filterUserNotification)
                                .collect(Collectors.toList()))
                        .orElseGet(Collections::emptyList);

                if (CollectionsUtils.isNotEmpty(check)) {
                    subscribers.addAll(check.stream().map(AnnounceVO::getUser).collect(Collectors.toSet()));
                }
            }
        }
        return subscribers;
    }


    private boolean filterUserNotification(AnnounceVO announce){

        return announce.getStatus() != StatusEnum.COMPLETED
                && (StringUtils.equals(announce.getDeparture(), announce.getDeparture())
                && StringUtils.equals(announce.getArrival(), announce.getArrival()))
                ||(StringUtils.equals(announce.getArrival(), announce.getArrival()));
    }

    public void generateEvent(AnnounceVO announce , String message, Object o)throws Exception {

        UserVO user= announce.getUser();

        Set subscribers = new HashSet();

        if(o instanceof Set){
            subscribers.addAll((Set)o);
        }else{
            subscribers=fillSubscribers(user,announce);

        }

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


    public  void notificationForBuyers(AnnounceVO announce) throws Exception{

        Set users=usersNotification(announce);

        if (CollectionsUtils.isNotEmpty(users)){
            String message=buildNotificationMessage(ANNOUNCE_BUYER,announce.getUser().getUsername(),announce.getDeparture(),
                    announce.getArrival(),DateUtils.getDateStandard(announce.getStartDate()),
                    DateUtils.getDateStandard(announce.getEndDate()),null);

            generateEvent(announce,message,users);
        }
    }
    @Override
    public void warning(Object o, Date endDate, BigDecimal weight, BigDecimal sumQtyRes){

        boolean closeDate= DateUtils.isBefore(endDate, DateUtils.currentDate()) || DateUtils.isSame(endDate, DateUtils.currentDate());
        boolean allReserved=(weight != null && sumQtyRes != null) && weight.compareTo(sumQtyRes)==0;

        //Si la modification se fait à la date courante ou bien si  tous les kg ont été reservés -> Completed
        if(BooleanUtils.isTrue(allReserved) || BooleanUtils.isTrue(closeDate)){

            StringBuilder sb = new StringBuilder(messageConfig.getAnnounceWarning());
            sb.append(BooleanUtils.isTrue(allReserved) ? messageConfig.getAnnounceWarningAvailability():messageConfig.getAnnounceWarningExpiration());


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


    public void manageAnnouncesFavorites(List<Object[]>lst, List<AnnounceVO> results){

        lst.stream().forEach(o->{
            int i=0;
            AnnounceVO announce = new AnnounceVO();
            announce.setId((Long) o[i++]);
            announce.setCode((String) o[i++]);
            announce.setAnnounceId(new AnnounceIdVO((String) o[i++]));
            announce.setCancelled((boolean) o[i++]);
            announce.setDateCreated((Timestamp) o[i++]);
            announce.setLastUpdated((Timestamp) o[i++]);
            announce.setAnnounceType(AnnounceType.getAnnounceType((String)o[i++]));
            announce.setArrival((String) o[i++]);
            announce.setDeparture((String) o[i++]);
            announce.setDescription((String) o[i++]);
            announce.setEndDate((Date) o[i++]);
            announce.setGoldPrice((BigDecimal) o[i++]);
            announce.setPreniumPrice((BigDecimal) o[i++]);
            announce.setPrice((BigDecimal) o[i++]);
            announce.setStartDate((Date) o[i++]);
            announce.setStatus(StatusEnum.getStatusEnum((String)o[i++]));
            announce.setEstimateValue((BigDecimal) o[i++]);
            announce.setCountReservation((Integer) o[i++]);
            announce.setRemainWeight((BigDecimal) o[i++]);
            announce.setTransport(TransportEnum.getTransportEnum((String)o[i++]));
            announce.setWeight((BigDecimal) o[i++]);

            try {
                Long  imageId= (Long)o[i++];
                Long  userId= (Long)o[i++];
                announce.setImage(imageId==null?null:(ImageVO) imageDAO.findById(ImageVO.class,imageId));
                announce.setUser(userId== null?null:userService.findById(userId));

            } catch (Exception e) {
                e.printStackTrace();
            }
            results.add(announce);

        });
    }
}