package com.marketplace.client;

import com.marketplace.client.config.ClientConfig;
import com.marketplace.client.context.ClientContext;
import com.marketplace.client.context.DaggerClientContext;
import com.marketplace.config.ConfigLoader;
import java.io.IOException;

public class ClientApplication {

  public static void main(String[] args) throws IOException {
    new ClientApplication().start();
  }

  void start() throws IOException {
    ClientConfig config = ConfigLoader.loadClasspathResource("application.yml", ClientConfig.class);
    ClientContext context = DaggerClientContext.builder()
        .config(config)
        .build();

//    UUID ownerId = UUID.fromString("8d4da197-0ba1-479e-9ae4-cd0e60b9ba72");
//
    ClassifiedAdRestService classifiedAdRestService = context.classifiedAdRestService();
//    var classifiedAd = ClassifiedAdRequest.builder()
//        .ownerId(ownerId)
//        .build();
//
//    Call<Void> res = classifiedAdRestService.createClassifiedAd(classifiedAd);
//    Response<Void> execute = res.execute();
//    if (execute.isSuccessful()) {
//      String location = execute.headers().get("Location");
//      System.out.println(location);
//    } else {
//      if(execute.errorBody() != null) {
//        System.out.println(execute.errorBody().string());
//      }
//    }

//    Call<ClassifiedAdResponse> call = classifiedAdRestService.findById(UUID.fromString("da31260e-b943-425f-8563-ddb6a911662d"));
//    Response<ClassifiedAdResponse> execute = call.execute();
//    if(execute.isSuccessful()){
//      ClassifiedAdResponse body = execute.body();
//      if(body != null) {
//        System.out.println(body.toString());
//      }
//    }else{
//      if (execute.errorBody() != null) {
//        System.out.println(execute.errorBody().string());
//      }
//    }
//    ClientContext context = DaggerClientContext.
//        builder()
////        .config(config)
//        .build();
  }

}
