package cm.packagemanager.pmanager.message.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.framework.ds.hibernate.enums.FindByType;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;

import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.messages.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.messages.UpdateMessageDTO;

import java.util.List;

public interface MessageDAO extends CommonDAO {


    int count(PageBy pageBy) throws Exception;

    MessageVO update(UpdateMessageDTO messageDTO) throws Exception;

    List<MessageVO> messagesByUser(UserVO user, PageBy pageBy) throws Exception;

    List<MessageVO> messagesBy(Long id, FindByType fbType,PageBy pageBy) throws Exception;

    List<MessageVO> messages(PageBy pageBy) throws Exception;

    MessageVO findById(MessageIdVO id) throws BusinessResourceException;

    boolean delete(Long id) throws BusinessResourceException;

    MessageVO addMessage(MessageDTO messageDTO) throws Exception;
}
