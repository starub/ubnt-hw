package lv.starub.ubnt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
class SSEStreamParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSEStreamParserApplication.class, args);
    }
}
