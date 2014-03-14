package store;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Scanner;

//TODO: Check connection statements
public class DatabaseConnection
{

        static Connection conn;
        static String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
        static String strUsername = "asarraf";
        static String strPassword = "4913604";
        static Scanner reader;

        public static void main(String[] args) throws SQLException
        {

                reader = new Scanner(System.in);
                // 1. Load the Oracle JDBC driver for this program
                try
                {
                        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
                }
                catch ( Exception e)
                {
                        e.printStackTrace();
                }

                 // 2. Test functions for each query
                conn = DriverManager.getConnection(strConn,strUsername,strPassword);

                print_all();
                
                conn.close();


        }

         public static void print_all() throws SQLException
     {
        int choice = -1;

         while(choice == -1)
         {


                 System.out.println("Start Menu");
                 System.out.println("0. eDepot");
                 System.out.println("1. eMart");
                 System.out.println("");
                 System.out.print("Choice: ");

                 //Read values
                 choice=reader.nextInt();
                 reader.nextLine(); //Get rid of unread new line
         }
         if(choice == 0)
                transactionMenu();
         else
                login();

     }
         public static void printTable() throws SQLException
         {
                 System.out.print("Which table to print? (Inventory or History): ");
                 String choice=reader.nextLine();

                //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + choice);
                if(choice.equals("Inventory")){
                        System.out.println("Stocknum    Manufacturer       Modelnum        Quantity  Min  Max  Location         Replenishment");
                        while(rs.next())
                        {
                                System.out.print(rs.getString(1) + " ");
                                System.out.print(rs.getString(2) + " ");
                                System.out.print(rs.getString(3) + " ");
                                System.out.print(rs.getInt(4) + "    ");
                                System.out.print(rs.getInt(5) + "   ");
                                System.out.print(rs.getInt(6) + "   ");
                                System.out.print(rs.getString(7) + " ");
                                System.out.println(rs.getInt(8));
                        }
                }
                else if(choice.equals("History"))
                {
                        System.out.println("OrderNum    CID         StockNum    Quantity   Total");
                        while(rs.next())
                        {
                                System.out.print(rs.getInt(1) + "   ");
                                System.out.print(rs.getString(2) + " ");
                                System.out.print(rs.getString(3) + " ");
                                System.out.print(rs.getInt(4) + "    ");
                                System.out.println(rs.getDouble(5));
                        }
                }

         }
         public static void custMenu(String cID, int manager) throws SQLException
     {
             int choice;

             while(true)
             {
                     choice=-1;

                     System.out.println("Customer Menu");
                     System.out.println("0. Exit");
                     System.out.println("1. Search");
                     System.out.println("2. View Cart");
                     System.out.println("3. Add to cart");
                     System.out.println("4. Remove from cart");
                     System.out.println("5. Checkout");
                     System.out.println("6. Rerun order");
                     System.out.println("7. View order");
                     System.out.println("");
                     if (manager == 1)
                     {
                        System.out.println("Manager Menu");
                         System.out.println("8. Print Monthly Summary");
                         System.out.println("9. Status Adjustment");
                         System.out.println("10. Send Order");
                         System.out.println("11. Change Price");
                         System.out.println("12. Delete Old Records");
                         System.out.println("(13. Print History)");
                         System.out.println("");
                     }

                     System.out.print("Choice: ");

                     //Read values
                     choice=reader.nextInt();
                     reader.nextLine(); //Get rid of unread new line

                     if(choice > 6 && manager == 0)
                        choice = -1;

                     switch (choice)
                     {
                             case 0: reader.close();
                                             return;
                             case 1: search();
                                             break;
                             case 2: viewCart(cID);
                                             break;
                             case 3: addCart(cID);
                                             break;
                             case 4: removeCart(cID);
                                             break;
                             case 5: checkout(cID);
                                             break;
                             case 6: rerun(cID);
                             		break;
                             case 7: viewOrder(cID);
                             		break;
                             case 8: printSummary();
                                                        break;
                             case 9: changeStatus();
                                                        break;
                             case 10: sendOrder();
                                                        break;
                             case 11: changePrice();
                                                        break;
                             case 12: deleteOld();
                                                        break;
                             case 13: printTable();
                                                        break;

                             default: break;
                     }
             }
     }

         public static void rerun(String cID) throws SQLException{
        	 System.out.println("Get order: ");
        	 String order=reader.nextLine();
        	 
        	 //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery ("SELECT stockNum,quantity,tprice FROM History WHERE orderNum=" + order );

             while(rs.next())
             {
                     String stockNum = rs.getString(1);
                     int quantity = rs.getInt(2);
                     Double tprice = rs.getDouble(3);
                     String updateCartSQL = "INSERT INTO Cart(cID, stockNum, quantity, tprice) VALUES (?, ?, ?, ?)";
                     System.out.println("Adding new item to cart!");
                     //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                     PreparedStatement p = conn.prepareStatement(updateCartSQL);
                     p.setString(1, cID);
                     p.setString(2, stockNum);
                     p.setInt(3, quantity);
                     p.setDouble(4, tprice);
                     p.executeUpdate();
                     p.close();
                     System.out.println("Added to Cart");
             }
             checkout(cID);
             rs.close();
             stmt.close();
             //conn.close();
        	 
        	 
         }
         
