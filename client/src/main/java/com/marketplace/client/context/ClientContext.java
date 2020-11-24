package com.marketplace.client.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.client.ClassifiedAdRestService;
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

  ObjectMapper objectMapper();

  @Component.Builder
  interface Builder {

    ClientContext build();

    @BindsInstance
    Builder config(ClientConfig config);
  }
}
