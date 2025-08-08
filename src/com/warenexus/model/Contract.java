package com.warenexus.model;

public class Contract {
    private int contractID;
    private int rentalOrderID;
    private String contractNumber;
    private String pdfPath;
    private String status;
    private double price; 
    private double deposit;
    private int rentalPeriod; // Thời gian thuê (tính bằng tháng)

    // Getters & Setters
    public int getRentalOrderID() {
        return rentalOrderID;
    }

    public void setRentalOrderID(int rentalOrderID) {
        this.rentalOrderID = rentalOrderID;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public double getDeposit() { return deposit; }

    public void setDeposit(double deposit) { this.deposit = deposit; }

    public int getRentalPeriod() { return rentalPeriod; }

    public void setRentalPeriod(int rentalPeriod) { this.rentalPeriod = rentalPeriod; }
}
