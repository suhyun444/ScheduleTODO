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
public class TodoWithScheduleDTO {
    private TodoDTO todo;
    private ScheduleDTO schedule;
}