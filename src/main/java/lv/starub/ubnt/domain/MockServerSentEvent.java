package lv.starub.ubnt.domain;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

public class MockServerSentEvent implements Serializable {

    private String id;

    private String event;

    private Duration retry;

    private String comment;

    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Duration getRetry() {
        return retry;
    }

    public void setRetry(Duration retry) {
        this.retry = retry;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MockServerSentEvent that = (MockServerSentEvent) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(event, that.event) &&
                Objects.equals(retry, that.retry) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, event, retry, comment, data);
    }

    @Override
    public String toString() {
        return "MockServerSentEvent{" +
                "id='" + id + '\'' +
                ", event='" + event + '\'' +
                ", retry=" + retry +
                ", comment='" + comment + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
