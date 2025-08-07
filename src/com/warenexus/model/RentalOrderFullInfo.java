package com.warenexus.model;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class RentalOrderFullInfo {
    private int accountID;
    private int warehouseID;
    private int rentalOrderID;
    private String warehouseName;
    private String warehouseAddress;
    private String customerName;
    private String customerEmail;
    private Date startDate;
    private Date endDate;
    private double deposit;
    private double totalPrice;
    private boolean depositPaid;
    private boolean finalPaid;
    private int daysUntilEndDate;
    private int daysUntilFinalPaymentDue;
    private int imageId;
    
       // Getters and Setters

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }
   

    public int getRentalOrderID() {
        return rentalOrderID;
    }

    public void setRentalOrderID(int rentalOrderID) {
        this.rentalOrderID = rentalOrderID;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isDepositPaid() {
        return depositPaid;
    }

    public void setDepositPaid(boolean depositPaid) {
        this.depositPaid = depositPaid;
    }

    public boolean isFinalPaid() {
        return finalPaid;
    }

    public void setFinalPaid(boolean finalPaid) {
        this.finalPaid = finalPaid;
    }

    public int getDaysUntilEndDate() {
        return daysUntilEndDate;
    }

    public void setDaysUntilEndDate(int daysUntilEndDate) {
        this.daysUntilEndDate = daysUntilEndDate;
    }

    public int getDaysUntilFinalPaymentDue() {
        return daysUntilFinalPaymentDue;
    }

    public void setDaysUntilFinalPaymentDue(int daysUntilFinalPaymentDue) {
        this.daysUntilFinalPaymentDue = daysUntilFinalPaymentDue;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    
    
}
