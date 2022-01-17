package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.notification.firebase.ent.dao.NotificationDAO;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationDAO notificationDAO;


    @Override
    public NotificationVO read(Long id) throws Exception {
        return notificationDAO.read(id);
    }
}
