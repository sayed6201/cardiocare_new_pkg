package com.daffodil.cardiocare.Models;
/*

"labTestReportServices":[
            {
               "reportServiceID":"RS20170000073",
               "testServiceItemID":25,
               "reportNo":"PLR201700000050",
               "":"2.5",
               "labTechnologistID":"E000043",
               "labConsultantID":"E000001",
               "labReleasedByID":null,
               "isResultInputed":true,
               "labTestServiceItemInfo":{
                  "reportingServiceName":"Total WBC Count",
                  "unit":"/cmm",
                  "defaultResult":"",
                  "refferenceRange":"4000- 11000"
               }
            },

 */

public class DetailLabReport {
    public String reportNo;
    public String receiptNo;
    public String charge;
    public String deliveryDateTime;
    public String reportDateTime;
    public String reportTitleName;
    public String specimenName;
    public String result;
    public String unit;
    public String reportingServiceName;
    public String defaultResult;
    public String refferenceRange;

    public DetailLabReport(String reportNo, String receiptNo, String charge, String deliveryDateTime, String reportDateTime, String reportTitleName, String specimenName, String result, String unit, String reportingServiceName, String defaultResult, String refferenceRange) {
        this.reportNo = reportNo;
        this.receiptNo = receiptNo;
        this.charge = charge;
        this.deliveryDateTime = deliveryDateTime;
        this.reportDateTime = reportDateTime;
        this.reportTitleName = reportTitleName;
        this.specimenName = specimenName;
        this.result = result;
        this.unit = unit;
        this.reportingServiceName = reportingServiceName;
        this.defaultResult = defaultResult;
        this.refferenceRange = refferenceRange;
    }

    public DetailLabReport(String reportNo, String receiptNo, String charge, String deliveryDateTime, String reportDateTime, String reportTitleName, String specimenName) {
        this.reportNo = reportNo;
        this.receiptNo = receiptNo;
        this.charge = charge;
        this.deliveryDateTime = deliveryDateTime;
        this.reportDateTime = reportDateTime;
        this.reportTitleName = reportTitleName;
        this.specimenName = specimenName;
    }

    public DetailLabReport(String reportNo, String receiptNo, String charge, String deliveryDateTime, String reportDateTime, String reportTitleName, String specimenName, String result) {
        this.reportNo = reportNo;
        this.receiptNo = receiptNo;
        this.charge = charge;
        this.deliveryDateTime = deliveryDateTime;
        this.reportDateTime = reportDateTime;
        this.reportTitleName = reportTitleName;
        this.specimenName = specimenName;
        this.result = result;

    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public String getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(String reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public String getReportTitleName() {
        return reportTitleName;
    }

    public void setReportTitleName(String reportTitleName) {
        this.reportTitleName = reportTitleName;
    }

    public String getSpecimenName() {
        return specimenName;
    }

    public void setSpecimenName(String specimenName) {
        this.specimenName = specimenName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getReportingServiceName() {
        return reportingServiceName;
    }

    public void setReportingServiceName(String reportingServiceName) {
        this.reportingServiceName = reportingServiceName;
    }

    public String getDefaultResult() {
        return defaultResult;
    }

    public void setDefaultResult(String defaultResult) {
        this.defaultResult = defaultResult;
    }

    public String getRefferenceRange() {
        return refferenceRange;
    }

    public void setRefferenceRange(String refferenceRange) {
        this.refferenceRange = refferenceRange;
    }


    @Override
    public String toString() {
        return "DetailLabReport{" +
                "reportNo='" + reportNo + '\'' +
                ", receiptNo='" + receiptNo + '\'' +
                ", charge='" + charge + '\'' +
                ", deliveryDateTime='" + deliveryDateTime + '\'' +
                ", reportDateTime='" + reportDateTime + '\'' +
                ", reportTitleName='" + reportTitleName + '\'' +
                ", specimenName='" + specimenName + '\'' +
                ", result='" + result + '\'' +
                ", unit='" + unit + '\'' +
                ", reportingServiceName='" + reportingServiceName + '\'' +
                ", defaultResult='" + defaultResult + '\'' +
                ", refferenceRange='" + refferenceRange + '\'' +
                '}';
    }
}
