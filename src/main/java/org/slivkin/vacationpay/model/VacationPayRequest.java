package org.slivkin.vacationpay.model;

import java.time.LocalDate;
import java.util.Objects;

public class VacationPayRequest {

    private double averageSalary;
    private int vacationDays;
    private LocalDate startDate;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VacationPayRequest that = (VacationPayRequest) o;
        return Double.compare(averageSalary, that.averageSalary) == 0 && vacationDays == that.vacationDays && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(averageSalary, vacationDays, startDate);
    }

    public VacationPayRequest() {
    }

    public VacationPayRequest(double averageSalary, int vacationDays, LocalDate startDate) {
        this.averageSalary = averageSalary;
        this.vacationDays = vacationDays;
        this.startDate = startDate;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(int vacationDays) {
        this.vacationDays = vacationDays;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
