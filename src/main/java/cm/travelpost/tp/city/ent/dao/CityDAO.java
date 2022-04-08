package cm.travelpost.tp.city.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.city.ent.vo.CityVO;
import cm.travelpost.tp.common.ent.vo.PageBy;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.ws.requests.CommonDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface CityDAO extends CommonDAO {

    List<CityVO> autocomplete(String search, boolean caseInsensitive) throws Exception;

    List<CityVO> cities(int page, int size) throws Exception;

    List<CityVO> cities(PageBy pageBy) throws Exception;

    int count(Object o,PageBy pageBy) throws  Exception;

    CityVO city(String id) throws Exception;

    CityVO create(CommonDTO cityDTO) throws  Exception;

    boolean delete(String id) throws Exception;

    CityVO update(CommonDTO updateCityDTO) throws   Exception;

    CityVO update(String id, String name) throws Exception;


    void delete(@NotNull CityVO city) throws BusinessResourceException;
}
