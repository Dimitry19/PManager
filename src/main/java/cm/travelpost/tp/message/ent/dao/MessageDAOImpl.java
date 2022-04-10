package cm.travelpost.tp.message.ent.dao;

import cm.framework.ds.hibernate.enums.FindBy;
import cm.travelpost.tp.announce.ent.dao.AnnounceDAO;
import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.common.Constants;
import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.common.ent.vo.PageBy;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.RecordNotFoundException;
import cm.travelpost.tp.common.exception.UserNotFoundException;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.common.utils.QueryUtils;
import cm.travelpost.tp.message.ent.vo.MessageIdVO;
import cm.travelpost.tp.message.ent.vo.MessageVO;
import cm.travelpost.tp.notification.enums.NotificationType;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.messages.MessageDTO;
import cm.travelpost.tp.ws.requests.messages.UpdateMessageDTO;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        MessageVO comment = findById(new MessageIdVO(mdto.getId(), Constants.DEFAULT_TOKEN));
        if (comment == null) {
            throw new RecordNotFoundException("Aucune message  trouvé");
        }
        comment.setContent(mdto.getContent());
        update(comment);

        String message= MessageFormat.format(notificationMessageCommentPattern,user.getUsername()
                ," a modifié son commentaire sur l'annonce "+comment.getAnnounce().getDeparture() +"/"+comment.getAnnounce().getArrival(),
                " pour la date " + DateUtils.getDateStandard(comment.getAnnounce().getStartDate())
                        + " et retour le "+ DateUtils.getDateStandard(comment.getAnnounce().getEndDate()), "");

        generateEvent(comment,message);
        return comment;
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

        MessageIdVO messageId = new MessageIdVO(id, Constants.DEFAULT_TOKEN);
        MessageVO comment = new MessageVO();
        setMessage(announce, user, comment, mdto);
        comment.setId(messageId);
        announce.addMessage(comment);
        save(comment);

        String message= MessageFormat.format(notificationMessageCommentPattern,user.getUsername()
                ," a commenté l'annonce "+announce.getDeparture() +"/"+announce.getArrival(),
                 " pour la date " + DateUtils.getDateStandard(announce.getStartDate())
                         + " et retour le "+ DateUtils.getDateStandard(announce.getEndDate()), "");

        generateEvent(comment,message);
        return comment;

    }

    @Override
    public List<MessageVO> messagesByUser(UserVO user, PageBy pageBy) throws Exception {

        logger.info("Message: user message");
        if (user == null) return null;
        return messagesBy(user.getId(), FindBy.USER,pageBy);
    }

    @Override
    public List<MessageVO> messagesBy(Long id, FindBy fbType, PageBy pageBy) throws Exception {

        if (id == null) return null;

        switch (fbType){
            case USER:
                return findByUser(MessageVO.class, id, pageBy);
            case ANNOUNCE:
                return findBy(MessageVO.FIND_BY_ANNOUNCE, MessageVO.class, id, ANNOUNCE_PARAM, pageBy);
        }
        return null;
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
    public boolean updateDelete(Object o) throws BusinessResourceException {
        boolean result = false;

        try {

            Long id=(Long)o;
            MessageIdVO messageId = new MessageIdVO(id, Constants.DEFAULT_TOKEN);
            MessageVO message = findById(messageId);
            if (message != null) {
                message.cancel();
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

    @Override
    public void generateEvent(Object obj , String message) throws Exception {

        MessageVO comment= (MessageVO) obj;
        UserVO user= comment.getUser();
        AnnounceVO announce=comment.getAnnounce();

        Set subscribers=new HashSet();
        subscribers.add(announce.getUser());

        if(CollectionsUtils.isNotEmpty(announce.getMessages())) {
            announce.getMessages().stream().filter(m->m.getUser()!=null && m.getUser().getId()!=user.getId() && m.getUser().isEnableNotification()).forEach(m->{
                subscribers.add(m.getUser());
            });
        }

        if (CollectionsUtils.isNotEmpty(subscribers)){
            fillProps(props,comment.getId().getId(),message, user.getId(),subscribers);
            generateEvent( NotificationType.ANNOUNCE);
        }
    }
}
