-------------------------------------------------
-- Seed sample data for WareNexusDB demo
-------------------------------------------------
INSERT INTO Service(ServiceName) VALUES 
('General Storage'), ('Cold Storage'), ('Bonded Warehouse');

INSERT INTO WarehouseType(ServiceID, TypeName) VALUES
(1, 'Standard Shed'),
(2, 'Temperature Controlled Cold Room'),
(3, 'Customs Bonded Area');

INSERT INTO Warehouse (WarehouseTypeID, Name, AddressLine, District, Size, PricePerUnit, Status, Description)
VALUES
(1, 'Central Da Nang Shed', '71 Nguyễn Văn Linh', 'Hải Châu', 500, 4.5, 'Available', 'Dry goods storage'),
(1, 'Long Hậu Logistic Park', 'KCN Long Hậu', 'Điện Bàn', 1200, 3.2, 'Available', 'Large scale storage'),
(2, 'Cold-Chain Hub', 'KCN Hòa Khánh', 'Liên Chiểu', 350, 6.0, 'Available', 'Perishables'),
(3, 'Bonded Zone A', 'Cảng Tiên Sa', 'Sơn Trà', 800, 5.0, 'Available', 'Customs bonded'),
(1, 'Mini Storage Box', '123 Lê Duẩn', 'Hải Châu', 100, 7.0, 'Available', 'Self-storage units'),
(2, 'Seafood Chill Room', 'Thọ Quang', 'Sơn Trà', 400, 6.5, 'Available', 'Seafood cold storage');
