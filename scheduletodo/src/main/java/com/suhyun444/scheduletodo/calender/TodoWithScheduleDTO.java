package com.suhyun444.scheduletodo.calender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoWithScheduleDTO {
    private TodoDTO todo;
    private ScheduleDTO schedule;
}