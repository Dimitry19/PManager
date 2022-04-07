package cm.packagemanager.pmanager.city.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.packagemanager.pmanager.city.ent.vo.CityVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.ws.requests.city.CityDTO;
import cm.packagemanager.pmanager.ws.requests.city.UpdateCityDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CityDAO extends CommonDAO {

    List<CityVO> autocomplete(String search, boolean caseInsensitive) throws Exception;

    List<CityVO> cities(int page, int size) throws Exception;

    List<CityVO> cities(PageBy pageBy) throws Exception;

    int count(Object o,PageBy pageBy) throws  Exception;

    CityVO city(String id) throws Exception;

    CityVO create(CityDTO announce) throws  Exception;

    boolean delete(String id) throws Exception;

    CityVO update(UpdateCityDTO updateCityDTO) throws   Exception;

    CityVO update(String id, String name) throws Exception;


    void delete(@NotNull CityVO city) throws BusinessResourceException;
}
