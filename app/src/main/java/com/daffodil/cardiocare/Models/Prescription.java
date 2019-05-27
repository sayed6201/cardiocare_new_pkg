package com.daffodil.cardiocare.Models;

public class Prescription {

    String imgUrl;
    String imgName;
    int resource;

    public Prescription() {}

    public Prescription(int resource) {
        this.resource = resource;
    }

    public int getResource() {
        return resource;
    }

    public Prescription(String imgUrl, String imgName) {
        this.imgUrl = imgUrl;
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "imgUrl='" + imgUrl + '\'' +
                ", imgName='" + imgName + '\'' +
                ", resource=" + resource +
                '}';
    }
}
