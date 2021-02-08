package cm.packagemanager.pmanager.announce.service;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnouncesVO;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import cm.packagemanager.pmanager.ws.requests.announces.MessageDTO;
import cm.packagemanager.pmanager.ws.requests.announces.UpdateAnnounceDTO;
import org.springframework.beans.factory.InitializingBean;


import java.util.List;

public interface AnnounceService extends InitializingBean {

	public AnnounceVO create(AnnounceDTO announceDTO ) throws Exception;

	public AnnounceVO update(UpdateAnnounceDTO announce) throws Exception;

	public AnnounceVO update(Integer id) throws Exception;

	public boolean delete(Long id) throws Exception;

	public MessageVO addMessage(MessageDTO mdto) throws Exception;

	public List<AnnounceVO> announces(int page, int size ) throws Exception;


	public int  count(int page, int size)  throws Exception;

	public void afterPropertiesSet() throws Exception ;

	public void destroy() throws Exception ;
}
