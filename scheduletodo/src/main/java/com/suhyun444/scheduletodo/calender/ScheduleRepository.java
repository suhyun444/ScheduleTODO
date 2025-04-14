package com.suhyun444.scheduletodo.calender;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//getbyid와 findbyid 차이점 찾아보기
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
