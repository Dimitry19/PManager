package cm.packagemanager.pmanager.announce.ent.service;


import cm.packagemanager.pmanager.announce.ent.dao.AnnounceDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("announceService")
@Transactional
public class AnnounceServiceImpl implements AnnounceService {

    @Autowired
    AnnounceDAO announceDAO;

    /**
     * Permet de verifier que le bean a été instancié et ceci peut etre  fait avec afterPropertiesSet
     * qui est une methode de l'interface InitializingBean (exemple plus bas)
     * La manière recommandée est d’utiliser l’annotation :@PostConstruct
     */
	/*@PostConstruct
	public void init() {
		System.out.println("Announce service starts...." );
	}
	*/
    public AnnounceVO create(AnnounceDTO announceDTO) throws Exception {

        return announceDAO.create(announceDTO);
    }

    public List<AnnounceVO> find(AnnounceSearchDTO asdto, PageBy pageBy) throws Exception {
        return announceDAO.find(asdto, pageBy);
    }

    public AnnounceVO update(UpdateAnnounceDTO announce) throws Exception {
        return announceDAO.update(announce);
    }

    public AnnounceVO update(Integer id) throws Exception {
        return announceDAO.update(id);
    }

    public AnnounceVO announce(Long id) throws Exception {
        return announceDAO.announce(id);
    }

    public List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws Exception {

        return announceDAO.announcesByUser(userId, pageBy);
    }

    public List<AnnounceVO> announcesByType(AnnounceType type, PageBy pageBy) throws Exception {

        return announceDAO.announcesByType(type, pageBy);
    }

    public boolean delete(Long id) throws Exception {
        return announceDAO.delete(id);
    }

    public List<AnnounceVO> announces(PageBy pageBy) throws Exception {
        return announceDAO.announces(pageBy);
    }


    public Page announces(Pageable pageable) throws Exception {
        return (Page) announceDAO.announces(pageable.getPageNumber(), pageable.getPageSize());
    }

    public int count(AnnounceSearchDTO announceSearch, PageBy pageBy) throws Exception {
        return announceDAO.count(announceSearch, pageBy);
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("Init method after properties are set : ");
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
