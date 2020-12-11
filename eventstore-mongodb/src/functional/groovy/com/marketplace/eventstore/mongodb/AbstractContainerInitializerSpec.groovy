package com.marketplace.eventstore.mongodb

import com.marketplace.common.config.MongoConfig
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodecProvider
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.codecs.pojo.PojoCodecProvider
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.Shared

import static org.bson.codecs.configuration.CodecRegistries.fromProviders

abstract class AbstractContainerInitializerSpec extends BaseFunctionalSpec {

    private static final int MONGO_PORT = 27017
    @Shared
    MongoConfig mongoConfig;
    @Shared
    MongoClient mongoClient;

    static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"))
                    .withExposedPorts(MONGO_PORT);

    def setupSpec() {
        mongoDBContainer.start()
        String hosts = mongoDBContainer.getHost();
        int port = mongoDBContainer.getMappedPort(27017);
        mongoConfig = new MongoConfig(hosts, "eventstore", port);
        mongoClient = createClient(mongoConfig, provideCodecRegistry());
    }

    static MongoClient createClient(MongoConfig config, CodecRegistry codecRegistry) {
        ConnectionString connectionString = new ConnectionString(config.getConnectionString());
        MongoClientSettings settings =
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .codecRegistry(codecRegistry)
                        .uuidRepresentation(UuidRepresentation.STANDARD)
                        .build();
        return MongoClients.create(settings);
    }

    static CodecRegistry provideCodecRegistry() {
        return CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
                fromProviders(new MongoEventEntityCodecProvider()));
    }

    static URL getResourceFromClasspath(String path) {
        return Thread.currentThread().getContextClassLoader().getResource(path)
    }

}