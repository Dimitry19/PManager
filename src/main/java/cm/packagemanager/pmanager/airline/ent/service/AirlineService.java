package cm.packagemanager.pmanager.airline.ent.service;

import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.ws.requests.airplane.UpdateAirlineDTO;

import java.util.List;

public interface AirlineService {

    AirlineVO add(String code, String description) throws Exception;

    AirlineVO update(UpdateAirlineDTO id) throws Exception;


    AirlineVO findByCode(String code) throws Exception;

    List<AirlineVO> all() throws Exception;

    boolean delete(Long id);
}
