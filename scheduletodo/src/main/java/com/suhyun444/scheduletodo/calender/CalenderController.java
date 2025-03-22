package com.suhyun444.scheduletodo.calender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
    public ResponseEntity<Schedule> AddSchedule(@RequestBody String entity) {
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    

}