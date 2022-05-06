package cm.framework.ds.common.security;

import org.jasypt.encryption.StringEncryptor;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;


@ManagedBean
public class CommonSecurityResource {


    @Resource(name ="jasyptStringEncryptor")
    protected StringEncryptor encryptorBean;
}
