package cm.packagemanager.pmanager.common.ent.dao;

import cm.packagemanager.pmanager.common.ent.vo.ImageVO;


public interface ImageDAO extends CommonDAO{
	ImageVO findByName(String name) throws Exception;
}
