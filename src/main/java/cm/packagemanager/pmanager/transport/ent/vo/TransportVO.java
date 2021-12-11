package cm.packagemanager.pmanager.transport.ent.vo;

import cm.packagemanager.pmanager.common.enums.TransportEnum;
import cm.packagemanager.pmanager.constant.FieldConstants;

import javax.persistence.*;
import java.io.Serializable;

/*
@Entity
@Table(name = "TRANSPORT", schema = "PUBLIC")
@TypeDef(
		name = "transport_enum",
		typeClass = TransportType.class
)
@NamedQueries({
		@NamedQuery(name = TransportVO.FINDBYID, query = "select tm from TransportVO tm where id  =:id"),
		@NamedQuery(name = TransportVO.FINDBYCODE, query = "select tm from TransportVO tm where code  =:code"),
		@NamedQuery(name = TransportVO.FINDBYDESC, query = "select tm from TransportVO tm where description =:description "),

})*/
public class TransportVO implements Serializable {


    private static final long serialVersionUID = 2284252532274015517L;

    public static final String FINDBYID = "cm.packagemanager.pmanager.transportMode.ent.vo.TransportModeVO.findById";
    public static final String FINDBYCODE = "cm.packagemanager.pmanager.transportMode.ent.vo.TransportModeVO.findByCode";
    public static final String FINDBYDESC = "cm.packagemanager.pmanager.transportMode.ent.vo.TransportModeVO.findByDesc";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private int id;


    @Basic(optional = false)
    @Column(name = "CODE", nullable = false, length = FieldConstants.AIR_ID_LEN)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "DESCRIPTION", updatable = true, nullable = false)
    private TransportEnum description;


    public TransportVO() {
        super();
        this.description = TransportEnum.PLANE;
    }

    public TransportVO(String code, TransportEnum description) {
        super();
        this.code = code;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TransportEnum getDescription() {
        return description;
    }

    public void setDescription(TransportEnum description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Transport Mode [id=" + id + ", code=" + code + ", description=" + description.name() + "]";
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
        TransportVO other = (TransportVO) obj;
        if (id != other.id)
            return false;
        if (description == null) {
			return other.description == null;
        } else return description.equals(other.description);
	}

    public int compareTo(TransportVO transport) {

        return this.code.compareTo(transport.getCode());

    }
}
