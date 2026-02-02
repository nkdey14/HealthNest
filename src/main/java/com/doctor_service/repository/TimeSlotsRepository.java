package com.doctor_service.repository;

import com.doctor_service.entity.TimeSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeSlotsRepository extends JpaRepository<TimeSlots, Long> {

    @Query("""
    SELECT t
    FROM TimeSlots t
    WHERE t.doctorAppointmentSchedule.id = :appointmentScheduleId
""")
    List<TimeSlots> getAllTimeSlots(@Param("appointmentScheduleId") Long appointmentScheduleId);

}
