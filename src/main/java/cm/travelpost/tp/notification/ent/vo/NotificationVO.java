/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.vo;

import cm.travelpost.tp.common.ent.vo.CommonVO;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.notification.enums.NotificationType;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notification")
@Filters({
        @Filter(name = FilterConstants.CANCELLED)

        //@Filter(name = FilterConstants.ACTIVE_MBR_WORK)
})
@Where(clause = FilterConstants.FILTER_NOTIFICATION_CANC_COMPLETED)
public class NotificationVO extends CommonVO {

    private static final long serialVersionUID = 6473029882566244786L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = FieldConstants.ENUM_LEN, nullable = false)
    private NotificationType type;

    @Basic(optional = false)
    @Column(name = "MESSAGE", nullable = false, length = FieldConstants.DESC)
    private String message;

    @Basic(optional = false)
    @Column(name = "TITLE", nullable = false, length = 60)
    private String title;

    @Basic(optional = true)
    @Column(name = "SESSION_ID")
    private String sessionId;

    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Basic(optional = false)
    @Column(name = "R_USER_ID", nullable = false)
    private Long random;



    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_notification", joinColumns = @JoinColumn(name = "NOTIFICATIONS_ID"),
            inverseJoinColumns = @JoinColumn(name = "USERS_ID"))
    private Set<UserVO> users = new HashSet();

    @Column(name = "R_ANNOUNCE_ID", nullable = true)
    private Long announceId;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = FieldConstants.ENUM_LEN)
    private StatusEnum status;

    @Transient
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAnnounceId() {
        return announceId;
    }

    public void setAnnounceId(Long announceId) {
        this.announceId = announceId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Set<UserVO> getUsers() {  return users;  }

    public void setUsers(Set<UserVO> users) {   this.users = users;  }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRandom() { return random; }

    public void setRandom(Long random) { this.random = random; }
}
