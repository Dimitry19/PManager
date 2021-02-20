package cm.packagemanager.pmanager.message.ent.service;

import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.message.ent.dao.MessageDAO;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.messages.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.messages.UpdateMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("messageService")
@Transactional
public class MessageServiceImpl implements MessageService{
	private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Autowired
	MessageDAO messageDAO;


	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RecordNotFoundException.class, BusinessResourceException.class})
	public MessageVO addMessage(MessageDTO mdto) throws BusinessResourceException, RecordNotFoundException {
		return messageDAO.addMessage(mdto);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delete(Long id) throws Exception {
		return messageDAO.delete(id);
	}

	@Override
	public List<MessageVO> messagesByUser(UserVO user,PageBy pageBy) throws BusinessResourceException, UserException {
		return messageDAO.messagesByUser(user, pageBy);
	}

	@Override
	public List<MessageVO> messagesByUser(Long id,PageBy pageBy) throws BusinessResourceException, UserException {
		return messageDAO.messagesByUser(id, pageBy);
	}

	@Override
	public List<MessageVO> messages(PageBy pageBy) throws BusinessResourceException {
		return messageDAO.messages(pageBy);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(PageBy pageBy) throws Exception {
		return messageDAO.count(pageBy);
	}

	@Override
	@Transactional(readOnly = true)
	public MessageVO findById(MessageIdVO id) throws BusinessResourceException {
		return messageDAO.findById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BusinessResourceException.class, Exception.class})
	public MessageVO update(UpdateMessageDTO updateMessageDTO) throws BusinessResourceException {
		return null;
	}
}