         public static void viewOrder(String cID) throws SQLException
         {
        	 System.out.println("Get order: ");
        	 String order=reader.nextLine();
        	 
              
                 //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                 Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT * FROM History WHERE orderNum =" + order);
                 System.out.println("orderNum     cID             stockNum     quantity   tprice");
                 while(rs.next())
                  {       //System.out.print(i + ": ");
                          System.out.print(rs.getInt(1) + "      ");
                          System.out.print(rs.getString(2));
                          System.out.print(rs.getString(3) + "      ");
                          System.out.print(rs.getInt(4) + "     ");
                          System.out.println(rs.getDouble(5));
                         
                  }
                 System.out.println("");
 	            rs.close();
 	            s.close();
 	            //conn.close();
         }
        public static void transactionMenu() throws SQLException
        {
                int choice;

                while(true)
                {
                        choice=-1;

                        System.out.println("Transaction Menu");
                        System.out.println("0. Exit");
                        System.out.println("1. Process Shipping Notice");
                        System.out.println("2. Receive Shipment");
                        System.out.println("3. Check Quantity");
                        System.out.println("4. Fill Order");
                        System.out.println("(5. Print Inventory)");
                        System.out.println("");
                        System.out.print("Choice: ");

                        //Read values
                        choice=reader.nextInt();
                        reader.nextLine(); //Get rid of unread new line

                        switch (choice)
                        {
                                case 0: reader.close();
                                                return;
                                case 1:
                                                String sIDString = null;
                                                int sID = -1;
                                                String name = null;
                                                String manufacturer = null;
                                                String modelNum = null;
                                                String quantityString = null;
                                                int quantity = -1;

                                                //Read values
                                                System.out.print("sID: ");
                                                sIDString = reader.nextLine();
                                                while(sIDString.trim().length() < 1)
                                                {
                                                        System.out.print("sID: ");
                                                        sIDString = reader.nextLine();
                                                }
                                                sID = Integer.parseInt(sIDString);

                                                System.out.print("Name: ");
                                                name = reader.nextLine();
                                                while(name.trim().length() < 1 || name.trim().length() > 20)
                                                {
                                                        System.out.print("Name: ");
                                                        name = reader.nextLine();
                                                }

                                                System.out.print("Manufacturer: ");
                                                manufacturer = reader.nextLine();
                                                while(manufacturer.trim().length() < 1 || manufacturer.trim().length() > 20)
                                                {
                                                        System.out.print("Manufacturer: ");
                                                        manufacturer = reader.nextLine();
                                                }

                                                System.out.print("ModelNum: ");
                                                modelNum = reader.nextLine();
                                                while(modelNum.trim().length() < 1 || modelNum.trim().length() > 20)
                                                {
                                                        System.out.print("ModelNum: ");
                                                        modelNum = reader.nextLine();
                                                }

                                                System.out.print("Quantity: ");
                                                quantityString = reader.nextLine();
                                                while(quantityString.trim().length() < 1)
                                                {
                                                        System.out.print("Quantity: ");
                                                        quantityString = reader.nextLine();
                                                }
                                                quantity = Integer.parseInt(quantityString);
                                                processShippingNotice(sID, name, manufacturer, modelNum, quantity);
                                                break;
                                case 2:
                                                String sIDString2 = null;
                                                int sID2 = -1;

                                                //Read values
                                                System.out.print("sID: ");
                                                sIDString2 = reader.nextLine();
                                                while(sIDString2.trim().length() < 1)
                                                {
                                                        System.out.print("sID: ");
                                                        sIDString2 = reader.nextLine();
                                                }
                                                sID2 = Integer.parseInt(sIDString2);
                                                receiveShipment(sID2);
                                                break;
                                case 3:
                                                String stockNum = null;

                                                //Read values
                                                System.out.print("stockNum: ");
                                                stockNum = reader.nextLine();
                                                while(stockNum.trim().length() != 7)
                                                {
                                                        System.out.print("stockNum: ");
                                                        stockNum = reader.nextLine();
                                                }
                                                checkQuantity(stockNum);
                                                break;
                                case 4:
                                                String orderNumString = null;
                                                int orderNum = -1;

                                                //Read values
                                                System.out.print("orderNum: ");
                                                orderNumString = reader.nextLine();
                                                while(orderNumString.trim().length() < 1)
                                                {
                                                        System.out.print("oderNum: ");
                                                        orderNumString = reader.nextLine();
                                                }
                                                orderNum = Integer.parseInt(orderNumString);
                                                fillOrder(orderNum);
                                                break;
                                case 5: printTable();
                                                break;

                                default: break;
                        }
                }
        }

