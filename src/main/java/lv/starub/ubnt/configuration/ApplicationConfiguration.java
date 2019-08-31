package lv.starub.ubnt.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapIndexConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
class ApplicationConfiguration {

    @Bean
    Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("lv.starub.ubnt"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfo(
                        "Reddit SSE Stream (http://stream.pushshift.io) reader",
                        "Backend service for parsing and analyzing SSE stream",
                        "1.0",
                        "",
                        new Contact("Stanislavs Rubens", "", "starub@gmail.com"),
                        "",
                        "",
                        Collections.emptyList()
                ));
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
