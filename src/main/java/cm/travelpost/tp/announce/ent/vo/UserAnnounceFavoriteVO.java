package cm.travelpost.tp.announce.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.user.ent.vo.UserVO;

import javax.persistence.*;
import java.util.Set;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = UserAnnounceFavoriteVO.FINDUSERBYID, query = "select a from UserAnnounceFavoriteVO a where a.user.id =:userId"),
})
@Table(name = "user_Announce_Favorite")
public class UserAnnounceFavoriteVO extends CommonVO {

    public static final String FINDUSERBYID = "cm.travelpost.tp.announce.ent.vo.UserAnnounceFavoriteVO.findUserByID";

    private long id;

    private Set<AnnounceVO> listAnnounceFavorite;

    private UserVO user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Long getId() {  return id; }

    public void setId(long id) {
        this.id = id;
    }


    @Access(AccessType.PROPERTY)
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name = "user_listAnnounce_favorite", joinColumns = @JoinColumn(name = "UserAnnounce_ID"), inverseJoinColumns = @JoinColumn(name = "ANNOUNCE_ID"))
    public Set<AnnounceVO> getListAnnounceFavorite() {
        return listAnnounceFavorite;
    }

    public void setListAnnounceFavorite(Set<AnnounceVO> listAnnounceFavorite) {
        this.listAnnounceFavorite = listAnnounceFavorite;
    }

    @OneToOne(mappedBy = "userAnnounceFavorite")
    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        user = user;
    }
}
