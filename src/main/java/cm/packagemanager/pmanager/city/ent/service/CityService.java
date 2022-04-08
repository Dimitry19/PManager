package cm.packagemanager.pmanager.city.ent.service;

import cm.packagemanager.pmanager.city.ent.vo.CityVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.ws.requests.CommonDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService extends InitializingBean {

    CityVO create(CommonDTO cityDTO) throws Exception;

    List<CityVO> autoComplete(String search, boolean caseInsensitive) throws Exception;

    CityVO update(CommonDTO announceId) throws Exception;

    CityVO update(String id, String name) throws Exception;

    CityVO city(String id) throws Exception;

    boolean delete(String id) throws Exception;

    List<CityVO> cities(PageBy pageBy) throws Exception;

    Page cities(Pageable pageable) throws Exception;

    int count(Object o,PageBy pageBy) throws Exception;

    void afterPropertiesSet() throws Exception;

    void destroy() throws Exception;
}
