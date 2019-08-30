package lv.starub.ubnt.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.RequiredArgsConstructor;
import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostPeriod;
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
        current.setBody("current post");

        Post oneMinute = new Post();
        oneMinute.setTimestamp(now.minusSeconds(PostPeriod.ONE_MINUTE.getValue()));
        oneMinute.setBody("one minute post");

        Post fiveMinutes = new Post();
        fiveMinutes.setTimestamp(now.minusSeconds(PostPeriod.FIVE_MINUTES.getValue()));
        fiveMinutes.setBody("five minutes post");

        Post oneHour = new Post();
        oneHour.setTimestamp(now.minusSeconds(PostPeriod.ONE_HOUR.getValue()));
        oneHour.setBody("one hour post");

        Post oneDay = new Post();
        oneDay.setTimestamp(now.minusSeconds(PostPeriod.ONE_DAY.getValue()));
        oneDay.setBody("one day post");

        Post allTime = new Post();
        allTime.setTimestamp(now.minusSeconds(PostPeriod.ALL_TIME.getValue()));
        allTime.setBody("all time post");

        IMap<Instant, Post> posts = hazelcastInstance.getMap("ALL_POSTS");

        posts.put(allTime.getTimestamp(), allTime);
        posts.put(oneDay.getTimestamp(), oneDay);
        posts.put(oneHour.getTimestamp(), oneHour);
        posts.put(fiveMinutes.getTimestamp(), fiveMinutes);
        posts.put(oneMinute.getTimestamp(), oneMinute);
        posts.put(current.getTimestamp(), current);
    }

}
