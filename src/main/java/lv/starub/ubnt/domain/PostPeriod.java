package lv.starub.ubnt.domain;

import java.time.Instant;

public enum PostPeriod {

    ONE_MINUTE(60), FIVE_MINUTES(300), ONE_HOUR(3600), ONE_DAY(86400), ALL_TIME(Instant.MAX.getEpochSecond());

    private long value;

    PostPeriod(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
