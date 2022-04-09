package cm.travelpost.tp.message.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.framework.ds.hibernate.enums.FindBy;
import cm.travelpost.tp.common.ent.vo.PageBy;
import cm.travelpost.tp.common.exception.BusinessResourceException;

import cm.travelpost.tp.message.ent.vo.MessageIdVO;
import cm.travelpost.tp.message.ent.vo.MessageVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.messages.MessageDTO;
import cm.travelpost.tp.ws.requests.messages.UpdateMessageDTO;

import java.util.List;

public interface MessageDAO extends CommonDAO {


    int count(PageBy pageBy) throws Exception;

    MessageVO update(UpdateMessageDTO messageDTO) throws Exception;

    List<MessageVO> messagesByUser(UserVO user, PageBy pageBy) throws Exception;

    List<MessageVO> messagesBy(Long id, FindBy fbType, PageBy pageBy) throws Exception;

    List<MessageVO> messages(PageBy pageBy) throws Exception;

    MessageVO findById(MessageIdVO id) throws BusinessResourceException;

    boolean delete(Long id) throws BusinessResourceException;

    MessageVO addMessage(MessageDTO messageDTO) throws Exception;
}
