package com.suhyun444.scheduletodo.calender;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TodoWithScheduleDTO {
    private TodoDTO todo;
    private ScheduleDTO schedule;
}