        @SuppressWarnings("resource")
        public static void processShippingNotice(int sID, String name, String manufacturer, String modelNum, int quantity) throws SQLException
        {
                if(name.length() > 20)
                {
                        System.out.println("ERROR: length of sID must be less than 20");
                        return;
                }
                else if(manufacturer.length() > 20)
                {
                        System.out.println("ERROR: length of sID must be less than 20");
                        return;
                }
                else if(modelNum.length() > 20)
                {
                        System.out.println("ERROR: length of sID must be less than 20");
                        return;
                }

                // Insert into Shipment table
                //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                stmt.executeUpdate ("INSERT INTO Shipment VALUES (" + sID + ",'" + name + "','" + manufacturer + "','" + modelNum + "'," + quantity + ")");

                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Shipment");

                System.out.println("Shipment Result:");
                while(rs.next())
                {
                        System.out.print("sID: ");
                        System.out.print(rs.getInt(1) + " ");
                        System.out.print("name: ");
                        System.out.print(rs.getString(2) + " ");
                        System.out.print("manufacturer: ");
                        System.out.print(rs.getString(3) + " ");
                        System.out.print("modelNum: ");
                        System.out.print(rs.getString(4) + " ");
                        System.out.print("quantity: ");
                        System.out.println(rs.getInt(5));
                }

                // Query Inventory man,mod, return stocknum
                stmt = conn.createStatement();
                ResultSet rs2 = stmt.executeQuery ("SELECT stockNum FROM Inventory WHERE manufacturer = '" + manufacturer + "' AND modelNum = '" + modelNum + "'");

                // If item exists
                if(rs2.next())
                {
                        String stockNum = rs2.getString(1);
                        // Update Replenishment in Inventory
                        stmt = conn.createStatement();
                        stmt.executeUpdate("UPDATE Inventory SET replenishment = replenishment + " + quantity + " WHERE stockNum = '" + stockNum + "'");
                        System.out.println("Updated replenishment");
                }
                else
                {
                                String stockNum = "AA";
                                stockNum += getStockNum();

                                stmt = conn.createStatement();
                                stmt.executeQuery ("INSERT INTO Inventory VALUES ('" + stockNum + "','" + manufacturer + "','" + modelNum + "'," + quantity + ",1," + quantity + ",'new shelf'," + quantity + ")");
                                System.out.println("Item is new, added new " + stockNum);
                }

                rs2.close();
                rs.close();
                stmt.close();
                //conn.close();
        }

        public static void receiveShipment(int sID) throws SQLException
        {
                // Select relevent info from Shipment
                //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery ("SELECT manufacturer,modelNum,quantity FROM Shipment WHERE sID = " + sID);

                while(rs.next())
                {
                        String man = rs.getString(1);
                        String mod = rs.getString(2);
                        int quan = rs.getInt(3);
                        // Update of replenishment, quan where stocknum
                        stmt = conn.createStatement();
                        stmt.executeUpdate("UPDATE Inventory SET replenishment = replenishment - " + quan + ", quantity = quantity + " + quan + " WHERE manufacturer = '" + man + "' AND modelNum = '" + mod + "'");
                        System.out.println("Received: " + man + " " + mod);
                }
                System.out.println("Shipment Fully Stocked");
                rs.close();
                stmt.close();
                //conn.close();
        }

        public static int checkQuantity(String stockNum) throws SQLException
        {
                if(stockNum.length() > 7)
                {
                        System.out.println("ERROR: length of stockNum must be 7");
                        return 0;
                }

                //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery ("SELECT quantity FROM Inventory WHERE stockNum = '" + stockNum + "'");
                int quantity=0;
                if(rs.next())
                {
                        quantity=rs.getInt(1);
                        System.out.println("stockNum " + stockNum + " has quantity: " + rs.getInt(1));
                }
                else
                {
                        System.out.println("Item not found.");
                }
                rs.close();
                stmt.close();
                //conn.close();
                return quantity;
        }

        public static int getSID() throws SQLException
        {
                //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT value FROM Defined WHERE type = 'sid'");
                if(rs.next())
                {
                        int result = rs.getInt(1);
                        stmt = conn.createStatement();
                        stmt.executeUpdate("UPDATE Defined SET value = value + 1 WHERE type = 'sid'");
                        rs.close();
                        stmt.close();
                        //conn.close();
                        return result;
                }
                else
                {
                        System.out.println("ERROR: no SID in table Defined");
                        rs.close();
                        stmt.close();
                        //conn.close();
                        return -1;
                }
        }

        public static int getStockNum() throws SQLException
        {
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT value FROM Defined WHERE type = 'stockNum'");
                if(rs.next())
                {
                        int result = rs.getInt(1);
                        stmt = conn.createStatement();
                        stmt.executeUpdate("UPDATE Defined SET value = value + 1 WHERE type = 'stockNum'");
                        rs.close();
                        stmt.close();
                        //conn.close();
                        return result;
                }
                else
                {
                        System.out.println("ERROR: no stockNum in table Defined");
                        rs.close();
                        stmt.close();
                        //conn.close();
                        return -1;
                }
        }

        public static void fillOrder(int orderNum) throws SQLException
        {
                // Select stock# and quantity from Orders where orderNum
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT stockNum,quantity FROM History WHERE orderNum = " + orderNum);

                // for ea in rs update inventory quantity -= quantity where stock#
                while(rs.next())
                {
                        String stockNum = rs.getString(1);
                        int quan = rs.getInt(2);

                        // Update of replenishment, quan where stocknum
                        stmt = conn.createStatement();
                        stmt.executeUpdate("UPDATE Inventory SET quantity = quantity - " + quan + " WHERE stockNum = '" + stockNum + "'");
                        System.out.println("Filled Order with: " + stockNum + " inventory less " + quan);
                }

                // query inventory where quan < min && same manufacturer if count >=3
                stmt = conn.createStatement();
                ResultSet rs2 = stmt.executeQuery("SELECT DISTINCT manufacturer FROM Inventory WHERE quantity < min GROUP BY manufacturer HAVING COUNT(*) > 2");
                while(rs2.next())
                {
                        System.out.println("in result set");
                        // send replenishment order for all same manu quan < max
                        int SID = getSID();
                        System.out.println("got sid: " + SID);
                        String manufacturer = rs2.getString(1);
                        System.out.println("got manufactuer: " + manufacturer);
                        stmt = conn.createStatement();
                        ResultSet rs3 = stmt.executeQuery("SELECT modelNum,quantity,max,replenishment FROM Inventory WHERE quantity < max AND manufacturer = '" + manufacturer + "'");
                        while(rs3.next())
                        {
                                int quantity = rs3.getInt(3) - rs3.getInt(4) - rs3.getInt(2);
                                if(quantity <= 0)
                                        System.out.println("Item " + manufacturer + "," + rs3.getString(1) + " has replenishment already ordered");
                                else{
                                        String modelNum = rs3.getString(1);
                                        processShippingNotice(SID, "Replenishment!", manufacturer, modelNum, quantity);
                                        System.out.println("Sent replenishment order for man: " + manufacturer + " mod: " + modelNum);
                                }
                        }
                        rs3.close();
                }
                rs2.close();
                rs.close();
                stmt.close();
                //conn.close();
        }


