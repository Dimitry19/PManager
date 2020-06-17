package cm.packagemanager.pmanager.user.ent.bo;

import cm.packagemanager.pmanager.user.ent.rep.RoleRP;
import cm.packagemanager.pmanager.user.ent.vo.RoleVO;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Stream;

@Service
public class RoleBOImpl implements RoleBO {
	private RoleRP roleRepository;

	public RoleBOImpl() {
		super();
	}

	@Autowired //autowired par constructeur pour bénéficier des tests unitaires
	public RoleBOImpl(RoleRP roleRepository) {
		super();
		this.roleRepository = roleRepository;
	}

	@Override
	public Collection<RoleVO> getAllRoles() {
		return IteratorUtils.toList(roleRepository.findAll().iterator());
	}

	@Override
	public Stream<RoleVO> getAllRolesStream() {
		return roleRepository.getAllRolesStream();
	}

	@Override
	public RoleVO findByDescription(String description) {
		return roleRepository.findByDescription(description);
	}
}
