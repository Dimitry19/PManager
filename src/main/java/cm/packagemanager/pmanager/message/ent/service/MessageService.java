package cm.packagemanager.pmanager.message.ent.service;

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

public interface MessageService {
	public List<MessageVO> messagesByUser(UserVO user, PageBy pageBy) throws BusinessResourceException, UserException;
	public List<MessageVO> messagesByUser(Long id, PageBy pageBy) throws BusinessResourceException, UserException;
	public int count(PageBy pageBy) throws Exception;
	public List<MessageVO> messages(PageBy pageBy) throws BusinessResourceException;
	public MessageVO findById(MessageIdVO id) throws BusinessResourceException;
	public MessageVO update(UpdateMessageDTO updateMessageDTO) throws BusinessResourceException, UserException;
	MessageVO addMessage(MessageDTO messageDTO) throws BusinessResourceException, RecordNotFoundException;
	public boolean delete(Long id) throws Exception;


}
