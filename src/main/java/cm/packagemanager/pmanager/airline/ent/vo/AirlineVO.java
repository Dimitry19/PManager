package cm.packagemanager.pmanager.airline.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 */

@Entity
@Table(name = "AIRLINE", schema = "PUBLIC")
@NamedQueries({
        @NamedQuery(name = AirlineVO.FINDBYCODE, query = "select a from AirlineVO a where a.id.code  =:code"),
        @NamedQuery(name = AirlineVO.ALL, query = "select a from AirlineVO a order by a.description"),
})
@Filters({
        @Filter(name = FilterConstants.CANCELLED)
        //@Filter(name=FilterConstants.ACTIVE_MBR)
})
public class AirlineVO extends CommonVO {

    public static final String FINDBYCODE = "cm.packagemanager.pmanager.airline.ent.vo.AirlineVO.findByCode";
    public static final String ALL = "cm.packagemanager.pmanager.airline.ent.vo.AirlineVO.all";

    @EmbeddedId
    private AirlineIdVO id;
    private String description;


    public AirlineIdVO getId() {
        return id;
    }

    @Basic(optional = false)
    @Column(name = "DESCRIPTION", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(AirlineIdVO id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AirlineVO airlineVO = (AirlineVO) o;
        return id.equals(airlineVO.id) && Objects.equals(description, airlineVO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description);
    }
}
