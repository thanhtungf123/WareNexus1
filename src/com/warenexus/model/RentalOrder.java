package com.warenexus.model;

import java.util.Date;

public class RentalOrder {
    private int rentalOrderID;
    private int accountID;
    private Integer staffAccountID;
    private int warehouseID;
    private Date startDate;
    private Date endDate;
    private Double deposit;
    private Double totalPrice;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDepositPaid;
    private Date depositPaidAt;
    private boolean isDepositRefunded;
    private Date depositRefundedAt;
    private boolean isNotificationSent;


    public int getRentalOrderID() { return rentalOrderID; }
    public void setRentalOrderID(int rentalOrderID) { this.rentalOrderID = rentalOrderID; }

    public int getAccountID() { return accountID; }
    public void setAccountID(int accountID) { this.accountID = accountID; }

    public Integer getStaffAccountID() { return staffAccountID; }
    public void setStaffAccountID(Integer staffAccountID) { this.staffAccountID = staffAccountID; }

    public int getWarehouseID() { return warehouseID; }
    public void setWarehouseID(int warehouseID) { this.warehouseID = warehouseID; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Double getDeposit() { return deposit; }
    public void setDeposit(Double deposit) { this.deposit = deposit; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public boolean isDepositPaid() { return isDepositPaid; }
    public void setDepositPaid(boolean depositPaid) { isDepositPaid = depositPaid; }

    public Date getDepositPaidAt() { return depositPaidAt; }
    public void setDepositPaidAt(Date depositPaidAt) { this.depositPaidAt = depositPaidAt; }

    public boolean isDepositRefunded() { return isDepositRefunded; }
    public void setDepositRefunded(boolean depositRefunded) { isDepositRefunded = depositRefunded; }

    public Date getDepositRefundedAt() { return depositRefundedAt; }
    public void setDepositRefundedAt(Date depositRefundedAt) { this.depositRefundedAt = depositRefundedAt; }
    public boolean isNotificationSent() { return isNotificationSent; }
    public void setNotificationSent(boolean notificationSent) { isNotificationSent = notificationSent; }

}
