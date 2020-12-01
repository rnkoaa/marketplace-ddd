package com.marketplace.eventstore.mongodb.application.codecs;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.base.GeneratorBase;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;
import org.bson.BsonBinary;
import org.bson.BsonWriter;
import org.bson.types.Decimal128;
import org.immutables.criteria.mongo.bson4jackson.Wrapper;

public class ObjectMapperBsonGenerator extends GeneratorBase implements Wrapper<BsonWriter> {

  private final BsonWriter writer;

  ObjectMapperBsonGenerator(int jsonFeatures, BsonWriter writer) {
    super(jsonFeatures, null);
    this.writer = Objects.requireNonNull(writer, "writer");
  }

  @Override
  public void writeStartArray() throws IOException {
    writer.writeStartArray();
  }

  @Override
  public void writeEndArray() throws IOException {
    writer.writeEndArray();
  }

  @Override
  public void writeStartObject() throws IOException {
    writer.writeStartDocument();
  }

  @Override
  public void writeEndObject() throws IOException {
    writer.writeEndDocument();
  }

  @Override
  public void writeFieldName(String name) throws IOException {
    writer.writeName(name);
  }

  @Override
  public void writeString(String text) throws IOException {
    if (text == null) {
      writeNull();
    } else {
      writer.writeString(text);
    }
  }

  @Override
  public void writeString(char[] text, int offset, int len) throws IOException {
    writer.writeString(new String(text, offset, len));
  }

  @Override
  public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
    writer.writeString(new String(text, offset, length));
  }

  @Override
  public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
    writer.writeString(new String(text, offset, length));
  }

  @Override
  public void writeRaw(String text) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRaw(String text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRaw(char[] text, int offset, int len) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeRaw(char c) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean canWriteBinaryNatively() {
    return true;
  }

  @Override
  public void writeBinary(Base64Variant bv, byte[] data, int offset, int len) throws IOException {
    BsonBinary binary = new BsonBinary(Arrays.copyOfRange(data, offset, offset + len));
    writer.writeBinaryData(binary);
  }

  @Override
  public void writeNumber(int number) throws IOException {
    writer.writeInt32(number);
  }

  @Override
  public void writeNumber(long number) throws IOException {
    writer.writeInt64(number);
  }

  @Override
  public void writeNumber(BigInteger number) throws IOException {
    if (number == null) {
      writeNull();
    } else {
      writeNumber(new BigDecimal(number));
    }
  }

  @Override
  public void writeNumber(double number) throws IOException {
    writer.writeDouble(number);
  }

  @Override
  public void writeNumber(float number) throws IOException {
    writer.writeDouble(number);
  }

  @Override
  public void writeNumber(BigDecimal number) throws IOException {
    if (number == null) {
      writeNull();
    } else {
      try {
        writer.writeDecimal128(new Decimal128(number));
      } catch (NumberFormatException e) {
        writer.writeString(number.toString());
      }
    }
  }

  @Override
  public void writeNumber(String encodedValue) throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void writeBoolean(boolean state) throws IOException {
    writer.writeBoolean(state);
  }

  @Override
  public void writeNull() throws IOException {
    writer.writeNull();
  }

  @Override
  public void flush() throws IOException {
    writer.flush();
  }

  @Override
  protected void _releaseBuffers() {}

  @Override
  protected void _verifyValueWrite(String typeMsg) throws IOException {}

  @Override
  public BsonWriter unwrap() {
    return writer;
  }
}
