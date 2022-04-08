package cm.travelpost.tp.image.ent.service;

import cm.travelpost.tp.announce.ent.dao.AnnounceDAO;
import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.image.ent.bo.ImageCompressBO;
import cm.travelpost.tp.image.ent.bo.ImageMultipart;
import cm.travelpost.tp.image.ent.dao.ImageDAO;
import cm.travelpost.tp.image.ent.vo.ImageVO;
import cm.travelpost.tp.common.enums.UploadImageType;
import cm.travelpost.tp.common.exception.UserNotFoundException;
import cm.travelpost.tp.common.utils.FileUtils;
import cm.travelpost.tp.common.utils.ImageUtils;
import cm.travelpost.tp.image.utils.ImageFormat;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

import static cm.travelpost.tp.common.Constants.ANNOUNCE_TYPE_IMG_UPLOAD;
import static cm.travelpost.tp.common.Constants.USER_TYPE_IMG_UPLOAD;

@Service
public class ImageServiceImpl implements ImageService {

    protected final Log logger = LogFactory.getLog(ImageServiceImpl.class);

    @Value("${profile.user.img.folder}")
    private String imageUser;

    @Value("${profile.announce.img.folder}")
    private String imageAnnounce;

    @Value("${file.storage.upload.folder}")
    private String FILE_DIRECTORY;

    @Autowired
    ImageDAO imageDAO;

    @Autowired
	ImageCompressBO imageCompressBO;

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

        ImageUtils.validate(file);

        ImageVO image = null;
        switch (type.name()) {
            case USER_TYPE_IMG_UPLOAD:
                logger.info("save user image");
                UserVO user = (UserVO) userDAO.checkAndResolve(UserVO.class,id);

                if (user == null){
                    throw new UserNotFoundException();
                }


               if (user.getImage() != null) {
                    image = user.getImage();
                }
                if(image==null) {
                    image = new ImageVO();
                    image.setName(fileUtils.generateFilename(file.getOriginalFilename()));
                }

                //ImageFormat imgFmt=compress(new ImageMultipart(file),file.getOriginalFilename(),imageUser);
                image.setType(USER_TYPE_IMG_UPLOAD);
                image.setOrigin(file.getContentType());
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

                if(image==null){
                    image=new ImageVO();
                    image.setName(fileUtils.generateFilename(file.getOriginalFilename()));
                }

                //compress(new ImageMultipart(file),file.getOriginalFilename(),imageAnnounce);
                image.setName(fileUtils.generateFilename(file.getOriginalFilename()));
                image.setOrigin(file.getContentType());
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

    @Override
    public boolean uploadFile(MultipartFile file, Long id, UploadImageType type) throws Exception {


        switch (type.name()) {
            case USER_TYPE_IMG_UPLOAD:
                return uploadFile(file, imageUser);

            case ANNOUNCE_TYPE_IMG_UPLOAD:
                return uploadFile(file, imageAnnounce);
        }
        return false;
    }

    @Override
    public boolean uploadFile(MultipartFile file) throws Exception {

        return uploadFile(file, FILE_DIRECTORY);
    }

    @Override
    public ImageFormat compress(ImageMultipart im, String name, String path) {
        return imageCompressBO.compress(im,name,path);
    }

    @Override
    public void compress(ImageMultipart im, String name) {
        imageCompressBO.compress(im,name);
    }


    private boolean uploadFile(MultipartFile file, String path) throws Exception {

        File convertFile = new File(path + file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(file.getBytes());
        fout.close();
        return true;
    }
}
