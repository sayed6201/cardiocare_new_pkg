package com.daffodil.cardiocare.Models;

import java.io.Serializable;

/*
"receiptNo": "PL20170000002",
"registrationID": "000001-01-15",
"receiptDateTime": "2017-07-14T16:54:42.007"
*/
public class LabReportModel implements Serializable {
    String receiptNo;
    String registrationID;
    String receiptDateTime;

    public LabReportModel(String receiptNo, String registrationID, String receiptDateTime) {
        this.receiptNo = receiptNo;
        this.registrationID = registrationID;
        this.receiptDateTime = receiptDateTime;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getReceiptDateTime() {
        return receiptDateTime;
    }

    public void setReceiptDateTime(String receiptDateTime) {
        this.receiptDateTime = receiptDateTime;
    }
}
