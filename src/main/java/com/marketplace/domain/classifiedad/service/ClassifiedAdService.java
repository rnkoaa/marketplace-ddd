package com.marketplace.domain.classifiedad.service;

import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.*;
import com.marketplace.domain.classifiedad.command.*;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.controller.*;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;
import com.marketplace.domain.shared.UserId;
import com.marketplace.framework.Strings;

import java.util.List;
import javax.inject.Inject;
import java.util.Optional;

public class ClassifiedAdService {

  private final ClassifiedAdRepository classifiedAdRepository;

  @Inject
  public ClassifiedAdService(ClassifiedAdRepository classifiedAdRepository) {
    this.classifiedAdRepository = classifiedAdRepository;
  }

  public CommandHandlerResult<CreateAdResponse> handle(CreateClassifiedAd command) {
    ClassifiedAdId classifiedAdId = (command.getId() == null)
        ? new ClassifiedAdId() : ClassifiedAdId.from(command.getId());

    var ownerId = new UserId(command.getOwnerId());
    var classifiedAd = new ClassifiedAd(classifiedAdId, ownerId);

    if (!Strings.isNullOrEmpty(command.getTitle())) {
      classifiedAd.updateTitle(new ClassifiedAdTitle(command.getTitle()));
    }

    if (!Strings.isNullOrEmpty(command.getText())) {
      classifiedAd.updateText(new ClassifiedAdText(command.getText()));
    }

    var saved = classifiedAdRepository.add(classifiedAd);
    if (saved == null) {
      return new CommandHandlerResult<>(null, false, "failed to save classifiedAd");
    }
    var classifiedAdResponse = new CreateAdResponse(command.getOwnerId(), classifiedAdId.id());

    return new CommandHandlerResult<>(classifiedAdResponse, true, "");
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAd command) {
    Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
    return mayBe.map(classifiedAd -> {
      if (command.getOwnerId() != null) {
        classifiedAd.updateOwner(new UserId(command.getOwnerId()));
      }
      if (!Strings.isNullOrEmpty(command.getTitle())) {
        classifiedAd.updateTitle(new ClassifiedAdTitle(command.getTitle()));
      }
      if (!Strings.isNullOrEmpty(command.getText())) {
        classifiedAd.updateText(new ClassifiedAdText(command.getText()));
      }
      if (command.getPrice() != null) {
        var money = new Money(command.getPrice().getAmount(), command.getPrice().getCurrencyCode(), new DefaultCurrencyLookup());
        var price = new Price(money);
        classifiedAd.updatePrice(price);
      }

      if (command.getApprovedBy() != null) {
        classifiedAd.approve(new UserId(command.getApprovedBy()));
      }

      return classifiedAdRepository.add(classifiedAd);
    }).map(classifiedAd -> {
      var updateResponse = new UpdateClassifiedAdResponse();
      return new CommandHandlerResult<>(updateResponse, true, "");
    }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
  }

  public AddPictureResponse handle(AddPictureToClassifiedAd addPictureToClassifiedAd) {
    Optional<ClassifiedAd> load = classifiedAdRepository.load(new ClassifiedAdId(addPictureToClassifiedAd.getId()));

    return load.map(classifiedAd -> {

      PictureSize pictureSize = new PictureSize(addPictureToClassifiedAd.getWidth(), addPictureToClassifiedAd.getHeight());
      var pictureId = classifiedAd.addPicture(addPictureToClassifiedAd.getUri(), pictureSize, 0);

      var savedClassifiedAd = classifiedAdRepository.add(classifiedAd);

      return AddPictureResponse.builder()
          .id(pictureId.id())
          .classifiedAdId(savedClassifiedAd.getId().id())
          .build();
    }).orElse(new AddPictureResponse());
  }

  public ResizePictureResponse handle(ResizeClassifiedAdPicture pictureDto) {
    Optional<ClassifiedAd> load = classifiedAdRepository.load(new ClassifiedAdId(pictureDto.getClassifiedAdId()));

    return load.map(classifiedAd -> {
      PictureSize pictureSize = new PictureSize(pictureDto.getWidth(), pictureDto.getHeight());
      var pictureId = classifiedAd.resizePicture(new PictureId(pictureDto.getId()), pictureSize);
      var savedClassifiedAd = classifiedAdRepository.add(classifiedAd);
      return ResizePictureResponse.builder()
          .classifiedAdId(savedClassifiedAd.getId().id())
          .id(pictureId.id())
          .build();
    }).orElse(new ResizePictureResponse());
  }

  public Optional<ClassifiedAd> findById(ClassifiedAdId classifiedAdId) {
    return classifiedAdRepository.load(classifiedAdId);
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdOwner command) {
    Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
    return mayBe.map(classifiedAd -> {
      if (command.getOwnerId() != null) {
        classifiedAd.updateOwner(new UserId(command.getOwnerId()));
      }
      return classifiedAdRepository.add(classifiedAd);
    }).map(classifiedAd -> {
      var updateResponse = new UpdateClassifiedAdResponse();
      return new CommandHandlerResult<>(updateResponse, true, "");
    }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdTitle command) {
    Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
    return mayBe.map(classifiedAd -> {
      if (!Strings.isNullOrEmpty(command.getTitle())) {
        classifiedAd.updateTitle(new ClassifiedAdTitle(command.getTitle()));
        return classifiedAdRepository.add(classifiedAd);
      }
      // TODO - throw illegalArgumentException
      return null;
    }).map(classifiedAd -> {
      var updateResponse = new UpdateClassifiedAdResponse();
      return new CommandHandlerResult<>(updateResponse, true, "");
    }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdText command) {
    Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
    return mayBe.map(classifiedAd -> {
      if (!Strings.isNullOrEmpty(command.getText())) {
        classifiedAd.updateText(new ClassifiedAdText(command.getText()));
        return classifiedAdRepository.add(classifiedAd);
      } else {
        // TODO - throw illegalArgumentException
        return null;
      }
    }).map(classifiedAd -> {
      var updateResponse = new UpdateClassifiedAdResponse();
      return new CommandHandlerResult<>(updateResponse, true, "");
    }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> handle(ApproveClassifiedAd command) {
    Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
    return mayBe.map(classifiedAd -> {
      if (command.getApproverId() != null) {
        classifiedAd.approve(UserId.from(command.getApproverId()));
        return classifiedAdRepository.add(classifiedAd);
      }
      // TODO - throw illegalArgumentException
      return null;
    }).map(classifiedAd -> {
      var updateResponse = new UpdateClassifiedAdResponse();
      return new CommandHandlerResult<>(updateResponse, true, "");
    }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> handle(PublishClassifiedAd command) {
    Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
    return mayBe.map(classifiedAd -> {
      classifiedAd.requestToPublish();
      return classifiedAdRepository.add(classifiedAd);
    }).map(classifiedAd -> {
      var updateResponse = new UpdateClassifiedAdResponse();
      return new CommandHandlerResult<>(updateResponse, true, "");
    }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> handle(UpdateClassifiedAdPrice command) {
    Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
    return mayBe.map(classifiedAd -> {
      Price price = new Price(new Money(command.getAmount(), command.getCurrency()));
      classifiedAd.updatePrice(price);
      return classifiedAdRepository.add(classifiedAd);
    }).map(classifiedAd -> {
      var updateResponse = new UpdateClassifiedAdResponse();
      return new CommandHandlerResult<>(updateResponse, true, "");
    }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));

  }

  public CommandHandlerResult<UpdateClassifiedAdResponse> handle(AddPicturesToClassifiedAd command) {
    Optional<ClassifiedAd> mayBe = classifiedAdRepository.load(new ClassifiedAdId(command.getId()));
    return mayBe.map(classifiedAd -> {
//      Price price = new Price(new Money(command.getAmount(), command.getCurrency()));
      List<PictureDto> pictures = command.getPictures();
      pictures.forEach(pic -> {
        var pictureSize = new PictureSize(pic.getWidth(), pic.getHeight());
        classifiedAd.addPicture(pic.getUri(), pictureSize, 0);
      });
      return classifiedAdRepository.add(classifiedAd);
    }).map(classifiedAd -> {
      var updateResponse = new UpdateClassifiedAdResponse();
      return new CommandHandlerResult<>(updateResponse, true, "");
    }).orElse(new CommandHandlerResult<>(null, false, "classifiedAd not found"));
  }
}
