package cm.packagemanager.pmanager.message.ent.dao;

import cm.packagemanager.pmanager.announce.ent.dao.AnnounceDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.ent.vo.CommonFilter;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.RecordNotFoundException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.utils.BigDecimalUtils;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.common.utils.QueryUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.messages.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.messages.UpdateMessageDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public class MessageDAOImpl extends CommonFilter implements MessageDAO {

	private static Logger logger = LoggerFactory.getLogger(MessageDAOImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	QueryUtils queryUtils;

	@Autowired
	UserDAO userDAO;

	@Autowired
	AnnounceDAO announceDAO;


	@Override
	public int count(PageBy pageBy) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query = session.createQuery("from MessageVO ");
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());

		int count = CollectionsUtils.isNotEmpty(query.list())?query.list().size():0;
		return count;
	}

	@Override
	public MessageVO update(UpdateMessageDTO mdto) throws BusinessResourceException, UserException {
		UserVO user = userDAO.findByUsername(mdto.getUsername());

		if(user==null){
			throw new RecordNotFoundException("Aucun utilisateur trouvé");
		}

		AnnounceVO announce = announceDAO.findById(mdto.getAnnounceId());
		if(announce==null){
			throw new RecordNotFoundException("Aucune annonce  trouvée");
		}

		MessageVO message =findById(new MessageIdVO(mdto.getId(), announce.getAnnounceId().getToken()));
		if(message==null){
			throw new RecordNotFoundException("Aucune message  trouvé");
		}

		setMessage(announce,user,message,mdto);
		Session session = sessionFactory.getCurrentSession();
		session.update(message);
		return message;
	}

	@Override
	public MessageVO addMessage(MessageDTO mdto) throws BusinessResourceException {

		UserVO user = userDAO.findByOnlyUsername(mdto.getUsername(),false);
		AnnounceVO announce=announceDAO.findById(mdto.getAnnounceId());

		if(user!=null && announce!=null){

			Long id= queryUtils.calcolateId(MessageVO.GET_ID_SQL);

			MessageIdVO messageId=new MessageIdVO(id,announce.getAnnounceId().getToken());

			MessageVO message =new MessageVO();
			setMessage(announce,user,message,mdto);
			message.setId(messageId);
			announce.addMessages(message);

			Session session=sessionFactory.getCurrentSession();
			session.save(message);
			return message;
		}
		return null;
	}

	@Override
	public List<MessageVO> messagesByUser(UserVO user, PageBy pageBy) throws BusinessResourceException, UserException {

		if (user==null) return null;
		List<MessageVO>messages=messagesByUser(user.getId(),pageBy);
		return  messages;
	}

	@Override
	public List<MessageVO> messagesByUser(Long id,PageBy pageBy) throws BusinessResourceException, UserException {

		if (id==null) return null;
		UserVO user=userDAO.findById(id);
		Session session=sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createQuery("from MessageVO  m where user.id=:userId");
		query.setParameter("userId",user.getId());
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());
		List<MessageVO>messages=query.getResultList();
		return  messages;
	}

	@Override
	public List<MessageVO> messages(PageBy pageBy) throws BusinessResourceException {

		Session session=sessionFactory.getCurrentSession();
		session.enableFilter(FilterConstants.CANCELLED);
		Query query=session.createNamedQuery(MessageVO.FINDALL);
		query.setFirstResult(pageBy.getPage());
		query.setMaxResults(pageBy.getSize());

		List<MessageVO>messages=query.getResultList();
		return  messages;
	}

	@Override
	public MessageVO findById(MessageIdVO id) throws BusinessResourceException {

		Session session = this.sessionFactory.getCurrentSession();
		MessageVO message=(MessageVO) manualFilter(session.find(MessageVO.class,id));
		return message;
	}


	@Override
	public boolean delete(Long id) throws BusinessResourceException {
		deleteObject(id);
		return  true;
	}

	@Override
	public void deleteObject(Object object) throws RecordNotFoundException {
		MessageIdVO id = (MessageIdVO)object;
		try{
			Session session=sessionFactory.getCurrentSession();

			MessageVO message=findById(id);
			if(message!=null){
				session.remove(message);
				session.flush();
			}
		}catch (Exception e){
			throw new BusinessResourceException(e.getMessage());
		}

	}
	private void setMessage(AnnounceVO announce, UserVO user,MessageVO message, MessageDTO mdto){
		message.setAnnounce(announce);
		message.setContent(mdto.getContent());
		message.setCancelled(false);
		message.setUser(user);
	}
	@Override
	public String composeQuery(Object o, String alias) {
		return null;
	}

	@Override
	public void composeQueryParameters(Object o, Query query) {

	}
}
