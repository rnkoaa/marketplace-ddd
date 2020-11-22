package com.marketplace.context;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.context.mongo.MongoConfigModule;
import com.marketplace.context.server.SparkServerModule;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.domain.userprofile.repository.UserProfileRepository;
import com.marketplace.server.SparkServer;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Component(modules = {
        SparkServerModule.class,
        ApplicationModule.class,
        MongoConfigModule.class,
        ObjectMapperModule.class
})
@Singleton
public interface ApplicationContext {

    SparkServer getServer();

    ClassifiedAdRepository getClassifiedAdRepository();

    UserProfileRepository getUserProfileRepository();

    @Component.Builder
    interface Builder {
        ApplicationContext build();

        @BindsInstance
        Builder config(ApplicationConfig config);
    }
}
