package cm.framework.ds.common.ent;

import cm.framework.ds.common.ent.vo.CommonIdVO;
import cm.framework.ds.common.ent.vo.CommonVO;
import cm.framework.ds.common.ent.vo.PageBy;
import cm.framework.ds.common.exception.GenericCRUDEException;

import java.util.List;

public interface GenericCRUDEService<T extends CommonVO, K extends CommonIdVO> {

	public T create(T t) throws GenericCRUDEException;
	public T read(K k) throws GenericCRUDEException;
	public T update(T t, K k) throws GenericCRUDEException;
	public List<T> all(PageBy pageBy) throws Exception;
	public boolean delete(K k) throws GenericCRUDEException;
}
