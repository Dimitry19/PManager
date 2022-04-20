package cm.travelpost.tp.announce.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.announce.ent.vo.ReservationVO;
import cm.travelpost.tp.common.ent.vo.PageBy;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.RecordNotFoundException;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.announces.AnnounceDTO;
import cm.travelpost.tp.ws.requests.announces.AnnounceSearchDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateAnnounceDTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AnnounceDAO extends CommonDAO {

    List<AnnounceVO> announces(int page, int size) throws AnnounceException,Exception;

    List<AnnounceVO> announces(PageBy pageBy) throws AnnounceException,Exception;

    int count(Object o,PageBy pageBy) throws AnnounceException, Exception;

    int count(Object o, StatusEnum status,PageBy pageBy) throws AnnounceException, Exception;

    List<AnnounceVO> announcesByUser(UserVO user) throws AnnounceException,Exception;

    List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws AnnounceException,Exception;

    @Transactional(readOnly = true)
    List<?> announcesByUser(Long userId, StatusEnum status, PageBy pageBy) throws AnnounceException,Exception;

    List<AnnounceVO> announcesBy(Object o, PageBy pageBy) throws AnnounceException,Exception;

    AnnounceVO announce(Long id) throws Exception;

    AnnounceVO create(AnnounceDTO announce) throws AnnounceException,Exception;

    AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException, RecordNotFoundException;

    boolean delete(Long id) throws Exception;

    AnnounceVO update(UpdateAnnounceDTO announceDTO) throws AnnounceException, Exception;

    AnnounceVO update(Integer id) throws BusinessResourceException;

    List<AnnounceVO> search(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception;

    void announcesStatus() throws AnnounceException, Exception;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {AnnounceException.class,Exception.class})
    void announcesToNotificate(int numberDays) throws AnnounceException,Exception;


    @Transactional
    List<ReservationVO> findReservations(Long id) throws AnnounceException,Exception;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, BusinessResourceException.class})
    BigDecimal checkQtyReservations(Long id, boolean onlyRefused) throws AnnounceException,Exception;

    void warning(Object o, Date endDate, BigDecimal weight, BigDecimal sumQtyRes);
}
