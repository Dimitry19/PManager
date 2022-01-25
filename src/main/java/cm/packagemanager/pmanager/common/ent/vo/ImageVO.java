package cm.packagemanager.pmanager.common.ent.vo;


import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "IMAGE")
@NamedQueries({
        @NamedQuery(name = ImageVO.IMG_BY_NAME, query = " select i from ImageVO  as i where i.name=: name"),
})
public class ImageVO extends WSCommonResponseVO {

    private static final long serialVersionUID = 1554805356908332718L;

    public static final String IMG_BY_NAME = "cm.packagemanager.pmanager.common.ent.vo.ImageVO.byName";

    private Long id;
    private String name;
    private String type;
    //image bytes can have large lengths so we specify a value

    //which is more than the default length for picByte column
    private byte[] picByte;

    @Transient
    private String origin;

    private UserVO user;
    private AnnounceVO announce;

    public ImageVO() {
        super();
    }

    public ImageVO(String name, String type, byte[] picByte) {

        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }

    public ImageVO(String type, byte[] picByte) {
        this.type = type;
        this.picByte = picByte;
    }

    public ImageVO(byte[] picByte) {
        this.picByte = picByte;
    }


    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Basic(optional = false)
    @Column(name = "NAME", unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	@Basic(optional = true)
	@Column(name = "PIC_BYTE", length = 15000)
	public byte[] getPicByte() {
		return picByte;
	}

	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}

    /**
     * Avec le mappedBy c'est l'entité UserVO qui se charge de manager la relation
     * Juste besoin de faire la user.setImage(image) à condition que les Cascades soient bien gerees et pas besoin de faire le
     * image.setUser(user)
     *
     * @return
     */
    @JsonIgnore
    @OneToOne(mappedBy = "image", cascade = CascadeType.DETACH)
    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    @JsonIgnore
    @OneToOne(mappedBy = "image", cascade = CascadeType.DETACH)
    public AnnounceVO getAnnounce() {
        return announce;
    }

    public void setAnnounce(AnnounceVO announce) {
        this.announce = announce;
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageVO imageVO = (ImageVO) o;
        return id.equals(imageVO.id) && Objects.equals(name, imageVO.name) && type.equals(imageVO.type) && Arrays.equals(picByte, imageVO.picByte);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, type);
        result = 31 * result + Arrays.hashCode(picByte);
        return result;
    }

    @Override
    public String toString() {
        return "ImagesVO{" + "id=" + id + ", name='" + name + '\'' + ", type='" + type + '\'' + ", picByte=" + Arrays.toString(picByte) + '}';
    }
}