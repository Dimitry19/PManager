package cm.packagemanager.pmanager.announce.ent.dao;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnouncesVO;
import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.*;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AnnounceDAO extends CommonDAO {

	List<AnnounceVO> announces(int page, int  size) throws BusinessResourceException;

	List<AnnounceVO> announces(PageBy pageBy) throws BusinessResourceException;

	int count(PageBy pageBy) throws BusinessResourceException;

	MessageVO addMessage(MessageDTO messageDTO) throws BusinessResourceException,RecordNotFoundException;

	AnnounceVO findByUser(UserVO user) throws BusinessResourceException,UserNotFoundException,RecordNotFoundException;

	List<AnnounceVO> findByUser(Long userId, PageBy pageBy) throws BusinessResourceException, UserNotFoundException, RecordNotFoundException, UserException;

	AnnounceVO findById(Long id) throws BusinessResourceException,RecordNotFoundException;

	AnnounceVO create(AnnounceDTO announce) throws BusinessResourceException, Exception;

	AnnounceVO delete(AnnounceVO announce) throws BusinessResourceException,RecordNotFoundException;

	boolean delete(Long id) throws BusinessResourceException;

	AnnounceVO addComment(MessageVO message) throws BusinessResourceException;

	AnnounceVO update(UpdateAnnounceDTO announceDTO) throws BusinessResourceException, UserException,RecordNotFoundException;

	AnnounceVO update(AnnounceVO announce) throws BusinessResourceException;

	AnnounceVO update(Integer id) throws BusinessResourceException;

	List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception;


}
