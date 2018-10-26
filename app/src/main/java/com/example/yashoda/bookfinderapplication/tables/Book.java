package com.example.yashoda.bookfinderapplication.tables;

public class Book
{
    private int bookID;
    private String emailAddress;
    private String title;
    private String author;
    private String summary;
    private Double price;
    private String status;
    private String picture;
    private String locationDetails;

    public Book(int bookID, String emailAddress, String title, String author, String summary, Double price, String status, String picture, String locationDetails) {
        this.bookID = bookID;
        this.emailAddress = emailAddress;
        this.title = title;
        this.author = author;
        this.summary = summary;
        this.price = price;
        this.status = status;
        this.picture = picture;
        this.locationDetails = locationDetails;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    @Override
    public String toString() {
        return "Title: \t"+title +
                "\nAuthor: \t"+author +
                "\nPrice: \t"+price;

               /* "bookID=" + bookID +
                ", emailAddress='" + emailAddress + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", summary='" + summary + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", picture='" + picture + '\'' +
                ", locationDetails='" + locationDetails + '\'' +
                '}';*/
    }
}
