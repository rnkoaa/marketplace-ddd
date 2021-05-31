package com.marketplace.client.functional;

import com.marketplace.client.userprofile.UserProfileRestService;
import com.marketplace.client.config.ClientConfig;
import com.marketplace.client.config.ImmutableClientConfig;
import com.marketplace.client.context.ClientContext;
import com.marketplace.client.context.DaggerClientContext;
import com.marketplace.config.ConfigLoader;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractFunctionalTest {

    ClientContext clientContext;
    protected UserProfileRestService userProfileRestService;

    @BeforeAll
    void setupAll() throws IOException {
        ClientConfig config = ConfigLoader.loadClasspathResource("application.yml", ImmutableClientConfig.class);
        clientContext = DaggerClientContext.
            builder()
            .config(config)
            .build();

        userProfileRestService = clientContext.getUserProfileRestService();
    }

    public ClientContext getClientContext() {
        return clientContext;
    }
}
