package com.marketpalce.config;

import com.marketplace.config.ConfigLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ConfigLoaderTests {

    static class ApplicationConfig {
        String instanceId;
        Application application;

        public String getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(String instanceId) {
            this.instanceId = instanceId;
        }

        public Application getApplication() {
            return application;
        }

        public void setApplication(Application application) {
            this.application = application;
        }

        @Override
        public String toString() {
            return "ApplicationConfig{" +
                    "instanceId='" + instanceId + '\'' +
                    ", application=" + application +
                    '}';
        }
    }

    static class Application {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    void testFileCanBeFoundAndLoaded() throws IOException {
        String resourceFileName = "application.yml";
//        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceFileName);
        String resource = ConfigLoader.resourceAsString(resourceFileName);
        System.out.println(resource);

        ApplicationConfig config = ConfigLoader.loadClasspathResource(resourceFileName, ApplicationConfig.class);

        assertThat(config).isNotNull();

        System.out.println(config.toString());
        assertThat(config.instanceId).isNotBlank().isEqualTo("instanceId");
        assertThat(config.application).isNotNull();
        assertThat(config.application.name).isNotBlank().isEqualTo("market-place");
    }
}
