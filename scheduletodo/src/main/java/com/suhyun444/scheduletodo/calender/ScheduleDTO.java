package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDTO {
    private String name;
    private String color;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    @Override
    public String toString() {
        return name + " " + color + " " + startDate.toString() + " ~ " + endDate.toString() + " " + description;
    }
    public Schedule ToEntity(){
        return Schedule.builder().name(name).color(color).startDate(startDate).endDate(endDate).description(description).build();
    }
}