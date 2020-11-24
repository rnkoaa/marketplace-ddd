package com.marketplace.context;

import com.marketplace.domain.classifiedad.query.ClassifiedAdMongoQueryRepository;
import com.marketplace.domain.classifiedad.query.ClassifiedAdMongoQueryRepositoryImpl;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdMongoCommandRepositoryImpl;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepository;
import com.marketplace.domain.userprofile.repository.UserProfileRepository;
import com.marketplace.domain.userprofile.repository.UserProfileRepositoryImpl;
import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public abstract class ApplicationModule {

  @Binds
  @Singleton
  abstract ClassifiedAdCommandRepository bindClassifiedAdCommandRepository(
      ClassifiedAdMongoCommandRepositoryImpl impl);

  @Binds
  @Singleton
  abstract ClassifiedAdMongoQueryRepository bindClassifiedAdQueryRepository(
      ClassifiedAdMongoQueryRepositoryImpl impl);

  @Binds
  @Singleton
  abstract UserProfileRepository bindUserProfileRepository(UserProfileRepositoryImpl impl);
}
