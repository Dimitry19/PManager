package cm.packagemanager.pmanager.common.ent.service;

import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
import cm.packagemanager.pmanager.common.enums.UploadImageType;
import io.netty.util.internal.MacAddressUtil;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService<T> {

    ImageVO findByName(String name) throws Exception;

    boolean delete(String name, Long id) throws Exception;

    ImageVO save(MultipartFile file, Long id, UploadImageType type) throws Exception;
}
