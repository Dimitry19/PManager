package cm.packagemanager.pmanager.common.ent.service;

import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
import cm.packagemanager.pmanager.common.enums.UploadImageType;

public interface ImageService {

	ImageVO findByName(String name) throws Exception;

	void delete(String name, Long id) throws Exception;

	void save(ImageVO image) throws Exception;

	void save(ImageVO image, Long id, UploadImageType type) throws Exception;
}
