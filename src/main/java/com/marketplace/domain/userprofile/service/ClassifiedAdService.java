package com.marketplace.domain.userprofile.service;

import com.marketplace.controller.classifiedad.*;
import com.marketplace.domain.PictureId;
import com.marketplace.domain.PictureSize;
import com.marketplace.domain.classifiedad.ClassifiedAd;
import com.marketplace.domain.classifiedad.ClassifiedAdId;
import com.marketplace.domain.classifiedad.CommandHandlerResult;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAd;
import com.marketplace.domain.classifiedad.command.CreateClassifiedAdCommandHandler;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAdCommandHandler;
import com.marketplace.domain.classifiedad.repository.ClassifiedAdRepository;

import javax.inject.Inject;
import java.util.Optional;

public class ClassifiedAdService {
    private final ClassifiedAdRepository classifiedAdRepository;
    private final CreateClassifiedAdCommandHandler createCommandHandler;
    private final UpdateClassifiedAdCommandHandler updateCommandHandler;

    @Inject
    public ClassifiedAdService(ClassifiedAdRepository classifiedAdRepository,
                               CreateClassifiedAdCommandHandler createCommandHandler,
                               UpdateClassifiedAdCommandHandler updateCommandHandler) {
        this.classifiedAdRepository = classifiedAdRepository;
        this.createCommandHandler = createCommandHandler;
        this.updateCommandHandler = updateCommandHandler;
    }

    public CommandHandlerResult<CreateAdResponse> handleCreate(CreateClassifiedAd createClassifiedAd) {
        return createCommandHandler.handle(createClassifiedAd);
    }

    public CommandHandlerResult<UpdateClassifiedAdResponse> handleUpdate(UpdateClassifiedAd updateClassifiedAd) {
        return updateCommandHandler.handle(updateClassifiedAd);
    }

    public AddPictureResponse handleAddPicture(AddPictureDto addPictureDto) {
        Optional<ClassifiedAd> load = classifiedAdRepository.load(new ClassifiedAdId(addPictureDto.getClassifiedAdId()));

        return load.map(classifiedAd -> {

            PictureSize pictureSize = new PictureSize(addPictureDto.getWidth(), addPictureDto.getHeight());
            var pictureId = classifiedAd.addPicture(addPictureDto.getUri(), pictureSize, 0);

            var savedClassifiedAd = classifiedAdRepository.add(classifiedAd);

            return AddPictureResponse.builder()
                    .id(pictureId.id())
                    .classifiedAdId(savedClassifiedAd.getId().id())
                    .build();
        }).orElse(new AddPictureResponse());
    }

    public ResizePictureResponse handleResizePicture(ResizePictureDto pictureDto) {
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
}
