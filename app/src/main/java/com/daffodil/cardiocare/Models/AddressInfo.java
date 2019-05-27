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


public class AddressInfo {
    String mobile;

    public AddressInfo(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
