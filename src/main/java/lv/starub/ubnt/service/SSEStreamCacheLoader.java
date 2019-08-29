package lv.starub.ubnt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import lv.starub.ubnt.configuration.SSEStreamProperties;
import lv.starub.ubnt.domain.MockServerSentEvent;
import lv.starub.ubnt.domain.PostType;
import lv.starub.ubnt.domain.RedditPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;

@Component
class SSEStreamCacheLoader {

    @Autowired
    SSEStreamProperties streamProperties;

    @Autowired
    HazelcastInstance hazelcastInstance;

    @Autowired
    ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(SSEStreamCacheLoader.class);

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
                content -> handleContent(content),
                error -> handleError(error),
                () -> handleCompletion());
    }

    void handleCompletion() {

        MockServerSentEvent sse = new MockServerSentEvent();

        sse.setId("32558278180");
        sse.setEvent("rc");
        sse.setData("{\"all_awardings\":[],\"approved_at_utc\":null,\"approved_by\":null,\"archived\":false,\"author\":\"frenchiefanatique\",\"author_flair_background_color\":null,\"author_flair_css_class\":null,\"author_flair_richtext\":[],\"author_flair_template_id\":null,\"author_flair_text\":null,\"author_flair_text_color\":null,\"author_flair_type\":\"text\",\"author_fullname\":\"t2_83esu\",\"author_patreon_flair\":false,\"banned_at_utc\":null,\"banned_by\":null,\"body\":\"\\\"I didn't think it would end this way...\\\"\\n\\n\\\"End? No, the journey doesn't end here. Death is just another path, one that we all must take. The grey rain curtain of this world rolls back, and all turns to silver glass...\\\" \\n\\nRewatched return of the King recently and this conversation got me gooood. I need to reread those books, dammit\",\"body_html\":\"&lt;div class=\\\"md\\\"&gt;&lt;p&gt;&amp;quot;I didn&amp;#39;t think it would end this way...&amp;quot;&lt;\\/p&gt;\\n\\n&lt;p&gt;&amp;quot;End? No, the journey doesn&amp;#39;t end here. Death is just another path, one that we all must take. The grey rain curtain of this world rolls back, and all turns to silver glass...&amp;quot; &lt;\\/p&gt;\\n\\n&lt;p&gt;Rewatched return of the King recently and this conversation got me gooood. I need to reread those books, dammit&lt;\\/p&gt;\\n&lt;\\/div&gt;\",\"can_gild\":true,\"can_mod_post\":false,\"collapsed\":false,\"collapsed_reason\":null,\"controversiality\":0,\"created\":1567095683.0,\"created_utc\":1567066883,\"distinguished\":null,\"downs\":0,\"edited\":false,\"gilded\":0,\"gildings\":{},\"id\":\"eygcw84\",\"is_submitter\":false,\"likes\":null,\"link_id\":\"t3_cwu6m0\",\"locked\":false,\"mod_note\":null,\"mod_reason_by\":null,\"mod_reason_title\":null,\"mod_reports\":[],\"name\":\"t1_eygcw84\",\"no_follow\":true,\"num_reports\":null,\"parent_id\":\"t1_eyfat0w\",\"permalink\":\"\\/r\\/AskReddit\\/comments\\/cwu6m0\\/what_movie_hit_you_the_hardest_emotionally\\/eygcw84\\/\",\"removal_reason\":null,\"replies\":\"\",\"report_reasons\":null,\"retrieved_on\":1567066893,\"saved\":false,\"score\":1,\"score_hidden\":true,\"send_replies\":true,\"stickied\":false,\"subreddit\":\"AskReddit\",\"subreddit_id\":\"t5_2qh1i\",\"subreddit_name_prefixed\":\"r\\/AskReddit\",\"subreddit_type\":\"public\",\"total_awards_received\":0,\"ups\":1,\"user_reports\":[]}");

        try {

            RedditPost post = objectMapper.readValue(sse.getData(), RedditPost.class);
            post.setTimestamp(Instant.now());
            post.setPostType("rs".equals(sse.getEvent()) ? PostType.SUBMISSION : PostType.COMMENT);
            hazelcastInstance.getMap("ALL_POSTS").put(post.getTimestamp(), post);

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Completed!!!");
    }

    void handleError(Throwable error) {
        logger.error("Error receiving SSE: {}", error);
    }

    void handleContent(ServerSentEvent<String> content) {
        logger.info("{} - event: name[{}], id [{}], data[{}], comment[{}]",
                System.currentTimeMillis(), content.event(), content.id(), content.data(), content.comment());
    }

}
