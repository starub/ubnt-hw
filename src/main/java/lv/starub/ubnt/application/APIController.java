package lv.starub.ubnt.application;

import lombok.RequiredArgsConstructor;
import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.PostType;
import lv.starub.ubnt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
class APIController {

    private static final String API_V_1 = "/api/v1/";

    private final PostService cacheService;

    @GetMapping(value = {API_V_1 + "posts/{period}", API_V_1 + "posts"})
    ResponseEntity<Collection<Post>> posts(@PathVariable("period") Optional<PostPeriod> period) {
        return new ResponseEntity<>(cacheService.posts(period), HttpStatus.OK);
    }

    @GetMapping(value = {API_V_1 + "count/{type}/{period}", API_V_1 + "count/{type}"})
    ResponseEntity<String> count(@PathVariable("type") PostType type, @PathVariable("period") Optional<PostPeriod> period) {
        return new ResponseEntity<>(cacheService.count(type, period), HttpStatus.OK);
    }

    @GetMapping(value = {API_V_1 + "top/{maxEntries}", API_V_1 + "top"})
    ResponseEntity<Map<String, Long>> top(@PathVariable("maxEntries") Optional<Long> maxEntries) {
        return new ResponseEntity<>(cacheService.top(maxEntries), HttpStatus.OK);
    }

    @GetMapping(value = {API_V_1 + "activity/{period}", API_V_1 + "activity"})
    ResponseEntity<String> activity(@PathVariable("period") Optional<PostPeriod> period) {
        return new ResponseEntity<>(cacheService.activity(period), HttpStatus.OK);
    }

}
