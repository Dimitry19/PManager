package cm.travelpost.tp.authentication.ent.service;

import cm.travelpost.tp.authentication.ent.vo.AuthenticationVO;

public interface AuthenticationService {

    public Long save(AuthenticationVO authentication) throws Exception;

    public AuthenticationVO findById(Long id) throws Exception;
}
