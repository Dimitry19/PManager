package cm.packagemanager.pmanager.announce.ent.service;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServiceAnnounce {

    //@Autowired
    //AnnounceRep movieRepository;

    // Save movies in the database.
    public void save(final AnnounceVO movie) {
        //movieRepository.save(movie);
    }

    // Fetch all movies from the database.
    public Page<AnnounceVO> getAllMovies(final Pageable pageable) {

        return null;//movieRepository.findAll(pageable);
    }

}
