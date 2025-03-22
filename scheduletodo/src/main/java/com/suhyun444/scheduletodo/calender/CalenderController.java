package com.suhyun444.scheduletodo.calender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


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
    @PostMapping("/add/schedule")
    public String AddSchedule(@ModelAttribute ScheduleDTO scheduleDTO) {
        System.out.println(scheduleDTO.toString()); 
        return "redirect:https://special-spork-p9px6j6vv6rcrjj6-8080.app.github.dev/";
    }
    

}