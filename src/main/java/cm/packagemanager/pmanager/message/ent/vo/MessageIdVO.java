package cm.packagemanager.pmanager.message.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonIdVO;
import javax.persistence.*;

/**
 *
 */
@Embeddable
@Access(AccessType.PROPERTY)
public class MessageIdVO extends CommonIdVO {


	private Long  id;

	public MessageIdVO(){}

	public MessageIdVO(Long id, String token) {

		this.id=id;
		this.token=token;
	}


	//@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	public Long getId() {
		return id;

	}

	public void setId(Long id) {
		this.id = id;
	}



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MessageIdVO that = (MessageIdVO) o;

		if (id!=that.id) return false;
		return token.equals(that.token);
	}

	@Override
	public int hashCode() {
		Long result = id+id;
		result = 31 * result + token.hashCode();
		return result.intValue();
	}
}
