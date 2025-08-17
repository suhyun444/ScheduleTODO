package com.suhyun444.scheduletodo;

import com.suhyun444.scheduletodo.calender.CalenderService;
import com.suhyun444.scheduletodo.calender.ScheduleDTO;
import com.suhyun444.scheduletodo.calender.ScheduleInfoDTO;
import com.suhyun444.scheduletodo.calender.TodoDTO;
import com.suhyun444.scheduletodo.calender.TodoWithScheduleDTO;
import com.suhyun444.scheduletodo.user.User;
import com.suhyun444.scheduletodo.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class CalenderServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("pass");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private CalenderService calenderService;

    @Autowired
    private UserRepository userRepository;

    private String testEmail = "user@test.com";

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        userRepository.save(new User(testEmail));
    }

    @Test
    void testSaveTodoWithSchedule_Save() {
        // given
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setName("Test Todo");
        todoDTO.setStartDate(LocalDate.now());
        todoDTO.setEndDate(LocalDate.now());

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setColor("blue");
        scheduleDTO.setDescription("test schedule");

        TodoWithScheduleDTO combinedDTO = new TodoWithScheduleDTO(todoDTO, scheduleDTO);

        // when
        ScheduleInfoDTO result = calenderService.SaveTodoWithSchedule(combinedDTO, testEmail);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Todo");
        assertThat(result.getColor()).isEqualTo("blue");
    }
    @Test
    void testSaveTodoWithSchedule_Update()
    {
        TodoDTO todoDTO = new TodoDTO(null,"Test",LocalDate.now(),LocalDate.now(),false);
        ScheduleDTO scheduleDTO = new ScheduleDTO(null,"blue","testDescription");
        TodoWithScheduleDTO combinedDTO = new TodoWithScheduleDTO(todoDTO,scheduleDTO);

        ScheduleInfoDTO result = calenderService.SaveTodoWithSchedule(combinedDTO,testEmail);

        todoDTO.setId(result.getId());
        todoDTO.setName("updated");
        scheduleDTO.setColor("green");
        combinedDTO = new TodoWithScheduleDTO(todoDTO,scheduleDTO);

        result = calenderService.SaveTodoWithSchedule(combinedDTO, testEmail);
        assertThat(result.getName()).isEqualTo("updated");
        assertThat(result.getColor()).isEqualTo("green");
    }
    @Test
    void testSaveTodoWithSchedule_NotFoundEmailException()
    {
        TodoDTO todoDTO = new TodoDTO(null,"Test",LocalDate.now(),LocalDate.now(),false);
        ScheduleDTO scheduleDTO = new ScheduleDTO(null,"blue","testDescription");
        TodoWithScheduleDTO combinedDTO = new TodoWithScheduleDTO(todoDTO,scheduleDTO);   
        
        assertThrows(NoSuchElementException.class,()->calenderService.SaveTodoWithSchedule(combined,"notfounded"));
        assertThrows(NoSuchElementException.class,()->calenderService.SaveTodoWithSchedule(combined,null));
    }
    @Test
    void testSaveTodoWithSchedule_NullTodoException()
    {
        ScheduleDTO scheduleDTO = new ScheduleDTO(null,"blue","testDescription");
        TodoWithScheduleDTO combinedDTO = new TodoWithScheduleDTO(null,scheduleDTO);   
        
        assertThrows(NullPointerException.class,()->calenderService.SaveTodoWithSchedule(combined,testEmail));
    }
    @Test
    void testSaveTodo() {
        TodoDTO todoDTO = new TodoDTO(null, "TestTodo", LocalDate.now(), LocalDate.now(), false);
        ScheduleInfoDTO result = calenderService.SaveTodo(todoDTO, testEmail);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TestTodo");

        Optional<User> user = userRepository.findByEmail(testEmail);
        assertThat(user).isPresent();
        assertThat(user.get().getTodos()).hasSize(1);
    }

    @Test
    void testSaveTodo_NewTodo() {
        TodoDTO todoDTO = new TodoDTO(null, "TestTodo", LocalDate.now(), LocalDate.now(), false);

        ScheduleInfoDTO result = calenderService.SaveTodo(todoDTO, testEmail);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("TestTodo");
    }

    @Test
    void testSaveTodo_UpdateExistingTodo() {
        TodoDTO todoDTO = new TodoDTO(null, "OldName", LocalDate.now(), LocalDate.now(), false);
        ScheduleInfoDTO saved = calenderService.SaveTodo(todoDTO, testEmail);

        TodoDTO updateDTO = new TodoDTO(saved.getId(), "UpdatedName",
                saved.getStartDate(), saved.getEndDate(), saved.getIsCompleted());

        ScheduleInfoDTO updated = calenderService.SaveTodo(updateDTO, testEmail);

        assertThat(updated.getName()).isEqualTo("UpdatedName");
    }

    @Test
    void testSaveTodo_UserNotFound_ThrowsException() {
        TodoDTO todoDTO = new TodoDTO(null, "FailCase", LocalDate.now(), LocalDate.now(), false);

        assertThrows(NoSuchElementException.class,()->calenderService.SaveTodo(todoDTO,"notfounded"));
    }
    @Test
    void testDeleteTodo() {
        // given
        TodoDTO todoDTO = new TodoDTO(null, "DeleteTodo", LocalDate.now(), LocalDate.now(), false);
        ScheduleInfoDTO saved = calenderService.SaveTodo(todoDTO, testEmail);

        // when
        calenderService.DeleteTodo(new TodoDTO(saved.getId(), saved.getName(),
                saved.getStartDate(), saved.getEndDate(), saved.isDone()));

        // then
        Optional<User> user = userRepository.findByEmail(testEmail);
        assertThat(user).isPresent();
        assertThat(user.get().getTodos()).isEmpty();
    }
    @Test
    void testDeleteTodo_Success() {
        TodoDTO todoDTO = new TodoDTO(null, "ToDelete", LocalDate.now(), LocalDate.now(), false);
        ScheduleInfoDTO saved = calenderService.SaveTodo(todoDTO, testEmail);

        calenderService.DeleteTodo(new TodoDTO(saved.getId(), saved.getName(),
                saved.getStartDate(), saved.getEndDate(), saved.isDone()));

        assertThat(calenderRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void testDeleteTodo_NotExistingId() {
        TodoDTO todoDTO = new TodoDTO(9999L, "Fake", LocalDate.now(), LocalDate.now(), false);

        // when + then: 예외 발생 (JPA EmptyResultDataAccessException or EntityNotFoundException)
        assertThatThrownBy(() -> calenderService.DeleteTodo(todoDTO))
                .isInstanceOf(Exception.class); // 구체 예외는 구현에 따라 다름
    }

    @Test
    void testDeleteTodo_DeleteTwice() {
        TodoDTO todoDTO = new TodoDTO(null, "DoubleDelete", LocalDate.now(), LocalDate.now(), false);
        ScheduleInfoDTO saved = calenderService.SaveTodo(todoDTO, testEmail);

        calenderService.DeleteTodo(new TodoDTO(saved.getId(), saved.getName(),
                saved.getStartDate(), saved.getEndDate(), saved.isDone()));

        assertThatThrownBy(() -> calenderService.DeleteTodo(
                new TodoDTO(saved.getId(), saved.getName(),
                        saved.getStartDate(), saved.getEndDate(), saved.isDone())))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testDeleteSchedule() {
        // given
        TodoDTO todoDTO = new TodoDTO(null, "ScheduleTodo", LocalDate.now(), LocalDate.now(), false);
        ScheduleDTO scheduleDTO = new ScheduleDTO(null, "blue", "description");
        TodoWithScheduleDTO withScheduleDTO = new TodoWithScheduleDTO(todoDTO, scheduleDTO);

        ScheduleInfoDTO saved = calenderService.SaveTodoWithSchedule(withScheduleDTO, testEmail);

        // when
        calenderService.DeleteSchedule(
                new TodoWithScheduleDTO(
                        new TodoDTO(saved.getId(), saved.getName(), saved.getStartDate(), saved.getEndDate(), saved.isDone()),
                        scheduleDTO
                )
        );

        // then
        Optional<User> user = userRepository.findByEmail(testEmail);
        assertThat(user).isPresent();
        assertThat(user.get().getTodos()).isEmpty();
    }

    @Test
    void testDeleteSchedule_Success() {
        TodoDTO todoDTO = new TodoDTO(null, "ScheduleDelete", LocalDate.now(), LocalDate.now(), false);
        ScheduleDTO scheduleDTO = new ScheduleDTO(null, "blue", "desc");
        TodoWithScheduleDTO combined = new TodoWithScheduleDTO(todoDTO, scheduleDTO);

        ScheduleInfoDTO saved = calenderService.SaveTodoWithSchedule(combined, testEmail);

        calenderService.DeleteSchedule(new TodoWithScheduleDTO(
                new TodoDTO(saved.getId(), saved.getName(), saved.getStartDate(),
                        saved.getEndDate(), saved.isDone()),
                new ScheduleDTO(saved.getId(), saved.getColor(), saved.getDescription())
        ));

        assertThat(calenderRepository.findById(saved.getId())).isEmpty();
        assertThat(scheduleRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void testDeleteSchedule_NoScheduleAttached() {
        TodoDTO todoDTO = new TodoDTO(null, "NoSchedule", LocalDate.now(), LocalDate.now(), false);
        ScheduleInfoDTO saved = calenderService.SaveTodo(todoDTO, testEmail);

        calenderService.DeleteSchedule(new TodoWithScheduleDTO(
                new TodoDTO(saved.getId(), saved.getName(), saved.getStartDate(),
                        saved.getEndDate(), saved.isDone()),
                null
        ));

        assertThat(calenderRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void testDeleteSchedule_InvalidId() {
        TodoDTO fakeTodo = new TodoDTO(9999L, "Invalid", LocalDate.now(), LocalDate.now(), false);
        ScheduleDTO fakeSchedule = new ScheduleDTO(9999L, "red", "fake");

        assertThatThrownBy(() -> calenderService.DeleteSchedule(new TodoWithScheduleDTO(fakeTodo, fakeSchedule)))
                .isInstanceOf(Exception.class);
    }

    @Test
    void testGetTodoList() {
        // given
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setName("Read Book");
        todoDTO.setStartDate(LocalDate.of(2025, 7, 31));
        todoDTO.setEndDate(LocalDate.of(2025, 7, 31));
        calenderService.SaveTodo(todoDTO, testEmail);

        // when
        List<ScheduleInfoDTO> list = calenderService.GetTodoList(testEmail);

        // then
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getName()).isEqualTo("Read Book");
    }

    @Test
    void testGetSchedulesInRange() {
        // given
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setName("Meeting");
        todoDTO.setStartDate(LocalDate.of(2025, 7, 30));
        todoDTO.setEndDate(LocalDate.of(2025, 7, 30));
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setColor("red");
        scheduleDTO.setDescription("Weekly meeting");
        TodoWithScheduleDTO combined = new TodoWithScheduleDTO(todoDTO, scheduleDTO);
        calenderService.SaveTodoWithSchedule(combined, testEmail);

        // when
        List<ScheduleInfoDTO> schedules = calenderService.GetSchedules(
                LocalDate.of(2025, 7, 29),
                LocalDate.of(2025, 8, 1),
                testEmail
        );

        // then
        assertThat(schedules).isNotEmpty();
        assertThat(schedules.get(0).getColor()).isEqualTo("red");
    }
}
