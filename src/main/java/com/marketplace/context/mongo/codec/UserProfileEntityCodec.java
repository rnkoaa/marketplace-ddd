package com.marketplace.context.mongo.codec;

import com.marketplace.domain.userprofile.entity.ImmutableUserProfileEntity;
import com.marketplace.framework.Strings;
import java.util.UUID;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class UserProfileEntityCodec implements Codec<ImmutableUserProfileEntity> {

  private final CodecRegistry codecRegistry;

  public UserProfileEntityCodec(CodecRegistry codecRegistry) {
    this.codecRegistry = codecRegistry;
  }


  @Override
  public ImmutableUserProfileEntity decode(BsonReader reader, DecoderContext decoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = documentCodec.decode(reader, decoderContext);
    UUID id = document.get("_id", UUID.class);
    String firstName = document.getString("firstName");
    String lastName = document.getString("lastName");
    String middleName = document.getString("middleName");
    String displayName = document.getString("displayName");
    String photoUrl = document.getString("photoUrl");

    var builder = ImmutableUserProfileEntity.builder()
        .id(id)
        .firstName(firstName)
        .lastName(lastName)
        .displayName(displayName);

    if (!Strings.isNullOrEmpty(middleName)) {
      builder.middleName(middleName);
    }

    if (!Strings.isNullOrEmpty(photoUrl)) {
      builder.photoUrl(photoUrl);
    }
    return builder.build();

  }

  @Override
  public void encode(BsonWriter writer, ImmutableUserProfileEntity value, EncoderContext encoderContext) {
    Codec<Document> documentCodec = codecRegistry.get(Document.class);
    Document document = new Document();
    document.put("_id", value.getId());
    document.put("firstName", value.getFirstName());
    document.put("lastName", value.getLastName());
    document.put("middleName", value.getMiddleName());
    document.put("displayName", value.getDisplayName());
    value.getPhotoUrl().ifPresent(photoUrl -> document.put("photoUrl", photoUrl));
    documentCodec.encode(writer, document, encoderContext);
  }

  @Override
  public Class<ImmutableUserProfileEntity> getEncoderClass() {
    return ImmutableUserProfileEntity.class;
  }

}
