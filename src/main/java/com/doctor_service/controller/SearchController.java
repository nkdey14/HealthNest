package com.doctor_service.controller;

import com.doctor_service.entity.Doctor;
import com.doctor_service.entity.DoctorAppointmentSchedule;
import com.doctor_service.entity.TimeSlots;
import com.doctor_service.repository.DoctorRepository;
import com.doctor_service.repository.TimeSlotsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
public class SearchController {

    private final DoctorRepository doctorRepository;
    private final TimeSlotsRepository timeSlotsRepository;

    public SearchController(DoctorRepository doctorRepository,
                            TimeSlotsRepository timeSlotsRepository) {
        this.doctorRepository = doctorRepository;
        this.timeSlotsRepository = timeSlotsRepository;
    }

    // http://localhost:8081/api/v1/doctor/search?specialization=general&areaName=btm
    @GetMapping("/search")
    public List<Doctor> searchDoctors(
            @RequestParam String specialization,
            @RequestParam String areaName
    ) {
        List<Doctor> doctors =
                doctorRepository.findBySpecializationAndArea(specialization, areaName);

        for (Doctor doctor : doctors) {
            List<DoctorAppointmentSchedule> appointmentSchedules =
                    doctor.getAppointmentSchedules();

            for (DoctorAppointmentSchedule schedule : appointmentSchedules) {
                List<TimeSlots> timeSlots =
                        timeSlotsRepository.getAllTimeSlots(schedule.getId());

                // IMPORTANT: attach timeslots to schedule if required
                schedule.setTimeSlots(timeSlots);
            }
        }

        return doctors;
    }
}
