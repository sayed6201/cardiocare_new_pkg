package com.daffodil.cardiocare.Models;

public class Doctors {
    String employeeId;
    String employeeFullName;
    String shortBio;
    String SpecialityId;
    String specialityFullName;

    public Doctors(){

    }

    public Doctors(String employeeId, String employeeFullName, String shortBio, String specialityId, String specialityFullName) {
        this.employeeId = employeeId;
        this.employeeFullName = employeeFullName;
        this.shortBio = shortBio;
        SpecialityId = specialityId;
        this.specialityFullName = specialityFullName;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public void setSpecialityId(String specialityId) {
        SpecialityId = specialityId;
    }

    public void setSpecialityFullName(String specialityFullName) {
        this.specialityFullName = specialityFullName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public String getShortBio() {
        return shortBio;
    }

    public String getSpecialityId() {
        return SpecialityId;
    }

    public String getSpecialityFullName() {
        return specialityFullName;
    }
}
