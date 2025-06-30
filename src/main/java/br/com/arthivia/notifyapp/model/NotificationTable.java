package br.com.arthivia.notifyapp.model;

import java.util.List;

public class NotificationTable {
    private final int id;
    private final String title;
    private final String message;
    private final List<String> dayWeek;
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

    public List<String> getDayWeek() {
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

    public NotificationTable(int id, String title, String message, List<String> dayWeek, String hour, int enable, int notified) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.dayWeek = dayWeek;
        this.hour = hour;
        this.enable = enable;
        this.notified = notified;
    }
}
