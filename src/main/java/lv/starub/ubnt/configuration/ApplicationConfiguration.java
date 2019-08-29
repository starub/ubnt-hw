package lv.starub.ubnt.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
class ApplicationConfiguration {

    @Bean
    Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("lv.starub.ubnt")).build();
    }

    @Bean
    Config hazelcastConfiguration() {
        return new Config()
                .setInstanceName("REDDIT_STREAM_CACHE")
                .addMapConfig(new MapConfig("ALL_POSTS")
                        .addMapIndexConfig(new MapIndexConfig().setAttribute("timestamp").setOrdered(true)));
    }

    @Bean
    ObjectMapper mapperConfiguration() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

}
