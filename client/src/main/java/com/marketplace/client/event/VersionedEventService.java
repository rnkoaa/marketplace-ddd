package com.marketplace.client.event;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VersionedEventService {

    @GET("")
    Call<Object> countEvents(String streamId);

}