        public static void search() throws SQLException
        {
                System.out.println("Time to search");
                String stockNum=null;
                String category=null;
                String manufacturer=null;
                String modelNum=null;
                String priceString=null;
                String accessories=null;
                double price=-1.0;
                String description;
                String dvalue=null;

                //Read values
                System.out.print("Stock Number: ");
                stockNum=reader.nextLine();
                System.out.print("Category: ");
                category=reader.nextLine();
                System.out.print("Manufacturer: ");
                manufacturer=reader.nextLine();
                System.out.print("Model Number: ");
                modelNum=reader.nextLine();
                System.out.print("Price: ");
                priceString=reader.nextLine();
                System.out.print("Accessories? (y/n) ");
                accessories=reader.nextLine();
                System.out.print("Description attribute: ");
                description=reader.nextLine();
                if(description.trim().length() != 0)
                {
                        System.out.print("Description attribute value: ");
                        dvalue=reader.nextLine();
                }



                //Create query
                String query="SELECT * FROM Catalog WHERE ";
                if(stockNum.trim().length() != 0)
                {
                        query+="stockNum='" + stockNum + "' AND ";
                }
                if(category.trim().length() != 0)
                {
                        query+="category='" + category + "' AND ";
                }
                if(manufacturer.trim().length() != 0)
                {
                        query+="manufacturer='" + manufacturer + "' AND ";
                }
                if(modelNum.trim().length() != 0)
                {
                        query+="modelNum='" + modelNum + "' AND ";
                }
                if(priceString.trim().length() != 0)
                {
                        price = Double.parseDouble(priceString);
                        query+="price=" + price + " AND ";
                }

                if(description.trim().length() != 0)
                {
                        query+="stockNum IN (SELECT stockNum FROM Description WHERE attribute='" + description + "' AND value='" + dvalue + "') AND ";
                }
                query+="1=1";

                //Execute Query
                System.out.println("Stock#  Category            Manufacturer    Model#                  Price Warranty");
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery (query);
                 //int i=0;
                 while(rs.next())
                 {       //System.out.print(i + ": ");
                         String tempStock=rs.getString(1);
                         System.out.print(tempStock + " ");
                         System.out.print(rs.getString(2) + " ");
                         System.out.print(rs.getString(3) + " ");
                         System.out.print(rs.getString(4) + "   ");
                         System.out.print(rs.getFloat(5) + "    ");
                         System.out.println(rs.getInt(6) + " ");

                         if(accessories.equals("y")){
                                 System.out.println("Accessories of " + tempStock + ":");
                                 searchAccessory(tempStock);
                                 System.out.println("End of Accessories of " + tempStock);
                         }
                         //i++;

                 }
                 System.out.println("");
                 rs.close();
                 stmt.close();
                 //conn.close();


        }
        public static void searchDescription(String attribute, String value) throws SQLException
        {
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery ("SELECT * FROM Catalog WHERE stockNum IN (SELECT stockNum FROM Description WHERE attribute='" + attribute + "' AND value='" + value + "')");
                 //int i=0;
                 while(rs.next())
                 {       //System.out.print(i + ": ");
                         System.out.print(rs.getString(1) + " ");
                         System.out.print(rs.getString(2) + " ");
                         System.out.print(rs.getString(3) + " ");
                         System.out.print(rs.getString(4) + " ");
                         System.out.print(rs.getFloat(5) + "    ");
                         System.out.println(rs.getInt(6) + " ");
                         //i++;

                 }
                 rs.close();
                 stmt.close();
                 //conn.close();
        }
        //TODO: Elaborate accessory search and description search
        public static void searchAccessory(String stockNum) throws SQLException
        {
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery ("SELECT * FROM Catalog C WHERE C.stockNum IN (SELECT A.childStockNum FROM Accessories A WHERE A.parentStockNum='" + stockNum + "')" );
                 //int i=0;
                 while(rs.next())
                 {       //System.out.print(i + ": ");
                         System.out.print(rs.getString(1) + " ");
                         System.out.print(rs.getString(2) + " ");
                         System.out.print(rs.getString(3) + " ");
                         System.out.print(rs.getString(4) + " ");
                         System.out.print(rs.getFloat(5) + "    ");
                         System.out.println(rs.getInt(6) + " ");
                         //i++;

                 }
                 rs.close();
                 stmt.close();
                 //conn.close();
        }

