package com.example.content_server.models.ocr;

import javax.persistence.*;


@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public class OcrCustomer {

    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "address")
    private String address;
    @Id
    @Column(name = "id_number")
    private Long idNumber;
    @Column(name = "existence_times")
    private Integer existenceTimes = 0;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(Long idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getExistenceTimes() {
        return existenceTimes;
    }

    public void setExistenceTimes(Integer existenceTimes) {
        this.existenceTimes = existenceTimes;
    }

    @Override
    public String toString() {
        return "OcrCustomer{" +
                "customerName='" + customerName + '\'' +
                ", address='" + address + '\'' +
                ", idNumber=" + idNumber +
                ", existenceTimes=" + existenceTimes +
                '}';
    }
}
