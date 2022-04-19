package cm.travelpost.tp.message.ent.vo;

import cm.travelpost.tp.announce.ent.vo.AnnounceMasterVO;
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

@Entity(name = "MessageVO")
@Table(name = "message")
@NamedQueries({
        @NamedQuery(name = MessageVO.FINDALL, query = "select m from MessageVO m "),
        @NamedQuery(name = MessageVO.FIND_BY_ANNOUNCE, query = "select m from MessageVO m where m.announce.id =: announceId order by m.dateCreated asc"),
        @NamedQuery(name = MessageVO.GET_ID_SQL, query = "select max(m.id.id) from MessageVO m ")
})
@Filters({
        @Filter(name = FilterConstants.CANCELLED)
})
@Where(clause= FilterConstants.FILTER_WHERE_MESSAGE_CANCELLED)
public class MessageVO extends CommonVO {

    public static final String FINDALL = "cm.travelpost.tp.message.ent.vo.MessageVO.findAll";

    public static final String FIND_BY_ANNOUNCE = "cm.travelpost.tp.message.ent.vo.MessageVO.findByAnnounce";

    public final static String GET_ID_SQL = "cm.travelpost.tp.message.ent.vo.MessageVO.getId";

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
    private AnnounceMasterVO announce;

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

    public AnnounceMasterVO getAnnounce() {
        return announce;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
        setUsername(user.getUsername());
        setUserId(user.getId());
    }

    public void setAnnounce(AnnounceMasterVO announce) {
        this.announce = announce;
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
        MessageVO messageVO = (MessageVO) o;
        return Objects.equals(content, messageVO.content) && username.equals(messageVO.username) && id.equals(messageVO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, id);
    }

}
