package cm.packagemanager.pmanager.user.ent.bo;

import cm.packagemanager.pmanager.common.exception.BusinessResourceException;
import cm.packagemanager.pmanager.user.ent.rep.RoleRP;
import cm.packagemanager.pmanager.user.ent.rep.UserRP;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;



@Service
public class UserBOImpl implements UserBO {

	private static final Logger logger = LoggerFactory.getLogger(UserBOImpl.class);
	private UserRP userRepository;
	private RoleRP roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserBOImpl() {
		super();
	}

	@Autowired
	public UserBOImpl(UserRP userRepository, RoleRP roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public Optional<UserVO> findByUsername(String username) throws BusinessResourceException {

		Optional<UserVO> userFound = userRepository.findByUsername(username);
		if (Boolean.FALSE.equals(userFound.isPresent())) {
			throw new BusinessResourceException("User Not Found", "L'utilisateur avec ce login n'existe pas :" + username);
		}
		return userFound;
	}

	@Override
	public Collection<UserVO> getAllUsers() {
		List<UserVO> users=userRepository.findAll();
		return IteratorUtils.toList(users.iterator());
	}

	@Override
	public UserVO findUserById(Long id) throws  BusinessResourceException{

		Optional<UserVO> userFound = userRepository.findById(id);
		if (Boolean.FALSE.equals(userFound.isPresent())){
			throw new BusinessResourceException("User Not Found", "Aucun utilisateur avec l'identifiant :" + id);
		}
		return userFound.get();
	}

	@Override
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
			UserVO result = userRepository.save(user);
			return  result;
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

	@Override
	@Transactional(readOnly=false)
	public void deleteUser(Long id) throws BusinessResourceException {
		try{
			userRepository.deleteById(id);
		}catch(EmptyResultDataAccessException ex){
			logger.error(String.format("Aucun utilisateur n'existe avec l'identifiant: "+id, ex));
			throw new BusinessResourceException("DeleteUserError", "Erreur de suppression de l'utilisateur avec l'identifiant: "+id, HttpStatus.NOT_FOUND);
		}catch(Exception ex){
			throw new BusinessResourceException("DeleteUserError", "Erreur de suppression de l'utilisateur avec l'identifiant: "+id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Optional<UserVO> findByUsernameAndPassword(String username, String password) throws BusinessResourceException{
		try {
			Optional<UserVO> userFound = this.findByUsername(username);
			if(bCryptPasswordEncoder.matches(password, userFound.get().getPassword())) {
				return userFound;
			} else {
				throw new BusinessResourceException("UserNotFound", "Mot de passe incorrect", HttpStatus.NOT_FOUND);
			}
		} catch (BusinessResourceException ex) {
			logger.error("Login ou mot de passe incorrect", ex);
			throw new BusinessResourceException("UserNotFound", "Login ou mot de passe incorrect", HttpStatus.NOT_FOUND);
		}catch (Exception ex) {
			logger.error("Une erreur technique est survenue", ex);
			throw new BusinessResourceException("TechnicalError", "Une erreur technique est survenue", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void addUserRole(UserVO user) {
		Set<RoleVO> roles= new HashSet<>();
		RoleVO roleUser = new RoleVO("ROLE_USER");//initialisation du rôle ROLE_USER
		roles.add(roleUser);
		user.setActive(0);

		Set<RoleVO> roleFromDB = extractRole_Java8(roles, roleRepository.getAllRolesStream());
		user.setRoles(roleFromDB);
	}

	private void updateUserRole(UserVO user) {

		Set<RoleVO> roleFromDB = extractRole_Java8(user.getRoles(), roleRepository.getAllRolesStream());
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




}
