package com.example.john.medicineapp;

/**
 * Created by john on 11/19/17.
 */

public class Medicine {
    private String Name;
    private int hour;
    private int numberOfDoses;


    public Medicine(String Name, int hour, int numberOfDoses) {
        this.Name = Name;
        this.hour = hour;
        this.numberOfDoses = numberOfDoses;
    }

    public int getHour() {
        return hour;
    }

    public int getNumberOfDoses() {
        return numberOfDoses;
    }

    public String getName() {
        return Name;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setNumberOfDoses(int numberOfDoses) {
        this.numberOfDoses = numberOfDoses;
    }

}
