package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ScheduleInfoDTO {
    private Long id;
    private String name;
    private String color;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCompleted;
    private String description;
}