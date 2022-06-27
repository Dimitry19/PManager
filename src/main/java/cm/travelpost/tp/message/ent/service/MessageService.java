package cm.travelpost.tp.message.ent.service;

import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.hibernate.enums.FindBy;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.message.ent.vo.MessageIdVO;
import cm.travelpost.tp.message.ent.vo.MessageVO;
import cm.travelpost.tp.ws.requests.messages.MessageDTO;
import cm.travelpost.tp.ws.requests.messages.UpdateMessageDTO;

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
