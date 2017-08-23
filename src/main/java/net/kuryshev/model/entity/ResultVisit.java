package net.kuryshev.model.entity;

public class ResultVisit {
    private int mark;
    private long visited_at;
    private String place;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public long getVisited_at() {
        return visited_at;
    }

    public void setVisited_at(long visited_at) {
        this.visited_at = visited_at;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
