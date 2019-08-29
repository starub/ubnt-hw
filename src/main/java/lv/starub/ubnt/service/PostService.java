package lv.starub.ubnt.service;

import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.PostType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface PostService {

    Collection<Post> posts(Optional<PostPeriod> period);

    String count(PostType type, Optional<PostPeriod> period);

    Map<String, Long> top(Optional<Long> limit);

    String activity(Optional<PostPeriod> period);
}
