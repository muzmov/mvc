package net.kuryshev.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private int id = -1;
    //100 symbols
    private String email;
    //50 symbols
    private String first_name;
    private String last_name;
    // "m" or "f"
    private String gender;
    //01.01.1930 - 01.01.1999
    private long birth_date = Long.MIN_VALUE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(long birth_date) {
        this.birth_date = birth_date;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", first_name='" + first_name + '\'' + ", last_name='" + last_name + '\'' + ", gender='" + gender + '\'' + ", birth_date=" + birth_date + '}';
    }
}
