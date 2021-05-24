package com.marketplace.domain.classifiedad.service;

import com.marketplace.cqrs.command.CommandHandlerResult;
import com.marketplace.cqrs.command.ImmutableCommandHandlerResult;
import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.command.*;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.controller.*;
import com.marketplace.domain.shared.UserId;
import com.marketplace.cqrs.framework.Strings;

import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import java.util.Optional;

public class ClassifiedAdService {

    private final AggregateStoreRepository aggregateStoreRepository;

    @Inject
    public ClassifiedAdService(AggregateStoreRepository aggregateStoreRepository) {
        this.aggregateStoreRepository = aggregateStoreRepository;
    }

    public CommandHandlerResult<CreateAdResponse> handle(CreateClassifiedAd command) {
        ClassifiedAdId classifiedAdId = command.getClassifiedAdId()
            .map(ClassifiedAdId::from)
            .orElseGet(ClassifiedAdId::new);

        var ownerId = new UserId(command.getOwnerId());
        var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);
        command.getTitle()
            .ifPresent(title -> classifiedAd.updateTitle(new ClassifiedAdTitle(title)));
        command.getText()
            .ifPresent(text -> classifiedAd.updateText(new ClassifiedAdText(text)));

        var saved = aggregateStoreRepository.add(classifiedAd);
        if (saved.isEmpty()) {
            return ImmutableCommandHandlerResult.<CreateAdResponse>builder()
                .isSuccessful(false)
                .message("failed to save classifiedAd")
                .build();
        }
        var classifiedAdResponse = ImmutableCreateAdResponse.builder()
            .ownerId(command.getOwnerId())
            .classifiedAdId(classifiedAdId.id())
            .build();

