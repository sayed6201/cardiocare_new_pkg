package com.daffodil.cardiocare.Models;

/*
{
        "appointment_Schedule_Day_Slot_MappingId": "636561291401410817",
        "doctorId": "E000018",
        "visitedDate": "2019-04-13",
        "timeSlot": "18:00:00",
        "patientInfo": {
        "sex": "male",
        "patientName": "test by siddik",
        "age2": 35,
        "addressInfo": {
        "mobile": "01847140114"
        }
        },
        "consultationMainServiceCharges": "19"
        }
*/

public class PatientInfo {
    String sex;
    String patientName;
    String age2;
    AddressInfo addressInfo;

    public PatientInfo(String sex, String patientName, String age2, AddressInfo addressInfo) {
        this.sex = sex;
        this.patientName = patientName;
        this.age2 = age2;
        this.addressInfo = addressInfo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAge2() {
        return age2;
    }

    public void setAge2(String age2) {
        this.age2 = age2;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }
}
