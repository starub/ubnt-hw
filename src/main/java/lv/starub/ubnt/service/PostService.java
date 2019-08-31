package lv.starub.ubnt.service;

import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostType;
import lv.starub.ubnt.domain.TimePeriod;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface PostService {

    Collection<Post> posts(Optional<TimePeriod> period);

    String count(PostType type, Optional<TimePeriod> period);

    Map<String, Long> top(Long limit);

    String activity(Optional<TimePeriod> period);
}
