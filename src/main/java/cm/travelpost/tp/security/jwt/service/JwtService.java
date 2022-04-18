package cm.travelpost.tp.security.jwt.service;


import cm.travelpost.tp.configuration.filters.CommonFilter;
import cm.travelpost.tp.user.ent.service.UserService;
import cm.travelpost.tp.user.ent.vo.UserVO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtService implements UserDetailsService {


    @Autowired
    UserService userService;


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserVO user=userService.findByUsername(s, Boolean.FALSE);
        return new User(user.getUsername(), "", CommonFilter.getRolesAuthoritiesUser(user));

    }


}
