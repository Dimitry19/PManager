package cm.framework.ds.hibernate.dao;

import cm.travelpost.tp.common.Constants;
import cm.travelpost.tp.common.event.IEvent;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.notification.enums.NotificationType;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;


public abstract class Generic<T, ID extends Serializable> extends CommonGenericDAO {

    public static String where=" where ";
    public String notificationMessagePattern = "{0} {1} {2}";
    public String notificationMessageCommentPattern = "{0} {1} {2} {3}";

    public abstract boolean updateDelete(Object id) throws BusinessResourceException, UserException;

    public  String composeQuery(Object o, String alias) throws Exception{
        return null;
    }

    public  void composeQueryParameters(Object o, Query query) throws Exception{

    }


    /**
     * @param message
     * @param notificationType
     * @param username
     * @param departure
     * @param arrival
     * @param startDate
     * @param endDate
     * @param kg
     * @return
     */
    public String  buildNotificationMessage(String message, @NotNull NotificationType notificationType, String username, String departure,
                                            String arrival, String startDate, String endDate, String kg){


        switch (notificationType){
            case USER:
                break;
            case SUBSCRIBE:
                message=MessageFormat.format(notificationMessagePattern,username," s'est abonné "," à votre profil");
                break;
            case UNSUBSCRIBE:
                message =MessageFormat.format(notificationMessagePattern,username," s'est désabonné "," de votre profil");
                break;
            case ANNOUNCE:
                message =MessageFormat.format(notificationMessagePattern,username,
                        " a creé l'annonce " + departure +"/"+arrival,
                        "pour la date " + startDate+ "et retour le "+ endDate);

                break;
            case ANNOUNCE_UPD:
                message =MessageFormat.format(notificationMessagePattern,username,
                        " a modifié l'annonce " + departure +"/"+arrival,
                        "pour la date " + startDate+ "et retour le "+ endDate);
                break;
            case ANNOUNCE_DEL:
                message =MessageFormat.format(notificationMessagePattern,username,
                        " a supprimé l'annonce " + departure +"/"+arrival,
                        "pour la date " + startDate+ "et retour le "+ endDate);
                break;
            case RESERVATION:
                message= MessageFormat.format(notificationMessagePattern,username,
                        " a fait une reservation  de [" +kg+" kg ] sur votre annonce "
                                + departure +"/"+arrival," de la date du " + startDate+ " et retour le "+ endDate);
                break;
            case RESERVATION_VALIDATE:
                 message= MessageFormat.format(notificationMessagePattern,username,
                        " a accepté votre reservation  de [" +kg+" kg ] sur l' annonce "
                                + departure +"/"+arrival," de la date du " + startDate+ " et retour le "+ endDate);
                break;
            case RESERVATION_UNVALIDATE:
                message= MessageFormat.format(notificationMessagePattern,username,
                        " a refusé votre reservation  de [" +kg+" kg ] sur l' annonce "
                                + departure +"/"+arrival," de la date du " + startDate+ " et retour le "+ endDate);
                break;
            case RESERVATION_UPD:

                 message= MessageFormat.format(notificationMessagePattern,username,
                         " a modifié une reservation sur votre annonce "+departure +"/"+arrival,
                        " du " + startDate + " et retour le "+ endDate+kg);

                break;
            case RESERVATION_DEL:

                message= MessageFormat.format(notificationMessagePattern,username,
                        " a supprimé une reservation  de [" +kg+" kg ] sur votre annonce "
                                + departure +"/"+arrival," de la date du " + startDate+ " et retour le "+ endDate);
                break;
            case COMMENT:
                message=MessageFormat.format(notificationMessageCommentPattern,username
                        ," a ajouté un commentaire sur l'annonce "+departure+"/"+arrival,
                        " pour la date " + startDate+ " et retour le "+ endDate, "");
                break;
            case COMMENT_UPD:
                message=MessageFormat.format(notificationMessageCommentPattern,username
                        ," a modifié un commentaire sur l'annonce "+departure+"/"+arrival,
                        " pour la date " + startDate+ " et retour le "+ endDate, "");
                break;
        }
        return message;
    }


    public void fillProps(Map props, Long id,String message, Long userId,Set subscribers) throws Exception {

            props.put(IEvent.PROP_ID,id);
            props.put(IEvent.PROP_MSG,message);
            props.put(IEvent.PROP_USR_ID,userId);
            props.put(IEvent.PROP_SUBSCRIBERS,subscribers);

    }


    public void buildAndOr(StringBuilder hql, boolean addCondition, boolean andOrOr) {
        if (addCondition) {
            if (!andOrOr) {
                hql.append(Constants.OR);
            } else {
                hql.append(Constants.AND);
            }
        }
    }

}
