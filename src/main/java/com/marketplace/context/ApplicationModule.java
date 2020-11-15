package com.marketplace.context;

import com.marketplace.domain.classifiedad.repository.ClassifiedAdMongoRepositoryImpl;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract ClassifiedAdRepository bindClassifiedAdRepository(ClassifiedAdMongoRepositoryImpl impl);

}
