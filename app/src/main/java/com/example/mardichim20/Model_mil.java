package com.example.mardichim20;

import android.os.Parcel;
import android.os.Parcelable;

public class Model_mil implements Parcelable {

    private String name;
    private String who;
    private int phoneNum;
    private String beginDate;
    private String endDate;
    private String place;
    private int id;
    private int pNum;
    private boolean isSelected = false;
    private int arrivals;


    public Model_mil(String name, String beginDate, String endDate, int phoneNum, String who, String place, int id) {
        this.name = name;
        this.who = who;
        this.phoneNum = phoneNum;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.place = place;
        this.id = id;
    }

    public Model_mil(String name, String beginDate, String endDate, int phoneNum, String who, String place, int id, int pNum , int arrivals) {
        this.name = name;
        this.who = who;
        this.phoneNum = phoneNum;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.place = place;
        this.id = id;
        this.pNum = pNum;
        this.arrivals = arrivals;
    }

    public Model_mil(String name, String beginDate, String endDate, int phoneNum, String who, String place, int id,int pNum) {
        this.name = name;
        this.who = who;
        this.phoneNum = phoneNum;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.place = place;
        this.id = id;
        this.pNum = pNum;
    }


    protected Model_mil(Parcel in) {
        name = in.readString();
        who = in.readString();
        phoneNum = in.readInt();
        beginDate = in.readString();
        endDate = in.readString();
        place = in.readString();
        id = in.readInt();
        pNum = in.readInt();
        isSelected = in.readByte() != 0;
        arrivals = in.readInt();
    }

    public static final Creator<Model_mil> CREATOR = new Creator<Model_mil>() {
        @Override
        public Model_mil createFromParcel(Parcel in) {
            return new Model_mil(in);
        }

        @Override
        public Model_mil[] newArray(int size) {
            return new Model_mil[size];
        }
    };

    @Override
    public String toString() {
        return "Model_mil " +
                "name='" + name + '\'' +
                ", who='" + who + '\'' +
                ", phoneNum=" + phoneNum +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", place='" + place + '\'' +
                ", id=" + id + '\'' +
                ", ID num=" + pNum +'\''+
                ", Arrived =" + arrivals;
    }

    public int getArrivals() {
        return arrivals;
    }

    public void setArrivals(int arrivals) {
        this.arrivals = arrivals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public int getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getIdMil() {
        return id;
    }

    public void setIdMil(int id) {
        this.id = id;
    }

    public int getpNum() {
        return pNum;
    }

    public void setpNum(int pNum) {
        this.pNum = pNum;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(who);
        dest.writeInt(phoneNum);
        dest.writeString(beginDate);
        dest.writeString(endDate);
        dest.writeString(place);
        dest.writeInt(id);
        dest.writeInt(pNum);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(arrivals);
    }
}
