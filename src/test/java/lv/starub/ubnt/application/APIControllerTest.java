package lv.starub.ubnt.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class APIControllerTest {

    @Autowired
    WebTestClient client;

    @Test
    @DisplayName("Test Reddit stream activity")
    void testRedditStreamActivity() {
        EntityExchangeResult<String> result = client
                .get()
                .uri("/api/v1/posts/activity")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();

        assertThat(result.getResponseBody())
                .contains("5 POST(S)")
                .contains("3 COMMENT(S)")
                .contains("2 SUBMISSION(S)");

        result = client
                .get()
                .uri("/api/v1/posts/activity/ONE_MINUTE")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult();

        assertThat(result.getResponseBody())
                .contains("1 POST(S)")
                .contains("1 COMMENT(S)")
                .contains("0 SUBMISSION(S)");

    }

    @Test
    @DisplayName("Test retrieving top 2 subreddits")
    void testRetrievingTop2Subreddits() {
        client
                .get()
                .uri("/api/v1/posts/top/2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[*].subreddit",
                        "one minute subreddit",
                        "current subreddit");
    }

    @Test
    @DisplayName("Test retrieving post count")
    void testRetrievingPostCount() {
        client
                .get()
                .uri("/api/v1/posts/count/COMMENT")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("\r\n3 COMMENT(S)\r\n");

        client
                .get()
                .uri("/api/v1/posts/count/COMMENT/ONE_MINUTE")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("\r\n1 COMMENT(S)\r\n");

    }

    @Test
    @DisplayName("Test retrieving all posts")
    void testRetrievingAllPosts() {
        client
                .get()
                .uri("/api/v1/posts/ONE_MINUTE")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[*].subreddit",
                        "current subreddit");
        client
                .get()
                .uri("/api/v1/posts")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[*].subreddit", "one day subreddit",
                        "one hour subreddit",
                        "five minutes subreddit",
                        "one minute subreddit",
                        "current subreddit");

    }

}