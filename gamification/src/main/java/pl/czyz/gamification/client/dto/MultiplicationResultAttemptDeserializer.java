package pl.czyz.gamification.client.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class MultiplicationResultAttemptDeserializer extends JsonDeserializer {
  @Override
  public Object deserialize(
      final JsonParser jsonParser, final DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    return null;
  }
}
