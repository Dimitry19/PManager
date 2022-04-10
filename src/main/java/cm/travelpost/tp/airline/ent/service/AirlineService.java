package cm.travelpost.tp.airline.ent.service;

import cm.travelpost.tp.airline.ent.vo.AirlineIdVO;
import cm.travelpost.tp.airline.ent.vo.AirlineVO;
import cm.travelpost.tp.common.exception.DashboardException;
import cm.travelpost.tp.ws.requests.CommonDTO;

import java.util.List;

public interface AirlineService {

    AirlineVO add(String code, String description) throws DashboardException,Exception;

    AirlineVO update(CommonDTO dto) throws DashboardException,Exception;

    AirlineVO findByCode(String code) throws DashboardException,Exception;

    List<AirlineVO> all() throws Exception;

    boolean delete(AirlineIdVO id);
}
