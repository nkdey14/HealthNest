package com.doctor_service.controller;

import com.doctor_service.entity.Doctor;
import com.doctor_service.entity.DoctorAppointmentSchedule;
import com.doctor_service.entity.TimeSlots;
import com.doctor_service.repository.DoctorRepository;
import com.doctor_service.repository.TimeSlotsRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
        LocalDate today = LocalDate.now();

        List<Doctor> doctors =
                doctorRepository.findBySpecializationAndArea(specialization, areaName);

        for (Doctor doctor : doctors) {
            List<LocalTime> allTimeSlots = new ArrayList<>();

            List<DoctorAppointmentSchedule> appointmentSchedules =
                    doctor.getAppointmentSchedules();

            for (DoctorAppointmentSchedule schedule : appointmentSchedules) {
                LocalDate scheduleDate = schedule.getDate();
                LocalTime now = LocalTime.now();

                List<TimeSlots> timeSlots =
                        timeSlotsRepository.getAllTimeSlots(schedule.getId());

                for(TimeSlots timeSlot : timeSlots) {
                    LocalTime slotTime = timeSlot.getTime();

                    if (scheduleDate.isEqual(today)){
                        if(slotTime.isAfter(now)){
                            allTimeSlots.add(slotTime);
                        }
                    } else if (scheduleDate.isAfter(today)) {
                            allTimeSlots.add(slotTime);
                    }
                }
            }
        }

        return doctors;
    }

    // http://localhost:8081/api/v1/doctor/getDoctorById?id=1
    @GetMapping("/getDoctorById")
    public Doctor getDoctorById(@RequestParam long id){
        return doctorRepository.findById(id).get();
    }
}
