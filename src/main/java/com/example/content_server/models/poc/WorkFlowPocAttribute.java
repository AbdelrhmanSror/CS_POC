package com.example.content_server.models.poc;

import java.util.Date;

public class WorkFlowPocAttribute {

    private String customerName;
    private String idNumber;
    private Date birthDate;
    private String residence;
    private String accountCreationBranch;
    private Date requestReceivedDate;
    private Integer orderSerialNumber;
    private Integer customerAnnualIncome;
    private Integer approximateMonthlyDeposit;
    private String notes;
    private String status;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getAccountCreationBranch() {
        return accountCreationBranch;
    }

    public void setAccountCreationBranch(String accountCreationBranch) {
        this.accountCreationBranch = accountCreationBranch;
    }

    public Date getRequestReceivedDate() {
        return requestReceivedDate;
    }

    public void setRequestReceivedDate(Date requestReceivedDate) {
        this.requestReceivedDate = requestReceivedDate;
    }

    public Integer getOrderSerialNumber() {
        return orderSerialNumber;
    }

    public void setOrderSerialNumber(Integer orderSerialNumber) {
        this.orderSerialNumber = orderSerialNumber;
    }

    public Integer getCustomerAnnualIncome() {
        return customerAnnualIncome;
    }

    public void setCustomerAnnualIncome(Integer customerAnnualIncome) {
        this.customerAnnualIncome = customerAnnualIncome;
    }

    public Integer getApproximateMonthlyDeposit() {
        return approximateMonthlyDeposit;
    }

    public void setApproximateMonthlyDeposit(Integer approximateMonthlyDeposit) {
        this.approximateMonthlyDeposit = approximateMonthlyDeposit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "WorkFlowPocAttribute{" +
                "customerName='" + customerName + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", birthDate=" + birthDate +
                ", residence='" + residence + '\'' +
                ", accountCreationBranch='" + accountCreationBranch + '\'' +
                ", requestReceivedDate=" + requestReceivedDate +
                ", orderSerialNumber='" + orderSerialNumber + '\'' +
                ", customerAnnualIncome='" + customerAnnualIncome + '\'' +
                ", approximateMonthlyDeposit='" + approximateMonthlyDeposit + '\'' +
                ", notes='" + notes + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
