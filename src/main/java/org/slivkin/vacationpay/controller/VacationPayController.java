package org.slivkin.vacationpay.controller;

import org.slivkin.vacationpay.exception.InvalidRequestException;
import org.slivkin.vacationpay.model.VacationPayRequest;
import org.slivkin.vacationpay.service.VacationPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
public class VacationPayController {

    private final VacationPayService vacationPayService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public VacationPayController(VacationPayService vacationPayService) {
        this.vacationPayService = vacationPayService;
    }

    @GetMapping("/calculate")
    public ResponseEntity<Double> calculateVacationPay(
            @RequestParam double averageSalary,
            @RequestParam int vacationDays,
            @RequestParam(required = false) String startDateStr) {

        validateInput(averageSalary, vacationDays);

        VacationPayRequest request = new VacationPayRequest(averageSalary, vacationDays, null);

        if (startDateStr != null && !startDateStr.isEmpty()) {
            request.setStartDate(parseStartDate(startDateStr));
        }

        double vacationPay = vacationPayService.calculateVacationPay(request);
        return ResponseEntity.ok(vacationPay);
    }

    private void validateInput(double averageSalary, int vacationDays) {
        if (averageSalary <= 0 || vacationDays <= 0) {
            throw new InvalidRequestException("Средняя зарплата и количество дней отпуска должны быть положительными");
        }
    }

    private LocalDate parseStartDate(String startDateStr) {
        try {
            return LocalDate.parse(startDateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidRequestException("Неверный формат даты начала отпуска, ожидается формат DD-MM-YYYY");
        }
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequest(InvalidRequestException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}