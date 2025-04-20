package org.slivkin.vacationpay;

import org.junit.jupiter.api.Test;
import org.slivkin.vacationpay.controller.VacationPayController;
import org.slivkin.vacationpay.model.VacationPayRequest;
import org.slivkin.vacationpay.service.VacationPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.format.DateTimeFormatter;

@WebMvcTest(VacationPayController.class)
class VacationPayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VacationPayService vacationPayService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    void calculateVacationPay_withoutStartDate_returnsCorrectResponse() throws Exception {
        double salary = 60000;
        int days = 14;
        double expectedPay = salary / 29.3 * days;

        when(vacationPayService.calculateVacationPay(
                new VacationPayRequest(salary, days, null))
        ).thenReturn(expectedPay);

        mockMvc.perform(get("/calculate")
                        .param("averageSalary", String.valueOf(salary))
                        .param("vacationDays", String.valueOf(days)))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedPay)));
    }

    @Test
    void calculateVacationPay_withStartDate_returnsCorrectResponse() throws Exception {
        double salary = 60000;
        int days = 3;
        String startDate = "21-04-2025";
        LocalDate parsedDate = LocalDate.parse(startDate, DATE_FORMATTER);
        double expectedPay = salary / 29.3 * 3;

        VacationPayRequest request = new VacationPayRequest(salary, days, parsedDate);

        when(vacationPayService.calculateVacationPay(request))
                .thenReturn(expectedPay);

        mockMvc.perform(get("/calculate")
                        .param("averageSalary", String.valueOf(salary))
                        .param("vacationDays", String.valueOf(days))
                        .param("startDateStr", startDate))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedPay)));
    }

    @Test
    void calculateVacationPay_invalidParams_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("averageSalary", "-10000")
                        .param("vacationDays", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void calculateVacationPay_invalidDateFormat_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/calculate")
                        .param("averageSalary", "50000")
                        .param("vacationDays", "5")
                        .param("startDateStr", "2025-04-15")) // неверный формат
                .andExpect(status().isBadRequest());
    }
}