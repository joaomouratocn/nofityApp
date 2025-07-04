package br.com.arthivia.notifyapp.model;

import java.time.DayOfWeek;
import java.util.List;

public class NotificationDao {
    private final int id;
    private final String title;
    private final String message;
    private final List<DayOfWeek> dayWeek;
    private final String hour;
    private final int enable;
    private final int notified;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public List<DayOfWeek> getDayWeek() {
        return dayWeek;
    }

    public String getHour() {
        return hour;
    }

    public int getEnable() {
        return enable;
    }

    public int getNotified() {
        return notified;
    }

    public NotificationDao(int id, String title, String message, List<DayOfWeek> dayWeek, String hour, int enable, int notified) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.dayWeek = dayWeek;
        this.hour = hour;
        this.enable = enable;
        this.notified = notified;
    }
}
