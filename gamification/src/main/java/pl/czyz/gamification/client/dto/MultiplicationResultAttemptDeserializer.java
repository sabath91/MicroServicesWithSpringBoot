package pl.czyz.gamification.client.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * Deserializes an attempt coming from the Multiplication microservice into the Gamification's
 * representation of an attempt.
 */
public class MultiplicationResultAttemptDeserializer extends
    JsonDeserializer<MultiplicationResultAttempt> {
  @Override
  public MultiplicationResultAttempt deserialize(
      final JsonParser jsonParser, final DeserializationContext deserializationContext)
      throws IOException {
    ObjectCodec oc = jsonParser.getCodec();
    JsonNode jsonNode = oc.readTree(jsonParser);
    String userAlias = jsonNode.get("user").get("alias").asText();
    int multiplicationFactorA = jsonNode.get("multiplication").get("factorA").asInt();
    int multiplicationFactorB = jsonNode.get("multiplication").get("factorB").asInt();
    int resultAttempt = jsonNode.get("resultAttempt").asInt();
    boolean isCorrect = jsonNode.get("correct").asBoolean();
    return new MultiplicationResultAttempt(
        userAlias, multiplicationFactorA, multiplicationFactorB, resultAttempt, isCorrect);
  }
}