        public static int getCartQuantity(String cID, String stockNum) throws SQLException {
                int quantity = -1;
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();

                ResultSet rs = s.executeQuery("SELECT * FROM Cart WHERE cID ='" + cID + "' AND stockNum = '" + stockNum + "'");
                if(rs.next()){
                        quantity = rs.getInt("quantity");
                        System.out.println("ITEM EXISTS! Quantity: " + quantity);
                }

                s.close();
                //conn.close();
                return quantity;
        }
        public static double getPrice(String stockNum) throws SQLException {
                double price = -1;
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();

                ResultSet rs = s.executeQuery("SELECT * FROM Catalog WHERE stockNum ='" + stockNum + "'");
                if(rs.next()){
                        price = rs.getDouble("price");
                        System.out.println("ITEM EXISTS! Price: " + price);
                }

                s.close();
                //conn.close();
                return price;
        }
        public static void addCart(String cID) throws SQLException
        {
                //Get stockNum and quantity
                String stockNum;
                int quantity;
                System.out.print("Enter Stock Number: ");
                stockNum=reader.nextLine();
                if(stockNum.trim().length() != 7){
                        System.out.print("ERROR: Stock Number must be length of 7");
                        return;
                }
                System.out.print("Enter quantity to add: ");
                quantity=reader.nextInt();
                if(quantity < 1){
                        System.out.print("ERROR: Quantity must be at least 1");
                        return;
                }

                int oldquantity = getCartQuantity(cID, stockNum);
                if(oldquantity > 0){
                        System.out.println("Old quantity: " + oldquantity + " Adding: " + quantity);
                        quantity += oldquantity;
                        System.out.println("NEW: " + quantity);
                }

                //Check eDepot for quantity
                int available=checkQuantity(stockNum);
                if(quantity > available){
                        System.out.println("Not enough available!");
                        return;
                }

                double price = getPrice(stockNum);
                if(price == -1)
                {
                        System.out.println("Item does not exist in catalog!");
                        return;
                }

                String updateCartSQL;
                double tprice=round(price*quantity, 2);
                System.out.println("TPrice: " + tprice);

                if(oldquantity > 0)
                {
                        updateCartSQL = "UPDATE Cart SET quantity=" + quantity + ", tprice=" + tprice + " WHERE cID='" + cID + "' AND stockNum='" + stockNum + "'";
                        System.out.println("Updating quantity and tprice of item!");
                        ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                        PreparedStatement p = conn.prepareStatement(updateCartSQL);
                        //p.setInt(1, quantity);
                        //p.setDouble(2, price*quantity);
                        //p.setString(3, cID);
                        //p.setString(4, stockNum);
                        p.executeUpdate();
                        p.close();
                        //conn.close();
                        System.out.println("Added to Cart");
                        /*////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                        Statement s = conn.createStatement();
                        System.out.println("BEFORE");
                        ResultSet rs=s.executeQuery("UPDATE Cart SET quantity=" + quantity + ", tprice=" + price*quantity + " WHERE cID='" + cID + "' AND stockNum='" + stockNum + "'");
                        System.out.println("AFTER");
                        rs.close();
                        //conn.close();
                        System.out.println("Added to Cart");*/
                }
                else
                {
                        updateCartSQL = "INSERT INTO Cart(cID, stockNum, quantity, tprice) VALUES (?, ?, ?, ?)";
                        System.out.println("Adding new item to cart!");
                        ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                        PreparedStatement p = conn.prepareStatement(updateCartSQL);
                        p.setString(1, cID);
                        p.setString(2, stockNum);
                        p.setInt(3, quantity);
                        p.setDouble(4, tprice);
                        p.executeUpdate();
                        p.close();
                        //conn.close();
                        System.out.println("Added to Cart");
                }
                }
                public static void removeCart(String cID) throws SQLException
                {
                        //Get stockNum and quantity
                        String stockNum;
                        int quantity;
                        System.out.print("Enter Stock Number: ");
                        stockNum=reader.nextLine();
                        if(stockNum.trim().length() != 7){
                                System.out.print("ERROR: Stock Number must be length of 7");
                                return;
                        }
                        System.out.print("Enter Quantity to remove: ");
                        quantity=reader.nextInt();
                        if(quantity < 1){
                                System.out.print("ERROR: Quantity must be at least 1");
                                return;
                        }
                        //reader.close();

                        int oldquantity = getCartQuantity(cID, stockNum);
                        if(oldquantity == -1)
                        {
                                System.out.println("Item doesn't exist in cart!");
                                return;
                        }

                        double price = getPrice(stockNum);
                        if(price == -1)
                        {
                                System.out.println("Item does not exist in catalog!");
                                return;
                        }

                        System.out.println("old quantity: " + oldquantity);
                        if(oldquantity != -1){
                                quantity = oldquantity - quantity;
                        }

                        String updateCartSQL;
                        if(quantity > 0)
                        {
                                double tprice=round(price*quantity, 2);
                                System.out.println("TPrice: " + tprice);
                                updateCartSQL = "UPDATE Cart SET quantity=" + quantity + ", tprice=" + tprice + " WHERE cID='" + cID + "' AND stockNum='" + stockNum + "'";
                                System.out.println("Updating quantity and tprice of item!");
                                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                                PreparedStatement p = conn.prepareStatement(updateCartSQL);
                                //p.setInt(1, quantity);
                                //p.setDouble(2, price*quantity);
                                //p.setString(3, cID);
                                //p.setString(4, stockNum);
                                p.executeUpdate();
                                p.close();
                                //conn.close();
                                System.out.println("Removed from Cart");
                        }
                        else
                        {
                                updateCartSQL = "DELETE FROM Cart WHERE cID='" + cID + "' AND stockNum='" + stockNum + "'";
                                System.out.println("Removing item from cart!");
                                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                                PreparedStatement p = conn.prepareStatement(updateCartSQL);
                                //p.setString(1, cID);
                                //p.setString(2, stockNum);

                                p.executeUpdate();

                                p.close();
                                //conn.close();
                                System.out.println("Deleted from Cart");
                        }


                }
        public static void viewCart(String cID) throws SQLException
        {
                double subtotal=0;
                double cost=0;
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();
                System.out.println("Stock Num   Quantity        Cost");
                ResultSet rs = s.executeQuery("SELECT * FROM Cart WHERE cID ='" + cID + "'");
                while(rs.next())
                 {       //System.out.print(i + ": ");
                         //System.out.print(rs.getString(1) + " ");
                         System.out.print(rs.getString(2) + "           ");
                         System.out.print(rs.getInt(3) + "      ");
                         cost=rs.getDouble(4);
                         System.out.println(cost);
                         subtotal+=cost;
                 }
                subtotal=round(subtotal, 2);
                System.out.println("Subtotal: "+subtotal);
                System.out.println("");
	            rs.close();
	            s.close();
	            //conn.close();
        }
        public static void checkout(String cID) throws SQLException
        {       //TODO: Test if no items in cart
                double subtotal=0;
                double discount=0;
                double shipping=0;
                double totalcost=0;

                //Temp values to fill history table
                String stockNum;
                int quantity;
                double tprice;

                String status=null;
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();

                int orderNum=getNewOrderNum();
                //String mydate=getDate();


                //Get subtotal
                ResultSet rs = s.executeQuery("SELECT * FROM Cart WHERE cID ='" + cID + "'");
                while(rs.next())
                 {
                        stockNum=rs.getString(2);
                        quantity=rs.getInt(3);
                        tprice=rs.getDouble(4);
                        //TODO: Check availability before checkout in addCart from eDepot???
                        System.out.println("eDepot: Check availability of product " + stockNum + " with quantity " + quantity);
                        PreparedStatement p = conn.prepareStatement("INSERT INTO History VALUES(" + orderNum +", '"+ cID +"', '" + stockNum + "', " + quantity + ", " + tprice +")");
                        p.executeUpdate();
                        p.close();
                        subtotal+=tprice;
                 }
                System.out.println("Subtotal: "+subtotal);

                //Get customer status and discount + shipping
                status=getStatus(cID);
                System.out.println("Customer has status: " + status);
                discount=getDiscount(status, subtotal);
                System.out.println("Status Discount: -" + discount);
                shipping=getShipping(subtotal);
                System.out.println("Shipping Cost: +" + shipping);

                //Calculate total cost
                totalcost=subtotal-discount+shipping;
                totalcost=round(totalcost, 2);
                System.out.println("Total Cost: " + totalcost);

                //Insert in Total table
                PreparedStatement updateTotal = conn.prepareStatement("INSERT INTO Total VALUES(" + orderNum + ", '" + cID + "', CURRENT_TIMESTAMP, " + totalcost + ")");
                updateTotal.executeUpdate();
                updateTotal.close();

                //Remove from cart
                PreparedStatement removeCart = conn.prepareStatement("DELETE Cart WHERE cID='" + cID + "'");
                removeCart.executeUpdate();
                removeCart.close();

                //Update customer number of purchases, purchase (0, 1, 2), and status
                //TODO: SHOULD PRICES FOR STATUS BE DETERMINED FROM SUBTOTAL OR TOTAL COST????
                updateCustomer(cID);

                System.out.println("");
                rs.close();
                s.close();
                //conn.close();
        }
        public static String getStatus(String cID) throws SQLException
        {
                String status=null;
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();

                ResultSet rs = s.executeQuery("SELECT * FROM Customers WHERE cID ='" + cID + "'");
                if(rs.next()){
                        status = rs.getString("status");
                        System.out.println("Customer status: " + status);
                }
                else
                {
                        System.out.println("SOMETHING WENT WRONG!!!!!");
                }
                rs.close();
                s.close();
                //conn.close();
                return status;
        }

