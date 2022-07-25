package me.rainj.flowlog.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.rainj.flowlog.exceptions.FlowlogException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Aggregation level.
 */
@RequiredArgsConstructor
@Getter
public enum AggregationLevel {
    /**
     * One minute aggregation.
     */
    ONE_MINUTE(60L),
    /**
     * Five minutes aggregation.
     */
    FIVE_MINUTES(5 * 60L),
    /**
     * One hour aggregation.
     */
    ONE_HOUR(60 * 60L),
    /**
     * One day aggregation.
     */
    ONE_DAY(24 * 60 * 60L);

    /**
     * Seconds of the aggregation level.
     */
    private final Long seconds;

    /**
     * Truncate Instant time to the aggregation level.
     * @param datetime the input date time.
     * @return the truncated datetime.
     */
    public Instant truncateTo(Instant datetime) {
        return Instant.ofEpochSecond((datetime.getEpochSecond() / this.seconds) * this.seconds);
    }

    /**
     * Truncate ZoneDateTime time to the aggregation level.
     * @param dateTime  the input date time.
     * @return the truncated datetime.
     */
    public ZonedDateTime truncateTo(ZonedDateTime dateTime) {
        return this.truncateTo(dateTime.toInstant()).atZone(ZoneId.of("UTC"));
    }

    /**
     * Get the previous aggregation level like a state machine.
     * @return the previous aggregation level.
     */
    public AggregationLevel previous() {
        switch (this) {
            case ONE_MINUTE: throw new FlowlogException("No aggregation level less than ONE_MINUTE");
            case FIVE_MINUTES: return ONE_MINUTE;
            case ONE_HOUR: return FIVE_MINUTES;
            case ONE_DAY: return ONE_HOUR;
            default: throw new FlowlogException("Unknown aggregation level: " + this.name());
        }
    }
}
