package cm.packagemanager.pmanager.user.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.enums.Gender;
import cm.packagemanager.pmanager.communication.ent.vo.CommunicationVO;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.image.ent.vo.ImageVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.notification.ent.vo.NotificationVO;
import cm.packagemanager.pmanager.review.ent.vo.ReviewVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.*;

import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.hibernate.annotations.FetchMode.SELECT;

/**
 * le @Fetch(value = SELECT) permet de corriger le probleme de chargement des elements (Set et List) meme
 * quand ils sont vides quand on utilise la methode find() de la session.
 * Corrige l'erreur  fail-safe cleanup (collections) : org.hibernate.engine.loading.CollectionLoadContext
 */

@Entity(name = "UserVO")
@Table(name = "TP_USER")
@NamedQueries({
        @NamedQuery(name = UserVO.Q_AC_ITEM, query = "select u from UserVO u where (upper(u.lastName) like :searchFilter) or(upper(u.firstName) like :" +
                "searchFilter ) or(u.username like :searchFilter) or( u.email like :searchFilter)  order by u.firstName"),
        @NamedQuery(name = UserVO.ALL, query = "select u from UserVO u   order by u.firstName"),
        @NamedQuery(name = UserVO.FINDBYID, query = "select u from UserVO u where u.id  =:id"),
        @NamedQuery(name = UserVO.USERNAME, query = "select u from UserVO u where u.username like :username "),
        @NamedQuery(name = UserVO.EMAIL, query = "select u from UserVO u where  u.email =:email "),
        @NamedQuery(name = UserVO.CONF_TOKEN, query = "select u from UserVO u where  u.confirmationToken =:ctoken "),
        @NamedQuery(name = UserVO.FACEBOOK, query = "select u from UserVO u where  u.facebookId =:facebookId "),
        @NamedQuery(name = UserVO.GOOGLE, query = "select u from UserVO u where  u.googleId =:googleId "),
        @NamedQuery(name = UserVO.JOB_CONFIRM, query = "select u from UserVO u where u.confirmationToken is not  null and u.active =:act"),
})

@Filters({
        @Filter(name = FilterConstants.CANCELLED),
        @Filter(name = FilterConstants.ACTIVE_MBR)
        //@Filter(name = FilterConstants.ACTIVE_MBR_WORK)
})
@Where(clause = FilterConstants.FILTER_WHERE_USER_CANCELLED)
public class UserVO extends CommonVO {


    private static final long serialVersionUID = 6181438160768077660L;

    public static final String FINDBYID = "cm.packagemanager.pmanager.user.ent.vo.UserVO.findById";
    public static final String Q_AC_ITEM = "cm.packagemanager.pmanager.user.ent.vo.UserVO.QAutocompleteItem";
    public static final String ALL = "cm.packagemanager.pmanager.user.ent.vo.UserVO.All";
    public static final String USERNAME = "cm.packagemanager.pmanager.user.ent.vo.UserVO.findLikeId";
    public static final String CONF_TOKEN = "cm.packagemanager.pmanager.user.ent.vo.UserVO.findCToken";
    public static final String EMAIL = "cm.packagemanager.pmanager.user.ent.vo.UserVO.findByEmail";
    public static final String FACEBOOK = "cm.packagemanager.pmanager.user.ent.vo.UserVO.findByFacebookId";
    public static final String GOOGLE = "cm.packagemanager.pmanager.user.ent.vo.UserVO.findByGoogleId";
    public static final String JOB_CONFIRM = "cm.packagemanager.pmanager.user.ent.vo.UserVO.toConfirmByJob";


    private Long id;

    private String firstName;

    private String email;

    private String username;

    private String password;

    private String lastName;

    private String phone;

    private Gender gender;

    private ImageVO image;

    private String facebookId;

    private String googleId;

    private Integer active;

    private boolean enableNotification = true;

    private Set<MessageVO> messages = new HashSet<>();

    private Set<AnnounceVO> announces = new HashSet<>();

    private Set<RoleVO> roles = new HashSet<>();

    private Set<ReviewVO> reviews = new HashSet<>();

    private Set<UserVO> subscribers = new HashSet<>();

    private Set<UserVO> subscriptions = new HashSet<>();

