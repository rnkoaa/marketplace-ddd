package com.marketplace.context;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.context.server.SparkServerModule;
import com.marketplace.cqrs.event.VersionedEvent;
import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryRepository;
import com.marketplace.domain.classifiedad.service.ClassifiedAdService;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import com.marketplace.eventstore.framework.event.EventStore;
import com.marketplace.server.SparkServer;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;
import org.jooq.DSLContext;

@Component(modules = {
    SparkServerModule.class,
    ApplicationModule.class,
    JooqModule.class,
    ObjectMapperModule.class,
    EventStoreModule.class,
})
@Singleton
public interface ApplicationContext {

    SparkServer getServer();

    UserProfileQueryRepository getUserProfileQueryRepository();

    AggregateStoreRepository getAggregateRepository();

    ClassifiedAdService getClassifiedAdService();

    DSLContext getDSLContext();

    ClassifiedAdQueryRepository getClassifiedAdQueryRepository();

    EventStore<VersionedEvent> getEventStore();

    @Component.Builder
    interface Builder {

        ApplicationContext build();

        @BindsInstance
        Builder config(ApplicationConfig config);
    }
}
