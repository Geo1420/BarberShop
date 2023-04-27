package com.example.barbershop.models;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentModel {
    private String userId;
    private Timestamp appointmentTime;

    public AppointmentModel(Timestamp appointmentTime, String userId) {
        this.userId = userId;
        this.appointmentTime = appointmentTime;
    }
    public  AppointmentModel() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Timestamp getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Timestamp appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    @NonNull
    @Override
    public String toString() {
        return "Reminder: " + appointmentTime.toDate();
    }
}
