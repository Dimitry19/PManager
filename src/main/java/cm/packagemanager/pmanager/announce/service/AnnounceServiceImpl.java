package cm.packagemanager.pmanager.announce.service;


import cm.packagemanager.pmanager.announce.ent.dao.AnnounceDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnouncesVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;


@Service("announceService")
public class AnnounceServiceImpl implements AnnounceService {

	@Autowired
	AnnounceDAO announceDAO;

	/**
	 * Permet de verifier que le bean a été instancié et ceci peut etre  fait avec afterPropertiesSet
	 * qui est une methode de l'interface InitializingBean (exemple plus bas)
	 * La manière recommandée est d’utiliser l’annotation :@PostConstruct
	 *
	 */
	/*@PostConstruct
	public void init() {
		System.out.println("Announce service starts...." );
	}
	*/

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnnounceVO create(AnnounceDTO announceDTO ) throws Exception {

		return announceDAO.create(announceDTO);
	}


	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnnounceVO update(UpdateAnnounceDTO announce) throws Exception {
		return announceDAO.update(announce);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public AnnounceVO update(Integer id) throws Exception {
		return announceDAO.update(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delete(Long id) throws Exception {
		return announceDAO.delete(id);
	}


	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public MessageVO addMessage(MessageDTO mdto) throws Exception{
		return announceDAO.addMessage(mdto);
	}

	@Transactional(readOnly = true)
	public List<AnnounceVO> announces(int page, int size ) {
		return announceDAO.announces(page, size);
	}

	@Transactional(readOnly = true)
	public int  count(int page, int size) {
		return announceDAO.count( page,  size);
	}

	public void afterPropertiesSet() throws Exception {
		System.out.println("Init method after properties are set : " );
	}

	public void destroy() throws Exception {
		System.out.println("Spring Container is destroy! Customer clean up");
	}


	/**
	 * Permet de verifier que le bean a été detruit  et ne fonctionne que pour les beans avec scope
	 * different de prototype
	 * La manière recommandée est d’utiliser l’annotation :@PreDestroy
	 *
	 */
	/*@PreDestroy
	public void destroy() {
		System.out.println("Au revoir de TodosServiceImpl" );
	}*/
}
