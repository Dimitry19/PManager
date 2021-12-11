package cm.packagemanager.pmanager.common.ent.service;

import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
import cm.packagemanager.pmanager.common.enums.UploadImageType;

public interface ImageService {

    ImageVO findByName(String name) throws Exception;

    boolean delete(String name, Long id) throws Exception;

    ImageVO save(String file, Long id, UploadImageType type) throws Exception;
}
