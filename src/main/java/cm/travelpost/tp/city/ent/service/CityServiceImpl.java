package cm.travelpost.tp.city.ent.service;


import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.city.ent.dao.CityDAO;
import cm.travelpost.tp.city.ent.vo.CityVO;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.ws.requests.CommonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("cityService")
@Transactional
public class CityServiceImpl implements CityService {

    private static Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    @Autowired
    CityDAO dao;

    /**
     * Permet de verifier que le bean a été instancié et ceci peut etre  fait avec afterPropertiesSet
     * qui est une methode de l'interface InitializingBean (exemple plus bas)
     * La manière recommandée est d’utiliser l’annotation :@PostConstruct
	 * @param cityDTO
	 */
	/*@PostConstruct
	public void init() {
		System.out.println("Announce service starts...." );
	}
	*/
    public CityVO create(CommonDTO cityDTO) throws Exception {

        return dao.create(cityDTO);
    }

    public List<CityVO> autoComplete(String search, boolean caseInsensitive) throws AnnounceException,Exception {
        return dao.autocomplete(search, caseInsensitive);
    }

    public CityVO update(CommonDTO city) throws AnnounceException,Exception {
        return dao.update(city);
    }

    public CityVO update(String id, String name) throws Exception {
        return dao.update(id,name);
    }

    public CityVO city(String id) throws AnnounceException,Exception {
        return dao.city(id);
    }


    public boolean delete(String id) throws Exception {
        return dao.delete(id);
    }

    public List<CityVO> cities(PageBy pageBy) throws AnnounceException,Exception {
        return dao.cities(pageBy);
    }


//    public Page cities(Pageable pageable) throws AnnounceException,Exception {
//        return (Page) dao.cities(pageable.getPageNumber(), pageable.getPageSize());
//    }

    public int count( Object o, PageBy pageBy) throws AnnounceException,Exception {
        return dao.count(o, pageBy);
    }

    public void afterPropertiesSet() throws AnnounceException {
        log.info("Init method after properties are set : ");
    }

    public void destroy() throws Exception {
      log.info("Spring Container is destroy! Customer clean up");
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
