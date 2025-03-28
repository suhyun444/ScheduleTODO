package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ScheduleDTO {
    private Long id;
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
        return Schedule.builder().id(id).name(name).color(color).startDate(startDate).endDate(endDate).description(description).build();
    }
}