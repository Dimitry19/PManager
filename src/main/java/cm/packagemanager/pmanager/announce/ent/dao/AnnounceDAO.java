package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnouncesVO;
import cm.packagemanager.pmanager.common.exception.*;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;

import java.util.List;

public interface AnnounceDAO {

	List<AnnounceVO> announces(int page, int size) throws BusinessResourceException;

	int count(int page, int size) throws BusinessResourceException;

	MessageVO addMessage(MessageDTO messageDTO) throws BusinessResourceException,RecordNotFoundException;

	AnnounceVO findByUser(UserVO user) throws BusinessResourceException,UserNotFoundException,RecordNotFoundException;

	AnnounceVO findById(Long id) throws BusinessResourceException,RecordNotFoundException;

	AnnounceVO create(AnnounceDTO announce) throws BusinessResourceException, Exception;

	AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException,RecordNotFoundException;

	boolean delete(Long id) throws BusinessResourceException;

	AnnounceVO addComment(MessageVO message) throws BusinessResourceException;

	AnnounceVO update(UpdateAnnounceDTO announceDTO) throws BusinessResourceException, UserException,RecordNotFoundException;

	AnnounceVO update(AnnounceVO announce) throws BusinessResourceException;

	AnnounceVO update(Integer id) throws BusinessResourceException;


}
