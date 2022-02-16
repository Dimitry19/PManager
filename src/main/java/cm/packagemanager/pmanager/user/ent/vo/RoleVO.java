package cm.packagemanager.pmanager.user.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.common.enums.RoleEnum;
import cm.packagemanager.pmanager.common.enums.RoleType;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "ROLE", schema = "PUBLIC")
@TypeDef(
        name = "role_enum",
        typeClass = RoleType.class
)
@NamedQueries({
        @NamedQuery(name = RoleVO.FINDBYID, query = "select r from RoleVO r where r.id  =:id"),
        @NamedQuery(name = RoleVO.FINDBYDESC, query = "select r from RoleVO r where r.description =:description "),

})
public class RoleVO extends WSCommonResponseVO {

    private static final long serialVersionUID = 2284252532274015507L;

    public static final String FINDBYID = "cm.packagemanager.pmanager.user.ent.vo.RoleVO.findById";
    public static final String FINDBYDESC = "cm.packagemanager.pmanager.user.ent.vo.RoleVO.findByDesc";

    private int id;

    private RoleEnum description;


    public RoleVO() {
        super();
        this.description = RoleEnum.USER;
    }

    public RoleVO(RoleEnum description) {
        super();
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    public int getId() {
        return id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "DESCRIPTION", updatable = true, nullable = false)
    public RoleEnum getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setDescription(RoleEnum description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", role=" + description.name() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
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
        RoleVO other = (RoleVO) obj;
        if (id != other.id)
            return false;
        if (description == null) {
			return other.description == null;
        } else return description.equals(other.description);
	}

    public int compareTo(RoleVO role) {
        return this.description.compareTo(role.getDescription());

    }
}