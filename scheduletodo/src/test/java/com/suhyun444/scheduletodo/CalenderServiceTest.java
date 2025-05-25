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
import com.suhyun444.scheduletodo.calender.ScheduleInfoDTO;
import com.suhyun444.scheduletodo.calender.Todo;
import com.suhyun444.scheduletodo.calender.TodoDTO;
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
    @InjectMocks
    private CalenderService calenderService;

    @Nested
    class SaveTodoMethod
    {
        @Test
        void SaveTodoAndGetScheduleDTO()
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
}
