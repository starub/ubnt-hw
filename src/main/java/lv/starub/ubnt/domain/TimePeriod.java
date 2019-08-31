package lv.starub.ubnt.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public enum TimePeriod {

    ONE_MINUTE(60), FIVE_MINUTES(300), ONE_HOUR(3600), ONE_DAY(86400), ALL_TIME(Instant.MAX.getEpochSecond());

    @Getter
    private final long value;

}
