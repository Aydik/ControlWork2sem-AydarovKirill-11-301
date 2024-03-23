package pack;

import java.time.LocalTime;

// 2. Создать класс BroadcastsTime работающий со временем в формате (hh:mm)
public class BroadcastsTime implements Comparable<BroadcastsTime> {
    private byte hour;
    private byte minutes;

    public BroadcastsTime(byte hour, byte minutes) {
        this.hour = hour;
        this.minutes = minutes;
    }
    public BroadcastsTime(LocalTime time) {
        this.hour = (byte)time.getHour();
        this.minutes = (byte)time.getMinute();
    }

    public byte getHour() {
        return hour;
    }

    public byte getMinutes() {
        return minutes;
    }

    @Override
    public int compareTo(BroadcastsTime otherTime) {
        if (this.hour != otherTime.hour)
            return Byte.compare(this.hour, otherTime.hour);
        else
            return Byte.compare(this.minutes, otherTime.minutes);
    }

    public boolean after(BroadcastsTime t) {
        return this.compareTo(t) > 0;
    }

    public boolean before(BroadcastsTime t) {
        return this.compareTo(t) < 0;
    }

    public boolean between(BroadcastsTime t1, BroadcastsTime t2) {
        return this.compareTo(t1) >= 0 && this.compareTo(t2) <= 0;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minutes);
    }
}

