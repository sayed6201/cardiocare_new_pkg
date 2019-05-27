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
public class AppointmentRequest {
    String appointment_Schedule_Day_Slot_MappingId;
    String doctorId;
    String visitedDate;
    String timeSlot;
    PatientInfo patientInfo;
    String consultationMainServiceCharges;

    public AppointmentRequest(String appointment_Schedule_Day_Slot_MappingId, String doctorId, String visitedDate, String timeSlot, PatientInfo patientInfo, String consultationMainServiceCharges) {
        this.appointment_Schedule_Day_Slot_MappingId = appointment_Schedule_Day_Slot_MappingId;
        this.doctorId = doctorId;
        this.visitedDate = visitedDate;
        this.timeSlot = timeSlot;
        this.patientInfo = patientInfo;
        this.consultationMainServiceCharges = consultationMainServiceCharges;
    }

    public String getAppointment_Schedule_Day_Slot_MappingId() {
        return appointment_Schedule_Day_Slot_MappingId;
    }

    public void setAppointment_Schedule_Day_Slot_MappingId(String appointment_Schedule_Day_Slot_MappingId) {
        this.appointment_Schedule_Day_Slot_MappingId = appointment_Schedule_Day_Slot_MappingId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getVisitedDate() {
        return visitedDate;
    }

    public void setVisitedDate(String visitedDate) {
        this.visitedDate = visitedDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    public String getConsultationMainServiceCharges() {
        return consultationMainServiceCharges;
    }

    public void setConsultationMainServiceCharges(String consultationMainServiceCharges) {
        this.consultationMainServiceCharges = consultationMainServiceCharges;
    }

}
