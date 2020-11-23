package com.marketplace.client;

import com.marketplace.client.config.ClientConfig;
import com.marketplace.client.context.ClientContext;
import com.marketplace.client.context.DaggerClientContext;
import com.marketplace.client.model.ClassifiedAdRequest;
import com.marketplace.config.ConfigLoader;
import java.io.IOException;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Response;

public class ClientApplication {

  public static void main(String[] args) throws IOException {
    new ClientApplication().start();
  }

  void start() throws IOException {
    ClientConfig config = ConfigLoader.loadClasspathResource("application.yml", ClientConfig.class);
    ClientContext context = DaggerClientContext.builder()
        .config(config)
        .build();

    UUID ownerId = UUID.fromString("8d4da197-0ba1-479e-9ae4-cd0e60b9ba72");

    ClassifiedAdRestService classifiedAdRestService = context.classifiedAdRestService();
    var classifiedAd = ClassifiedAdRequest.builder()
        .ownerId(ownerId)
        .build();

    Call<Void> res = classifiedAdRestService.createClassifiedAd(classifiedAd);
    Response<Void> execute = res.execute();
    if (execute.isSuccessful()) {
      String location = execute.headers().get("Location");
      System.out.println(location);
    } else {
      if(execute.errorBody() != null) {
        System.out.println(execute.errorBody().string());
      }
    }

//    classifiedAdRestService.getById()
//    ClientContext context = DaggerClientContext.
//        builder()
////        .config(config)
//        .build();
  }

}
