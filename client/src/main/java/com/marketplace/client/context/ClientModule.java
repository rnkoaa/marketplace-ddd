package com.marketplace.client.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.client.classifiedad.ClassifiedAdRestService;
import com.marketplace.client.userprofile.UserProfileRestService;
import com.marketplace.client.config.ClientConfig;
import com.marketplace.common.ObjectMapperBuilder;
import dagger.Module;
import dagger.Provides;
import java.io.IOException;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public abstract class ClientModule {

    @Provides
    @Singleton
    static OkHttpClient okHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new RequestBodyLoggingInterceptor());
        return httpClient.build();
    }

    static class RequestBodyLoggingInterceptor implements Interceptor {

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request original = chain.request();
            String requestBody = bodyToString(original);
            System.out.println(requestBody);
            return chain.proceed(original);
        }

        private static String bodyToString(final Request request) {

            try {
                final Request copy = request.newBuilder().build();
                String s = copy.url().encodedPath();
                final Buffer buffer = new Buffer();
                RequestBody body = copy.body();
                if (body != null) {
                    body.writeTo(buffer);
                }
                System.out.println(s);
                String requestBody = buffer.readUtf8();

                return String.format("[Path: %s, Body: (%s)]", s, requestBody);
            } catch (final IOException e) {
                return "did not work";
            }
        }
    }

    @Provides
    @Singleton
    @Named("yamlObjectMapper")
    static ObjectMapper provideYamlObjectMapper() {
        return new ObjectMapperBuilder().build();
    }

    @Provides
    @Singleton
    static ObjectMapper provideObjectMapper() {
        return new ObjectMapperBuilder().build();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(ClientConfig config, ObjectMapper objectMapper, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
            .baseUrl(config.getApplicationURL())
            .client(okHttpClient)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build();
    }

    @Provides
    @Singleton
    static UserProfileRestService provideUserProfileRestService(Retrofit retrofit) {
        return retrofit.create(UserProfileRestService.class);
    }

    @Provides
    @Singleton
    static ClassifiedAdRestService provideClassifiedAdRestService(Retrofit retrofit) {
        return retrofit.create(ClassifiedAdRestService.class);
    }
}
