package com.marketplace.client.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.client.ClassifiedAdRestService;
import com.marketplace.client.UserProfileRestService;
import com.marketplace.client.config.ClientConfig;
import com.marketplace.common.ObjectMapperBuilder;
import dagger.Module;
import dagger.Provides;
import java.io.IOException;
import java.util.Objects;
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
//        return new OkHttpClient();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                String requestBody = bodyToString(original);

//                RequestBody body = original.body();
                System.out.println(requestBody);
//                body.

//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                    .header("Authorization", "auth-value"); // <-- this is the important line

//                Request request = requestBuilder.build();
                return chain.proceed(original);
            }
        });

        return httpClient.build();
    }

    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            RequestBody body = copy.body();
            if (body != null) {
                body.writeTo(buffer);
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
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
