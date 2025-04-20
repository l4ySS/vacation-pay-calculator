package org.slivkin.vacationpay.service;

import org.slivkin.vacationpay.model.VacationPayRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class VacationPayService {
    private final HolidayService holidayService;
    private static final double AVERAGE_DAYS_IN_MONTH = 29.3;

    public VacationPayService(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    public double calculateVacationPay(VacationPayRequest request) {

        if (request.getStartDate() == null) {
            return calculateBaseVacationPay(request.getAverageSalary(), request.getVacationDays());
        }

        long workingDays = countWorkingDays(request.getStartDate(), request.getVacationDays());
        return calculateBaseVacationPay(request.getAverageSalary(), workingDays);
    }


    private double calculateBaseVacationPay(double averageSalary, long days) {
        return averageSalary / AVERAGE_DAYS_IN_MONTH * days;
    }

    private long countWorkingDays(LocalDate startDate, int vacationDays) {
        return startDate.datesUntil(startDate.plusDays(vacationDays))
                .filter(date -> !isWeekend(date) && !holidayService.isHoliday(date))
                .count();
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
    }
}
