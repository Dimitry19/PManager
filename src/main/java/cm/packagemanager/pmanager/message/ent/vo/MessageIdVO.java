package cm.packagemanager.pmanager.message.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonIdVO;
import javax.persistence.*;

/**
 *
 */
@Embeddable
public class MessageIdVO extends CommonIdVO {


	private int  id;

	public MessageIdVO(){}

	public MessageIdVO(int id, String token) {

		this.id=id;
		this.token=token;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ID")
	public int getId() {
		return id;

	}

	public void setId(int id) {
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
		int result = id+id;
		result = 31 * result + token.hashCode();
		return result;
	}
}
