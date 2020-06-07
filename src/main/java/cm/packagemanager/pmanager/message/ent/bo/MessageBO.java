package cm.packagemanager.pmanager.message.ent.bo;

import cm.packagemanager.pmanager.message.ent.vo.MessageIdVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageBO extends JpaRepository<MessageVO, MessageIdVO> {
}
