package lv.starub.ubnt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Data
public class Post implements Serializable {

    @JsonProperty
    @ApiModelProperty(notes = "Post author", example = "starub", required = true)
    private String author;

    @JsonProperty
    @ApiModelProperty(notes = "Subreddit name", example = "AskReddit", required = true, position = 1)
    private String subreddit;

    @JsonProperty
    @ApiModelProperty(notes = "Post content", example = "lorem ipsum bla-bla-bla", required = true, position = 2)
    private String body;

    @JsonProperty
    @ApiModelProperty(notes = "Relative permanent link to post", example = "/r/leagueoflegends/comments/cxhnt7/tickets_for_the_worlds_semifinals/eyl5hhi/", required = true, position = 3)
    private String permalink;

    @JsonProperty
    @ApiModelProperty(notes = "Post score (upvotes/downvotes)", example = "123", required = true, position = 4)
    private String score;

    @JsonProperty("total_awards_received")
    @ApiModelProperty(notes = "Reddit Gold/Silver/Bronze awards for post content", example = "1 gold emblem, 2 bronze emblems", required = true, position = 5)
    private String totalAwardsReceived;

    @NotNull
    @ApiModelProperty(notes = "Caching timestamp", example = "Instant.now()", required = true, position = 6)
    private Instant timestamp;

    @NotNull
    @ApiModelProperty(notes = "Post type", example = "SUBMISSION", required = true, position = 7)
    private PostType postType;

}
