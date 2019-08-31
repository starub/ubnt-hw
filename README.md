# Ubiquiti Networks Homework

### Reddit SSE stream (http://stream.pushshift.io) parser & analyzer

**Technology stack**

1. Spring 5 (core framework)
2. Spring Boot 2 (executable fat jar extension with embedded Tomcat server)
3. Spring Webflux (reactive streams library for SSE steam parsing)
4. Hazelcast (cache)
5. Lombok (boilerplate code reduction)
6. Swagger/Swagger UI (API documentation & GUI)
7. Junit 5 (testing framework)
8. AssertJ (assertions library)
9. Mockito (mock testing framework)

**Prerequisites**

1. Java >= 1.8 
2. Gradle >= 5.6.1
3. Git >= 2.17.1

**Installation**

1. `git clone https://github.com/starub/ubnt-hw.git`
2. `cd ubnt-hw`
3. `./gradlew clean build`

**Launch**

1. production mode - `./gradlew clean bootRun`
2. development mode (static cache entries) - `./gradlew -PspringProfile=dev clean bootRun`
3. standalone jar - `./gradlew clean bootJar`, `cd build/libs`, `java -jar -Dspring.profiles.active=prod ubnt-hw.jar` 
4. hit `ctrl-c` to stop

**Tests & Coverage**

1. testing - `./gradlew clean test`, test report directory : `build/reports/tests/test`
2. coverage - `./gradlew clean test jacocoTestReport`, coverage report directory : `build/reports/tests/coverage`

**Usage**

1. Launch application
2. Open URL `http://localhost:8080/swagger-ui.html` for API documentation and GUI
3. Also possible using `curl` utility, e.g `curl http://localhost:8080/api/v1/posts/ALL_TIME`

**Questions/Comments ?**

Please contact me for details

# Thanks!

