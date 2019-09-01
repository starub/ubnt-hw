# Ubiquiti Networks Homework

### Reddit SSE stream (http://stream.pushshift.io) parser & analyzer

**Technology stack**

1. Java 8
2. Spring 5 (core framework)
3. Spring Boot 2 (executable fat jar extension with embedded Tomcat server)
4. Spring Webflux (reactive streams library for SSE steam parsing)
5. Hazelcast (cache)
6. Lombok (boilerplate code reduction)
7. Swagger/Swagger UI (API documentation & GUI)
8. Junit 5 (testing framework)
9. AssertJ (assertions library)
10. Mockito (mock testing framework)

**Prerequisites**

1. Java >= 1.8 
2. Gradle >= 5.2
3. Git >= 2.0

**Installation**

1. `git clone https://github.com/starub/ubnt-hw.git`
2. `cd ubnt-hw`
3. `./gradlew clean build`

**Launch**

1. gradle environment execution, production mode - `./gradlew clean bootRun`
2. gradle environment execution, development mode (static cache entries) - `./gradlew -PspringProfile=dev clean bootRun`
3. standalone jar - `./gradlew clean bootJar`, `cd build/libs`, `java -jar -Dspring.profiles.active=prod ubnt-hw.jar` 
4. hit `ctrl-c` to stop

**Tests & Coverage**

1. testing - `./gradlew clean test`, test report directory : `build/reports/tests/test`
2. coverage - `./gradlew clean test jacocoTestReport`, coverage report directory : `build/reports/tests/coverage`

**Usage**

1. launch application
2. open URL `http://localhost:8080/swagger-ui.html` for API documentation and GUI
3. also possible using `curl` utility, e.g `curl http://localhost:8080/api/v1/posts/ALL_TIME`

**Questions/Comments ?**

Please contact me for details

# Thanks!

