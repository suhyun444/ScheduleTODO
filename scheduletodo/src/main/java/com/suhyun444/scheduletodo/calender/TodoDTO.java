package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TodoDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCompleted;
    public Todo ToEntity(){
        return Todo.builder().id(id).name(name).startDate(startDate).endDate(endDate).isCompleted(isCompleted).build();
    }
}