        return ImmutableCommandHandlerResult.<CreateAdResponse>builder()
            .result(classifiedAdResponse)
            .isSuccessful(true)
            .build();
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAd command) {
        Optional<UUID> mayBeClassifiedAdId = command.getClassifiedAdId();
        return mayBeClassifiedAdId
            .flatMap(classifiedAdId -> findById(new ClassifiedAdId(classifiedAdId)))
            .flatMap(classifiedAd -> {
                command.getOwnerId().ifPresent(ownerId -> classifiedAd.updateOwner(new UserId(ownerId)));
                command.getTitle().ifPresent(title -> classifiedAd.updateTitle(new ClassifiedAdTitle(title)));
                command.getText().ifPresent(text -> classifiedAd.updateText(new ClassifiedAdText(text)));
                command.getPrice().ifPresent(priceDto -> {
                    var money = new Money(priceDto.getAmount(), priceDto.getCurrencyCode(),
                        new DefaultCurrencyLookup());
                    var price = new Price(money);
                    classifiedAd.updatePrice(price);
                });

                command.getApprovedBy().ifPresent(approver -> classifiedAd.approve(new UserId(approver)));

                return aggregateStoreRepository.add(classifiedAd);
            })
            .map(it -> (ClassifiedAd) it)
            .map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .result(ImmutableUpdateClassifiedAdResponse.builder()
                    .id(classifiedAd.getId().id())
                    .ownerId(classifiedAd.getOwnerId().id())
                    .build())
                .isSuccessful(true)
                .build()
            ).orElse(ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .isSuccessful(false)
                .message("classifiedAd not found to be updated")
                .build());
    }

    public AddPictureResponse handle(AddPictureToClassifiedAd command) {
        return findById(new ClassifiedAdId(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                PictureSize pictureSize = new PictureSize(command.getWidth(), command.getHeight());
                var pictureId = classifiedAd.addPicture(command.getUri(), pictureSize, 0);
                return aggregateStoreRepository.add(classifiedAd)
                    .map(res -> ImmutableAddPictureResponse.builder()
                        .id(pictureId.id())
                        .status(true)
                        .classifiedAdId(res.getId().id())
                        .build());
            }).orElse(ImmutableAddPictureResponse
                .builder()
                .status(false)
                .message("classifiedAd not found to be updated")
                .build()
            );
    }

    public ResizePictureResponse handle(ResizeClassifiedAdPicture command) {
        return findById(new ClassifiedAdId(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                PictureSize pictureSize = new PictureSize(command.getWidth(), command.getHeight());
                var pictureId = classifiedAd.resizePicture(new PictureId(command.getId()), pictureSize);
                return aggregateStoreRepository.add(classifiedAd)
                    .map(res -> ImmutableResizePictureResponse.builder()
                        .classifiedAdId(res.getId().id())
                        .id(pictureId.id())
                        .status(true)
                        .build());

            }).orElseGet(() -> ImmutableResizePictureResponse.builder()
                .status(false)
                .message("classifiedAd not found to be updated")
                .build());
    }

    public Optional<ClassifiedAd> findById(ClassifiedAdId classifiedAdId) {
        return aggregateStoreRepository.load(classifiedAdId)
            .map(it -> (ClassifiedAd) it);
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdOwner command) {
        return findById(new ClassifiedAdId(command.getId()))
            .flatMap(classifiedAd -> {
                if (command.getOwnerId() != null) {
                    classifiedAd.updateOwner(new UserId(command.getOwnerId()));
                }
                return aggregateStoreRepository.add(classifiedAd);
            }).map(it -> (ClassifiedAd) it)
            .map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .result(ImmutableUpdateClassifiedAdResponse.builder()
                    .ownerId(classifiedAd.getOwnerId().id())
                    .id(classifiedAd.getId().id())
                    .build())
                .isSuccessful(true)
                .build()
            ).orElse(ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .isSuccessful(false)
                .message("classifiedAd not found to be updated")
                .build());
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdTitle command) {
        return findById(new ClassifiedAdId(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                if (!Strings.isNullOrEmpty(command.getTitle())) {
                    classifiedAd.updateTitle(new ClassifiedAdTitle(command.getTitle()));
                    return aggregateStoreRepository.add(classifiedAd);
                }
                // TODO - throw illegalArgumentException
                return null;
            })
            .map(it -> (ClassifiedAd) it)
            .map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .result(ImmutableUpdateClassifiedAdResponse.builder()
                    .ownerId(classifiedAd.getOwnerId().id())
                    .id(classifiedAd.getId().id())
                    .build())
                .isSuccessful(true)
                .build()
            ).orElse(ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .isSuccessful(false)
                .message("classifiedAd not found to be updated")
                .build());
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdText command) {
        return findById(new ClassifiedAdId(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                if (!Strings.isNullOrEmpty(command.getText())) {
                    classifiedAd.updateText(new ClassifiedAdText(command.getText()));
                    return aggregateStoreRepository.add(classifiedAd);
                } else {
                    // TODO - throw illegalArgumentException
                    return null;
                }
            })
            .map(it -> (ClassifiedAd) it)
            .map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .result(ImmutableUpdateClassifiedAdResponse.builder()
                    .ownerId(classifiedAd.getOwnerId().id())
                    .id(classifiedAd.getId().id())
                    .build())
                .isSuccessful(true)
                .build()
            ).orElse(ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .isSuccessful(false)
                .message("classifiedAd not found to be updated")
                .build());
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(ApproveClassifiedAd command) {
        return findById(new ClassifiedAdId(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                if (command.getApproverId() != null) {
                    classifiedAd.approve(UserId.from(command.getApproverId()));
                    return aggregateStoreRepository.add(classifiedAd);
                }
                // TODO - throw illegalArgumentException
                return null;
            })
            .map(it -> (ClassifiedAd) it)
            .map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .result(ImmutableUpdateClassifiedAdResponse.builder()
                    .ownerId(classifiedAd.getOwnerId().id())
                    .id(classifiedAd.getId().id())
                    .build())
                .isSuccessful(true)
                .build()
            ).orElse(ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .isSuccessful(false)
                .message("classifiedAd not found to be updated")
                .build());
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(PublishClassifiedAd command) {
        return findById(new ClassifiedAdId(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                classifiedAd.requestToPublish();
                return aggregateStoreRepository.add(classifiedAd);
            })
            .map(it -> (ClassifiedAd) it)
            .map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .result(ImmutableUpdateClassifiedAdResponse.builder()
                    .ownerId(classifiedAd.getOwnerId().id())
                    .id(classifiedAd.getId().id())
                    .build())
                .isSuccessful(true)
                .build()
            ).orElse(ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .isSuccessful(false)
                .message("classifiedAd not found to be updated")
                .build());
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdPrice command) {
        return findById(new ClassifiedAdId(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                Price price = new Price(new Money(command.getAmount(), command.getCurrency()));
                classifiedAd.updatePrice(price);
                return aggregateStoreRepository.add(classifiedAd);
            })
            .map(it -> (ClassifiedAd) it)
            .map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .result(ImmutableUpdateClassifiedAdResponse.builder()
                    .ownerId(classifiedAd.getOwnerId().id())
                    .id(classifiedAd.getId().id())
                    .build())
                .isSuccessful(true)
                .build()
            ).orElse(ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .isSuccessful(false)
                .message("classifiedAd not found to be updated")
                .build());

    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(AddPicturesToClassifiedAd command) {
        return findById(new ClassifiedAdId(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                List<PictureDto> pictures = command.getPictures();
                pictures.forEach(pic -> {
                    var pictureSize = new PictureSize(pic.getWidth(), pic.getHeight());
                    classifiedAd.addPicture(pic.getUri(), pictureSize, 0);
                });
                return aggregateStoreRepository.add(classifiedAd);
            })
            .map(it -> (ClassifiedAd) it)
            .map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .result(ImmutableUpdateClassifiedAdResponse.builder()
                    .ownerId(classifiedAd.getOwnerId().id())
                    .id(classifiedAd.getId().id())
                    .build())
                .isSuccessful(true)
                .build()
            ).orElse(ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
                .isSuccessful(false)
                .message("classifiedAd not found to be updated")
                .build());
    }
}
