package com.marketplace.client;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VersionedEventService {

    @GET("")
    Call<Object> countEvents(String streamId);

}
