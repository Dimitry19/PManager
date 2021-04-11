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

	List<AnnounceVO> announces(int page, int  size) throws Exception;

	List<AnnounceVO> announces(PageBy pageBy) throws Exception;

	int count(AnnounceSearchDTO announceSearch, PageBy pageBy) throws BusinessResourceException, Exception;

	List<AnnounceVO> findByUser(UserVO user) throws Exception;

	List<AnnounceVO> findByUser(Long userId, PageBy pageBy) throws Exception;

	List<AnnounceVO> findByType(AnnounceType type, PageBy pageBy) throws Exception;

	AnnounceVO findById(Long id) throws BusinessResourceException,RecordNotFoundException, Exception;

	AnnounceVO create(AnnounceDTO announce) throws BusinessResourceException, Exception;

	AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException,RecordNotFoundException;

	boolean delete(Long id) throws BusinessResourceException;

	AnnounceVO update(UpdateAnnounceDTO announceDTO) throws Exception;


	AnnounceVO update(Integer id) throws BusinessResourceException;

	List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception;


	void announcesStatus() throws Exception;





}
