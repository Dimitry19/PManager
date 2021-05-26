package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.exception.*;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;


import java.util.List;

public interface AnnounceDAO extends CommonDAO {

	public List<AnnounceVO> announces(int page, int  size) throws Exception;

	public List<AnnounceVO> announces(PageBy pageBy) throws Exception;

	public int count(AnnounceSearchDTO announceSearch, PageBy pageBy) throws BusinessResourceException, Exception;

	public List<AnnounceVO> announcesByUser(UserVO user) throws Exception;

	public List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws Exception;

	public List<AnnounceVO> announcesByType(AnnounceType type, PageBy pageBy) throws Exception;

	public AnnounceVO announce(Long id) throws BusinessResourceException,RecordNotFoundException, Exception;

	public AnnounceVO create(AnnounceDTO announce) throws BusinessResourceException, Exception;

	public AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException,RecordNotFoundException;

	public boolean delete(Long id) throws BusinessResourceException;

	public AnnounceVO update(UpdateAnnounceDTO announceDTO) throws Exception;

	public AnnounceVO update(Integer id) throws BusinessResourceException;

	public List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception;

	public void announcesStatus() throws Exception;





}
