package com.example.nativeapplication.model;

public class TimeSlot {
    private Integer id;
    private String dateTime;
    private Boolean booked;
    private ServiceProfessional serviceProfessional;

    public Integer getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Boolean getBooked() {
        return booked;
    }

    public ServiceProfessional getServiceProfessional() {
        return serviceProfessional;
    }

    public void setId(Integer id) { this.id = id; }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setBooked(Boolean booked) {
        this.booked = booked;
    }

    public void setServiceProfessional(ServiceProfessional serviceProfessional) {
        this.serviceProfessional = serviceProfessional;
    }
}
