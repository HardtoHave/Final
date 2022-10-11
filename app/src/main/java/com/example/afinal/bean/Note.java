package com.example.afinal.bean;

public class Note {
    //database id
    public final long id;
    //event deadline
    private String deadline;
    //event record time
    private String scheduled;
    //event statue
    private String statue;
    //caption of the event
    private String caption;
    //event setup time
    private String time;


    public Note(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
