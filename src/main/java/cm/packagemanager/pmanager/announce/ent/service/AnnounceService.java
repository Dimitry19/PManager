package cm.packagemanager.pmanager.announce.ent.service;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnounceService extends InitializingBean {

    AnnounceVO create(AnnounceDTO announceDTO) throws Exception;

    List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception;

    AnnounceVO update(UpdateAnnounceDTO announce) throws Exception;

    AnnounceVO update(Integer id) throws Exception;

    List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws Exception;

    List<AnnounceVO> announcesByType(AnnounceType type, PageBy pageBy) throws Exception;

    AnnounceVO announce(Long id) throws Exception;

    boolean delete(Long id) throws Exception;

    List<AnnounceVO> announces(PageBy pageBy) throws Exception;

    Page announces(Pageable pageable) throws Exception;


    int count(AnnounceSearchDTO announceSearch, PageBy pageBy) throws Exception;

    void afterPropertiesSet() throws Exception;

    void destroy() throws Exception;
}
