package cm.travelpost.tp.announce.ent.dao;

import cm.framework.ds.hibernate.dao.CommonDAO;
import cm.travelpost.tp.user.ent.vo.UserVO;

public interface UserAnnounceFavoriteDAO extends CommonDAO {

    public boolean createUserAnnounceFavorite(long idUser, long idAnnounce);

    public UserVO findUserAnnounceByID(long id) throws Exception;
}
