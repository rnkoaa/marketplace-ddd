package com.marketplace.domain.userprofile.controller;

import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.DisplayName;
import com.marketplace.domain.userprofile.FullName;
import com.marketplace.domain.userprofile.UserProfile;
import com.marketplace.domain.userprofile.entity.UserProfileEntity;
import com.marketplace.domain.userprofile.repository.UserProfileQueryRepository;
import io.vavr.control.Try;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class UserProfileCommandService {

    private final AggregateStoreRepository aggregateStoreRepository;
    private final UserProfileQueryRepository userProfileQueryRepository;

    @Inject
    public UserProfileCommandService(UserProfileQueryRepository userProfileQueryRepository,
        AggregateStoreRepository aggregateStoreRepository
    ) {
        this.userProfileQueryRepository = userProfileQueryRepository;
        this.aggregateStoreRepository = aggregateStoreRepository;
    }

    public Try<CreateUserProfileResult> handle(CreateUserProfileCommand command) {
        var userId = UserId.newId();

        Optional<UserProfileEntity> existingUserProfile = userProfileQueryRepository
            .findByDisplayName(command.getDisplayName());

        if (existingUserProfile.isPresent()) {
            return Try.failure(
                new DuplicateDisplayNameException("display_name '" + command.getDisplayName() + "' already exists"));
        }

        var displayName = new DisplayName(command.getDisplayName());
        UserProfile userProfile = new UserProfile(userId, command.fullName(), displayName);
        return Try.of(() -> aggregateStoreRepository.add(userProfile))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> ImmutableCreateUserProfileResult.builder()
                .id(user.getId().id())
                .build());
    }

    public Try<UpdateUserProfileResult> handle(UpdateUserProfileCommand command) {
        Optional<UserProfile> load = aggregateStoreRepository
            .load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it);
        return Try.ofSupplier(load::get)
            .flatMap(userProfile -> {
                command.getDisplayName()
                    .filter(it -> !it.isEmpty())
                    .ifPresent(displayName -> {
                        userProfile.updateDisplayName(new DisplayName(displayName));
                    });

                command.getPhotoUrl()
                    .filter(it -> !it.isEmpty())
                    .ifPresent(userProfile::updatePhoto);

                if (command.getFirstName().filter(it -> !it.isEmpty()).isPresent()
                    || command.getLastName().filter(it -> !it.isEmpty()).isPresent()
                    || command.getMiddleName().filter(it -> !it.isEmpty()).isPresent()) {
                    String firstName = command.getFirstName()
                        .filter(it -> !it.isEmpty())
                        .orElseGet(() -> userProfile.getFullName().firstName());
                    String lastName = command.getLastName()
                        .filter(it -> !it.isEmpty())
                        .orElseGet(() -> userProfile.getFullName().lastName());
                    String middleName = command.getMiddleName()
                        .filter(it -> !it.isEmpty())
                        .orElseGet(() -> userProfile.getFullName().middleName());

                    var fullName = new FullName(firstName, middleName, lastName);
                    userProfile.updateUserFullName(fullName);
                }
                return doTryUpdates(userProfile);
            });
    }

    public Try<UpdateUserProfileResult> handle(UpdateUserFullNameCommand command) {
        Optional<UserProfile> load = aggregateStoreRepository
            .load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it);
        return Try.ofSupplier(load::get)
            .flatMap(userProfile -> {
                userProfile.updateUserFullName(
                    new FullName(command.getFirstName(),
                        command.getMiddleName().orElse(""),
                        command.getLastName())
                );
                return doTryUpdates(userProfile);
            });
    }

    public Try<UpdateUserProfileResult> handle(UpdateUserDisplayNameCommand command) {
        Optional<UserProfile> loadedUserProfile = aggregateStoreRepository
            .load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it);

        return Try.ofSupplier(loadedUserProfile::get)
            .flatMap(userProfile -> {
                userProfile.updateDisplayName(new DisplayName(command.getDisplayName()));
                return doTryUpdates(userProfile);
            })
            ;
    }

    private Try<UpdateUserProfileResult> doTryUpdates(UserProfile userProfile) {
        return Try.of(() -> aggregateStoreRepository.add(userProfile))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> ImmutableUpdateUserProfileResult.builder()
                .id(userProfile.getId().id())
                .build());
    }

    public Try<LoadUserProfileResponse> handle(LoadUserProfileCommand command) {
        Optional<LoadUserProfileResponse> loadedUserProfile = aggregateStoreRepository
            .load(UserId.from(command.getUserId()))
            .map(it -> (UserProfile) it)
            .map(userProfile -> ImmutableLoadUserProfileResponse.builder()
                .userId(userProfile.getId().id())
                .firstName(userProfile.getFullName().firstName())
                .lastName(userProfile.getFullName().lastName())
                .middleName(userProfile.getFullName().middleName() != null ? userProfile.getFullName().middleName() : "")
                .photoUrl(userProfile.getPhotoUrl() != null ? userProfile.getPhotoUrl() : "")
                .displayName(userProfile.getDisplayName().toString())
                .build());

        return Try.of(() -> loadedUserProfile
            .orElseThrow(() -> new NotFoundException("user with id '" + command.getUserId() + "' not found"))
        );
    }
}