        public static double getDiscount(String status, Double subtotal)throws SQLException
        {
                double discount=0;
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();

                ResultSet rs = s.executeQuery("SELECT * FROM Discount WHERE status ='" + status + "'");
                if(rs.next()){
                        discount = rs.getDouble("percent");
                }
                rs.close();
                s.close();
                //conn.close();
                return (discount*subtotal);

        }
        public static double getShipping(Double subtotal) throws SQLException
        {
                double minShipping=99999;
                double percent=0;
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery("SELECT * FROM Discount WHERE status ='Shipping'");
                if(rs.next())
                {
                        minShipping = rs.getDouble("min");
                        System.out.println("Minimum subtotal needed to waive shipping: " + minShipping);
                        percent = rs.getDouble("percent");
                        System.out.println("S&H Percent: " + percent);
                }
                rs.close();
                s.close();
                //conn.close();
                if(subtotal>minShipping){
                        return 0;
                }
                else{
                        return (subtotal*percent);
                }
        }
        public static int getNewOrderNum() throws SQLException
        {
                int orderNum=99999;
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery("SELECT value FROM Defined WHERE type ='orderNum'");
                if(rs.next())
                {
                        orderNum=rs.getInt(1);
                        System.out.println("Order number: " + orderNum);
                        PreparedStatement p = conn.prepareStatement("UPDATE Defined SET value=value+1 WHERE type='orderNum'");
                        p.executeUpdate();
                        p.close();
                }
                else
                {
                        System.out.println("ERROR: Something went wrong");
                }
                rs.close();
                s.close();
                //conn.close();
                return orderNum;
        }

