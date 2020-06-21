package cm.packagemanager.pmanager.user.ent.bo;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


public interface UserBO{

	Collection<UserVO> getAllUsers();

	UserVO findUserById(Long id) throws BusinessResourceException;

	Optional<UserVO> findByUsername(String login) throws BusinessResourceException, UserException;

	UserVO findByCred(String login) throws  UserException;

	UserVO saveOrUpdateUser(UserVO user) throws BusinessResourceException;

	void deleteUser(Long id) throws BusinessResourceException;

	Optional<UserVO> findByUsernameAndPassword(String login, String password) throws BusinessResourceException;

}
