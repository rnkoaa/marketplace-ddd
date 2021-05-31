package com.marketplace.client.model.classifiedad;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableUpdateClassifiedAdPictures.class)
public interface UpdateClassifiedAdPictures {

    List<Picture> getPictures();

}
