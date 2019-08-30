package lv.starub.ubnt.application;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class APIControllerTest {

    @Autowired
    WebTestClient client;

    @Test
    void posts() {
        client
                .get()
                .uri("/api/v1/posts")
                .exchange()
                .expectStatus().isOk();
    }

}