package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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


    @GetMapping({"/","/login/google"})
    public String start()
    {
        return "redirect:/oauth2/authorization/google";
    }
    @GetMapping("/calender")
    public String CalenderView()
    {
        return "index";
    }
    @PostMapping("/save/schedule")
    public ResponseEntity<ScheduleInfoDTO> SaveSchedule(@AuthenticationPrincipal OAuth2User pricipal, @RequestBody TodoWithScheduleDTO todoWithScheduleDTO) {
        ScheduleInfoDTO result = calenderService.SaveTodoWithSchedule(todoWithScheduleDTO,pricipal.getAttribute("email"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @PostMapping("/delete/schedule")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteSchedule(@RequestBody TodoWithScheduleDTO todoWithScheduleDTO) {
        System.out.println(todoWithScheduleDTO.getTodo().getId());
        calenderService.DeleteSchedule(todoWithScheduleDTO);
    }
    @PostMapping("/save/todo")
    public ResponseEntity<ScheduleInfoDTO> SaveTodo(@AuthenticationPrincipal OAuth2User pricipal, @RequestBody TodoDTO todoDTO)
    {
        ScheduleInfoDTO result = calenderService.SaveTodo(todoDTO,pricipal.getAttribute("email"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @PostMapping("/delete/todo")
    @ResponseStatus(HttpStatus.OK)
    public void DeleteTodo(@RequestBody TodoDTO todoDTO)
    {
        System.out.println("Deletes todo");
        calenderService.DeleteTodo(todoDTO);
    }
    @GetMapping("/get/schedules")
    public ResponseEntity<List<ScheduleInfoDTO>> getSchedules(@AuthenticationPrincipal OAuth2User pricipal,@RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end) {
        List<ScheduleInfoDTO> result = calenderService.GetSchedules(start,end,pricipal.getAttribute("email"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    @GetMapping("/get/todolist")
    public ResponseEntity<List<ScheduleInfoDTO>> getTodoList(@AuthenticationPrincipal OAuth2User pricipal)
    {
        List<ScheduleInfoDTO> result = calenderService.GetTodoList(pricipal.getAttribute("email"));
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    
}