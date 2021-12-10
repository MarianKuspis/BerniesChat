package com.utm.utmshop;

import java.util.Calendar;
import java.util.Date;

public class Message {
    private String username;
    private String text;
    private long time;

    Date date = new Date();

    public Message() {}

    public Message(String username, String text, long time) {
        this.username = username;
        this.text = text;
        this.time = new Date().getTime();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = new Date().getTime();
    }
}
