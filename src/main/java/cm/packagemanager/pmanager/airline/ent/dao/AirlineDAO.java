package cm.packagemanager.pmanager.airline.ent.dao;

import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.common.exception.AnnounceException;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.DashboardException;
import cm.packagemanager.pmanager.ws.requests.airplane.UpdateAirlineDTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AirlineDAO {

    AirlineVO add(String code, String description) throws DashboardException,Exception;

    AirlineVO update(UpdateAirlineDTO id) throws DashboardException,Exception;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class,BusinessResourceException.class, AnnounceException.class})
    boolean delete(String code) throws DashboardException;

    AirlineVO findByCode(String code) throws DashboardException,Exception;

    List<AirlineVO> all() throws DashboardException,Exception;

}
