package cm.packagemanager.pmanager.airline.ent.service;

import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.common.exception.DashboardException;
import cm.packagemanager.pmanager.ws.requests.airplane.UpdateAirlineDTO;

import java.util.List;

public interface AirlineService {

    AirlineVO add(String code, String description) throws DashboardException,Exception;

    AirlineVO update(UpdateAirlineDTO id) throws DashboardException,Exception;

    AirlineVO findByCode(String code) throws DashboardException,Exception;

    List<AirlineVO> all() throws Exception;

    boolean delete(Long id);
}
