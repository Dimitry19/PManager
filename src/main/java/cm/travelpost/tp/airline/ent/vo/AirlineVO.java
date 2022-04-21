package cm.travelpost.tp.airline.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

/**
 *
 */

@Entity
@Table(name = "airline")
@NamedQueries({
        @NamedQuery(name = AirlineVO.FINDBYCODE, query = "select a from AirlineVO a where a.id.code  =:code"),
        @NamedQuery(name = AirlineVO.ALL, query = "select a from AirlineVO a order by a.description"),
})
@Filters({
        @Filter(name = FilterConstants.CANCELLED)
})
@Where(clause = FilterConstants.FILTER_AIRLINE_CANC)
public class AirlineVO extends CommonVO {

    public static final String FINDBYCODE = "cm.travelpost.tp.airline.ent.vo.AirlineVO.findByCode";
    public static final String ALL = "cm.travelpost.tp.airline.ent.vo.AirlineVO.all";

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
