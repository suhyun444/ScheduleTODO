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
    @Autowired
    private ScheduleRepository scheduleRepository;

    public ScheduleInfoDTO SaveTodoWithSchedule(TodoWithScheduleDTO todoWithScheduleDTO)
    {
        Todo todo = todoWithScheduleDTO.getTodo().ToEntity();
        if(todo.getId() != null)
        {
            Schedule schedule = scheduleRepository.getReferenceById(todo.getId());
            schedule.setColor(todoWithScheduleDTO.getSchedule().getColor());
            schedule.setDescription(todoWithScheduleDTO.getSchedule().getDescription());
            todo.setSchedule(schedule);
        }        
        else
            todo.setSchedule(todoWithScheduleDTO.getSchedule().ToEntity(todo));
        calenderRepository.save(todo);
        return todo.ToScheduleInfoDTO();
    }
    public void DeleteSchedule(TodoWithScheduleDTO todoWithScheduleDTO)
    {
        Todo todo = todoWithScheduleDTO.getTodo().ToEntity();
        calenderRepository.delete(todo);
    }
    public List<ScheduleInfoDTO> GetSchedules(LocalDate start, LocalDate end)
    {
        List<Todo> entities = calenderRepository.findTodosWithScheduleInRange(start, end);
        List<ScheduleInfoDTO> dtoList = new ArrayList<>();
        for(int i=0;i<entities.size();++i)
        {
           dtoList.add(entities.get(i).ToScheduleInfoDTO());
        }
        return dtoList;
    }
    public List<TodoDTO> GetTodoList()
    {
        List<Todo> entities = calenderRepository.findTodoListOrderByDate();
        List<TodoDTO> dtoList = new ArrayList<>();
        for(int i=0;i<entities.size();++i)
        {
            dtoList.add(entities.get(i).ToDTO());
        }
        return dtoList;
    }
}
