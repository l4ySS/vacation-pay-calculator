package org.slivkin.vacationpay;

import org.slivkin.vacationpay.model.VacationPayRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slivkin.vacationpay.service.HolidayService;
import org.slivkin.vacationpay.service.VacationPayService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VacationPayCalculatorServiceTest {

    @InjectMocks
    private VacationPayService vacationPayCalculatorService;

    @Mock
    private HolidayService holidayService;

    private static final double DELTA = 0.001;

    @Test
    void calculateVacationPay_withoutStartDate() {
        VacationPayRequest request = new VacationPayRequest(60000, 14, null);
        double expectedPay = 60000.0 / 12 * 29.3 * 14;
        assertEquals(expectedPay, vacationPayCalculatorService.calculateVacationPay(request), DELTA);
    }

    @Test
    void calculateVacationPay_withStartDate_noHolidaysOrWeekends() {
        LocalDate startDate = LocalDate.of(2025, 4, 21); // Понедельник
        VacationPayRequest request = new VacationPayRequest(60000, 5, startDate);
        when(holidayService.isHoliday(startDate)).thenReturn(false);
        when(holidayService.isHoliday(startDate.plusDays(1))).thenReturn(false);
        when(holidayService.isHoliday(startDate.plusDays(2))).thenReturn(false);
        when(holidayService.isHoliday(startDate.plusDays(3))).thenReturn(false);
        when(holidayService.isHoliday(startDate.plusDays(4))).thenReturn(false);

        double expectedPay = 60000.0 / 12 * 29.3 / 29.3 * 5;
        assertEquals(expectedPay, vacationPayCalculatorService.calculateVacationPay(request), DELTA);
    }

    @Test
    void calculateVacationPay_withStartDate_withWeekend() {
        LocalDate startDate = LocalDate.of(2025, 4, 25); // Пятница
        VacationPayRequest request = new VacationPayRequest(60000, 3, startDate);
        when(holidayService.isHoliday(startDate)).thenReturn(false); // Пятница
        when(holidayService.isHoliday(startDate.plusDays(1))).thenReturn(false); // Суббота
        when(holidayService.isHoliday(startDate.plusDays(2))).thenReturn(false); // Воскресенье

        double expectedPay = 60000.0 / 12 * 29.3 / 29.3 * 1;
        assertEquals(expectedPay, vacationPayCalculatorService.calculateVacationPay(request), DELTA);
    }

    @Test
    void calculateVacationPay_withStartDate_withHoliday() {
        LocalDate startDate = LocalDate.of(2025, 5, 8); // Четверг
        VacationPayRequest request = new VacationPayRequest(60000, 2, startDate);
        when(holidayService.isHoliday(startDate)).thenReturn(false); // Четверг
        when(holidayService.isHoliday(startDate.plusDays(1))).thenReturn(true);  // Пятница (праздник)

        double expectedPay = 60000.0 / 12 * 29.3 / 29.3 * 1;
        assertEquals(expectedPay, vacationPayCalculatorService.calculateVacationPay(request), DELTA);
    }

    @Test
    void calculateVacationPay_withStartDate_weekendAndHoliday() {
        LocalDate startDate = LocalDate.of(2025, 5, 9); // Пятница (праздник)
        VacationPayRequest request = new VacationPayRequest(60000, 3, startDate);
        when(holidayService.isHoliday(startDate)).thenReturn(true);   // Пятница (праздник)
        when(holidayService.isHoliday(startDate.plusDays(1))).thenReturn(false); // Суббота
        when(holidayService.isHoliday(startDate.plusDays(2))).thenReturn(false); // Воскресенье

        double expectedPay = 60000.0 / 12 * 29.3 / 29.3 * 0;
        assertEquals(expectedPay, vacationPayCalculatorService.calculateVacationPay(request), DELTA);
    }
}