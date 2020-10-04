package cm.packagemanager.pmanager.announce.service;


import cm.packagemanager.pmanager.announce.ent.dao.AnnounceDAO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.ws.requests.AnnounceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("announceService")
public class AnnounceService {

	@Autowired
	AnnounceDAO announceDAO;


	@Transactional
	public AnnounceVO create(AnnounceDTO announceDTO ) throws Exception {

		return announceDAO.create(announceDTO);
	}


	@Transactional
	public AnnounceVO update(AnnounceVO announce) throws Exception {
		return announceDAO.update(announce);
	}

	@Transactional
	public AnnounceVO update(Integer id) throws Exception {
		return announceDAO.update(id);
	}


	@Transactional
	public List<AnnounceVO> announces(int page, int size) {
		return announceDAO.announces(page, size);
	}

	@Transactional
	public List<AnnounceVO> announces() {
		return announceDAO.announces();
	}
}