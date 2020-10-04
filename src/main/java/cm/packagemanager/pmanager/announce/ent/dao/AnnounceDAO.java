package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.AnnounceDTO;

import java.util.List;

public interface AnnounceDAO {

	List<AnnounceVO> announces(int page, int size) throws BusinessResourceException;

	List<AnnounceVO> announces() throws BusinessResourceException;

	AnnounceVO findByUser(UserVO user) throws BusinessResourceException;

	AnnounceVO create(AnnounceDTO announce) throws BusinessResourceException;

	AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException;

	AnnounceVO delete(Integer id) throws BusinessResourceException;

	AnnounceVO addComment(MessageVO message) throws BusinessResourceException;

	AnnounceVO update(AnnounceVO announce) throws BusinessResourceException;

	AnnounceVO update(Integer id) throws BusinessResourceException;


}
