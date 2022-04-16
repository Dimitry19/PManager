package cm.travelpost.tp.batch.config;

import org.springframework.batch.core.configuration.annotation.SimpleBatchConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

public class SimpleBacthConfiguration extends SimpleBatchConfiguration {

    @Override
    @Bean("simpleBatchTransactionManager")
    public PlatformTransactionManager transactionManager() throws Exception {
        return super.transactionManager();
    }
}
