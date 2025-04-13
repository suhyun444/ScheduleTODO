package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;



@Controller
public class CalenderController
{
    @Autowired
    private CalenderService calenderService;

    @GetMapping("")
    public String start()
    {
        return "index";
    }
    @PostMapping("/save/schedule")
    public ResponseEntity<TodoWithScheduleDTO> SaveSchedule(@RequestBody TodoWithScheduleDTO todoWithScheduleDTO) {
        System.out.println("asdasdasdasdasdadsasdasd");
        System.out.println(todoWithScheduleDTO);
        System.out.println(todoWithScheduleDTO.getTodo().getName());
        //TodoWithScheduleDTO result = calenderService.SaveTodoWithSchedule(scheduleDTO);
        //return new ResponseEntity<>(result,HttpStatus.OK);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
    @PostMapping("/delete/schedule")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
        calenderService.DeleteSchedule(scheduleDTO);
    }
    @GetMapping("/get/schedules")
    public ResponseEntity<List<ScheduleDTO>> getMethodName(@RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end) {
        List<ScheduleDTO> result = calenderService.GetSchedules(start,end);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}