package cm.packagemanager.pmanager.message.ent.dao;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDAOImpl implements MessageDAO {

	private static Logger logger = LoggerFactory.getLogger(MessageDAOImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<MessageVO> messagesByUser(UserVO user) throws BusinessResourceException {

		Session session=sessionFactory.getCurrentSession();
		MessageVO message= (MessageVO) session.byId(MessageIdVO.class);
		return null;
	}

	@Override
	public List<MessageVO> messagesByUser(Long id) throws BusinessResourceException {

		return null;
	}

	@Override
	public List<MessageVO> messages(Long id) throws BusinessResourceException {
		return null;
	}

	@Override
	public void deleteObject(Object object) throws RecordNotFoundException {

	}

}
