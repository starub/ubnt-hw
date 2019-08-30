package lv.starub.ubnt.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lv.starub.ubnt.configuration.SSEStreamProperties;
import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostType;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostCachePopulationServiceTest {

    @Mock
    IMap map;

    @Mock
    HazelcastInstance hazelcastInstance;

    @Spy
    ObjectMapper objectMapper;

    @Mock
    SSEStreamProperties properties;

    @InjectMocks
    PostCachePopulationService service;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void init() throws IOException {

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        PostCachePopulationService serviceSpy = spy(service);

        ServerSentEvent<String> event = ServerSentEvent.<String>builder().id("32566330566").event("rc").data("{\"all_awardings\":[],\"approved_at_utc\":null,\"approved_by\":null,\"archived\":false,\"author\":\"cocatee\",\"author_flair_background_color\":\"\",\"author_flair_css_class\":\"eu\",\"author_flair_richtext\":[],\"author_flair_template_id\":null,\"author_flair_text\":\"\",\"author_flair_text_color\":\"dark\",\"author_flair_type\":\"text\",\"author_fullname\":\"t2_m3eti\",\"author_patreon_flair\":false,\"banned_at_utc\":null,\"banned_by\":null,\"body\":\"I`m honest. Since these are eTickets i have no clue how to sell them to you without you having the problem that i can easily scam you.\",\"body_html\":\"&lt;div class=\\\"md\\\"&gt;&lt;p&gt;I`m honest. Since these are eTickets i have no clue how to sell them to you without you having the problem that i can easily scam you.&lt;\\/p&gt;\\n&lt;\\/div&gt;\",\"can_gild\":true,\"can_mod_post\":false,\"collapsed\":false,\"collapsed_reason\":null,\"controversiality\":0,\"created\":1567205468.0,\"created_utc\":1567176668,\"distinguished\":null,\"downs\":0,\"edited\":false,\"gilded\":0,\"gildings\":{},\"id\":\"eyl5hhi\",\"is_submitter\":false,\"likes\":null,\"link_id\":\"t3_cxhnt7\",\"locked\":false,\"mod_note\":null,\"mod_reason_by\":null,\"mod_reason_title\":null,\"mod_reports\":[],\"name\":\"t1_eyl5hhi\",\"no_follow\":true,\"num_reports\":null,\"parent_id\":\"t1_eyl514n\",\"permalink\":\"\\/r\\/leagueoflegends\\/comments\\/cxhnt7\\/tickets_for_the_worlds_semifinals\\/eyl5hhi\\/\",\"removal_reason\":null,\"replies\":\"\",\"report_reasons\":null,\"retrieved_on\":1567176669,\"saved\":false,\"score\":1,\"score_hidden\":true,\"send_replies\":true,\"stickied\":false,\"subreddit\":\"leagueoflegends\",\"subreddit_id\":\"t5_2rfxx\",\"subreddit_name_prefixed\":\"r\\/leagueoflegends\",\"subreddit_type\":\"public\",\"total_awards_received\":0,\"ups\":1,\"user_reports\":[]}").build();
        doReturn(Flux.just(event)).when(serviceSpy).getSSEEventStream();

        when(objectMapper.readValue(event.data(), Post.class)).thenCallRealMethod();

        when(hazelcastInstance.getMap("ALL_POSTS")).thenReturn(map);

        when(map.put(any(Instant.class), any(Post.class))).then(invocation -> {

            Post post = invocation.getArgument(1, Post.class);

            assertThat(post.getPostType()).isEqualTo(PostType.COMMENT);

            assertThat(post.getAuthor()).isEqualTo("cocatee");
            assertThat(post.getBody()).isEqualTo("I`m honest. Since these are eTickets i have no clue how to sell them to you without you having the problem that i can easily scam you.");
            assertThat(post.getPermalink()).isEqualTo("/r/leagueoflegends/comments/cxhnt7/tickets_for_the_worlds_semifinals/eyl5hhi/");
            assertThat(post.getScore()).isEqualTo("1");
            assertThat(post.getSubreddit()).isEqualTo("leagueoflegends");
            assertThat(post.getTotalAwardsReceived()).isEqualTo("0");

            return post;
        });

        serviceSpy.init();

        verify(map, atMostOnce()).put(any(Instant.class), any(Post.class));
        verify(serviceSpy, atMostOnce()).handleEvent(event);
        verify(serviceSpy, atMostOnce()).handleCompletion();
        verify(serviceSpy, never()).handleError(any(Throwable.class));


        doCallRealMethod().when(serviceSpy).getSSEEventStream();

        serviceSpy.init();

        verify(serviceSpy, atMostOnce()).handleError(any(Throwable.class));

        doReturn(Flux.just(ServerSentEvent.builder().build())).when(serviceSpy).getSSEEventStream();

        serviceSpy.init();

        verify(serviceSpy, atMostOnce()).handleError(any(Throwable.class));
    }
}