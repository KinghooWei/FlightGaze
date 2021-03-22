package com.xwlab.blacklist;

import android.graphics.Bitmap;

public class KeyPersonnel {

    private String identifyId;

    private String name;

    private Bitmap face;

    private double[] faceFeature;

    private double[] featureWithMask;

    private String gender;

    private int age;

    private String type;

    private String caseFile;

    public KeyPersonnel(String identifyId, String name, Bitmap face, double[] faceFeature, double[] featureWithMask, String gender, int age, String type, String caseFile) {
        this.identifyId = identifyId;
        this.name = name;
        this.face = face;
        this.faceFeature = faceFeature;
        this.featureWithMask = featureWithMask;
        this.gender = gender;
        this.age = age;
        this.type = type;
        this.caseFile = caseFile;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "identifyId='" + identifyId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birth='" + age + '\'' +
                ", type='" + type + '\'' +
                ", caseFile='" + caseFile + '\'' +
                '}';
    }

    public String getIdentifyId() {
        return identifyId;
    }

    public void setIdentifyId(String identifyId) {
        this.identifyId = identifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getFace() {
        return face;
    }

    public void setFace(Bitmap face) {
        this.face = face;
    }

    public double[] getFaceFeature() {
        return faceFeature;
    }

    public void setFaceFeature(double[] faceFeature) {
        this.faceFeature = faceFeature;
    }

    public double[] getFeatureWithMask() {
        return featureWithMask;
    }

    public void setFeatureWithMask(double[] featureWithMask) {
        this.featureWithMask = featureWithMask;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaseFile() {
        return caseFile;
    }

    public void setCaseFile(String caseFile) {
        this.caseFile = caseFile;
    }
}
