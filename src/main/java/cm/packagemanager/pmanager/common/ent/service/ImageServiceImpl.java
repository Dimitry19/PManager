package cm.packagemanager.pmanager.common.ent.service;

import cm.packagemanager.pmanager.announce.ent.dao.AnnounceDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.dao.ImageDAO;
import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
import cm.packagemanager.pmanager.common.enums.UploadImageType;
import cm.packagemanager.pmanager.common.exception.UserNotFoundException;
import cm.packagemanager.pmanager.common.utils.FileUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

import static cm.packagemanager.pmanager.common.Constants.ANNOUNCE_TYPE_IMG_UPLOAD;
import static cm.packagemanager.pmanager.common.Constants.USER_TYPE_IMG_UPLOAD;

@Service
public class ImageServiceImpl implements ImageService {

    protected final Log logger = LogFactory.getLog(ImageServiceImpl.class);

    @Value("${profile.user.img.folder}")
    private String imageUser;

    @Value("${profile.announce.img.folder}")
    private String imageAnnounce;

    @Autowired
    ImageDAO imageDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    AnnounceDAO announceDAO;

    @Autowired
    FileUtils fileUtils;


    @Override
    public ImageVO findByName(String name) throws Exception {
        logger.info("find image by name ");
        ImageVO retrievedImage = imageDAO.findByName(name);
        return retrievedImage;
    }

    @Override
    @Transactional
    public boolean delete(String name, Long id) throws Exception {
        logger.info("delete image by name ");
        ImageVO image = imageDAO.findByName(name);
        if (image == null) {
            image = (ImageVO) imageDAO.findById(ImageVO.class, id);
        }
        if (image == null) throw new Exception("Image not found");
        imageDAO.delete(ImageVO.class, image.getId(), true);
        return imageDAO.find(ImageVO.class, id) != null;
    }


    @Override
    public ImageVO save(MultipartFile file, Long id, UploadImageType type) throws Exception {

        fileUtils.checkType(file);


        ImageVO image = new ImageVO();
        switch (type.name()) {
            case USER_TYPE_IMG_UPLOAD:
                logger.info("save user image");
                UserVO user = userDAO.findById(id);
                if (user == null)
                    throw new UserNotFoundException();

               if (user.getImage() != null) {
                    image = user.getImage();
                }
                image.setType(USER_TYPE_IMG_UPLOAD);
                image.setName(file.getOriginalFilename());
                image.setPicByte(file.getBytes());
                user.setImage(image);
                userDAO.update(user);
                logger.info("save user image end");
                break;

            case ANNOUNCE_TYPE_IMG_UPLOAD:
                logger.info("save announce image");
                image.setType(ANNOUNCE_TYPE_IMG_UPLOAD);

                AnnounceVO announce = announceDAO.announce(id);
                if (announce == null)
                    throw new Exception("Announce non trouvée");

                if (announce.getImage() != null) {
                    image = announce.getImage();
                }
                image.setName(file.getOriginalFilename());
                image.setPicByte(file.getBytes());
                announce.setImage(image);
                announceDAO.update(announce);
                logger.info("save announce image end");
                break;
            default:
                break;
        }
        return imageDAO.findByName(image.getName());
    }
}
