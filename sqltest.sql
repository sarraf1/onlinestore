DELETE Inventory;
DELETE Shipment;
DELETE Orders;
DELETE Catalog;
DELETE Accessories;
DELETE Description;
DELETE Customers;
DELETE Discount;
DELETE History;
DELETE Cart;

INSERT INTO Inventory VALUES ( 'AA00000','Verizon','a01',10,1,12,'a1',0);
INSERT INTO Inventory VALUES ( 'AA00001','Verizon','a02',10,1,12,'a2',0);
INSERT INTO Inventory VALUES ( 'AA00002','Verizon','a03',10,1,12,'a3',0);
INSERT INTO Inventory VALUES ( 'AA00003','Sprint','a01',10,1,12,'b1',0);
INSERT INTO Inventory VALUES ( 'AA00004','Sprint','a02',10,1,12,'b2',0);
INSERT INTO Inventory VALUES ( 'AA00005','Sprint','a03',10,1,12,'b3',0);
INSERT INTO Inventory VALUES ( 'AA00006','Dell','a01',1,1,12,'c1',0);

INSERT INTO Shipment VALUES ( ‘1‘,‘DellDepot‘,‘Dell‘,‘a01‘,5);

INSERT INTO Catalog VALUES ( ‘AA00000‘,‘phone‘,‘Verizon‘,‘a01‘,10.50,2);
INSERT INTO Catalog VALUES ( ‘AA00001‘,‘phone‘,‘Verizon‘,‘a02‘,11,1);
INSERT INTO Catalog VALUES ( ‘AA00002‘,‘phone‘,‘Verizon‘,‘a03‘,50,10);
INSERT INTO Catalog VALUES ( ‘AA00003‘,‘phone‘,‘Sprint‘,‘a01‘,100,2);
INSERT INTO Catalog VALUES ( ‘AA00004‘,‘phone‘,‘Sprint‘,‘a02‘,99.99,6);
INSERT INTO Catalog VALUES ( ‘AA00005‘,‘phone‘,‘Sprint‘,‘a03‘,125.00,2);
INSERT INTO Catalog VALUES ( ‘AA00006‘,‘computer‘,‘Dell‘,‘a01‘,100.01,24);

INSERT INTO Accessories VALUES ( ‘AA00000‘,‘AA00001‘);
INSERT INTO Accessories VALUES ( ‘AA00000‘,‘AA00002‘);
INSERT INTO Accessories VALUES ( ‘AA00002‘,‘AA00001‘);
INSERT INTO Accessories VALUES ( ‘AA00002‘,‘AA00000‘);
INSERT INTO Accessories VALUES ( ‘AA00001‘,‘AA00000‘);
INSERT INTO Accessories VALUES ( ‘AA00001‘,‘AA00002‘);
INSERT INTO Accessories VALUES ( ‘AA00003‘,‘AA00004‘);
INSERT INTO Accessories VALUES ( ‘AA00003‘,‘AA00005‘);
INSERT INTO Accessories VALUES ( ‘AA00005‘,‘AA00003‘);
INSERT INTO Accessories VALUES ( ‘AA00005‘,‘AA00004‘);
INSERT INTO Accessories VALUES ( ‘AA00004‘,‘AA00003‘);
INSERT INTO Accessories VALUES ( ‘AA00004‘,‘AA00005‘);

INSERT INTO Description VALUES ( ‘AA00000‘,‘ram‘,‘2gb‘);
INSERT INTO Description VALUES ( ‘AA00000‘,‘charge time‘,‘2hrs‘);
INSERT INTO Description VALUES ( ‘AA00001‘,‘ram‘,‘1gb‘);
INSERT INTO Description VALUES ( ‘AA00001‘,‘resolution‘,‘1080p‘);
INSERT INTO Description VALUES ( ‘AA00002‘,‘ram‘,‘3gb‘);
INSERT INTO Description VALUES ( ‘AA00002‘,‘resolution‘,‘over9000‘);
INSERT INTO Description VALUES ( ‘AA00003‘,‘ram‘,‘2gb‘);
INSERT INTO Description VALUES ( ‘AA00004‘,‘ram‘,‘5gb‘);
INSERT INTO Description VALUES ( ‘AA00005‘,‘ram‘,‘2gb‘);
INSERT INTO Description VALUES ( ‘AA00006‘,‘ram‘,‘8gb‘);

INSERT INTO Customers VALUES ( ‘uno‘,‘pass123‘,‘austin jace fitz‘,‘123@123.com‘,‘296 where rd SB‘,‘Gold‘);
INSERT INTO Customers VALUES ( ‘sarrafGUY‘,‘banks$teal‘,‘alex sarraf‘,‘abc@apple.com‘,‘DP OCEANSIDE PARTY‘,‘New‘);

INSERT INTO Discount VALUES (‘New‘,0.10);
INSERT INTO Discount VALUES (‘Gold‘,0.10);
INSERT INTO Discount VALUES (‘Silver‘,0.5);
INSERT INTO Discount VALUES (‘Green‘,0.0);
INSERT INTO Discount VALUES (‘Shipping‘,0.10);

INSERT INTO History VALUES ( 1,‘uno‘,3/3/2014,125.00);