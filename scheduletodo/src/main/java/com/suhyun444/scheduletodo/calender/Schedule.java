package com.suhyun444.scheduletodo.calender;


import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
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
public class Schedule {
    @Id 
    private Long id;
    @MapsId
    @OneToOne
    @JoinColumn(name="id")
    private Todo todo;
    @Column(nullable = false)
    private String color;
    @Column(nullable = true)
    private String description;
    public ScheduleDTO ToDTO(){
        return ScheduleDTO.builder().id(id).color(color).description(description).build();
    }
}
