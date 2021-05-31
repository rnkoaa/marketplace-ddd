package com.marketplace.client.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.client.classifiedad.ClassifiedAdRestService;
import com.marketplace.client.userprofile.UserProfileRestService;
import com.marketplace.client.config.ClientConfig;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

@Component(
    modules = {
        ClientModule.class
    }
)
@Singleton
public interface ClientContext {

    ClassifiedAdRestService classifiedAdRestService();

    UserProfileRestService getUserProfileRestService();

    ObjectMapper objectMapper();

    ObjectMapper yamlObjectMapper();

    @Component.Builder
    interface Builder {

        ClientContext build();

        @BindsInstance
        Builder config(ClientConfig config);
    }
}
