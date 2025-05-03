package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<ScheduleInfoDTO> SaveSchedule(@RequestBody TodoWithScheduleDTO todoWithScheduleDTO) {
        ScheduleInfoDTO result = calenderService.SaveTodoWithSchedule(todoWithScheduleDTO);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @PostMapping("/delete/schedule")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteSchedule(@RequestBody TodoWithScheduleDTO todoWithScheduleDTO) {
        calenderService.DeleteSchedule(todoWithScheduleDTO);
    }
    @PostMapping("/save/todo")
    @ResponseStatus(HttpStatus.OK)
    public void SaveTodo(@RequestBody TodoDTO todoDTO)
    {
        calenderService.SaveTodo(todoDTO);
    }
    @PostMapping("/delete/todo")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteTodo(@RequestBody TodoDTO todoDTO)
    {
        System.out.println("Deletes todo");
        calenderService.DeleteTodo(todoDTO);
    }
    @GetMapping("/get/schedules")
    public ResponseEntity<List<ScheduleInfoDTO>> getSchedules(@RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end) {
        List<ScheduleInfoDTO> result = calenderService.GetSchedules(start,end);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @GetMapping("/get/todolist")
    public ResponseEntity<List<TodoDTO>> getTodoList(@RequestParam("now") LocalDate now)
    {
        List<TodoDTO> result = calenderService.GetTodoList();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}