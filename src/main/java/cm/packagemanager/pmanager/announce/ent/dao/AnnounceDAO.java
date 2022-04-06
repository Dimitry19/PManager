package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.exception.AnnounceException;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;

import java.util.List;

public interface AnnounceDAO extends CommonDAO {

    List<AnnounceVO> announces(int page, int size) throws AnnounceException,Exception;

    List<AnnounceVO> announces(PageBy pageBy) throws AnnounceException,Exception;

    int count(AnnounceSearchDTO announceSearch, Long userId, AnnounceType type,PageBy pageBy) throws AnnounceException, Exception;

    List<AnnounceVO> announcesByUser(UserVO user) throws AnnounceException,Exception;

    List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws AnnounceException,Exception;

    List<AnnounceVO> announcesByType(AnnounceType type, PageBy pageBy) throws AnnounceException,Exception;

    AnnounceVO announce(Long id) throws Exception;

    AnnounceVO create(AnnounceDTO announce) throws AnnounceException,Exception;

    AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException, RecordNotFoundException;

    boolean delete(Long id) throws Exception;

    AnnounceVO update(UpdateAnnounceDTO announceDTO) throws AnnounceException, Exception;

    AnnounceVO update(Integer id) throws BusinessResourceException;

    List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception;

    void announcesStatus() throws AnnounceException, Exception;


}
