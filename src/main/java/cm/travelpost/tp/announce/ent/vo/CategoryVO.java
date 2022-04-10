package cm.travelpost.tp.announce.ent.vo;


import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.constant.FieldConstants;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */

@Entity
@Table(name = "CATEGORY")
@NamedQueries({
        @NamedQuery(name = CategoryVO.FIND_BY_CODE, query = "select pc from CategoryVO pc where pc.code =:code"),
})
public class CategoryVO implements Serializable {


    public static final String FIND_BY_CODE = "cm.travelpost.tp.announce.ent.vo.ProductCategoryVO.findByCode";
    private static final long serialVersionUID = -6108078165603686675L;



    private String code;

    private String description;

    public CategoryVO() {
    }

    @Id
    @Basic(optional = false)
    @Column(name = "CODE", nullable = false, length = FieldConstants.AUTH_USER_LEN)
    public String getCode() {  return code;  }

    @Basic(optional = false)
    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryVO that = (CategoryVO) o;

        if (!code.equals(that.code)) return false;
        return code.equals(that.code);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        if (StringUtils.isNotEmpty(description)) {
            result = 31 * result + description.hashCode();
        }
        return result;
    }


    @Override
    public String toString() {
        return "CategoryVO{" + "code='" + code + '\'' + ", description='" + description + '\'' + '}';
    }
}
