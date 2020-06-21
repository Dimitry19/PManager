package cm.packagemanager.pmanager.user.ent.bo;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.dao.RoleDAO;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class UserBOImpl {

	private static final Logger logger = LoggerFactory.getLogger(UserBOImpl.class);
/*

	@Autowired
	private UserDAO userDAO;


	@Autowired
	private RoleDAO roleDAO;


	public UserBOImpl() {
		super();
	}



	public Collection<UserVO> getAllUsers() {
		List<UserVO> users=userDAO.getAllUsers();
		return IteratorUtils.toList(users.iterator());
	}


	public UserVO findUserById(Long id) throws BusinessResourceException {
		return  userDAO.getUser(id.intValue());

	}



	@Transactional(readOnly=false)
	public UserVO saveOrUpdateUser(UserVO user) throws BusinessResourceException {
		try{
			if(null ==user.getId()) {//pas d'Id --> création d'un user
				addUserRole(user);//Ajout d'un rôle par défaut
				user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			} else {//sinon, mise à jour d'un user

				Optional<UserVO> userFromDB = Optional.ofNullable(findUserById(user.getId()));
				if(! bCryptPasswordEncoder.matches(user.getPassword(), userFromDB.get().getPassword())) {
					user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));//MAJ du mot de passe s'il a été modifié
				} else {

					user.setPassword(userFromDB.get().getPassword());//Sinon, on remet le password déjà haché
				}
				updateUserRole(user);//On extrait le rôle en cas de mise à jour
			}
			UserVO usr = userDAO.save(user);
			return  usr;
		} catch(DataIntegrityViolationException ex){
			logger.error("Utilisateur non existant", ex);
			throw new BusinessResourceException("DuplicateValueError", "Un utilisateur existe déjà avec le compte : "+user.getUsername(), HttpStatus.CONFLICT);
		} catch (BusinessResourceException e) {
			logger.error("Utilisateur non existant", e);
			throw new BusinessResourceException("UserNotFound", "Aucun utilisateur avec l'identifiant: "+user.getId(), HttpStatus.NOT_FOUND);
		} catch(Exception ex){
			logger.error("Erreur technique de création ou de mise à jour de l'utilisateur", ex);
			throw new BusinessResourceException("SaveOrUpdateUserError", "Erreur technique de création ou de mise à jour de l'utilisateur: "+user.getUsername(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Transactional(readOnly=false)
	public void deleteUser(Long id) throws BusinessResourceException {
		try{
			//userDAO.deleteById(id);
		}catch(EmptyResultDataAccessException ex){
			logger.error(String.format("Aucun utilisateur n'existe avec l'identifiant: "+id, ex));
			throw new BusinessResourceException("DeleteUserError", "Erreur de suppression de l'utilisateur avec l'identifiant: "+id, HttpStatus.NOT_FOUND);
		}catch(Exception ex){
			throw new BusinessResourceException("DeleteUserError", "Erreur de suppression de l'utilisateur avec l'identifiant: "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	private void addUserRole(UserVO user) {
		Set<RoleVO> roles= new HashSet<>();
		RoleVO roleUser = new RoleVO("ROLE_USER");//initialisation du rôle ROLE_USER
		roles.add(roleUser);
		user.setActive(0);

		//Set<RoleVO> roleFromDB = extractRole_Java8(roles, roleDAO.getAllRolesStream());
		Set<RoleVO> roleFromDB = extractRole_Java8(roles, null);
		user.setRoles(roleFromDB);
	}

	private void updateUserRole(UserVO user) {

		//Set<RoleVO> roleFromDB = extractRole_Java8(user.getRoles(), roleDAO.getAllRolesStream());
		Set<RoleVO> roleFromDB = extractRole_Java8(user.getRoles(), null);
		user.setRoles(roleFromDB);
	}

	private Set<RoleVO> extractRole_Java8(Set<RoleVO> rolesSetFromUser, Stream<RoleVO> roleStreamFromDB) {
		// Collect UI role names
		Set<String> uiRoleNames = rolesSetFromUser.stream()
				.map(RoleVO::getDescription)
				.collect(Collectors.toCollection(HashSet::new));
		// Filter DB roles
		return roleStreamFromDB
				.filter(role -> uiRoleNames.contains(role.getDescription()))
				.collect(Collectors.toSet());
	}

	@SuppressWarnings("unused")
	private Set<RoleVO> extractRoleUsingCompareTo_Java8(Set<RoleVO> rolesSetFromUser, Stream<RoleVO> roleStreamFromDB) {
		return roleStreamFromDB
				.filter(roleFromDB -> rolesSetFromUser.stream()
						.anyMatch( roleFromUser -> roleFromUser.compareTo(roleFromDB) == 0))
				.collect(Collectors.toCollection(HashSet::new));
	}

	@SuppressWarnings("unused")
	private Set<RoleVO>  extractRole_BeforeJava8(Set<RoleVO> rolesSetFromUser, Collection<RoleVO> rolesFromDB) {
		Set<RoleVO> rolesToAdd = new HashSet<>();
		for(RoleVO roleFromUser:rolesSetFromUser){
			for(RoleVO roleFromDB:rolesFromDB){
				if(roleFromDB.compareTo(roleFromUser)==0){
					rolesToAdd.add(roleFromDB);
					break;
				}
			}
		}
		return rolesToAdd;
	}

*/

}