        //Updates customer purchases and status
        public static void updateCustomer(String cID) throws SQLException {
                //conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();

                //Customer values
                double total=0;
                String status=null;

                ResultSet rs = s.executeQuery("SELECT total FROM Total WHERE cID ='" + cID + "' ORDER BY dop DESC");

                int i=0;
                while(rs.next())
                {
                        if(i==3)
                                break;
                        total+=rs.getDouble(1);
                        i++;
                }
                status=getNewStatus(total);
                s=conn.createStatement();
                s.executeUpdate("UPDATE Customers SET status='" + status + "' WHERE cID='" + cID + "'");

               
                rs.close();
                s.close();
                //conn.close();
        }

        public static String getNewStatus(double total) throws SQLException
        {
                System.out.println("Getting new status...");
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement s = conn.createStatement();

                String statusCheck;
                double goldMin=0;
                double silverMin=0;
                double silverMax=0;
                double greenMin=0;
                double greenMax=0;

                //Get status values
                ResultSet rs2 = s.executeQuery("SELECT * FROM Discount");
                while(rs2.next()){
                        statusCheck=rs2.getString("status");
                        statusCheck=statusCheck.trim();
                        if(statusCheck.equals("Gold")){
                                goldMin=rs2.getDouble("min");
                                //goldMax=rs2.getDouble("max");
                                if(total > goldMin){
                                        rs2.close();
                                        s.close();
                                        //conn.close();
                                        return("Gold");
                                }
                        }
                        else if(statusCheck.equals("Silver")){
                                silverMin=rs2.getDouble("min");
                                silverMax=rs2.getDouble("max");
                                if(total > silverMin && total <= silverMax){
                                        rs2.close();
                                        s.close();
                                        //conn.close();
                                        return("Silver");
                                }
                        }
                        else if(statusCheck.equals("Green")){
                                greenMin=rs2.getDouble("min");
                                greenMax=rs2.getDouble("max");
                                if(total > greenMin && total <= greenMax){
                                        rs2.close();
                                        s.close();
                                        //conn.close();
                                        return("Green");
                                }
                        }
                }
                rs2.close();
                s.close();
                //conn.close();
                System.out.println("ERROR: Something went wrong with getNewStatus!!!");
                return("Green");
        }

