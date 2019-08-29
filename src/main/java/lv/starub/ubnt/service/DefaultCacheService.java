package lv.starub.ubnt.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.Predicates;
import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.PostType;
import lv.starub.ubnt.domain.RedditPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;

@Component
public class DefaultCacheService implements CacheService {

    @Autowired
    HazelcastInstance cache;

    @Override
    public Collection<RedditPost> posts(PostPeriod period) {
        Instant now = Instant.now();
        return cache.<Instant, RedditPost>getMap("ALL_POSTS").values(Predicates.between("timestamp", now.minusSeconds(period.getValue()), now));
    }

    @Override
    public String count(PostPeriod period) {

        Collection<RedditPost> posts = posts(period);

        long comments = posts.stream().filter(post -> PostType.COMMENT == post.getPostType()).count();
        long submissions = posts.stream().filter(post -> PostType.SUBMISSION == post.getPostType()).count();

        return String.format("comments : %d, submissions : %d", comments, submissions);
    }
}
