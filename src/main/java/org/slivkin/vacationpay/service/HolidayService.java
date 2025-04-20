package org.slivkin.vacationpay.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayService {
    private static final List<LocalDate> HOLIDAYS = List.of(
            LocalDate.of(2025, 1, 1),  // Новый год
            LocalDate.of(2025, 1, 7),  // Рождество
            LocalDate.of(2025, 2, 23), // День защитника Отечества
            LocalDate.of(2025, 3, 8),  // Международный женский день
            LocalDate.of(2025, 5, 1),  // День труда
            LocalDate.of(2025, 5, 9),  // День Победы
            LocalDate.of(2025, 6, 12)  // День России
    );

    public boolean isHoliday(LocalDate date) {
        return HOLIDAYS.contains(date);
    }
}
