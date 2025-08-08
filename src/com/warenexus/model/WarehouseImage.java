package com.warenexus.model;

public class WarehouseImage {
    private int imageID;
    private String warehouseID;
    private byte[] imageData;
    private String imageFileName;

    public WarehouseImage(int imageID, String warehouseID, byte[] imageData, String imageFileName) {
        this.imageID = imageID;
        this.warehouseID = warehouseID;
        this.imageData = imageData;
        this.imageFileName = imageFileName;
    }

    public WarehouseImage() {
    }

    public int getImageID() {
        return imageID;
    }

    public String getWarehouseID() {
        return warehouseID;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public void setWarehouseID(String warehouseID) {
        this.warehouseID = warehouseID;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}
