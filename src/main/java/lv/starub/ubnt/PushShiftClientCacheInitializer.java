package lv.starub.ubnt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Component
public class PushShiftClientCacheInitializer {

    @PostConstruct
    private void init() {

        Logger logger = LoggerFactory.getLogger("http://stream.pushshift.io");

        WebClient client = WebClient.create("http://stream.pushshift.io");
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> handleContent(logger, content),
                error -> handleError(logger, error),
                () -> handleCompletion(logger));
    }

    private void handleCompletion(Logger logger) {
        logger.info("Completed!!!");
    }

    private void handleError(Logger logger, Throwable error) {
        logger.error("Error receiving SSE: {}", error);
    }

    private void handleContent(Logger logger, ServerSentEvent<String> content) {
        logger.info("{} - event: name[{}], id [{}], data[{}], comment[{}]",
                System.currentTimeMillis(), content.event(), content.id(), content.data(), content.comment());
    }


}
