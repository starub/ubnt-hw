package lv.starub.ubnt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.starub.ubnt.configuration.SSEStreamProperties;
import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostType;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
class PostCachePopulationService {

    private final SSEStreamProperties streamProperties;

    private final HazelcastInstance hazelcastInstance;

    private final ObjectMapper objectMapper;


    @PostConstruct
    void init() {

        Flux<ServerSentEvent<String>> eventStream = getSSEEventStream();

        eventStream.subscribe(
                this::handleEvent,
                this::handleError,
                this::handleCompletion);
    }

    void handleCompletion() {
        log.info("SSE stream completed");
    }

    void handleError(Throwable e) {
        log.error("Error receiving SSE stream", e);
    }

    void handleEvent(ServerSentEvent<String> event) {

        log.info("Received event: name[{}], id [{}], data[{}], comment[{}]",
                event.event(), event.id(), event.data(), event.comment());
        try {

            Post post = objectMapper.readValue(event.data(), Post.class);

            post.setTimestamp(Instant.now());
            post.setPostType("rs".equals(event.event()) ? PostType.SUBMISSION : PostType.COMMENT);

            if (!StringUtils.isEmpty(post.getSubreddit())) {
                hazelcastInstance.getMap("ALL_POSTS").put(post.getTimestamp(), post);
            }

        } catch (Throwable e) {
            log.error("Error parsing SSE", e);
        }

    }

    Flux<ServerSentEvent<String>> getSSEEventStream() {
        WebClient client = WebClient.create(streamProperties.getUrl());
        ParameterizedTypeReference<ServerSentEvent<String>> type
                = new ParameterizedTypeReference<ServerSentEvent<String>>() {
        };

        return client.get()
                .uri("/")
                .retrieve()
                .bodyToFlux(type);
    }

}
