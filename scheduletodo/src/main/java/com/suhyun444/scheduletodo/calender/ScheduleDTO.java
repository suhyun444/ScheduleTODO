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
    private String color;
    private String description;
    public Schedule ToEntity(Todo todo){
        return Schedule.builder().id(id).todo(todo).color(color).description(description).build();
    }
}