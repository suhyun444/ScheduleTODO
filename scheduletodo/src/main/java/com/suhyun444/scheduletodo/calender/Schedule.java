package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//https://cjw-awdsd.tistory.com/46
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = true)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = true)
    private String description;
    public ScheduleDTO ToDTO(){
        return ScheduleDTO.builder().name(name).color(color).startDate(startDate).endDate(endDate).description(description).build();
    }
}
