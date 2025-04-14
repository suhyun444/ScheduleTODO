package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//구름ide는 시간당 비용이므로 계속 켜져있는 컨테이너에 적합하지 않다
//gcp사용하는 방법을 알아보자 https://namu.wiki/w/Google%20Cloud%20Platform#s-5
@Repository
public interface CalenderRepository extends JpaRepository<Todo,Long> {
        @Query("SELECT t FROM Todo t JOIN FETCH t.schedule s WHERE t.endDate > :start AND t.startDate <= :end")
        List<Todo> findTodosWithScheduleInRange(@Param("start") LocalDate start,@Param("end") LocalDate end);
}

/*
 * findByEndDateGreaterThanAndStartDateLessThanEqual
 * select * from todo,schedule where todo.id = schedule.id and endDate > start and startDate <= end;
 */