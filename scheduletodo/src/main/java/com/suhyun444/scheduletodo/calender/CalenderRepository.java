package com.suhyun444.scheduletodo.calender;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//구름ide는 시간당 비용이므로 계속 켜져있는 컨테이너에 적합하지 않다
//gcp사용하는 방법을 알아보자 https://namu.wiki/w/Google%20Cloud%20Platform#s-5
@Repository
public interface CalenderRepository extends JpaRepository<Schedule,Long> {
        List<Schedule> findByEndDateGreaterThanAndStartDateLessThanEqual(LocalDate start,LocalDate end);
}
