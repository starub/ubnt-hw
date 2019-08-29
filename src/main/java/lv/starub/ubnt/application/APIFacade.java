package lv.starub.ubnt.application;

import lombok.RequiredArgsConstructor;
import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.PostType;
import lv.starub.ubnt.domain.RedditPost;
import lv.starub.ubnt.service.CacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
class APIFacade {

    private static final String API_V_1 = "/api/v1/";

    private final CacheService cacheService;

    @RequestMapping(value = {API_V_1 + "posts/{period}", API_V_1 + "posts"}, method = RequestMethod.GET)
    ResponseEntity<Collection<RedditPost>> posts(@PathVariable("period") Optional<PostPeriod> period) {
        return new ResponseEntity<>(cacheService.posts(period), HttpStatus.OK);
    }

    @RequestMapping(value = {API_V_1 + "count/{type}/{period}", API_V_1 + "count/{type}"}, method = RequestMethod.GET)
    ResponseEntity<String> count(@PathVariable("type") PostType type, @PathVariable("period") Optional<PostPeriod> period) {
        return new ResponseEntity<>(cacheService.count(type, period), HttpStatus.OK);
    }

    @RequestMapping(value = {API_V_1 + "top/{maxEntries}", API_V_1 + "top"}, method = RequestMethod.GET)
    ResponseEntity<Map<String, Long>> top(@PathVariable("maxEntries") Optional<Long> maxEntries) {
        return new ResponseEntity<>(cacheService.top(maxEntries), HttpStatus.OK);
    }

}
