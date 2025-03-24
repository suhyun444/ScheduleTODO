package com.suhyun444.scheduletodo.calender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalenderRepository extends JpaRepository<Schedule,Long> {
        
}
