package cm.packagemanager.pmanager.user.service;

import cm.packagemanager.pmanager.user.ent.dao.RoleDAO;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import cm.packagemanager.pmanager.ws.requests.users.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

public interface RoleService {

	public RoleVO add(RoleDTO role) throws Exception;
}
