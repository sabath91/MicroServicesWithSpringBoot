package pl.czyz.gamification.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.czyz.gamification.service.GameService;

@Slf4j
@Component
class EventHandler {

  private GameService gameService;

  public EventHandler(final GameService gameService) {
    this.gameService = gameService;
  }

  @RabbitListener(queues = "${multiplication.queue}")
  void handleMultiplicationSolved(final MultiplicationSolvedEvent event) {
    log.info("Multiplication Solved Event received: {}", event.getMultiplicationResultAttemptId());
    try {
      gameService.newAttemptForUser(
          event.getUserId(), event.getMultiplicationResultAttemptId(), event.isCorrect());
    } catch (final Exception e) {
      log.error("Error when trying to process Multiplication Solved Event", e);
      throw new AmqpRejectAndDontRequeueException(e);
    }
  }
}
