package lv.starub.ubnt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import lv.starub.ubnt.configuration.SSEStreamProperties;
import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Profile("prod")
class PostCachePopulationService {

    private final SSEStreamProperties streamProperties;

    private final HazelcastInstance hazelcastInstance;

    private final ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(PostCachePopulationService.class);

    @PostConstruct
    void init() {

        WebClient client = WebClient.create(streamProperties.getUrl());
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {
        };

        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri("/")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                this::handleEvent,
                this::handleError,
                this::handleCompletion);
    }

    private void handleCompletion() {
        logger.info("Completed");
    }

    private void handleError(Throwable error) {
        logger.error("Error receiving SSE: {}", error);
    }

    private void handleEvent(ServerSentEvent<String> event) {

        logger.info("Received event: name[{}], id [{}], data[{}], comment[{}]",
                event.event(), event.id(), event.data(), event.comment());
        try {

            Post post = objectMapper.readValue(event.data(), Post.class);

            post.setTimestamp(Instant.now());
            post.setPostType("rs".equals(event.event()) ? PostType.SUBMISSION : PostType.COMMENT);

            if (!StringUtils.isEmpty(post.getSubreddit())) {
                hazelcastInstance.getMap("ALL_POSTS").put(post.getTimestamp(), post);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

}
