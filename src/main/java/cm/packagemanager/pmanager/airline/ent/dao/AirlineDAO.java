package cm.packagemanager.pmanager.airline.ent.dao;

import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;

import java.util.List;

public interface AirlineDAO {

    AirlineVO findByCode(String code) throws BusinessResourceException;

    List<AirlineVO> all() throws BusinessResourceException;

}
