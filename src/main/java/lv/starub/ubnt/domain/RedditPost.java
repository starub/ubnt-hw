package lv.starub.ubnt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class RedditPost implements Serializable {

    @JsonProperty
    private String author;

    @JsonProperty
    private String subreddit;

    @JsonProperty
    private String body;

    @JsonProperty
    private String permalink;

    @JsonProperty
    private String score;

    @JsonProperty("total_awards_received")
    private String totalAwardsReceived;

    private Instant timestamp;

    private PostType postType;

}
