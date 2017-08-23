package net.kuryshev.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.kuryshev.model.CustomDoubleSerializer;

public class Avg {
    double avg;

    @JsonSerialize(using = CustomDoubleSerializer.class)
    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }
}
