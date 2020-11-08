package com.marketplace.fixtures;

import com.marketplace.controller.ClassifiedAdController;
import com.marketplace.domain.ClassifiedAdRepository;
import com.marketplace.domain.ClassifiedAdRepositoryImpl;
import com.marketplace.domain.command.CreateClassifiedAdCommandHandler;
import com.marketplace.domain.command.UpdateClassifiedAdCommandHandler;

import java.util.HashMap;
import java.util.Map;

public class ApplicationRunner {
    static Map<String, Object> beanInitializers = new HashMap<>();

    public static <T> T getBean(Class<T> clzz) {
        Object o = beanInitializers.get(clzz.getSimpleName());
        if (o == null) {
            switch (clzz.getSimpleName()) {
                case "ClassifiedAdController":
                    o = classifiedAdController();
                    beanInitializers.put(clzz.getSimpleName(), o);
                    return clzz.cast(o);
                case "ClassifiedAdRepository":
                    o = classifiedAdRepository();
                    beanInitializers.put(clzz.getSimpleName(), o);
                    return clzz.cast(o);
                case "CreateClassifiedAdCommandHandler":
                    o = createClassifiedAdCommandHandler();
                    beanInitializers.put(clzz.getSimpleName(), o);
                    return clzz.cast(o);
                case "UpdateClassifiedAdCommandHandler":
                    o = updateClassifiedAdCommandHandler();
                    beanInitializers.put(clzz.getSimpleName(), o);
                    return clzz.cast(o);
            }
        }
        return clzz.cast(o);
    }

    static ClassifiedAdRepository classifiedAdRepository() {
        Object o = beanInitializers.get(ClassifiedAdRepository.class.getSimpleName());
        if (o != null) {
            return (ClassifiedAdRepository) o;
        }
        var repository = new ClassifiedAdRepositoryImpl();
        beanInitializers.put(ClassifiedAdRepository.class.getSimpleName(), repository);
        return repository;
    }

    static UpdateClassifiedAdCommandHandler updateClassifiedAdCommandHandler() {
        Object o = beanInitializers.get(UpdateClassifiedAdCommandHandler.class.getSimpleName());
        if (o != null) {
            return (UpdateClassifiedAdCommandHandler) o;
        }
        var commandHandler = new UpdateClassifiedAdCommandHandler(classifiedAdRepository());
        beanInitializers.put(UpdateClassifiedAdCommandHandler.class.getSimpleName(), commandHandler);
        return commandHandler;
    }

    static CreateClassifiedAdCommandHandler createClassifiedAdCommandHandler() {
        Object o = beanInitializers.get(CreateClassifiedAdCommandHandler.class.getSimpleName());
        if (o != null) {
            return (CreateClassifiedAdCommandHandler) o;
        }
        var commandHandler = new CreateClassifiedAdCommandHandler(classifiedAdRepository());
        beanInitializers.put(CreateClassifiedAdCommandHandler.class.getSimpleName(), commandHandler);
        return commandHandler;
    }

    static ClassifiedAdController classifiedAdController() {
        Object o = beanInitializers.get(ClassifiedAdController.class.getSimpleName());
        if (o != null) {
            return (ClassifiedAdController) o;
        }
        var controller = new ClassifiedAdController(createClassifiedAdCommandHandler(), updateClassifiedAdCommandHandler());
        beanInitializers.put(ClassifiedAdController.class.getSimpleName(), controller);
        return controller;
    }
}