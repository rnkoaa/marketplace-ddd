package com.marketplace.domain.classifiedad.service;

import com.marketplace.cqrs.framework.Strings;
import com.marketplace.domain.AggregateStoreRepository;
import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.ClassifiedAdText;
import com.marketplace.domain.classifiedad.ClassifiedAdTitle;
import com.marketplace.domain.classifiedad.DefaultCurrencyLookup;
import com.marketplace.domain.classifiedad.Money;
import com.marketplace.domain.classifiedad.Price;
import com.marketplace.domain.classifiedad.command.ApproveClassifiedAd;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.ImmutablePictureDto;
import com.marketplace.domain.classifiedad.command.ImmutablePriceDto;
import com.marketplace.domain.classifiedad.command.PublishClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdOwner;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdPrice;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdText;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdTitle;
import com.marketplace.domain.classifiedad.controller.AddPictureToClassifiedAd;
import com.marketplace.domain.classifiedad.controller.AddPicturesToClassifiedAd;
import com.marketplace.domain.classifiedad.controller.CreateAdResponse;
import com.marketplace.domain.classifiedad.controller.ImmutableCreateAdResponse;
import com.marketplace.domain.classifiedad.controller.ImmutableLoadClassifiedAdResponse;
import com.marketplace.domain.classifiedad.controller.ImmutableLoadClassifiedAdResponse.Builder;
import com.marketplace.domain.classifiedad.controller.ImmutableUpdateClassifiedAdResponse;
import com.marketplace.domain.classifiedad.controller.LoadClassifiedAdCommand;
import com.marketplace.domain.classifiedad.controller.LoadClassifiedAdResponse;
import com.marketplace.domain.classifiedad.controller.ResizeClassifiedAdPicture;
import com.marketplace.domain.classifiedad.controller.UpdateClassifiedAdResponse;
import com.marketplace.domain.shared.UserId;
import com.marketplace.domain.userprofile.controller.NotFoundException;
import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class ClassifiedAdService {

    private final AggregateStoreRepository aggregateStoreRepository;

    @Inject
    public ClassifiedAdService(AggregateStoreRepository aggregateStoreRepository) {
        this.aggregateStoreRepository = aggregateStoreRepository;
    }

    public Try<CreateAdResponse> handle(CreateClassifiedAd command) {
        ClassifiedAdId classifiedAdId = command.getClassifiedAdId()
            .map(ClassifiedAdId::from)
            .orElseGet(ClassifiedAdId::new);

        var ownerId = new UserId(command.getOwnerId());
        var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);
        command.getTitle()
            .ifPresent(title -> classifiedAd.updateTitle(new ClassifiedAdTitle(title)));
        command.getText()
            .ifPresent(text -> classifiedAd.updateText(new ClassifiedAdText(text)));

        return Try.of(() -> aggregateStoreRepository.add(classifiedAd))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> ImmutableCreateAdResponse.builder()
                .ownerId(command.getOwnerId())
                .classifiedAdId(classifiedAdId.id())
                .build());
    }

    public Try<UpdateClassifiedAdResponse> handle(UpdateClassifiedAd command) {
        UUID classifiedAdId = command.getClassifiedAdId()
            .orElseThrow(() -> new IllegalArgumentException("classified id is invalid"));
        return update(classifiedAdId, classifiedAd -> {
            command.getOwnerId().ifPresent(ownerId -> classifiedAd.updateOwner(new UserId(ownerId)));
            command.getTitle().ifPresent(title -> classifiedAd.updateTitle(new ClassifiedAdTitle(title)));
            command.getText().ifPresent(text -> classifiedAd.updateText(new ClassifiedAdText(text)));
            command.getPrice().ifPresent(priceDto -> {
                var money = new Money(priceDto.getAmount(), priceDto.getCurrencyCode(),
                    new DefaultCurrencyLookup());
                var price = new Price(money);
                classifiedAd.updatePrice(price);
            });

            if (command.getPictures() != null) {
                command.getPictures()
                    .forEach(pictureDto -> classifiedAd.addPicture(
                        pictureDto.getUri(),
                        new PictureSize(pictureDto.getWidth(), pictureDto.getHeight()),
                        pictureDto.getOrder()
                        )
                    );
            }

            command.getApprovedBy().ifPresent(approver -> {
                classifiedAd.approve(new UserId(approver));
            });

            // if state is marked as PENDING_REVIEW, this classifiedAd is about to be published
            command.getState()
                .filter(classifiedAdState -> classifiedAdState == ClassifiedAdState.PENDING_REVIEW)
                .ifPresent(res -> classifiedAd.requestToPublish());

            return doTryUpdates(classifiedAd);
        });
    }

    public Try<UpdateClassifiedAdResponse> handle(AddPictureToClassifiedAd command) {
        return update(command.getClassifiedAdId(), classifiedAd -> {
            PictureSize pictureSize = new PictureSize(command.getWidth(), command.getHeight());
            classifiedAd.addPicture(command.getUri(), pictureSize, 0);
            return doTryUpdates(classifiedAd);
        });
    }

    public Try<UpdateClassifiedAdResponse> handle(ResizeClassifiedAdPicture command) {
        return update(command.getClassifiedAdId(), classifiedAd -> {
            PictureSize pictureSize = new PictureSize(command.getWidth(), command.getHeight());
            classifiedAd.resizePicture(new PictureId(command.getId()), pictureSize);
            return doTryUpdates(classifiedAd);
        });
    }

    public Optional<ClassifiedAd> findById(ClassifiedAdId classifiedAdId) {
        return aggregateStoreRepository.load(classifiedAdId)
            .map(it -> (ClassifiedAd) it);
    }

    public Try<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdOwner command) {
        return update(command.getClassifiedAdId(), classifiedAd -> {
            if (command.getOwnerId() != null) {
                classifiedAd.updateOwner(new UserId(command.getOwnerId()));
            }
            return doTryUpdates(classifiedAd);
        });
    }

    public Try<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdTitle command) {
        return update(command.getClassifiedAdId(), classifiedAd -> {
            if (!Strings.isNullOrEmpty(command.getTitle())) {
                classifiedAd.updateTitle(new ClassifiedAdTitle(command.getTitle()));
            }
            return doTryUpdates(classifiedAd);
        });
    }

    public Try<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdText command) {
        return update(command.getClassifiedAdId(), classifiedAd -> {
            if (!Strings.isNullOrEmpty(command.getText())) {
                classifiedAd.updateText(new ClassifiedAdText(command.getText()));
            }
            return doTryUpdates(classifiedAd);
        });
    }

    public Try<UpdateClassifiedAdResponse> handle(ApproveClassifiedAd command) {
        return update(command.getClassifiedAdId(), classifiedAd -> {
            if (command.getApproverId() != null) {
                classifiedAd.approve(UserId.from(command.getApproverId()));
            }
            return doTryUpdates(classifiedAd);
        });
    }

    public Try<UpdateClassifiedAdResponse> handle(PublishClassifiedAd command) {
        return update(command.getClassifiedAdId(), classifiedAd -> {
            classifiedAd.requestToPublish();
            return doTryUpdates(classifiedAd);
        });
    }

    private Try<UpdateClassifiedAdResponse> update(UUID classifiedAd,
        Function<ClassifiedAd, Try<UpdateClassifiedAdResponse>> func) {
        return loadClassifiedAd(ClassifiedAdId.from(classifiedAd))
            .flatMap(func);
    }

    public Try<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdPrice command) {
        return loadClassifiedAd(ClassifiedAdId.from(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                Price price = new Price(new Money(command.getAmount(), command.getCurrency()));
                classifiedAd.updatePrice(price);
                return doTryUpdates(classifiedAd);
            });
    }

    public Try<UpdateClassifiedAdResponse> handle(AddPicturesToClassifiedAd command) {
        return loadClassifiedAd(ClassifiedAdId.from(command.getClassifiedAdId()))
            .flatMap(classifiedAd -> {
                List<PictureDto> pictures = command.getPictures();
                pictures.forEach(pic -> {
                    var pictureSize = new PictureSize(pic.getWidth(), pic.getHeight());
                    classifiedAd.addPicture(pic.getUri(), pictureSize, 0);
                });
                return doTryUpdates(classifiedAd);
            });
    }

    private Try<UpdateClassifiedAdResponse> doTryUpdates(ClassifiedAd classifiedAd) {
        return Try.of(() -> aggregateStoreRepository.add(classifiedAd))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> ImmutableUpdateClassifiedAdResponse.builder()
                .id(classifiedAd.getId().id())
                .ownerId(classifiedAd.getOwnerId().id())
                .build());
    }

    private Try<ClassifiedAd> loadClassifiedAd(ClassifiedAdId classifiedAdId) {
        return Try.of(() ->
            aggregateStoreRepository.load(classifiedAdId)
                .map(it -> (ClassifiedAd) it)
                .orElseThrow(() -> new NotFoundException("ClassifiedAd with id '" + classifiedAdId + "' not found")));
    }

    public Try<LoadClassifiedAdResponse> handle(LoadClassifiedAdCommand command) {
        return loadClassifiedAd(ClassifiedAdId.from(command.getClassifiedAdId()))
            .map(res -> {
                Builder builder = ImmutableLoadClassifiedAdResponse.builder()
                    .classifiedAdId(res.getId().id())
                    .owner(res.getOwnerId().id())
                    .state(res.getState());
                if (res.getTitle() != null) {
                    builder.title(res.getTitle().toString());
                }
                if (res.getText() != null) {
                    builder.text(res.getText().toString());
                }

                if (res.getPrice() != null) {
                    builder.price(ImmutablePriceDto.builder()
                        .amount(res.getPrice().money().amount())
                        .currencyCode(res.getPrice().money().currencyCode())
                        .build());
                }

                if (res.getPictures() != null) {
                    List<ImmutablePictureDto> pictures = res.getPictures().stream()
                        .map(picture -> ImmutablePictureDto.builder()
                            .id(picture.getId().id())
                            .height(picture.getSize().height())
                            .width(picture.getSize().width())
                            .uri(picture.getUri())
                            .build())
                        .toList();
                    builder.pictures(pictures);
                }

                return builder.build();
            });
    }
}
