package com.daffodil.cardiocare.Models;

public class TestReportModel {
    public String result;
    public String unit;
    public String reportingServiceName;
    public String defaultResult;
    public String refferenceRange;

    public TestReportModel(String result, String reportingServiceName, String unit,  String defaultResult, String refferenceRange) {
        this.result = result;
        this.unit = unit;
        this.reportingServiceName = reportingServiceName;
        this.defaultResult = defaultResult;
        this.refferenceRange = refferenceRange;
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
}
