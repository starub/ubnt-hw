package lv.starub.ubnt.service;

import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.RedditPost;

import java.util.Collection;

public interface CacheService {

    Collection<RedditPost> posts(PostPeriod period);

    String count(PostPeriod period);
}
