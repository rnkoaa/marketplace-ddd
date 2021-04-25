package com.marketplace.domain.classifiedad.service;

import com.marketplace.cqrs.command.CommandHandlerResult;
import com.marketplace.cqrs.command.ImmutableCommandHandlerResult;
import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.command.*;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.controller.*;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdCommandRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.cqrs.framework.Strings;

import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import java.util.Optional;

public class ClassifiedAdService {

    private final ClassifiedAdCommandRepository classifiedAdCommandRepository;

    @Inject
    public ClassifiedAdService(ClassifiedAdCommandRepository classifiedAdCommandRepository) {
        this.classifiedAdCommandRepository = classifiedAdCommandRepository;
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

        var saved = classifiedAdCommandRepository.add(classifiedAd);
        if (saved == null) {
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
            .flatMap(classifiedAdId -> classifiedAdCommandRepository.load(new ClassifiedAdId(classifiedAdId)))
            .map(classifiedAd -> {
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

                return classifiedAdCommandRepository.add(classifiedAd);
            }).map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
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

    public AddPictureResponse handle(AddPictureToClassifiedAd addPictureToClassifiedAd) {
        Optional<ClassifiedAd> load = classifiedAdCommandRepository
            .load(new ClassifiedAdId(addPictureToClassifiedAd.getClassifiedAdId()));

        return load.map(classifiedAd -> {

            PictureSize pictureSize = new PictureSize(addPictureToClassifiedAd.getWidth(),
                addPictureToClassifiedAd.getHeight());
            var pictureId = classifiedAd.addPicture(addPictureToClassifiedAd.getUri(), pictureSize, 0);

            var savedClassifiedAd = classifiedAdCommandRepository.add(classifiedAd);

            return ImmutableAddPictureResponse.builder()
                .id(pictureId.id())
                .status(true)
                .classifiedAdId(savedClassifiedAd.getId().id())
                .build();
        }).orElse(ImmutableAddPictureResponse
            .builder()
            .status(false)
            .message("classifiedAd not found to be updated")
            .build()
        );
    }

    public ResizePictureResponse handle(ResizeClassifiedAdPicture pictureDto) {
        Optional<ClassifiedAd> load = classifiedAdCommandRepository
            .load(new ClassifiedAdId(pictureDto.getClassifiedAdId()));

        return load.map(classifiedAd -> {
            PictureSize pictureSize = new PictureSize(pictureDto.getWidth(), pictureDto.getHeight());
            var pictureId = classifiedAd.resizePicture(new PictureId(pictureDto.getId()), pictureSize);
            var savedClassifiedAd = classifiedAdCommandRepository.add(classifiedAd);
            return ImmutableResizePictureResponse.builder()
                .classifiedAdId(savedClassifiedAd.getId().id())
                .id(pictureId.id())
                .status(true)
                .build();
        }).orElseGet(() -> ImmutableResizePictureResponse.builder()
            .status(false)
            .message("classifiedAd not found to be updated")
            .build());
    }

    public Optional<ClassifiedAd> findById(ClassifiedAdId classifiedAdId) {
        return classifiedAdCommandRepository.load(classifiedAdId);
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdOwner command) {
        Optional<ClassifiedAd> mayBe = classifiedAdCommandRepository.load(new ClassifiedAdId(command.getId()));
        return mayBe.map(classifiedAd -> {
            if (command.getOwnerId() != null) {
                classifiedAd.updateOwner(new UserId(command.getOwnerId()));
            }
            return classifiedAdCommandRepository.add(classifiedAd);
        }).map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
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
        Optional<ClassifiedAd> mayBe = classifiedAdCommandRepository
            .load(new ClassifiedAdId(command.getClassifiedAdId()));
        return mayBe.map(classifiedAd -> {
            if (!Strings.isNullOrEmpty(command.getTitle())) {
                classifiedAd.updateTitle(new ClassifiedAdTitle(command.getTitle()));
                return classifiedAdCommandRepository.add(classifiedAd);
            }
            // TODO - throw illegalArgumentException
            return null;
        }).map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
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
        Optional<ClassifiedAd> mayBe = classifiedAdCommandRepository
            .load(new ClassifiedAdId(command.getClassifiedAdId()));
        return mayBe.map(classifiedAd -> {
            if (!Strings.isNullOrEmpty(command.getText())) {
                classifiedAd.updateText(new ClassifiedAdText(command.getText()));
                return classifiedAdCommandRepository.add(classifiedAd);
            } else {
                // TODO - throw illegalArgumentException
                return null;
            }
        }).map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
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
        Optional<ClassifiedAd> mayBe = classifiedAdCommandRepository
            .load(new ClassifiedAdId(command.getClassifiedAdId()));
        return mayBe.map(classifiedAd -> {
            if (command.getApproverId() != null) {
                classifiedAd.approve(UserId.from(command.getApproverId()));
                return classifiedAdCommandRepository.add(classifiedAd);
            }
            // TODO - throw illegalArgumentException
            return null;
        }).map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
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
        Optional<ClassifiedAd> mayBe = classifiedAdCommandRepository
            .load(new ClassifiedAdId(command.getClassifiedAdId()));
        return mayBe.map(classifiedAd -> {
            classifiedAd.requestToPublish();
            return classifiedAdCommandRepository.add(classifiedAd);
        }).map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
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
        Optional<ClassifiedAd> mayBe = classifiedAdCommandRepository
            .load(new ClassifiedAdId(command.getClassifiedAdId()));
        return mayBe.map(classifiedAd -> {
            Price price = new Price(new Money(command.getAmount(), command.getCurrency()));
            classifiedAd.updatePrice(price);
            return classifiedAdCommandRepository.add(classifiedAd);
        }).map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
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
        Optional<ClassifiedAd> mayBe = classifiedAdCommandRepository
            .load(new ClassifiedAdId(command.getClassifiedAdId()));
        return mayBe.map(classifiedAd -> {
//      Price price = new Price(new Money(command.getAmount(), command.getCurrency()));
            List<PictureDto> pictures = command.getPictures();
            pictures.forEach(pic -> {
                var pictureSize = new PictureSize(pic.getWidth(), pic.getHeight());
                classifiedAd.addPicture(pic.getUri(), pictureSize, 0);
            });
            return classifiedAdCommandRepository.add(classifiedAd);
        }).map(classifiedAd -> ImmutableCommandHandlerResult.<UpdateClassifiedAdResponse>builder()
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
