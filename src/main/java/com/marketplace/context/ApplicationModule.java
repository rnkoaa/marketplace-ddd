package com.marketplace.context;

import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryRepository;
import com.marketplace.domain.classifiedad.query.ClassifiedAdQueryRepositoryImpl;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepository;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepositoryImpl;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepositoryImpl;
import dagger.Binds;
import dagger.Module;
import javax.inject.Singleton;

@Module
public abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract ClassifiedAdCommandRepository bindClassifiedAdCommandRepository(
        ClassifiedAdCommandRepositoryImpl impl);

    @Binds
    @Singleton
    abstract ClassifiedAdQueryRepository bindClassifiedAdQueryRepository(
        ClassifiedAdQueryRepositoryImpl impl);

    @Binds
    @Singleton
    abstract UserProfileQueryRepository bindUserProfileQueryRepository(UserProfileQueryRepositoryImpl impl);
}
