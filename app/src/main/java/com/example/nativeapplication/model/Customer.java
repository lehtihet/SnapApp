package com.example.nativeapplication.model;

import java.sql.Timestamp;

public class Customer {

    private Integer id;
    private String firstName;
    private String lastName;
    private String postalCode;
    private String address;
    private String city;
    private String email;
    private String phoneNumber;
    private String timestamp;
    private TimeSlot timeSlot;

    public Customer(Customer customer) {
        this.firstName = customer.getFn();
        this.lastName = customer.getLn();
        this.postalCode = customer.getPostalCode();
        this.address = customer.getAddress();
        this.city = customer.getCity();
        this.email = customer.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.timestamp = customer.getTimestamp();
        this.timeSlot = customer.getTimeslot();
    }

    public Customer(String firstName, String lastName, String postalCode, String address, String city, String email, String phoneNumber, String timestamp, TimeSlot timeSlot) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.address = address;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.timestamp = timestamp;
        this.timeSlot = timeSlot;
    }

    private TimeSlot getTimeslot() {
        return this.timeSlot;
    }

    private String getTimestamp() {
        return this.timestamp;
    }

    private String getPhoneNumber() {
        return this.phoneNumber;
    }

    private String getEmail() {
        return this.email;
    }

    private String getCity() {
        return this.city;
    }

    private String getAddress() {
        return this.address;
    }

    private String getPostalCode() {
        return this.postalCode;
    }

    private String getLn() {
        return this.lastName;
    }

    private String getFn() {
        return this.firstName;
    }


}
