package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalenderService {
    @Autowired
    private CalenderRepository calenderRepository;
    
    public TodoWithScheduleDTO SaveTodoWithSchedule(TodoWithScheduleDTO todoWithScheduleDTO)
    {
        Todo todo = todoWithScheduleDTO.getTodo().ToEntity();
        todo.setSchedule(todoWithScheduleDTO.getSchedule().ToEntity(todo));
        calenderRepository.save(todo);
        return todo.ToTodoWithScheduleDTO();
    }
    public void DeleteSchedule(ScheduleDTO scheduleDTO)
    {
        //calenderRepository.delete(scheduleDTO.ToEntity());
    }
    public List<ScheduleDTO> GetSchedules(LocalDate start, LocalDate end)
    {
        //List<Schedule> entities = calenderRepository.findByEndDateGreaterThanAndStartDateLessThanEqual(start, end);
        List<ScheduleDTO> dtoList = new ArrayList<>();
        //for(int i=0;i<entities.size();++i)
        //{
        //    dtoList.add(entities.get(i).ToDTO());
        //}
        return dtoList;
    }
}
