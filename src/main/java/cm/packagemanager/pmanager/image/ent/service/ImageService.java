package cm.packagemanager.pmanager.image.ent.service;

import cm.packagemanager.pmanager.image.ent.bo.ImageMultipart;
import cm.packagemanager.pmanager.image.ent.vo.ImageVO;
import cm.packagemanager.pmanager.common.enums.UploadImageType;
import cm.packagemanager.pmanager.image.utils.ImageFormat;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService<T> {

    ImageVO findByName(String name) throws Exception;

    boolean delete(String name, Long id) throws Exception;

    ImageVO save(MultipartFile file, Long id, UploadImageType type) throws Exception;

    boolean uploadFile(MultipartFile file, Long id, UploadImageType type) throws Exception;

    boolean uploadFile(MultipartFile file) throws Exception;

    ImageFormat compress(ImageMultipart im, String name,String path);
     void compress(ImageMultipart im, String name);
}
