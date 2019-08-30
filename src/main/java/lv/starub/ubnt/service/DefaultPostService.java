package lv.starub.ubnt.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.query.Predicates;
import lombok.RequiredArgsConstructor;
import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.PostType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Component
@RequiredArgsConstructor
public class DefaultPostService implements PostService {

    private final HazelcastInstance cache;

    @Override
    public Collection<Post> posts(Optional<PostPeriod> period) {
        Instant now = Instant.now();
        return cache.<Instant, Post>getMap("ALL_POSTS").values(Predicates.between("timestamp", now.minusSeconds(period.orElse(PostPeriod.ALL_TIME).getValue()), now));
    }

    @Override
    public String count(PostType type, Optional<PostPeriod> period) {
        Collection<Post> posts = posts(period);
        return String.format("\r\n%d %s(S)\r\n", posts.stream().filter(post -> type == post.getPostType()).count(), type);
    }

    @Override
    public Map<String, Long> top(Optional<Long> maxEntries) {
        Collection<Post> posts = posts(Optional.of(PostPeriod.ALL_TIME));

        return posts.stream()
                .collect(Collectors.groupingBy(Post::getSubreddit, Collectors.counting()))
                .entrySet().stream()
                .sorted(comparing(Map.Entry<String, Long>::getValue).thenComparing(Map.Entry::getKey)
                        .reversed())
                .limit(maxEntries.orElse(Long.MAX_VALUE))
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), LinkedHashMap::putAll);

    }

    @Override
    public String activity(Optional<PostPeriod> period) {
        return String.format("\r\n%d POST(S)\r\n %s %s\r\n", posts(period).size(), count(PostType.SUBMISSION, period), count(PostType.COMMENT, period));
    }

}
