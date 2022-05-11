package cm.framework.ds.common.security;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;


@ManagedBean
public class CommonSecurityResource {


    @Resource(name ="jasyptStringEncryptor")
    protected StringEncryptor encryptorBean;

    @Value("${jwt.expirationDateInMs}")
    protected int jwtExpirationInMs;

    @Value("${custom.api.auth.http.tokenValue}")
    protected String token;
}
