package cm.travelpost.tp.city.ent.vo;

import java.io.Serializable;

public class Currency implements Serializable {

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public void setCode(String code) { this.code = code; }
    public void setName(String name) { this.name = name;    }
}
