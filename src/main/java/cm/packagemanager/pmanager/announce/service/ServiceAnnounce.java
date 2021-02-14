package cm.packagemanager.pmanager.announce.service;

import cm.packagemanager.pmanager.announce.ent.dao.AnnounceRep;
import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;

@Service
public class ServiceAnnounce {

	//@Autowired
	AnnounceRep movieRepository;

	// Save movies in the database.
	public void save(final AnnounceVO movie) {
		//movieRepository.save(movie);
	}

	// Fetch all movies from the database.
	public Page<AnnounceVO> getAllMovies(final Pageable pageable) {

		return null;//movieRepository.findAll(pageable);
	}

}
