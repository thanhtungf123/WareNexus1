package com.warenexus.model;

import java.util.Date;

public class Warehouse {

    private int    id;
    private String name;
    private String address;
    private String ward;
    private String district;
    private double size;
    private double pricePerUnit;
    private String status;
    private int    warehouseTypeId;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String description;
    private Date   createdAt;

    /* Full constructor used by DAO */
    public Warehouse(int id, String name, String address, String ward, String district,
                 double size, double pricePerUnit, String status, int warehouseTypeId,
                 String imageUrl, Double latitude, Double longitude,
                 String description, Date createdAt) {
    this.id = id; this.name = name; this.address = address;
    this.ward = ward; this.district = district;
    this.size = size; this.pricePerUnit = pricePerUnit; this.status = status;
    this.warehouseTypeId = warehouseTypeId; this.imageUrl = imageUrl;
    this.latitude = latitude;  this.longitude = longitude;
    this.description = description; this.createdAt = createdAt;
}

// 👉 THÊM constructor mặc định này:
public Warehouse() {
    // Dùng cho servlet khi tạo đối tượng rỗng để set dữ liệu từng trường
}

    /* ---------------- getters & setters ---------------- */
    public int    getId()            { return id; }
    public void   setId(int id)      { this.id = id; }

    public String getName()          { return name; }
    public void   setName(String n)  { this.name = n; }

    public String getAddress()       { return address; }
    public void   setAddress(String a){ this.address = a; }

    public String getWard()          { return ward; }
    public void   setWard(String w)  { this.ward = w; }

    public String getDistrict()      { return district; }
    public void   setDistrict(String d){ this.district = d; }

    public double getSize()          { return size; }
    public void   setSize(double s)  { this.size = s; }

    public double getPricePerUnit()  { return pricePerUnit; }
    public void   setPricePerUnit(double p){ this.pricePerUnit = p; }

    public String getStatus()        { return status; }
    public void   setStatus(String s){ this.status = s; }

    public int    getWarehouseTypeId(){ return warehouseTypeId; }
    public void   setWarehouseTypeId(int t){ this.warehouseTypeId = t; }

    public String getImageUrl()      { return imageUrl; }
    public void   setImageUrl(String i){ this.imageUrl = i; }

    public Double getLatitude()      { return latitude; }
    public void   setLatitude(Double lat){ this.latitude = lat; }

    public Double getLongitude()     { return longitude; }
    public void   setLongitude(Double lon){ this.longitude = lon; }

    public String getDescription()   { return description; }
    public void   setDescription(String d){ this.description = d; }

    public Date   getCreatedAt()     { return createdAt; }
    public void   setCreatedAt(Date c){ this.createdAt = c; }
}
