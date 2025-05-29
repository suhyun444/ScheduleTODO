package com.suhyun444.scheduletodo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.suhyun444.scheduletodo.calender.CalenderService;
import com.suhyun444.scheduletodo.calender.Schedule;
import com.suhyun444.scheduletodo.calender.ScheduleDTO;
import com.suhyun444.scheduletodo.calender.ScheduleInfoDTO;
import com.suhyun444.scheduletodo.calender.ScheduleRepository;
import com.suhyun444.scheduletodo.calender.Todo;
import com.suhyun444.scheduletodo.calender.TodoDTO;
import com.suhyun444.scheduletodo.calender.TodoWithScheduleDTO;
import com.suhyun444.scheduletodo.calender.CalenderRepository;
import com.suhyun444.scheduletodo.user.UserRepository;
import com.suhyun444.scheduletodo.user.User;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CalenderServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CalenderRepository calenderRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @InjectMocks
    private CalenderService calenderService;

    @Nested
    class SaveTodoMethod
    {
        @Test
        void SaveTodo_TodoIdIsNull()
        {
            String email = "test@test.com";
            User mockUser = User.builder().id(1L).email(email).build();
    
            TodoDTO todo = new TodoDTO();
            todo.setName("test");
            todo.setStartDate(LocalDate.of(2025,5,12));
            todo.setEndDate(LocalDate.of(2025,5,20));
            todo.setCompleted(false);
    
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
    
            ScheduleInfoDTO result = calenderService.SaveTodo(todo, email);
            assertThat(result.getName()).isEqualTo("test");
            verify(calenderRepository).save(any(Todo.class));
        }
        @Test
        void SaveTodo_TodoIdNull_WithSchedule()
        {
            String email = "test@test.com";
            User mockUser = User.builder().id(1L).email(email).build();
            Schedule mockSchedule = Schedule.builder().id(1L).color("FFFFFF").description("descriptiontest").build();
            TodoDTO todo = new TodoDTO();
            todo.setId(1L);
            todo.setName("test");
            todo.setStartDate(LocalDate.of(2025,5,12));
            todo.setEndDate(LocalDate.of(2025,5,20));
            todo.setCompleted(false);
    
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
            when(scheduleRepository.findById(1L)).thenReturn(Optional.of(mockSchedule));
            
            ScheduleInfoDTO result = calenderService.SaveTodo(todo, email);
            assertThat(result.getDescription()).isEqualTo("descriptiontest");
            verify(calenderRepository).save(any(Todo.class));
        }
        @Test
        void SaveTodo_TodoIdNotNull_WithNullSchedule()
        {
            String email = "test@test.com";
            User mockUser = User.builder().id(1L).email(email).build();
            
            TodoDTO todo = new TodoDTO();
            todo.setId(1L);
            todo.setName("test");
            todo.setStartDate(LocalDate.of(2025,5,12));
            todo.setEndDate(LocalDate.of(2025,5,20));
            todo.setCompleted(false);
            
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
            when(scheduleRepository.findById(todo.getId())).thenReturn(Optional.empty());
    
            ScheduleInfoDTO result = calenderService.SaveTodo(todo, email);
            assertThat(result.getDescription()).isEqualTo(null);
            verify(calenderRepository).save(any(Todo.class));
        }
        @Test
        void SaveTodo_NullEmailException()
        {
            TodoDTO todo = new TodoDTO();
    
            when(userRepository.findByEmail(null)).thenReturn(Optional.empty());
    
            assertThrows(NoSuchElementException.class,()->calenderService.SaveTodo(todo,null));
        }
        @Test
        void SaveTodo_NullTodoException()
        {
            String email = "test@test.com";
            User mockUser = User.builder().id(1L).email(email).build();
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        
            assertThrows(NullPointerException.class,()->calenderService.SaveTodo(null,email));
        }
        @Test
        void SaveTodo_NotFoundEmailException()
        {
            TodoDTO todo = new TodoDTO();
        
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        
            assertThrows(NoSuchElementException.class,()->calenderService.SaveTodo(todo,"notfound@not.not"));
        }
    }
    @Nested
    class SaveTodoWithScheduleMethod
    {
        @Test
        void SaveTodoWithSchedule_TodoIdIsNull()
        {
            User mockUser = User.builder().id(1L).email("test@test.com").build();
            TodoWithScheduleDTO todoWithScheduleDTO = TodoWithScheduleDTO.builder()
            .todo(TodoDTO.builder().id(null).name("test").startDate(LocalDate.now()).endDate(LocalDate.now()).isCompleted(false).build())
            .schedule(ScheduleDTO.builder().id(null).color("FFFFFF").description("description").build()).build();
            String email = "test@test.com";

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

            ScheduleInfoDTO result = calenderService.SaveTodoWithSchedule(todoWithScheduleDTO,email);
            assertThat(result.getName()).isEqualTo("test");
            assertThat(result.getDescription()).isEqualTo("description");
        }
        @Test
        void SaveTodoWithSchedule_TodoIdNotNull()
        {
            User mockUser = User.builder().id(1L).email("test@test.com").build();
            Schedule schedule = Schedule.builder().id(1L).color("FFFFFF").description("des").build();
            TodoWithScheduleDTO todoWithScheduleDTO = TodoWithScheduleDTO.builder()
            .todo(TodoDTO.builder().id(1L).name("test").startDate(LocalDate.now()).endDate(LocalDate.now()).isCompleted(false).build())
            .schedule(ScheduleDTO.builder().id(1L).color("FFFFFF").description("description").build()).build();
            String email = "test@test.com";

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
            when(scheduleRepository.getReferenceById(1L)).thenReturn(schedule);

            ScheduleInfoDTO result = calenderService.SaveTodoWithSchedule(todoWithScheduleDTO, email);
            assertThat(result.getName()).isEqualTo("test");
            assertThat(result.getDescription()).isEqualTo("description");
        }
        @Test
        void SaveTodoWithSchedule_NotFoundEmailException()
        {
            TodoWithScheduleDTO todoWithScheduleDTO = TodoWithScheduleDTO.builder()
            .todo(TodoDTO.builder().id(1L).name("test").startDate(LocalDate.now()).endDate(LocalDate.now()).isCompleted(false).build())
            .schedule(ScheduleDTO.builder().id(1L).color("FFFFFF").description("description").build()).build();
            String email = "test@test.com";

            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            assertThrows(NoSuchElementException.class,()->calenderService.SaveTodoWithSchedule(todoWithScheduleDTO, email));
        }
        @Test
        void SaveTodoWithSchedule_NullEmailException()
        {
            TodoWithScheduleDTO todoWithScheduleDTO = TodoWithScheduleDTO.builder()
            .todo(TodoDTO.builder().id(1L).name("test").startDate(LocalDate.now()).endDate(LocalDate.now()).isCompleted(false).build())
            .schedule(ScheduleDTO.builder().id(1L).color("FFFFFF").description("description").build()).build();

            assertThrows(NoSuchElementException.class,()->calenderService.SaveTodoWithSchedule(todoWithScheduleDTO, null));
        }
        @Test
        void SaveTodoWithSchedule_NullTodoException()
        {
            User mockUser = User.builder().id(1L).email("test@test.com").build();
            String email = "test@test.com";

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

            assertThrows(NullPointerException.class, () -> calenderService.SaveTodoWithSchedule(null, email));
        }
    }
    @Nested
    class DeleteScheduleMethod
    {
        @Test
        void DeleteSchedule_Success()
        {
            TodoWithScheduleDTO todoWithScheduleDTO = TodoWithScheduleDTO.builder()
            .todo(TodoDTO.builder().id(1L).name("test").startDate(LocalDate.now()).endDate(LocalDate.now()).isCompleted(false).build())
            .schedule(ScheduleDTO.builder().id(1L).color("FFFFFF").description("description").build()).build();
            
            calenderService.DeleteSchedule(todoWithScheduleDTO);            
            verify(calenderRepository).delete(any(Todo.class));
        }
        @Test
        void DeleteSchedule_NullTodoException()
        {
            assertThrows(NullPointerException.class,()->calenderService.DeleteSchedule(null));
        }
    }
    @Nested
    class DeleteTodoMethod
    {
        @Test
        void DeleteTodo_Success()
        {
            TodoDTO todoDTO = TodoDTO.builder().id(1L).name("test").startDate(LocalDate.now()).endDate(LocalDate.now()).isCompleted(false).build();

            calenderService.DeleteTodo(todoDTO);            
            verify(calenderRepository).delete(any(Todo.class));
        }
        @Test
        void DeleteTodo_NullTodoException()
        {
            assertThrows(NullPointerException.class,()->calenderService.DeleteTodo(null));
        }
    }
}
