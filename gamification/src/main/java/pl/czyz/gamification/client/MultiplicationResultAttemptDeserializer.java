package pl.czyz.gamification.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import pl.czyz.gamification.client.dto.MultiplicationResultAttempt;

public class MultiplicationResultAttemptDeserializer
    extends JsonDeserializer<MultiplicationResultAttempt> {
  @Override
  public MultiplicationResultAttempt deserialize(
      final JsonParser jsonParser, final DeserializationContext deserializationContext)
      throws IOException, JsonProcessingException {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode node = oc.readTree(jsonParser);

    final String userAlias = node.get("user").get("alias").asText();
    final int multiplicationFactorA = node.get("multiplication").get("factorA").asInt();
    final int multiplicationFactorB = node.get("multiplication").get("factorB").asInt();
    final int resultAttempt = node.get("resultAttempt").asInt();
    final boolean correct = node.get("correct").asBoolean();
    return new MultiplicationResultAttempt(
        userAlias, multiplicationFactorA, multiplicationFactorB, resultAttempt, correct);
  }
}
