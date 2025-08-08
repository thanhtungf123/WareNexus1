package com.warenexus.model;

import java.util.Date;

public class Payment {
    private int paymentID;
    private int rentalOrderID;
    private int paymentCategoryID; // <== mới thêm
    private double amount;
    private Date paymentDate;
    private String status;
    private String transactionID;

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getRentalOrderID() {
        return rentalOrderID;
    }

    public void setRentalOrderID(int rentalOrderID) {
        this.rentalOrderID = rentalOrderID;
    }

    public int getPaymentCategoryID() {
        return paymentCategoryID;
    }

    public void setPaymentCategoryID(int paymentCategoryID) {
        this.paymentCategoryID = paymentCategoryID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
}
