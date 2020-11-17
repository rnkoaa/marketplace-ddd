package com.marketplace;

import com.marketplace.config.ApplicationConfig;
import com.marketplace.config.ConfigLoader;
import com.marketplace.context.ApplicationContext;
import com.marketplace.context.DaggerApplicationContext;
import com.marketplace.server.SparkServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainApplication {

    AtomicBoolean enabled = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException, IOException {
        new MainApplication().start();
    }

    public void start() throws IOException {
        ApplicationConfig config = ConfigLoader.loadClasspathResource("application.yml", ApplicationConfig.class);
        ApplicationContext context = DaggerApplicationContext.
                builder()
                .config(config)
                .build();
        SparkServer server = context.getServer();
        server.run();

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
