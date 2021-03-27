package cm.packagemanager.pmanager.announce.ent.service;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.PageBy;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceSearchDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface AnnounceService extends InitializingBean {

	public AnnounceVO create(AnnounceDTO announceDTO ) throws Exception;

	public List<AnnounceVO> find(AnnounceSearchDTO announceSearchDTO, PageBy pageBy) throws Exception;

	public AnnounceVO update(UpdateAnnounceDTO announce) throws Exception;

	public AnnounceVO update(Integer id) throws Exception;

	public List<AnnounceVO> findByUser(Long userId, PageBy pageBy) throws Exception;
	public List<AnnounceVO> findByType(AnnounceType type, PageBy pageBy) throws Exception;

	public AnnounceVO findById(Long id) throws UserException;

	public boolean delete(Long id) throws Exception;

	public List<AnnounceVO> announces(PageBy pageBy ) throws Exception;

	public Page announces(Pageable pageable) throws Exception;


	public int  count(AnnounceSearchDTO announceSearch,PageBy pageBy)  throws Exception;

	public void afterPropertiesSet() throws Exception ;

	public void destroy() throws Exception ;
}
