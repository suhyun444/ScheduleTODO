package com.suhyun444.scheduletodo.calender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private Long id;
    private String color;
    private String description;
    public Schedule ToEntity(Todo todo){
        return Schedule.builder().id(id).todo(todo).color(color).description(description).build();
    }
}