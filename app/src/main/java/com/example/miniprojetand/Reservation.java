package com.example.miniprojetand;

public class Reservation {

    public String hotelName, hotelPrice;
    public String firstName, lastName, phone, email, checkInDate, checkOutDate;
    public String adultsCount, childrenCount, roomsCount;

    public Reservation(String hotelName, String hotelPrice,
                       String firstName, String lastName, String phone, String email,
                       String checkInDate, String checkOutDate,
                       String adultsCount, String childrenCount, String roomsCount) {
        this.hotelName = hotelName;
        this.hotelPrice = hotelPrice;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.adultsCount = adultsCount;
        this.childrenCount = childrenCount;
        this.roomsCount = roomsCount;
    }

    public Reservation(String hotelName, String hotelPrice, String checkInDate) {
        this.hotelName = hotelName;
        this.hotelPrice = hotelPrice;
        this.checkInDate = checkInDate;
    }
    @Override
    public String toString() {
        return "Reservation{" +
                "hotelName='" + hotelName + '\'' +
                ", hotelPrice='" + hotelPrice + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", checkInDate='" + checkInDate + '\'' +
                ", checkOutDate='" + checkOutDate + '\'' +
                ", adultsCount='" + adultsCount + '\'' +
                ", childrenCount='" + childrenCount + '\'' +
                ", roomsCount='" + roomsCount + '\'' +
                '}';
    }
}