package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;

import com.suhyun444.scheduletodo.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
public class Todo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = true)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private Boolean isCompleted;

    @OneToOne(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public TodoDTO ToDTO(){
        return TodoDTO.builder().id(id).name(name).startDate(startDate).endDate(endDate).isCompleted(isCompleted).build();
    }
    public TodoWithScheduleDTO ToTodoWithScheduleDTO()
    {
        return TodoWithScheduleDTO.builder().todo(ToDTO()).schedule(schedule.ToDTO()).build();
    }
    public ScheduleInfoDTO ToScheduleInfoDTO()
    {
        return ScheduleInfoDTO.builder().id(id).name(name).color((schedule == null) ? null : schedule.getColor()).startDate(startDate).endDate(endDate)
        .isCompleted(isCompleted).description((schedule == null) ? null : schedule.getDescription()).build();
    }
}
