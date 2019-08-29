package lv.starub.ubnt.application;

import lv.starub.ubnt.domain.PostPeriod;
import lv.starub.ubnt.domain.RedditPost;
import lv.starub.ubnt.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
class APIFacade {

    private static final String BASE_API_PATH = "/api/v1/";

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = BASE_API_PATH + "posts/{period}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Collection<RedditPost>> posts(@PathVariable("period") PostPeriod period) {
        return new ResponseEntity<>(cacheService.posts(period), HttpStatus.OK);
    }

    @RequestMapping(value = BASE_API_PATH + "count/{period}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<String> count(@PathVariable("period") PostPeriod period) {
        return new ResponseEntity<>(cacheService.count(period), HttpStatus.OK);
    }

}
