package com.example.content_server.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OcrCustomer")
public class OcrCustomer {

    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "address")
    private String address;
    @Id
    @Column(name = "id_number")
    private Long idNumber;

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

    @Override
    public String toString() {
        return "OcrCustomer{" +
                "customerName='" + customerName + '\'' +
                ", address='" + address + '\'' +
                ", idNumber='" + idNumber + '\'' +
                '}';
    }
}
