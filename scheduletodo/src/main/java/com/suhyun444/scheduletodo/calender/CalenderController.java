package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
    public ResponseEntity<String> SaveSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
        calenderService.SaveSchedule(scheduleDTO);
        return ResponseEntity.ok("https://special-spork-p9px6j6vv6rcrjj6-8080.app.github.dev/");
    }
    @PostMapping("/delete/schedule")
    public ResponseEntity<String> DeleteSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
        calenderService.DeleteSchedule(scheduleDTO);
        return ResponseEntity.ok("https://special-spork-p9px6j6vv6rcrjj6-8080.app.github.dev/");
    }
    @GetMapping("/get/schedules")
    public ResponseEntity<List<ScheduleDTO>> getMethodName(@RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end) {
        List<ScheduleDTO> result = calenderService.GetSchedules(start,end);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}