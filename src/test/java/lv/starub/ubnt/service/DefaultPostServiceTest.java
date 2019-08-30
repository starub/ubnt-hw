package lv.starub.ubnt.service;

import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.PostType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DefaultPostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("Test retrieval of all posts within one minute period")
    void testPostsRetrievalOneMinuteInterval() {
        Collection<Post> posts = postService.posts(Optional.of(PostPeriod.ONE_MINUTE));
        assertThat(posts).hasSize(1);
        assertThat(posts).extracting(Post::getSubreddit).containsExactly("current subreddit");
    }

    @Test
    @DisplayName("Test retrieval of all posts within five minutes period")
    void testPostsRetrievalFiveMinutesInterval() {
        Collection<Post> posts = postService.posts(Optional.of(PostPeriod.FIVE_MINUTES));
        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(Post::getSubreddit).containsExactly("one minute subreddit", "current subreddit");
    }

    @Test
    @DisplayName("Test retrieval of all posts within one hour period")
    void testPostsRetrievalOneHourInterval() {
        Collection<Post> posts = postService.posts(Optional.of(PostPeriod.ONE_HOUR));
        assertThat(posts).hasSize(3);
        assertThat(posts).extracting(Post::getSubreddit).containsExactly("five minutes subreddit", "one minute subreddit", "current subreddit");
    }

    @Test
    @DisplayName("Test retrieval of all posts within one day period")
    void testPostsRetrievalOneDayInterval() {
        Collection<Post> posts = postService.posts(Optional.of(PostPeriod.ONE_DAY));
        assertThat(posts).hasSize(4);
        assertThat(posts).extracting(Post::getSubreddit).containsExactly("one hour subreddit", "five minutes subreddit", "one minute subreddit", "current subreddit");
    }

    @Test
    @DisplayName("Test retrieval of all posts within none/ALL_TIME period")
    void testAllPostsRetrieval() {
        Collection<Post> posts = postService.posts(Optional.empty());
        assertThat(posts).hasSize(5);
        assertThat(posts).extracting(Post::getSubreddit).containsExactly("one day subreddit", "one hour subreddit", "five minutes subreddit", "one minute subreddit", "current subreddit");

        posts = postService.posts(Optional.of(PostPeriod.ALL_TIME));
        assertThat(posts).hasSize(5);
        assertThat(posts).extracting(Post::getSubreddit).containsExactly("one day subreddit", "one hour subreddit", "five minutes subreddit", "one minute subreddit", "current subreddit");
    }

    @Test
    @DisplayName("Test comments and submissions count within period")
    void testCommentAndSubmissionCount() {
        assertThat(postService.count(PostType.COMMENT, Optional.empty())).isEqualTo("\r\n3 COMMENT(S)\r\n");
        assertThat(postService.count(PostType.SUBMISSION, Optional.of(PostPeriod.ALL_TIME))).isEqualTo("\r\n2 SUBMISSION(S)\r\n");
        assertThat(postService.count(PostType.COMMENT, Optional.of(PostPeriod.ONE_MINUTE))).isEqualTo("\r\n1 COMMENT(S)\r\n");
    }

    @Test
    @DisplayName("Test retrieval of top(N) most active subreddits")
    void testMostActiveSubredditTop() {
        assertThat(postService.top(Optional.empty())).hasSize(5);
        assertThat(postService.top(Optional.of(Long.valueOf(1)))).hasSize(1);
        assertThat(postService.top(Optional.of(Long.valueOf(2)))).hasSize(2);
        assertThat(postService.top(Optional.of(Long.valueOf(3)))).hasSize(3);
        assertThat(postService.top(Optional.of(Long.valueOf(4)))).hasSize(4);
        assertThat(postService.top(Optional.of(Long.valueOf(5)))).hasSize(5);
        assertThat(postService.top(Optional.of(Long.valueOf(6)))).hasSize(5);
    }

    @Test
    @DisplayName("Test Reddit stream activity within period")
    void testRedditStreamActivity() {
        assertThat(postService.activity(Optional.empty())).contains("5 POST(S)").contains("2 SUBMISSION(S)").contains("3 COMMENT(S)");
        assertThat(postService.activity(Optional.of(PostPeriod.ONE_MINUTE))).contains("1 POST(S)").contains("0 SUBMISSION(S)").contains("1 COMMENT(S)");
    }

}