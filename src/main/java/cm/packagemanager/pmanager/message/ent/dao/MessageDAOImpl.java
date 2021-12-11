package cm.packagemanager.pmanager.message.ent.dao;

import cm.packagemanager.pmanager.announce.ent.dao.AnnounceDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.ent.dao.Generic;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.common.utils.QueryUtils;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.messages.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.messages.UpdateMessageDTO;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDAOImpl extends Generic implements MessageDAO {

    private static Logger logger = LoggerFactory.getLogger(MessageDAOImpl.class);

    @Autowired
    QueryUtils queryUtils;

    @Autowired
    UserDAO userDAO;

    @Autowired
    AnnounceDAO announceDAO;

    @Override
    public int count(PageBy pageBy) throws BusinessResourceException {
        logger.info("Message: count");
        return count(MessageVO.class, pageBy);
    }

    @Override
    public MessageVO update(UpdateMessageDTO mdto) throws Exception {
        logger.info("Message: update");

        UserVO user = userDAO.findByUsername(mdto.getUsername());

        if (user == null) {
            user = userDAO.findByEmail(mdto.getUsername());
        }

        if (user == null) {
            throw new RecordNotFoundException("Aucun utilisateur trouvé");
        }

        MessageVO message = findById(new MessageIdVO(mdto.getId(), Constants.DEFAULT_TOKEN));
        if (message == null) {
            throw new RecordNotFoundException("Aucune message  trouvé");
        }
        message.setContent(mdto.getContent());
        update(message);
        return message;
    }

    @Override
    public MessageVO addMessage(MessageDTO mdto) throws Exception {

        logger.info("Message: add message");

        UserVO user = userDAO.findByOnlyUsername(mdto.getUsername(), false);

        if (user == null) {
            user = userDAO.findByEmail(mdto.getUsername());
        }

        if (user == null) {
            throw new UserNotFoundException("Utisateur non trouvé");
        }
        AnnounceVO announce = announceDAO.announce(mdto.getAnnounceId());

        if (announce == null) {
            throw new Exception("Announce non trouvé");
        }

        Long id = queryUtils.calcolateId(MessageVO.GET_ID_SQL);

        MessageIdVO messageId = new MessageIdVO(id, announce.getAnnounceId().getToken());
        MessageVO message = new MessageVO();
        setMessage(announce, user, message, mdto);
        message.setId(messageId);
        announce.addMessage(message);
        save(message);
        return message;

    }

    @Override
    public List<MessageVO> messagesByUser(UserVO user, PageBy pageBy) throws Exception {

        if (user == null) return null;
        return messagesByUser(user.getId(), pageBy);
    }

    @Override
    public List<MessageVO> messagesByUser(Long id, PageBy pageBy) throws Exception {
        logger.info("Message: user message");

        if (id == null) return null;
        return findByUser(MessageVO.class, id, pageBy);
    }

    @Override
    public List<MessageVO> messages(PageBy pageBy) throws Exception {
        logger.info("Message: all message");

        return findBy(MessageVO.FINDALL, MessageVO.class, null, null, pageBy);
    }

    @Override
    public MessageVO findById(MessageIdVO id) throws BusinessResourceException {
        logger.info("Message: findBy");
        MessageVO message = (MessageVO) findById(MessageVO.class, id);
        return message;
    }


    @Override
    public boolean delete(Long id) throws BusinessResourceException {
        logger.info("Message: delete");
        //MessageIdVO messageId= new MessageIdVO(id, Constants.DEFAULT_TOKEN);
        //delete(MessageVO.class,messageId,true);
        return updateDelete(id);
    }

    private void setMessage(AnnounceVO announce, UserVO user, MessageVO message, MessageDTO mdto) {
        message.setAnnounce(announce);
        message.setContent(mdto.getContent());
        message.setCancelled(false);
        message.setUser(user);
    }

    @Override
    public boolean updateDelete(Long id) throws BusinessResourceException {
        boolean result = false;

        try {


            MessageIdVO messageId = new MessageIdVO(id, Constants.DEFAULT_TOKEN);
            MessageVO message = findById(messageId);
            if (message != null) {
                message.setCancelled(true);
                message = (MessageVO) merge(message);
                result = (message != null) && (message.isCancelled());
            }
        } catch (Exception e) {
            throw new BusinessResourceException(e.getMessage());
        }
        return result;
    }

    @Override
    public String composeQuery(Object o, String alias) {
        return null;
    }

    @Override
    public void composeQueryParameters(Object o, Query query) {

    }
}
