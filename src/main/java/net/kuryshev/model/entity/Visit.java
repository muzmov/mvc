package net.kuryshev.model.entity;

public class Visit {
    private int id = -1;
    private int location = -1;
    private int user = -1;
    private long visited_at = Long.MIN_VALUE;
    private int mark = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public long getVisited_at() {
        return visited_at;
    }

    public void setVisited_at(long visited_at) {
        this.visited_at = visited_at;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
