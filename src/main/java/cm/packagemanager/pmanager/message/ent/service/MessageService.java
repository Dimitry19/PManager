package cm.packagemanager.pmanager.message.ent.service;

import cm.framework.ds.hibernate.enums.FindBy;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.ws.requests.messages.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.messages.UpdateMessageDTO;

import java.util.List;

public interface MessageService {

    int count(PageBy pageBy) throws Exception;

    MessageVO addMessage(MessageDTO messageDTO) throws Exception;

    MessageVO update(UpdateMessageDTO updateMessageDTO) throws Exception;

    boolean delete(Long id) throws Exception;

    MessageVO findById(MessageIdVO id) throws BusinessResourceException;

    List<MessageVO> messagesBy(Long id, FindBy fbType, PageBy pageBy) throws Exception;

    List<MessageVO> messages(PageBy pageBy) throws Exception;

}