        public static String getDate()
        {
                Calendar calendar = Calendar.getInstance();
                int monthNum = calendar.get(Calendar.MONTH);
                String month = null;
                DateFormatSymbols arrayOfMonths = new DateFormatSymbols();
                String[] months = arrayOfMonths.getMonths();
                if (monthNum >= 0 && monthNum <= 11 )
                {
                        month = months[monthNum];
                }

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);
                String myString = day + "-" + month + "-" + year;
                return myString;
        }

        public static void deleteOld() throws SQLException
        {
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT cID FROM Total GROUP BY cID HAVING COUNT(*) > 2");
                while(rs.next())
                {
                        String cID = rs.getString(1);
                        stmt = conn.createStatement();
                        ResultSet rs2 = stmt.executeQuery("SELECT orderNum FROM Total WHERE cID = '" + cID + "' ORDER BY dop DESC");
                        int i = 0;
                        while(rs2.next())
                        {
                                int orderNum = rs2.getInt(1);
                                if(i > 2)
                                {
                                        stmt.executeUpdate("DELETE FROM Total WHERE orderNum =" + orderNum);
                                        stmt.executeUpdate("DELETE FROM History WHERE orderNum =" + orderNum);
                                }
                                i++;
                        }
                        rs2.close();
                }
                rs.close();
                stmt.close();
                //conn.close();
        }

        public static void printSummary() throws SQLException
        {
                System.out.print("Month (January=01): ");
                String month=reader.nextLine();
                System.out.print("Year: ");
                String year=reader.nextLine();

                int lastDay=getLastDay(Integer.parseInt(month));

                //Get product quantities and sales
                ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT stockNum FROM Catalog");
                System.out.println("Summary\n");
                System.out.println("StockNum    Quantity        Total Sale");

                while(rs.next())
                {
                        int quantity=0;
                        double total=0;
                        String stockNum = rs.getString(1);
                        stmt = conn.createStatement();
                        ResultSet rs2 = stmt.executeQuery("SELECT SUM(quantity),SUM(tprice) FROM History,Total WHERE stockNum='" + stockNum + "' AND Total.orderNum=History.orderNum AND dop >= TO_TIMESTAMP('" + year + "-" + month + "-01 00:00:00','yyyy-mm-dd hh24:mi:ss') and dop <= TO_TIMESTAMP('" + year + "-" + month + "-" + lastDay + " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
                        if(rs2.next())
                        {
                                quantity=rs2.getInt(1);
                                total=rs2.getDouble(2);
                        }
                        System.out.print(stockNum + "           ");
                        System.out.print(quantity + "           ");
                        System.out.println(total + "    ");
                        rs2.close();
                }
                System.out.println("");
                rs.close();

                //Get category quantities and sales
                stmt = conn.createStatement();
                ResultSet rs3 = stmt.executeQuery("SELECT DISTINCT category FROM Catalog");
                System.out.println("Category    Quantity        Total Sale");

                while(rs3.next())
                {
                        int quantity=0;
                        double total=0;
                        String category = rs3.getString(1);
                        stmt = conn.createStatement();
                        ResultSet rs4 = stmt.executeQuery("SELECT SUM(quantity),SUM(tprice) FROM History,Total WHERE stockNum IN (SELECT stockNum FROM Catalog WHERE category='" + category + "') AND Total.orderNum=History.orderNum AND dop >= TO_TIMESTAMP('" + year + "-" + month + "-01 00:00:00','yyyy-mm-dd hh24:mi:ss') and dop <= TO_TIMESTAMP('" + year + "-" + month + "-" + lastDay + " 23:59:59', 'yyyy-mm-dd hh24:mi:ss')");
                        if(rs4.next())
                        {
                                quantity=rs4.getInt(1);
                                total=rs4.getDouble(2);
                        }
                        System.out.print(category + "   ");
                        System.out.print(quantity + "           ");
                        System.out.println(total + "    ");
                        rs4.close();
                }
                System.out.println("");
                rs3.close();

                //Get best customer
                stmt = conn.createStatement();
                ResultSet rs5 = stmt.executeQuery("WITH mytable AS (select distinct Total.cID,SUM(History.quantity) as totalQuantity,SUM(Total.total) as totalSum FROM Total,History WHERE Total.orderNum=History.orderNum AND dop >= TO_TIMESTAMP('" + year + "-" + month + "-01 00:00:00','yyyy-mm-dd hh24:mi:ss') and dop <= TO_TIMESTAMP('" + year + "-" + month + "-" + lastDay + " 23:59:59', 'yyyy-mm-dd hh24:mi:ss') GROUP BY Total.cID) SELECT cID, totalQuantity, totalSum FROM mytable WHERE totalSum=(SELECT MAX(totalSum) from mytable)");
                if(rs5.next()){
                        String bestCustomer=rs5.getString(1);
                        int totalQuantity=rs5.getInt(2);
                        Double totalPurchase=rs5.getDouble(3);
                        System.out.println("Customer who purchased most: " + bestCustomer + "Total Quantity: " + totalQuantity + "     Total Purchased: " + totalPurchase);
                }
                else
                {
                        System.out.println("No sales this month! No best customer!");
                }
                stmt.close();
                //conn.close();
        }
        private static int getLastDay(int month) {

                switch(month)
            {
            case 1: return 31;
            case 2: return 28;
            case 3: return 31;
            case 4: return 30;
            case 5: return 31;
            case 6: return 30;
            case 7: return 31;
            case 8: return 31;
            case 9: return 30;
            case 10: return 31;
            case 11: return 30;
            case 12: return 31;
            }
            return -1;
        }
        public static void changePrice() throws SQLException
    {
                String choice;
                int price;
                System.out.println("Enter StockNum of item to change price");
            System.out.println("");
            System.out.print("Choice: ");

            //Read values
            choice=reader.nextLine();

                System.out.println("Enter desired price");
            System.out.println("");
            System.out.print("Price: ");

            price=reader.nextInt();

            ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate("UPDATE Catalog SET price =" + price + " WHERE stockNum = '" + choice + "'");
            if(result == 0)
                System.out.println("Item not found.");
            else
                System.out.println("Success!");

            stmt.close();
            //conn.close();
    }

         public static void login() throws SQLException
     {
                int valid = 0;
             ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
             Statement stmt = conn.createStatement();
                while(valid == 0)
                {
                                String username;
                                String password;

                            System.out.print("Username: ");

                            //Read values
                            username=reader.nextLine();

                            System.out.print("Password: ");

                            password=reader.nextLine();

                            stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("Select isManager FROM Customers WHERE cID = '" + username + "' AND password = '" + password + "'");
                        if(rs.next())
                        {
                                System.out.println("Success!");
                                valid = 1;
                                int manager = rs.getInt(1);
                                rs.close();
                                stmt.close();
                                //conn.close();
                                custMenu(username, manager);

                        }
                        else
                                System.out.println("Login failed");
                        rs.close();
                }

     }

     public static void changeStatus() throws SQLException
     {
                String cID;
                String color;
                        System.out.println("Enter cID to change his/her status");
                    System.out.println("");
                    System.out.print("cID: ");

                    //Read values
                    cID=reader.nextLine();

                        System.out.println("Enter desired status (Gold/Green/Silver/New)");
                    System.out.println("");
                    System.out.print("Color: ");

                    color=reader.nextLine();

             ////conn = DriverManager.getConnection(strConn,strUsername,strPassword);
             Statement stmt = conn.createStatement();
             int result = stmt.executeUpdate("UPDATE Customers SET status = '" + color + "' WHERE cID = '" + cID + "'");
             if(result == 0)
                System.out.println("Failed.");
             else
                System.out.println("Success!");

             stmt.close();
             //conn.close();
     }

     public static void sendOrder() throws SQLException
     {
                int choice = 1;
             int SID = getSID();
             System.out.println("got sid: " + SID);
                while(choice == 1)
                {
                        String name;
                        String manufacturer;
                        String modelNum;
                        int quantity;

                            System.out.print("name: ");
                            //Read values
                            name=reader.nextLine();

                            System.out.print("manufacturer: ");
                            //Read values
                            manufacturer=reader.nextLine();

                            System.out.print("modelNum: ");
                            //Read values
                            modelNum=reader.nextLine();

                            System.out.print("quantity: ");
                            //Read values
                            quantity=reader.nextInt();

                            processShippingNotice(SID, name, manufacturer, modelNum, quantity);

                            System.out.print("add another item to order?? 1 = yes 0 = no: ");
                            //Read values
                            choice=reader.nextInt();
                            reader.nextLine();

                }
     }
     public static double round(double value, int places) {
    	    if (places < 0) throw new IllegalArgumentException();

    	    long factor = (long) Math.pow(10, places);
    	    value = value * factor;
    	    long tmp = Math.round(value);
    	    return (double) tmp / factor;
    	}


}