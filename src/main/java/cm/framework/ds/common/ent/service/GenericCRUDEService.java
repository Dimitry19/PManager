package cm.framework.ds.common.ent.service;

import cm.framework.ds.common.ent.vo.CommonIdVO;
import cm.framework.ds.common.ent.vo.CommonVO;
import cm.framework.ds.common.exception.GenericCRUDEException;

public interface GenericCRUDEService<T extends CommonVO, K extends CommonIdVO> {

	public T create(T t) throws GenericCRUDEException;
	public T read(K k) throws GenericCRUDEException;
	public T update(T t, K k) throws GenericCRUDEException;
	public boolean delete(K k) throws GenericCRUDEException;
}