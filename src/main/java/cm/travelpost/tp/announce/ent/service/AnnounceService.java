package cm.travelpost.tp.announce.ent.service;

import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.common.ent.vo.PageBy;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.ws.requests.announces.AnnounceDTO;
import cm.travelpost.tp.ws.requests.announces.AnnounceSearchDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateAnnounceDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnnounceService extends InitializingBean {

    AnnounceVO create(AnnounceDTO announceDTO) throws AnnounceException,Exception;

    List<AnnounceVO> search(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws AnnounceException,Exception;

    AnnounceVO update(UpdateAnnounceDTO announceId) throws AnnounceException,Exception;

    AnnounceVO update(Integer id) throws AnnounceException,Exception;

    List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws AnnounceException,Exception;

    List<AnnounceVO> announcesBy(Object o, PageBy pageBy) throws AnnounceException,Exception;

    AnnounceVO announce(Long id) throws AnnounceException,Exception;

    boolean delete(Long id) throws AnnounceException,Exception;

    List<AnnounceVO> announces(PageBy pageBy) throws AnnounceException,Exception;

    Page announces(Pageable pageable) throws AnnounceException,Exception;

    int count(Object o,PageBy pageBy) throws AnnounceException,Exception;

    void afterPropertiesSet() throws Exception;

    void destroy() throws Exception;
}