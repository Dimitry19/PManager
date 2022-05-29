package cm.travelpost.tp.announce.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.dao.Generic;
import cm.framework.ds.hibernate.enums.FindBy;
import cm.travelpost.tp.announce.ent.dao.ReservationDAO;
import cm.travelpost.tp.announce.ent.vo.ReservationVO;
import cm.travelpost.tp.announce.enums.ReservationType;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.ws.requests.announces.ReservationDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateReservationDTO;
import cm.travelpost.tp.ws.requests.announces.ValidateReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cm.framework.ds.hibernate.enums.FindBy.ANNOUNCE;
import static cm.framework.ds.hibernate.enums.FindBy.USER;

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
    public int count(Long id, PageBy pageBy, FindBy findBy, ReservationType type) throws Exception {

        if (findBy == ANNOUNCE){

                return dao.countByNameQuery(ReservationVO.FIND_BY_ANNOUNCE, ReservationVO.class, id, ANNOUNCE_PARAM, pageBy);
        }else if( findBy == USER){
                if(type == null || type == ReservationType.CREATED ){
                    return dao.countByNameQuery(ReservationVO.FIND_BY_USER, ReservationVO.class, id, USER_PARAM, pageBy) ;
                }
                return CollectionsUtils.size(reservationsByUser(id,type,pageBy));
        }
        return 0;
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
    public boolean updateDelete(Object id) throws BusinessResourceException, UserException {
        return false;
    }

}
