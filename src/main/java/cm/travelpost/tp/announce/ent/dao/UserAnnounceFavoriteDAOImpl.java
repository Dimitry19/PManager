package cm.travelpost.tp.announce.ent.dao;

import cm.framework.ds.hibernate.dao.Generic;
import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.announce.ent.vo.UserAnnounceFavoriteVO;
import cm.travelpost.tp.common.exception.BusinessResourceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserAnnounceFavoriteDAOImpl extends Generic implements UserAnnounceFavoriteDAO{


    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AnnounceDAO announceDAO;

    private static Logger logger = LoggerFactory.getLogger(ReservationDAOImpl.class);


    @Override
    public boolean updateDelete(Object id) throws BusinessResourceException, UserException {
        return false;
    }

    @Override
    public boolean createUserAnnounceFavorite(long idUser, long idAnnounce) {

        try {
            UserVO userVO = userDAO.findById(idUser);
            AnnounceVO announce = announceDAO.announce(idAnnounce);
            UserAnnounceFavoriteVO userAnnounceFavoriteVO = new UserAnnounceFavoriteVO();
            userAnnounceFavoriteVO.setUser(userVO);
            userAnnounceFavoriteVO.getListAnnounceFavorite().add(announce);

            save(userAnnounceFavoriteVO);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public UserVO findUserAnnounceByID(long idUser) throws Exception {

        if (logger.isDebugEnabled()) {
            logger.info("User: find by id User {}", idUser);
        }

        return (UserVO) findByUniqueResult(UserAnnounceFavoriteVO.FINDUSERBYID, UserVO.class, idUser,USER_PARAM, null, getFilters());
    }


}
