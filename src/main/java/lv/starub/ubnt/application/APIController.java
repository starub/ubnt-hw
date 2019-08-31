package lv.starub.ubnt.application;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lv.starub.ubnt.domain.Post;
import lv.starub.ubnt.domain.PostType;
import lv.starub.ubnt.domain.TimePeriod;
import lv.starub.ubnt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Api(description = "Set of endpoints for retrieving, counting and analyzing posts by certain time periods.")
class APIController {

    private final PostService postService;

    @GetMapping
    @ApiOperation("Returns all cached posts.")
    ResponseEntity<Collection<Post>> allPosts() {
        return new ResponseEntity<>(postService.posts(Optional.empty()), HttpStatus.OK);
    }

    @GetMapping("/{period}")
    @ApiOperation("Returns cached posts within time period.")
    ResponseEntity<Collection<Post>> postsByPeriod(@PathVariable("period") @ApiParam(value = "Time period (ONE_MINUTE,FIVE_MINUTES,ONE_HOUR,ONE_DAY,ALL_TIME)", defaultValue = "ONE_MINUTE", example = "ONE_MINUTE") Optional<TimePeriod> period) {
        return new ResponseEntity<>(postService.posts(period), HttpStatus.OK);
    }

    @GetMapping("/count/{type}")
    @ApiOperation("Returns all cached posts by type.")
    ResponseEntity<String> countByType(@PathVariable("type") @ApiParam(value = "Post type", required = true, defaultValue = "COMMENT", example = "COMMENT") PostType type) {
        return new ResponseEntity<>(postService.count(type, Optional.empty()), HttpStatus.OK);
    }

    @GetMapping("/count/{type}/{period}")
    @ApiOperation("Returns cached posts by type within time period.")
    ResponseEntity<String> count(@PathVariable("type") @ApiParam(value = "Post type", required = true, defaultValue = "COMMENT", example = "COMMENT") PostType type, @PathVariable("period") @ApiParam(value = "Time period (ONE_MINUTE,FIVE_MINUTES,ONE_HOUR,ONE_DAY,ALL_TIME)", defaultValue = "ONE_HOUR", example = "ONE_HOUR") Optional<TimePeriod> period) {
        return new ResponseEntity<>(postService.count(type, period), HttpStatus.OK);
    }

    @GetMapping("/top/{maxEntries}")
    @ApiOperation("Returns top N subreddits by SUBMISSION and COMMENT sum.")
    ResponseEntity<Map<String, Long>> top(@PathVariable("maxEntries") @ApiParam(value = "Max entries", format = "###", required = true, defaultValue = "100", example = "100") Long maxEntries) {
        return new ResponseEntity<>(postService.top(maxEntries), HttpStatus.OK);
    }

    @GetMapping("/activity")
    @ApiOperation("Returns number of cached POSTS,SUBMISSIONS and COMMENTS")
    ResponseEntity<String> activity() {
        return new ResponseEntity<>(postService.activity(Optional.empty()), HttpStatus.OK);
    }

    @GetMapping("/activity/{period}")
    @ApiOperation("Returns number of cached POSTS,SUBMISSIONS and COMMENTS within time period.")
    ResponseEntity<String> activity(@PathVariable("period") @ApiParam(value = "Time period (ONE_MINUTE,FIVE_MINUTES,ONE_HOUR,ONE_DAY,ALL_TIME)", defaultValue = "FIVE_MINUTES", example = "FIVE_MINUTES") Optional<TimePeriod> period) {
        return new ResponseEntity<>(postService.activity(period), HttpStatus.OK);
    }

}
