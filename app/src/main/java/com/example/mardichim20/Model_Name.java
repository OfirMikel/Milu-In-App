package com.example.mardichim20;

public class Model_Name {
    private String name;
    private int phone;
    private int number;
    private int id;
    private boolean isSelected;


    public Model_Name(String name, int phone, int number) {
        this.name = name;
        this.phone = phone;
        this.number = number;
    }

    public Model_Name(String name, int phone, int number,int id) {
        this.name = name;
        this.phone = phone;
        this.number = number;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Model_Name " +
                "name='" + name + '\'' +
                ", phone=" + phone +
                ", number=" + number ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
