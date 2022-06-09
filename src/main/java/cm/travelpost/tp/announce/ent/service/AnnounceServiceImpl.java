package cm.travelpost.tp.announce.ent.service;


import cm.framework.ds.common.ent.vo.PageBy;
import cm.travelpost.tp.announce.ent.dao.AnnounceDAO;
import cm.travelpost.tp.announce.ent.vo.AnnounceCompletedVO;
import cm.travelpost.tp.announce.ent.vo.AnnounceMasterVO;
import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.exception.AnnounceException;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.user.ent.dao.UserDAO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.announces.AnnounceDTO;
import cm.travelpost.tp.ws.requests.announces.AnnounceSearchDTO;
import cm.travelpost.tp.ws.requests.announces.UpdateAnnounceDTO;
import cm.travelpost.tp.ws.requests.users.UsersAnnounceFavoriteDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service("announceService")
@Transactional
public class AnnounceServiceImpl implements AnnounceService {

    protected final Log logger = LogFactory.getLog(AnnounceServiceImpl.class);

    @Autowired
    AnnounceDAO dao;


    @Autowired
    private UserDAO userDAO;


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
    public AnnounceMasterVO create(AnnounceDTO announceDTO) throws AnnounceException,Exception {

        return dao.create(announceDTO);
    }

    public List<AnnounceVO> search(AnnounceSearchDTO asdto, PageBy pageBy) throws AnnounceException,Exception {
        return dao.search(asdto, pageBy);
    }

    public AnnounceVO update(UpdateAnnounceDTO dto) throws AnnounceException,Exception {
        return dao.update(dto);
    }

    public AnnounceVO update(Integer id) throws AnnounceException {
        return dao.update(id);
    }

    public AnnounceVO announce(Long id) throws AnnounceException,Exception {
        return dao.announce(id);
    }


    public AnnounceCompletedVO announceCompleted(Long id) throws AnnounceException,Exception {
        return dao.announceCompleted(id);
    }

    public List<AnnounceVO> announcesByUser(Long userId, PageBy pageBy) throws AnnounceException,Exception {

        return dao.announcesByUser(userId, pageBy);
    }

    public List<?> announcesByUser(Long userId, StatusEnum status, PageBy pageBy) throws AnnounceException,Exception {

        return dao.announcesByUser(userId, status,pageBy);
    }

    public List<AnnounceVO> announcesBy(Object o ,  PageBy pageBy) throws AnnounceException,Exception {

        return dao.announcesBy(o, pageBy);
    }

    public boolean delete(Long id) throws Exception {
        return dao.delete(id);
    }

    public List<AnnounceVO> announces(PageBy pageBy) throws AnnounceException,Exception {
        return dao.announces(pageBy);
    }


    public Page announces(Pageable pageable) throws AnnounceException,Exception {
        return (Page) dao.announces(pageable.getPageNumber(), pageable.getPageSize());
    }

    public int count( Object o, PageBy pageBy) throws AnnounceException,Exception {
        return dao.count(o, pageBy);
    }

    public int count(Object o, StatusEnum status,PageBy pageBy) throws AnnounceException,Exception {
        return dao.count(o, status,pageBy);
    }

    public void afterPropertiesSet() throws AnnounceException {
       logger.info("Init method after properties are set : ");
    }

    public void destroy() throws Exception {
        logger.info("Spring Container is destroy! Customer clean up");
    }

    @Override
    public boolean addAnnounceFavorites(UsersAnnounceFavoriteDTO userAnnounceFavoriteDTO) throws UserException {

        long idUser = userAnnounceFavoriteDTO.getIdUser();
        long idAnnounce = userAnnounceFavoriteDTO.getIdAnnounce();

        try {
            UserVO userVO= userDAO.findById(idUser);
            if (userVO != null) {
                AnnounceVO announceVO = dao.announce(idAnnounce);

                if(!userVO.getListAnnounceFavorites().contains(announceVO))
                {
                    userVO.getListAnnounceFavorites().add(announceVO);
                    userDAO.merge(userVO);
                    return true;
                }
            }
        } catch (UserException e) {
            logger.error("Erreur pour recuperer l'utilisateur "+ idUser);
            throw new UserException("Erreur pour recuperer l'utilisateur "+ idUser);
        }
        catch (Exception e) {
            logger.error("Erreur pour recuperer l'Annonce "+ idAnnounce);
            throw new UserException("Erreur pour recuperer l'Annonce "+ idAnnounce);

        }
        return false;
    }

    @Override
    public boolean removeAnnounceFavorites(UsersAnnounceFavoriteDTO userAnnounceFavoriteDTO) throws UserException {

        long idUser = userAnnounceFavoriteDTO.getIdUser();
        long idAnnounce = userAnnounceFavoriteDTO.getIdAnnounce();

        try {
            UserVO userVO = userDAO.findById(idUser);
            if (userVO != null) {
                AnnounceVO announceVO = dao.announce(idAnnounce);
                if(userVO.getListAnnounceFavorites().contains(announceVO))
                {
                    userVO.getListAnnounceFavorites().remove(announceVO);
                    userDAO.merge(userVO);
                    return true;
                }


            }

        }catch (UserException e) {
            logger.error("Erreur pour recuperer l'utilisateur "+ idUser);
            throw new UserException("Erreur pour recuperer l'utilisateur "+ idUser);
        }
        catch (Exception e) {
            logger.error("Erreur pour recuperer l'Annonce "+ idAnnounce);
            throw new UserException("Erreur pour recuperer l'Annonce "+ idAnnounce);

        }
        return false;
    }

    @Override
    public List<AnnounceVO> announcesFavoritesByUser(long idUser) {
        try {
            UserVO userVO = userDAO.findById(idUser);
            if(userVO != null)
            {
                List<AnnounceVO> resultAnnounceFavorite = new ArrayList<AnnounceVO>();
                Set<AnnounceVO> announceList = userVO.getListAnnounceFavorites();
                announceList.forEach(a -> {
                    resultAnnounceFavorite.add(a);
                });

                return resultAnnounceFavorite;
            }
        }catch (UserException e) {
            logger.error("Erreur pour recuperer l'utilisateur "+ idUser);
            throw new UserException("Erreur pour recuperer l'utilisateur "+ idUser);
        }
        return null;
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
