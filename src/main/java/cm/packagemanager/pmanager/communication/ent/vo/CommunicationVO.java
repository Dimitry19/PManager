package cm.packagemanager.pmanager.communication.ent.vo;

import cm.packagemanager.pmanager.administrator.ent.vo.AdminVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.enums.CommunicationType;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@NamedQueries({
        @NamedQuery(name = CommunicationVO.FIND_BY_USER_ID, query = "select c from CommunicationVO c")
})
@Table(name = "COMMUNICATION", schema = "PUBLIC")
public class CommunicationVO extends CommonVO {

    public static final String FIND_BY_USER_ID = "cm.packagemanager.pmanager.communication.ent.vo.CommunicationVO.findByUserId";

    private static final long serialVersionUID = 4708927204204501030L;
    private long id;
    private AdminVO admin;
    private String content;
    private CommunicationType type;
    private Set<UserVO> users = new HashSet<>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @Column(name = "CONTENT", nullable = false)
    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    public CommunicationType getType() {
        return this.type;
    }

    public void setType(CommunicationType type) {
        this.type = type;
    }

    /**
     * Le mappedBy doit indiquer le nom de l'attribut qui indique le nom de la relation correspondante dans l'entité maître (CommuninicationVO).
     * cela cree automatiquement la table de jointure
     * si c'est pas specifié on devra faire une table de jointure
     * http://blog.paumard.org/cours/jpa/chap03-entite-relation.html
     */
    @ManyToMany(mappedBy = "communications", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    public Set<UserVO> getUsers() {
        return users;
    }


    public void setUsers(Set<UserVO> users) {
        this.users = users;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "R_ADMIN_ID", updatable = false)
    public AdminVO getAdmin() {
        return admin;
    }

    public void setAdmin(AdminVO admin) {
        this.admin = admin;
    }


    public void addUser(UserVO user) {
        users.add(user);
    }

    public void removeUser(UserVO user) {
        if (!users.isEmpty())
            users.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommunicationVO that = (CommunicationVO) o;
        return id == that.id && content.equals(that.content) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, type);
    }

    @Override
    public String toString() {
        return "Communication{" + "id=" + id + ", content='" + content + '\'' + ", type=" + type + ", users=" + users + '}';
    }
}
