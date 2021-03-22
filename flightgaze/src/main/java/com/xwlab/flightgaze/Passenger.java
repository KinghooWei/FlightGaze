package com.xwlab.flightgaze;

import android.graphics.Bitmap;

public class Passenger {

    private String passengerId;

    private String passengerName;

    private Bitmap passengerFace;

    private double[] faceFeature;

    private double[] featureWithMask;

    private String flightClass;

    private String seat;

    private short gate;

    private boolean mask;

    public Passenger(String passengerId, String passengerName, Bitmap passengerFace, double[] faceFeature, double[] featureWithMask, String flightClass, String seat, short gate) {
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.passengerFace = passengerFace;
        this.faceFeature = faceFeature;
        this.featureWithMask = featureWithMask;
        this.flightClass = flightClass;
        this.seat = seat;
        this.gate = gate;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "passengerId='" + passengerId + '\'' +
                ", passengerName='" + passengerName + '\'' +
                ", seat='" + seat + '\'' +
                '}';
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Bitmap getPassengerFace() {
        return passengerFace;
    }

    public void setPassengerFace(Bitmap passengerFace) {
        this.passengerFace = passengerFace;
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

    public String getFlightClass() {
        return flightClass;
    }

    public void setFlightClass(String flightClass) {
        this.flightClass = flightClass;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public short getGate() {
        return gate;
    }

    public void setGate(short gate) {
        this.gate = gate;
    }

    public boolean isMask() {
        return mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }
}
