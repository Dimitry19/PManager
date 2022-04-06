package cm.packagemanager.pmanager.announce.ent.service;

import cm.framework.ds.hibernate.dao.Generic;
import cm.packagemanager.pmanager.announce.ent.dao.ReservationDAO;
import cm.packagemanager.pmanager.announce.ent.vo.ReservationVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.ReservationType;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.ws.requests.announces.ReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateReservationDTO;
import cm.packagemanager.pmanager.ws.requests.announces.ValidateReservationDTO;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReservationServiceImpl extends Generic implements ReservationService {

    @Autowired
    ReservationDAO dao;

    @Override
    public ReservationVO addReservation(ReservationDTO reservationDTO) throws Exception {
        return dao.addReservation(reservationDTO);
    }

    @Override
    public ReservationVO updateReservation(UpdateReservationDTO reservationDTO) throws Exception {
        return dao.updateReservation(reservationDTO);
    }

    @Override
    public ReservationVO getReservation(long id) throws Exception {
        return dao.getReservation(id);
    }

    @Override
    public ReservationVO validate(ValidateReservationDTO reservationDTO) throws Exception {
        return dao.validate(reservationDTO);
    }

    @Override
    public boolean deleteReservation(Long id) throws Exception {
        return dao.deleteReservation(id);
    }

    @Override
    public int count(Long id, PageBy pageBy, boolean isUser) throws Exception {
        return (isUser) ? dao.countByNameQuery(ReservationVO.FINDBYUSER, ReservationVO.class, id, USER_PARAM, pageBy) :
                dao.countByNameQuery(ReservationVO.FINDBYANNOUNCE, ReservationVO.class, id, ANNOUNCE_PARAM, pageBy);
    }

    @Override
    public List reservationsByUser(Long userId, ReservationType type, PageBy pageBy) throws Exception {

        return dao.reservationByUser(userId, type, pageBy);
    }

    @Override
    public List<ReservationVO> receivedReservations(Long userId, PageBy pageBy) throws Exception {

        return dao.otherReservations(userId, pageBy);
    }

    @Override
    public List<ReservationVO> reservationsByAnnounce(Long announceId, PageBy pageBy) throws Exception {
        return dao.reservationByAnnounce(announceId, pageBy);
    }

    @Override
    public boolean updateDelete(Long id) throws BusinessResourceException, UserException {
        return false;
    }

    @Override
    public String composeQuery(Object o, String alias) throws Exception {
        return null;
    }

    @Override
    public void composeQueryParameters(Object o, Query query) throws Exception {

    }
}
