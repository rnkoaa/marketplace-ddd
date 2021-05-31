package com.marketplace.client;

import com.marketplace.client.model.classifiedad.ApproveClassifiedAdRequest;
import com.marketplace.client.model.classifiedad.ClassifiedAdRequest;
import com.marketplace.client.model.classifiedad.ClassifiedAdResponse;
import com.marketplace.client.model.classifiedad.UpdateClassifiedAdPriceRequest;
import com.marketplace.client.model.classifiedad.UpdateClassifiedAdTextRequest;
import com.marketplace.client.model.classifiedad.UpdateClassifiedAdTitleRequest;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ClassifiedAdRestService {

  @POST("classified_ad")
  Call<Void> createClassifiedAd(@Body ClassifiedAdRequest classifiedAdRequest);

  @PUT("classified_ad/{classifiedAdId}/")
  Call<Void> updateClassifiedAd(@Path("classifiedAdId") @Body ClassifiedAdRequest classifiedAdRequest);

  @PUT("classified_ad/{classifiedAdId}/title")
  Call<Void> updateClassifiedAdTitle(@Path("classifiedAdId") UUID classifiedAdId, @Body UpdateClassifiedAdTitleRequest classifiedAdRequest);

  @PUT("classified_ad/{classifiedAdId}/text")
  Call<Void> updateClassifiedAdText(@Path("classifiedAdId") UUID classifiedAdId, @Body UpdateClassifiedAdTextRequest classifiedAdRequest);

  @PUT("classified_ad/{classifiedAdId}/price")
  Call<Void> updateClassifiedAdPrice(@Path("classifiedAdId") UUID classifiedAdId, @Body UpdateClassifiedAdPriceRequest classifiedAdRequest);

  @PUT("classified_ad/{classifiedAdId}/approve")
  Call<Void> approveClassifiedAd(@Path("classifiedAdId") UUID classifiedAdId, @Body ApproveClassifiedAdRequest classifiedAdRequest);

  @PUT("classified_ad/{classifiedAdId}/publish")
  Call<Void> publishClassifiedAd(@Path("classifiedAdId") UUID classifiedAdId);

  @GET("classified_ad/{classifiedAdId}")
  Call<ClassifiedAdResponse> findById(@Path("classifiedAdId") UUID classifiedAdId);

}
