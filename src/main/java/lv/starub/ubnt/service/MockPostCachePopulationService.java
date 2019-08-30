package lv.starub.ubnt.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.RequiredArgsConstructor;
import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.PostType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class MockPostCachePopulationService {

    private final HazelcastInstance hazelcastInstance;

    @PostConstruct
    void init() {

        Instant now = Instant.now();

        Post current = new Post();
        current.setTimestamp(now);
        current.setSubreddit("current subreddit");
        current.setBody("current post");
        current.setPostType(PostType.COMMENT);

        Post oneMinute = new Post();
        oneMinute.setTimestamp(now.minusSeconds(PostPeriod.ONE_MINUTE.getValue()));
        oneMinute.setSubreddit("one minute subreddit");
        oneMinute.setBody("one minute post");
        oneMinute.setPostType(PostType.SUBMISSION);

        Post fiveMinutes = new Post();
        fiveMinutes.setTimestamp(now.minusSeconds(PostPeriod.FIVE_MINUTES.getValue()));
        fiveMinutes.setSubreddit("five minutes subreddit");
        fiveMinutes.setBody("five minutes post");
        fiveMinutes.setPostType(PostType.COMMENT);

        Post oneHour = new Post();
        oneHour.setTimestamp(now.minusSeconds(PostPeriod.ONE_HOUR.getValue()));
        oneHour.setSubreddit("one hour subreddit");
        oneHour.setBody("one hour post");
        oneHour.setPostType(PostType.SUBMISSION);

        Post oneDay = new Post();
        oneDay.setTimestamp(now.minusSeconds(PostPeriod.ONE_DAY.getValue()));
        oneDay.setSubreddit("one day subreddit");
        oneDay.setBody("one day post");
        oneDay.setPostType(PostType.COMMENT);

        IMap<Instant, Post> posts = hazelcastInstance.getMap("ALL_POSTS");

        posts.put(oneDay.getTimestamp(), oneDay);
        posts.put(oneHour.getTimestamp(), oneHour);
        posts.put(fiveMinutes.getTimestamp(), fiveMinutes);
        posts.put(oneMinute.getTimestamp(), oneMinute);
        posts.put(current.getTimestamp(), current);
    }

}