    private Set<CommunicationVO> communications = new HashSet<>();

    private String confirmationToken;

    private Set<NotificationVO> notifications = new HashSet();

    private String error;

    private double rating = 0;


    public UserVO() {
        super();
    }


    public UserVO(Long id, String username, String password, Integer active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;

    }

    public UserVO(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public UserVO(String username, String password, Integer active) {
        this.username = username;
        this.password = password;
        this.active = active;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Long getId() {  return id; }

    @NaturalId
    @Basic(optional = false)
    @Column(name = "USERNAME", unique = true, nullable = false, length = FieldConstants.AUTH_USER_LEN)
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Basic(optional = false)
    @Column(name = "PASSWORD", nullable = false)
    public String getPassword() {   return password;  }

    @NaturalId
    @Basic(optional = false)
    @Column(name = "EMAIL", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    @Basic(optional = false)
    @Column(name = "FIRST_NAME", nullable = false)
    public String getFirstName() { return firstName; }

    @Basic(optional = false)
    @Column(name = "LAST_NAME", nullable = false)
    public String getLastName() {
        return lastName;
    }

    @Basic(optional = true)
    @Column(name = "PHONE", nullable = true, length = FieldConstants.PHONE_LEN)
    public String getPhone() {
        return phone;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 10)
    public Gender getGender() {
        return gender;
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public ImageVO getImage() {
        return image;
    }

    @Basic(optional = true)
    @Column(name = "FACEBOOK_ID")
    @JsonIgnore
    public String getFacebookId() {
        return facebookId;
    }

    @Basic(optional = true)
    @Column(name = "GOOGLE_ID")
    @JsonIgnore
    public String getGoogleId() {
        return googleId;
    }

    @JsonIgnore
    @Column(name = "ACTIVE", insertable = true, updatable = true, nullable = false)
    public Integer getActive() {
        return active;
    }

    @JsonIgnore
    @Basic(optional = true)
    @Column(name = "CONFIRM_TOKEN")
    public String getConfirmationToken() {
        return confirmationToken;
    }

    @Basic(optional = false)
    @Column(name = "ENABLE_NOTIF",nullable = false)
    public boolean isEnableNotification() {
        return enableNotification;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy(clause = "id.id DESC")
    @Where(clause = "cancelled = false")
    @JsonIgnore
    public Set<MessageVO> getMessages() {
        return messages;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    //@Fetch(value = SELECT)
    @OrderBy(clause = "startDate DESC")
    @Where(clause = "cancelled = false")
    @JsonIgnore
    public Set<AnnounceVO> getAnnounces() {
        return announces;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "R_USER"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    public Set<RoleVO> getRoles() {
        return roles;
    }

    @Access(AccessType.PROPERTY)
    @Fetch(value = SELECT)
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    public Set<ReviewVO> getReviews() {
        return reviews;
    }


    @Access(AccessType.PROPERTY)
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "SUBSCRIBERS", joinColumns = @JoinColumn(name = "R_USER_ID"), inverseJoinColumns = @JoinColumn(name = "SUBSCRIBER_ID"))
    public Set<UserVO> getSubscribers() {
        return subscribers;
    }

    @Access(AccessType.PROPERTY)
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "SUBSCRIPTIONS", joinColumns = @JoinColumn(name = "SUBSCRIPTION_ID"), inverseJoinColumns = @JoinColumn(name = "R_USER_ID"))
    public Set<UserVO> getSubscriptions() {
        return subscriptions;
    }

    @Access(AccessType.PROPERTY)
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    public Set<CommunicationVO> getCommunications() {   return communications;   }

    @Access(AccessType.PROPERTY)
    @ManyToMany(mappedBy = "users",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JsonBackReference
    public Set<NotificationVO> getNotifications() {
        return notifications;
    }


    @Transient
    @JsonProperty
    public double getRating() {
        return rating;
    }

    @Transient
    @JsonIgnore
    public String getError() {
        return error;
    }


    public void setSubscribers(Set<UserVO> subscribers) {
        this.subscribers = subscribers;
    }

    public void setSubscriptions(Set<UserVO> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setNotifications(Set<NotificationVO> notifications) {
        this.notifications = notifications;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setEnableNotification(boolean enableNotification) { this.enableNotification = enableNotification;  }

    public void setCommunications(Set<CommunicationVO> communications) {
        this.communications = communications;
    }

    public void setImage(ImageVO image) {
        this.image = image;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMessages(Set<MessageVO> messages) {
        this.messages = messages;
    }

    public void setRoles(Set<RoleVO> roles) {
        this.roles = roles;
    }

    public void setAnnounces(Set<AnnounceVO> announces) {
        this.announces = announces;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setReviews(Set<ReviewVO> reviews) {
        this.reviews = reviews;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }


    public void addAnnounce(AnnounceVO announce) {
        this.announces.add(announce);
        announce.setUser(this);

    }

    public void removeAnnounce(AnnounceVO announce) {
        announce.setUser(null);
        this.announces.remove(announce);
    }

    public void removeAnnounces() {
        Iterator<AnnounceVO> iterator = this.announces.iterator();

        while (iterator.hasNext()) {
            AnnounceVO announce = iterator.next();
            announce.setUser(null);
            iterator.remove();
        }
    }

    public void addRole(RoleVO role) {
        roles.add(role);
    }

    public void removeRole(RoleVO role) {
        if (!roles.isEmpty())
            roles.remove(role);
    }

    public void addMessage(MessageVO message) {
        this.messages.add(message);
        message.setUser(this);
    }

    public void removeMessage(MessageVO message) {
        message.setUser(null);
        this.messages.remove(message);
    }

    public void removeMessages() {
        Iterator<MessageVO> iterator = this.messages.iterator();

        while (iterator.hasNext()) {
            MessageVO message = iterator.next();
            message.setUser(null);
            iterator.remove();
        }
    }

    public void addSubscriber(UserVO subscriber) {
        this.subscribers.add(subscriber);
    }

    public void removeSubscriber(UserVO subscriber) { this.subscribers.remove(subscriber);  }

    public void removeSubscribers() {

        Iterator<UserVO> iterator = this.subscribers.iterator();

        while (iterator.hasNext()) {
            iterator.remove();
        }
    }

    public void addSubscription(UserVO subscription) {
        this.subscriptions.add(subscription);
    }

    public void removeSubscription(UserVO subscription) {
        this.subscriptions.remove(subscription);
    }

    public void addNotification(NotificationVO notification) {
        this.notifications.add(notification);
    }

    public void removeNotification(NotificationVO notification) {  this.notifications.remove(notification);   }

    public void removeSubscriptions() {
        Iterator<UserVO> iterator = this.subscriptions.iterator();

        while (iterator.hasNext()) {
            iterator.remove();
        }
    }

    public void addReview(ReviewVO review) {
        this.reviews.add(review);
        review.setUser(this);
    }

    public void removeReview(ReviewVO review) {
        review.setUser(null);
        this.reviews.remove(review);
    }

    public void removeReviews() {

        Iterator<ReviewVO> iterator = this.reviews.iterator();

        while (iterator.hasNext()) {
            ReviewVO review = iterator.next();

            review.setUser(null);
            iterator.remove();
        }
    }

    public void updateDeleteChildrens() {

        Iterator<ReviewVO> iteReview = this.reviews.iterator();

        while (iteReview.hasNext()) {
            ReviewVO review = iteReview.next();
            review.cancel();
        }

        Iterator<AnnounceVO> iterAnnounce = this.announces.iterator();

        while (iterAnnounce.hasNext()) {
            AnnounceVO announce = iterAnnounce.next();
            announce.cancel();
        }

        Iterator<MessageVO> iterMessage = this.messages.iterator();
        while (iterMessage.hasNext()) {
            MessageVO message = iterMessage.next();
            message.cancel();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((roles == null) ? 0 : roles.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserVO other = (UserVO) obj;
        if (active == null) {
            if (other.active != null)
                return false;
        } else if (!active.equals(other.active))
            return false;

        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (roles == null) {
            return other.roles == null;
        } else return roles.equals(other.roles);
    }

    @Override
    public String toString() {
        return "UserVO{" + "id=" + id + ", firstName='" + firstName + '\'' + ", username='" + username + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", phone='" + phone + '\'' + ", gender=" + gender + ", rating=" + rating + '}';
    }
}
