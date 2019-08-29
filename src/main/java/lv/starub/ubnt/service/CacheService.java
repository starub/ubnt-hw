package lv.starub.ubnt.service;

import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.PostType;
import lv.starub.ubnt.domain.RedditPost;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface CacheService {

    Collection<RedditPost> posts(Optional<PostPeriod> period);

    String count(PostType type, Optional<PostPeriod> period);

    Map<String, Long> top(Optional<Long> limit);

}
