package lv.starub.ubnt.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "stream")
public class SSEStreamProperties {

    @Getter
    @Setter
    private String url;

}
