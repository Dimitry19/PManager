package cm.packagemanager.pmanager.message.ent.dao;

import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.messages.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.messages.UpdateMessageDTO;

import java.util.List;

public interface MessageDAO extends CommonDAO {


	public int  count(PageBy pageBy)  throws Exception;
	public  MessageVO update(UpdateMessageDTO messageDTO) throws BusinessResourceException, UserException;
	public List<MessageVO> messagesByUser(UserVO user,PageBy pageBy) throws BusinessResourceException, UserException;
	public List<MessageVO> messagesByUser(Long id,PageBy pageBy) throws BusinessResourceException, UserException;
	public List<MessageVO> messages(PageBy pageBy ) throws BusinessResourceException;
	public MessageVO findById(MessageIdVO id) throws BusinessResourceException;
	public boolean delete(Long id) throws BusinessResourceException;
	MessageVO addMessage(MessageDTO messageDTO) throws BusinessResourceException, RecordNotFoundException;
}
