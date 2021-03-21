package cm.packagemanager.pmanager.common.ent.service;

import cm.packagemanager.pmanager.announce.ent.dao.AnnounceDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;

import cm.packagemanager.pmanager.common.ent.dao.ImageDAO;
import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
import cm.packagemanager.pmanager.common.enums.UploadImageType;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cm.packagemanager.pmanager.common.Constants.ANNOUNCE_TYPE_IMG_UPLOAD;
import static cm.packagemanager.pmanager.common.Constants.USER_TYPE_IMG_UPLOAD;

@Service
public class ImageServiceImpl implements ImageService{

	protected final Log logger = LogFactory.getLog(ImageServiceImpl.class);

	@Autowired
	ImageDAO imageDAO;

	@Autowired
	UserDAO userDAO;

	@Autowired
	AnnounceDAO announceDAO;


	@Override
	public ImageVO findByName(String name) throws Exception {
		logger.info("find image by name ");
		return imageDAO.findByName(name);
	}

	@Override
	@Transactional
	public void delete(String name, Long id) throws Exception {
		logger.info("delete image by name ");
		 ImageVO image=imageDAO.findByName(name);
		 if (image==null){
		 	image= (ImageVO) imageDAO.findById(ImageVO.class,id);
		 }
		 if (image==null) throw new Exception("Image not found");

		 imageDAO.delete(ImageVO.class,image.getId(),true);
	}

	@Override
	public void save(ImageVO image) throws Exception {
		logger.info("save image");
		imageDAO.save(image);
	}

	@Override
	public void save(ImageVO image, Long id, UploadImageType type) throws Exception {
		switch (type.name()){
			case USER_TYPE_IMG_UPLOAD:

				logger.info("save user image");
				UserVO user=userDAO.findById(id);
				user.setImage(image);
				userDAO.update(user);
				logger.info("save user image end");
				break;

			case ANNOUNCE_TYPE_IMG_UPLOAD:
				logger.info("save announce image");
				AnnounceVO announce=announceDAO.findById(id);
				announce.setImage(image);
				announceDAO.update(announce);
				logger.info("save announce image end");
				break;
			default:
				break;
		}
	}
}
