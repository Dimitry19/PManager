package cm.framework.ds.hibernate.dao;

import cm.travelpost.tp.common.event.IEvent;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.notification.enums.NotificationType;
import org.hibernate.QueryException;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;


public abstract class Generic extends CommonGenericDAO {

    public String notificationMessagePattern = "{0} {1} {2}";
    public String notificationMessageCommentPattern = "{0} {1} {2} {3}";

    public abstract boolean updateDelete(Object id) throws BusinessResourceException, UserException;

    public  String composeQuery(Object o, String alias) throws QueryException {
        if(o==null || alias == null){
            return "";
        }
        return null;
    }

    public  void composeQueryParameters(Object o, Query query) throws Exception{

    }


    /**
     * @param notificationType
     * @param username
     * @param departure
     * @param arrival
     * @param startDate
     * @param endDate
     * @param kg
     * @return
     */
    public String  buildNotificationMessage(@NotNull NotificationType notificationType, String username, String departure,
                                            String arrival, String startDate, String endDate, String kg){

        username = "Un utilisateur ";
        String message= null;
        switch (notificationType){
            case USER:
                break;
            case SUBSCRIBE:
                message=MessageFormat.format(notificationMessagePattern,username," s'est abonné à votre profil ","");
                break;
            case UNSUBSCRIBE:
                message =MessageFormat.format(notificationMessagePattern,username," s'est désabonné à votre profil","");
                break;
            case ANNOUNCE:
                message =MessageFormat.format(notificationMessagePattern,username,
                        " a creé l'annonce " + partOneMessage(departure,arrival),
                        partTwoMessage("pour ",startDate,(endDate)));

                break;
            case ANNOUNCE_UPD:
                message =MessageFormat.format(notificationMessagePattern,username," a modifié l'annonce " + partOneMessage(departure,arrival),
                        partTwoMessage("pour ",startDate,(endDate)));
                break;
            case ANNOUNCE_DEL:
                message =MessageFormat.format(notificationMessagePattern,username,
                        " a supprimé l'annonce " + partOneMessage(departure,arrival), partTwoMessage(startDate,(endDate)));
                break;
            case RESERVATION:
                message= MessageFormat.format(notificationMessagePattern,username,
                        " a fait une reservation  de [" +kg+" kg ] sur votre annonce " + partOneMessage(departure,arrival), partTwoMessage(startDate,(endDate)));
                break;
            case RESERVATION_VALIDATE:
                 message= MessageFormat.format(notificationMessagePattern,username,
                        " a accepté votre reservation  de [" +kg+" kg ] sur l' annonce "  + partOneMessage(departure,arrival), partTwoMessage(startDate,(endDate)));
                break;
            case RESERVATION_UNVALIDATE:
                message= MessageFormat.format(notificationMessagePattern,username,
                        " a refusé votre reservation  de [" +kg+" kg ] sur l' annonce " + partOneMessage(departure,arrival), partTwoMessage(startDate,(endDate+kg)));
                break;
            case RESERVATION_UPD:

                 message= MessageFormat.format(notificationMessagePattern,username,
                         " a modifié une reservation sur votre annonce "+partOneMessage(departure,arrival), partTwoMessage(startDate,(endDate+kg)));

                break;
            case RESERVATION_DEL:

                message= MessageFormat.format(notificationMessagePattern,username,
                        " a supprimé une reservation  de [" +kg+" kg ] sur votre annonce " + partOneMessage(departure,arrival), partTwoMessage(startDate, endDate));
                break;
            case COMMENT:
                message=MessageFormat.format(notificationMessageCommentPattern,username
                        ," a ajouté un commentaire sur l'annonce "+partOneMessage(departure,arrival), partTwoMessage(startDate, endDate),"");
                break;
            case COMMENT_UPD:
                message=MessageFormat.format(notificationMessageCommentPattern,username
                        ," a modifié un commentaire sur l'annonce "+partOneMessage(departure,arrival), partTwoMessage(startDate, endDate), "");
                break;
        }
        return message;
    }

    private String partTwoMessage(String startDate, String endDate){
        return ",date de l'annonce [" + startDate+ "]  et retour le  [ "+ endDate+"]";
    }

    public String partTwoMessage(String start,String startDate, String endDate){
        return start +"la  [" + startDate+ "]  et retour le  [ "+ endDate+"]";
    }

    public String partOneMessage(String departure, String arrival){

        return departure +"/"+arrival;
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
                hql.append(OR);
            } else {
                hql.append(AND);
            }
        }
    }

}
