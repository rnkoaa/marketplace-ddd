package com.marketplace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.context.ObjectMapperModule;
import com.marketplace.controller.ClassifiedAdCommandHandler;
import com.marketplace.controller.ClassifiedAdController;
import com.marketplace.domain.ClassifiedAdRepositoryImpl;
import com.marketplace.domain.command.CreateClassifiedAdCommandHandler;
import com.marketplace.domain.command.UpdateClassifiedAdCommandHandler;
import com.marketplace.server.SparkServer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainApplication {

    AtomicBoolean enabled = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        var classifiedAdRepository = new ClassifiedAdRepositoryImpl();
        var updateClassifiedAdCommandHandler = new UpdateClassifiedAdCommandHandler(classifiedAdRepository);
        var createClassifiedAdCommandHandler = new CreateClassifiedAdCommandHandler(classifiedAdRepository);
        var classifiedAdCommandHandler = new ClassifiedAdCommandHandler(classifiedAdRepository);
        ClassifiedAdController controller = new ClassifiedAdController(createClassifiedAdCommandHandler,
                updateClassifiedAdCommandHandler,
                classifiedAdCommandHandler);
        ObjectMapper objectMapper = ObjectMapperModule.objectMapper();
        new SparkServer(objectMapper, controller).run();
    }

    public void run() throws InterruptedException {
        var app = new MainApplication();
        new Thread(() -> {
            try {
                app.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(app::stop).start();
    }

    private void stop() {
        System.out.println("got stop signal, stopping...");
        enabled.compareAndSet(true, false);
    }

    void consume() throws InterruptedException {
        while (enabled.get()) {
            System.out.println("Hello, World");
            TimeUnit.MILLISECONDS.sleep(200);
        }
    }
}
