package cm.packagemanager.pmanager.announce.ent.dao;

/**
 * Dans la methode addMessage() La recherche entre user et announce a été faite dans cet ordre pour eviter le
 * fait que l'attrbut utisateur ne soit pas dechiffré dans l'entité announce
 * et aussi eviter HibernateException: Found two representations of same collection
 */

import cm.framework.ds.hibernate.dao.Generic;
import cm.packagemanager.pmanager.airline.ent.dao.AirlineDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceIdVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.CategoryVO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.enums.TransportEnum;
import cm.packagemanager.pmanager.common.enums.ValidateEnum;
import cm.packagemanager.pmanager.common.exception.AnnounceException;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.*;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.notification.enums.NotificationType;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.Session;
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




    @Override
    @Transactional(readOnly = true)
    public int count(AnnounceSearchDTO announceSearch, Long userId, AnnounceType type,PageBy pageBy) throws AnnounceException,Exception {

        logger.info(" Announce - count");
        if (announceSearch != null) {
            Session session = this.sessionFactory.getCurrentSession();
            session.enableFilter(FilterConstants.CANCELLED);
            String where = composeQuery(announceSearch, "a");
            Query query = session.createQuery("select  distinct  a from AnnounceVO  as a join a.categories as c " + where);
            composeQueryParameters(announceSearch, query);
            List result=query.list();
            return CollectionsUtils.isNotEmpty(result) ? result.size() : 0;
        }
        if(userId!=null) {
            return countByNameQuery(AnnounceVO.FINDBYUSER,AnnounceVO.class,userId,"userId",null);
        }
        if(type!=null) {
            return countByNameQuery(AnnounceVO.FINDBYTYPE,AnnounceVO.class,type,"type",null);
        }
        return count(AnnounceVO.class, null);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> announces(PageBy pageBy) throws AnnounceException,Exception {

        List<AnnounceVO> announces = allAndOrderBy(AnnounceVO.class, "startDate", true, pageBy);
        return announces;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> announces(int page, int size) throws AnnounceException,Exception {

        PageBy pageBy = new PageBy(page, size);
        List<AnnounceVO> announces = allAndOrderBy(AnnounceVO.class, "startDate", true, pageBy);
        return announces;

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
        List<AnnounceVO> announces = findByUserNameQuery(AnnounceVO.SQL_FIND_BY_USER, AnnounceVO.class, userId, pageBy);
        return announces;

    }

    /**
     * Retourne les annonces ayant un certain type (BUYER,SELLER)dont on a passé en parametre l'id
     *
     * @param type type de l'annonce
     *  @param pageBy optionnel si on veut un resultat paginé
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> announcesByType(AnnounceType type, PageBy pageBy) throws AnnounceException,Exception {

        List<AnnounceVO> announces = findBy(AnnounceVO.FINDBYTYPE, AnnounceVO.class, type, "type", pageBy);
        return announces;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    public AnnounceVO announce(Long id) throws Exception {

        AnnounceVO announce = (AnnounceVO) findById(AnnounceVO.class, id);
        if (announce == null) return null;
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


            String message= MessageFormat.format(notificationMessagePattern,announce.getUser().getUsername(),
                    " a creé l'annonce " + announce.getDeparture() +"/"+announce.getArrival(),
                    "pour la date " + DateUtils.getDateStandard(announce.getStartDate())
                            + "et retour le "+ DateUtils.getDateStandard(announce.getEndDate()));
            generateEvent(announce,message);

            return announce;
        }
        return null;
    }

    /**
     * Cette methode permet d'ajourner une annonce
     * @param udto  ddonnees de l'annonce à ajourner
     * @return AnnonceVO
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,Exception.class})
    public AnnounceVO update(UpdateAnnounceDTO udto) throws AnnounceException,Exception {

        UserVO user = userDAO.findById(udto.getUserId());

        if (user == null) {
            throw new RecordNotFoundException("Aucun utilisateur trouvé");
        }

        AnnounceVO announce = announce(udto.getId());
        if (announce == null) {
            throw new RecordNotFoundException("Aucune annonce  trouvée");
        }
        if (CollectionsUtils.isEmpty(udto.getCategories())) {
            udto.setCategories(new ArrayList<>());
            udto.getCategories().add(constants.DEFAULT_CATEGORIE);
        }
        double rating = calcolateAverage(user);
        announce.getUserInfo().setRating(rating);
        setAnnounce(announce, user, udto,false);
        update(announce);

        String message= MessageFormat.format(notificationMessagePattern,user.getUsername(),
                " a modifié l'annonce "+announce.getDeparture() +"/"+announce.getArrival(),
                " de " + DateUtils.getDateStandard(announce.getStartDate())
                        + " au "+ DateUtils.getDateStandard(announce.getEndDate()));
        generateEvent(announce,message);
        return announce;

    }

    /**
     * Recherche annonces
     *
     * @param announceSearchDTO
     * @param pageBy
     * @return
     * @throws Exception
     */

    @Override
    @Transactional(readOnly = true)
    public List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws AnnounceException,Exception {
        Session session = sessionFactory.getCurrentSession();
        session.enableFilter(FilterConstants.CANCELLED);

        String where = composeQuery(announceSearchDTO, "a");
        Query query = session.createQuery(" select distinct a from AnnounceVO  as a join a.categories as c " + where);
        composeQueryParameters(announceSearchDTO, query);
        pageBy(query, pageBy);
        List<AnnounceVO> result= query.list();


        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,Exception.class})
    public void announcesStatus() throws AnnounceException,Exception {
        List<AnnounceVO> announces = Optional.ofNullable(allAndOrderBy(AnnounceVO.class, "startDate", true, null)).orElseGet(Collections::emptyList);
        if (CollectionsUtils.isNotEmpty(announces)) {
            announces.stream().filter(ann -> !ann.isCancelled() && DateUtils.isBefore(ann.getEndDate(), DateUtils.currentDate()))
                    .forEach(a -> {
                        a.setStatus(StatusEnum.COMPLETED);
                        update(a);
                        try {
                            List<ReservationVO> reservations = findReservations(a.getId());
                            updateReservationsStatus(reservations, StatusEnum.COMPLETED);
                        } catch (Exception e) {
                            logger.error("Erreur durant l''execution du Batch d''ajournement de status de l'annonce");
                            e.printStackTrace();
                        }
                    });
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,BusinessResourceException.class,AnnounceException.class})
    public boolean delete(Long id) throws BusinessResourceException {
        logger.info("Announce: delete");

        //delete(AnnounceVO.class,id,true);

        try {
            AnnounceVO announce = announce(id);

            String message= MessageFormat.format(notificationMessagePattern,announce.getUser().getUsername(),
                    " a supprimé l'annonce "+announce.getDeparture() +"/"+announce.getArrival(),
                    " du " + DateUtils.getDateStandard(announce.getStartDate())
                            + " au "+ DateUtils.getDateStandard(announce.getEndDate()));
            generateEvent(announce,message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateDelete(id);
    }


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

        BigDecimal price = adto.getPrice();
        BigDecimal preniumPrice = adto.getPreniumPrice();
        BigDecimal goldenPrice = adto.getGoldPrice();
        BigDecimal weight = adto.getWeight();

        if(!DateUtils.validLongValue(adto.getStartDate()) || !DateUtils.validLongValue(adto.getEndDate())){

            throw new AnnounceException("Une des dates n'est pas valide");

        }

        Date startDate=DateUtils.milliSecondToDate(adto.getStartDate());
        Date endDate = DateUtils.milliSecondToDate(adto.getEndDate());


        if (weight == null ) {
            throw new AnnounceException("Quantité n'est pas valide, elle doit etre superieure a zero");
        }

        if (startDate == null || endDate == null) {
            throw new AnnounceException("Une des dates n'est pas valide");
        }

        if (DateUtils.isBefore(startDate, DateUtils.currentDate())) {
            throw new AnnounceException("La date de depart n'est pas valide, elle doit etre minimum la date courante");
        }


        if (DateUtils.isAfter(startDate, endDate)) {
            throw new AnnounceException("La date de depart ne peut pas etre superieure à celle de retour");
        }

        BigDecimal oldRemainWeight = announce.getRemainWeight()==null ? BigDecimal.ZERO:announce.getRemainWeight();
        BigDecimal oldWeight = announce.getWeight();
        BigDecimal diffWeight = oldWeight!=null ? weight.subtract(oldWeight): weight;

        BigDecimal sumQtyRes=BigDecimal.ZERO;

        if (BooleanUtils.isFalse(isCreate)){

            List<ReservationVO> reservations= findReservations(announce.getId());

            // Il y a deja des reservations faites sur l'annonce
            if(announce.getCountReservation()!=null && CollectionsUtils.isNotEmpty(reservations)){
                //Je somme les quantités de toutes les reservations acceptées
                sumQtyRes=reservations.stream().filter(r->!r.getValidate().equals(ValidateEnum.REFUSED))
                        .map(ReservationVO::getWeight).reduce(BigDecimal.ZERO,BigDecimal::add);

                if (sumQtyRes.compareTo(BigDecimal.ZERO)>0 && BigDecimalUtils.lessThan(weight, sumQtyRes)) {
                    throw new UnsupportedOperationException("Impossible de reduire la quantité , car il existe des resevations pour une quantité "+sumQtyRes+" Kg");
                }
            }
            boolean closeDate= DateUtils.isBefore(endDate, DateUtils.currentDate()) ||
                    DateUtils.isSame(endDate, DateUtils.currentDate());

            //Si la modification se fait à la date courante ou bien si  tous les kg ont été reservés -> Completed
            if((weight.compareTo(sumQtyRes)==0 || closeDate) || (weight.compareTo(sumQtyRes)==0 && closeDate)){
                announce.setStatus(StatusEnum.COMPLETED);
                updateReservationsStatus(reservations, StatusEnum.COMPLETED);
            }

        }

        announce.setPrice(price);
        announce.setPreniumPrice(preniumPrice);
        announce.setGoldPrice(goldenPrice);
        announce.setWeight(weight);
        announce.setRemainWeight(weight.subtract(sumQtyRes));
        //announce.setRemainWeight(oldRemainWeight.add(diffWeight));
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

    public List<ReservationVO> findReservations(Long id) throws AnnounceException,Exception {

        try {
            return findBy(ReservationVO.FINDBYANNOUNCE, ReservationVO.class, id, "announceId", null);
        } catch (AnnounceException e) {
            logger.error("Erreur durant la recuperation des reservations liées à l'annonce id={}", id);
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReservations(Long id) throws Exception {

        try {

            List<ReservationVO> reservations = findReservations(id);
            for (ReservationVO reservation : reservations) {
                reservation.setCancelled(true);
                update(reservation);
            }
        } catch (Exception e) {
            logger.error("Erreur durant la recuperation des reservations liées à l'annonce id={}", id);
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
    public boolean updateDelete(Long id) throws BusinessResourceException {
        boolean result = false;

        try {

            Session session = sessionFactory.getCurrentSession();
            AnnounceVO announce = announce(id);
            if (announce != null) {
                announce.updateDeleteChildrens();
                announce.setCancelled(true);
                merge(announce);
                announce = session.get(AnnounceVO.class, id);
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
    public String composeQuery(Object obj, String alias) throws Exception {

        AnnounceSearchDTO announceSearch = (AnnounceSearchDTO) obj;

        StringBuilder hql = new StringBuilder();

        boolean joinCategory=StringUtils.isNotEmpty(announceSearch.getCategory());

        hql.append(where);


        try {
            boolean andOrOr = announceSearch.isAnd();
            boolean addCondition = false;

            if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
                hql.append(alias + ".transport=:transport ");
            }

            addCondition = addCondition(hql.toString());

            if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(alias + ".announceType=:announceType ");
            }

            addCondition = addCondition(hql.toString());

            if (ObjectUtils.isCallable(announceSearch, "price")) {
                BigDecimal price = announceSearch.getPrice();
                //AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
                if (price.compareTo(BigDecimal.ZERO) >= 0) {
                    buildAndOr(hql, addCondition, andOrOr);
                    hql.append(" ( " + alias + ".goldPrice<=:goldPrice or " + alias + ".price<=:price or " + alias + ".preniumPrice<=:preniumPrice) ");
                }
            }
            addCondition = addCondition(hql.toString());

            if (ObjectUtils.isCallable(announceSearch, "startDate") && announceSearch.getStartDate() > 0) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(alias + ".startDate=:startDate ");
            }

            addCondition = addCondition(hql.toString());

            if (ObjectUtils.isCallable(announceSearch, "endDate") && announceSearch.getEndDate() > 0) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(alias + ".endDate=:endDate ");
            }

            addCondition = addCondition(hql.toString());

            if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(alias + ".departure like:departure ");
            }
            addCondition = addCondition(hql.toString());

            if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(alias + ".arrival like:arrival ");
            }


            addCondition = addCondition(hql.toString());
            if (joinCategory) {
                buildAndOr(hql, addCondition, andOrOr);
                hql.append(" c.code =:category");
            }
			/*addCondition = addCondition(hql.toString());
			if (ObjectUtils.isCallable(announceSearch,"userId")){
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(alias+".user.id=:userId ");
			}
			addCondition = addCondition(hql.toString());
			if (StringUtils.isNotEmpty(announceSearch.getUser())) {
				buildAndOr(hql, addCondition, andOrOr);
				hql.append(" ( "+alias+".user.username like:user or "+alias+".user.email like:email) ");
			}*/
            addCondition = addCondition(hql.toString());
            if (!addCondition) {
                hql = new StringBuilder();
            }
            hql.append(" group by " + alias + ".id ");
            hql.append(" order by " + alias + ".startDate desc");
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception("Erreur dans la fonction " + AnnounceDAO.class.getName() + " composeQuery");

        }
        return hql.toString();
    }

    @Override
    public void composeQueryParameters(Object obj, Query query) throws Exception {

        AnnounceSearchDTO announceSearch = (AnnounceSearchDTO) obj;

        try {

            if (StringUtils.isNotEmpty(announceSearch.getTransport())) {
                TransportEnum transport = getTransport(announceSearch.getTransport());
                query.setParameter("transport", transport);
            }

            if (StringUtils.isNotEmpty(announceSearch.getAnnounceType())) {
                AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
                query.setParameter("announceType", announceType);
            }

            if (ObjectUtils.isCallable(announceSearch, "price")) {

                BigDecimal price = announceSearch.getPrice();
                //AnnounceType announceType = getAnnounceType(announceSearch.getAnnounceType());
                if (price.compareTo(BigDecimal.ZERO) >= 0) {
                    query.setParameter("goldPrice", price);
                    query.setParameter("price", price);
                    query.setParameter("preniumPrice", price);
                }
            }
            if (ObjectUtils.isCallable(announceSearch, "startDate") && announceSearch.getStartDate() > 0) {
                Date startDate = DateUtils.milliSecondToDate(announceSearch.getStartDate());
                query.setParameter("startDate", startDate);

            }
            if (ObjectUtils.isCallable(announceSearch, "endDate") && announceSearch.getEndDate() > 0) {
                Date endDate = DateUtils.milliSecondToDate(announceSearch.getEndDate());
                query.setParameter("endDate", endDate);
            }

            if (StringUtils.isNotEmpty(announceSearch.getDeparture())) {
                query.setParameter("departure", "%" + announceSearch.getDeparture().trim().toUpperCase() + "%");
            }

            if (StringUtils.isNotEmpty(announceSearch.getArrival())) {
                query.setParameter("arrival", "%" + announceSearch.getArrival().trim().toUpperCase() + "%");
            }

            if (StringUtils.isNotEmpty(announceSearch.getCategory())) {
                query.setParameter("category", announceSearch.getCategory());
            }
			/*if (ObjectUtils.isCallable(announceSearch,"userId")){
				query.setParameter("userId", announceSearch.getUserId());
			}
			if (StringUtils.isNotEmpty(announceSearch.getUser())) {
				query.setParameter("user", "%"+announceSearch.getUser()+"%");
				query.setParameter("email", "%"+announceSearch.getUser()+"%");
			}*/

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new Exception("Erreur dans la fonction " + AnnounceDAO.class.getName() + " composeQueryParameters");
        }
    }


    private boolean addCondition(String val){
        return  StringUtils.isNotEmpty(val) && !StringUtils.equals(val,  where);
    }

    private void handleCategories(AnnounceVO announce, List<String> categories) throws AnnounceException {

        announce.getCategories().clear();
        if (CollectionsUtils.isNotEmpty(categories)) {
            categories.stream().filter(cat -> StringUtils.isNotEmpty(cat)).forEach(x -> {
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
    private void updateReservationsStatus(List<ReservationVO> reservations,StatusEnum status){

        if(CollectionsUtils.isNotEmpty(reservations)){
            reservations.stream().forEach(r -> {
                r.setStatus(status);
                update(r);
            });
        }

    }

    @Override
    public void generateEvent(Object obj , String message) throws Exception {

        AnnounceVO announce= (AnnounceVO) obj;
        UserVO user= announce.getUser();

        Set subscribers=new HashSet();

        if(CollectionsUtils.isNotEmpty(user.getSubscribers())){
            subscribers.addAll(user.getSubscribers().stream().filter(u->u.isEnableNotification()).collect(Collectors.toSet()));
        }
        List<ReservationVO> reservations=findReservations(announce.getId());
        if(CollectionsUtils.isNotEmpty(reservations)){
            reservations.stream().filter(r->r.getUser()!=user && r.getUser().isEnableNotification()).forEach(r->{
                subscribers.add(r.getUser());

            });
        }

        if(CollectionsUtils.isNotEmpty(announce.getMessages())) {
            announce.getMessages().stream().filter(m->m.getUser()!=user && m.getUser().isEnableNotification()).forEach(m->{
                subscribers.add(m.getUser());
            });
        }
        if (CollectionsUtils.isNotEmpty(subscribers)){
            fillProps(props,announce.getId(),message, user.getId(),subscribers);
            generateEvent( NotificationType.ANNOUNCE);
        }

    }
}