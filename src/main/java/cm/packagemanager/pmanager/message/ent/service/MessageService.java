package cm.packagemanager.pmanager.message.ent.service;

import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.messages.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.messages.UpdateMessageDTO;

import java.util.List;

public interface MessageService {
    List<MessageVO> messagesByUser(UserVO user, PageBy pageBy) throws Exception;

    List<MessageVO> messagesByUser(Long id, PageBy pageBy) throws Exception;

    int count(PageBy pageBy) throws Exception;

    List<MessageVO> messages(PageBy pageBy) throws Exception;

    MessageVO findById(MessageIdVO id) throws BusinessResourceException;

    MessageVO update(UpdateMessageDTO updateMessageDTO) throws Exception;

    MessageVO addMessage(MessageDTO messageDTO) throws Exception;

    boolean delete(Long id) throws Exception;


}
