package cm.packagemanager.pmanager.message.ent.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService{
	private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);
}
