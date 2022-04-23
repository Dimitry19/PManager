/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.city.ent.vo;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "city")
@NamedQueries(value = {

        @NamedQuery(name = CityVO.AUTOCOMPLETE, query = "select c from CityVO c where (upper(c.id) like :search )  or (upper(c.name) like :search ) " +
                " or (c.id like :search) or (c.name like :search) order by c.id desc"),

})

public class CityVO  implements Serializable {

    private static final long serialVersionUID = -6128390864869421614L;
    public static final String AUTOCOMPLETE = "cm.travelpost.tp.city.ent.vo.CityVO.autoComplete";


    private String id;

    private String name;

    public CityVO() {
        super();
    }


    @Id
    @Column(name = "ID", nullable = false, unique = true)
    public String getId() {
        return id;
    }

    @Basic(optional = false)
    @Column(name = "NAME", nullable = false)
    public String getName() {
        return name;
    }

    public void setId(String id) {this.id = id; }

    public void setName(String name) {  this.name = name; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        result = prime * result +  name.hashCode();
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
        CityVO other = (CityVO) obj;
        if (id.equals(other.id))
            return false;
        if (name == null) {
			return other.name == null;
        } else return name.equals(other.name);
	}

}
