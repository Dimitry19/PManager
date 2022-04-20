package cm.travelpost.tp.message.ent.vo;

import cm.travelpost.tp.announce.ent.vo.AnnounceCompletedVO;
import cm.travelpost.tp.common.ent.vo.CommonVO;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 */

@Entity
@Table(name = "v_message_completed")
@NamedQueries({
        @NamedQuery(name = MessageCompletedVO.FINDALL, query = "select m from MessageCompletedVO m "),
        @NamedQuery(name = MessageCompletedVO.FIND_BY_ANNOUNCE, query = "select m from MessageCompletedVO m where m.announceCompleted.id =: announceId order by m.dateCreated asc"),
        @NamedQuery(name = MessageCompletedVO.GET_ID_SQL, query = "select max(m.id.id) from MessageCompletedVO m ")
})
@Filters({
        @Filter(name = FilterConstants.CANCELLED)
})
@Where(clause= FilterConstants.FILTER_WHERE_MESSAGE_CANCELLED)
public class MessageCompletedVO extends CommonVO {

    public static final String FINDALL = "cm.travelpost.tp.message.ent.vo.MessageCompletedVO.findAll";

    public static final String FIND_BY_ANNOUNCE = "cm.travelpost.tp.message.ent.vo.MessageCompletedVO.findByAnnounce";

    public final static String GET_ID_SQL = "cm.travelpost.tp.message.ent.vo.MessageCompletedVO.getId";

    private static final long serialVersionUID = 1L;

    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "R_USER_ID", updatable = false)
    @JsonBackReference
    private UserVO user;


    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "R_ANNOUNCE", referencedColumnName = "ID", updatable = false)
    @JsonBackReference
    private AnnounceCompletedVO announceCompleted;

    @Basic(optional = false)
    @Column(name = "CONTENT", nullable = false)
    @JsonProperty
    private String content;

    @Transient
    private String username;

    @Transient
    private Long userId;

    @EmbeddedId
    MessageIdVO id;

    public MessageIdVO getId() {
        return id;
    }

    public void setId(MessageIdVO id) {
        this.id = id;
    }

    private String getContent() {
        return content;
    }

    public AnnounceCompletedVO getAnnounceCompleted() {
        return announceCompleted;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
        setUsername(user.getUsername());
        setUserId(user.getId());
    }

    public void setAnnounceCompleted(AnnounceCompletedVO announce) {
        this.announceCompleted = announce;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @JsonProperty
    public String getUsername() {
        return username;//.getUsername();
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @JsonProperty
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageCompletedVO messageVO = (MessageCompletedVO) o;
        return Objects.equals(content, messageVO.content) && username.equals(messageVO.username) && id.equals(messageVO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, id);
    }

}
