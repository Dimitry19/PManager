package cm.travelpost.tp.airline.ent.dao;

import cm.travelpost.tp.airline.ent.vo.AirlineIdVO;
import cm.travelpost.tp.airline.ent.vo.AirlineVO;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.DashboardException;
import cm.travelpost.tp.ws.requests.CommonDTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AirlineDAO {

    AirlineVO add(String code, String description) throws DashboardException,Exception;

    AirlineVO update(CommonDTO id) throws DashboardException,Exception;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,BusinessResourceException.class, AnnounceException.class})
    boolean delete(String code) throws DashboardException;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,BusinessResourceException.class, AnnounceException.class})
    boolean delete(AirlineIdVO code) throws DashboardException;

    AirlineVO findByCode(String code) throws DashboardException,Exception;

    List<AirlineVO> all() throws DashboardException,Exception;

}
