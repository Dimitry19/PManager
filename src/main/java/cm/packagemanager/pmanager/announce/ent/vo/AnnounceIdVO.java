package cm.packagemanager.pmanager.announce.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonIdVO;
import javax.persistence.*;


@Embeddable
public class AnnounceIdVO extends CommonIdVO {



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AnnounceIdVO that = (AnnounceIdVO) o;

		if (!token.equals(that.token)) return false;
		return token.equals(that.token);
	}

	@Override
	public int hashCode() {
		int result = token.hashCode();
		result = 31 * result + token.hashCode();
		return result;
	}


}
