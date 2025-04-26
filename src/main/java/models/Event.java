package models;

import java.time.LocalDate;

public class Event {
    private int id;
    private LocalDate date;
    private String description;

    public Event(int id, LocalDate date, String description) {
        this.id = id;
        this.date = date;
        this.description = description;
    }

    public Event(LocalDate date, String description) {
        this.date = date;
        this.description = description;
    }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
}
