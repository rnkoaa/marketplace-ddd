package com.marketplace.context;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.context.server.SparkServerModule;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryRepository;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepository;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import com.marketplace.domain.userprofile.repository.UserProfileCommandRepository;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import com.marketplace.server.SparkServer;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;
import org.jooq.DSLContext;

@Component(modules = {
    SparkServerModule.class,
    ApplicationModule.class,
    JooqModule.class,
    ObjectMapperModule.class
})
@Singleton
public interface ApplicationContext {

    SparkServer getServer();

    ClassifiedAdCommandRepository getClassifiedAdRepository();

    UserProfileQueryRepository getUserProfileQueryRepository();

    UserProfileCommandRepository getUserProfileCommandRepository();

    ClassifiedAdService getClassifiedAdService();

    DSLContext getDSLContext();

    ClassifiedAdQueryRepository getClassifiedAdQueryRepository();

    @Component.Builder
    interface Builder {

        ApplicationContext build();

        @BindsInstance
        Builder config(ApplicationConfig config);
    }
}
