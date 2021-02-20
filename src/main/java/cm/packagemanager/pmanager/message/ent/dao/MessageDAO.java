package cm.packagemanager.pmanager.message.ent.dao;

import cm.packagemanager.pmanager.common.ent.dao.CommonDAO;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;

import java.util.List;

public interface MessageDAO extends CommonDAO {

	public List<MessageVO> messagesByUser(UserVO user) throws BusinessResourceException;
	public List<MessageVO> messagesByUser(Long id) throws BusinessResourceException;
	public List<MessageVO> messages(Long id) throws BusinessResourceException;
}
