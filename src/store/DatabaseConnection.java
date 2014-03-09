package store;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Scanner;


public class DatabaseConnection {
	
	static Connection conn;
	static String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	static String strUsername = "asarraf";
	static String strPassword = "4913604";
	static Scanner reader;

	public static void main(String[] args) throws SQLException{
		
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
		///////////////////////////////////////////////////

		 // 2. Test functions for each query
		print_all();
		

		}

		public static void print_all() throws SQLException
		{
		 /*conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		 Statement stmt = conn.createStatement();
		 ResultSet rs = stmt.executeQuery ("SELECT * FROM Catalog");
		 System.out.println("result:");
		 while(rs.next())
		 {	 
			 System.out.print(rs.getString(1) + " ");
			 System.out.print(rs.getString(2) + " ");
			 System.out.println(rs.getString(3));

			 // Get the value from column "columnName" with integer type
			 //System.out.println(rs.getInt("getString"));
			 // Get the value from column "columnName" with float type
			 //System.out.println(rs.getFloat("columnName"));
			 // Get the value from the third column with string type
			 //System.out.println(rs.getString(3));
		 }
		 rs.close();
		 stmt.close();
		 conn.close();*/
			
		//TEST FUNCTIONS!!!!
			
		

		//searchStockNum("AA00000");
		//searchCategory("phone");
		//searchModel("a01");
		//searchDescription("ram", "2gb");
		//searchAccessory("AA00000");
		//searchManufacturerAndPrice("Verizon", 11.0);
		//searchPriceAndWarranty(11.0, 1);
		//searchStockNumAndCategoryAndModelNum("AA00001", "phone", "a02");
		//search();
		custMenu("sarrafGUY");
		
		}
		
		public static void custMenu(String cID) throws SQLException
		{
			int choice;
			

			while(true){
				choice=-1;

				System.out.println("Customer Menu");
				System.out.println("0. Exit");
				System.out.println("1. Search");
				System.out.println("2. View Cart");
				System.out.println("3. Add to cart");
				System.out.println("4. Remove from cart");
				System.out.println("5. Checkout");
				System.out.println("");
				System.out.print("Choice: ");

				//Read values
				choice=reader.nextInt();
				reader.nextLine(); //Get rid of unread new line

				switch (choice) {
	            	case 0: reader.close();
	            			return;
	            	case 1:	search();
	            			break;
	            	case 2:	viewCart(cID);
	            			break;
	            	case 3: addCart(cID);
	            			break;
	            	case 4: removeCart(cID);
	            			break;
	            	case 5: checkout(cID);
	            			break;
	            
	            	default: break;
				}

			}
		}
		
		
		public static void processShippingNotice(String sID, String name, String manufacturer, String modelNum, int quantity) throws SQLException
		{
			if(sID.length() > 20)
			{
				System.out.println("ERROR: length of sID must be less than 20");
				return;
			}
			else if(name.length() > 20)
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
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate ("INSERT INTO Shipment VALUES ('" + sID + "','" + name + "','" + manufacturer + "','" + modelNum + "'," + quantity + ")");
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Shipment");
			
			System.out.println("Shipment Result:");
			while(rs.next())
			{	 
				System.out.print("sID: ");
				System.out.print(rs.getString(1) + " ");
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
				// TODO if stock num does not exist make new data???????????????????????????????????????????? 
				//System.out.println("Item is new, added new " + stockNum);
			}
			
			rs2.close();	
			rs.close();
			stmt.close();
			conn.close();
		}
		
		public static void receiveShipment(String sID) throws SQLException
		{
			if(sID.length() > 20)
			{
				System.out.println("ERROR: length of sID must be less than 20");
				return;
			}
			
			// Select relevent info from Shipment
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery ("SELECT manufacturer,modelNum,quantity FROM Shipment WHERE sID = '" + sID + "'");
			
			while(rs.next())
			{	 
				String man = rs.getString(3);
				String mod = rs.getString(4);
				int quan = rs.getInt(5);
				
				// Update of replenishment, quan where stocknum
				stmt = conn.createStatement();
				stmt.executeUpdate("UPDATE Inventory SET replenishment = replenishment - " + quan + " AND quantity = quantity + " + quan + " WHERE manufacturer = '" + man + "' AND modelNum = '" + mod + "'");
				System.out.println("Received: " + man + " " + mod);
			}		
			System.out.println("Shipment Fully Stocked");
			rs.close();
			stmt.close();
			conn.close();
		}	
		
		public static void checkQuantity(String stockNum) throws SQLException
		{
			if(stockNum.length() > 7)
			{
				System.out.println("ERROR: length of stock number must be 7");
				return;
			}
			
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery ("SELECT quantity FROM Inventory WHERE stockNum = '" + stockNum + "'");
			
			if(rs.next())
			{
				System.out.println("stockNum " + stockNum + " has quantity: " + rs.getInt(1));
			}
			else
			{
				System.out.println("Item not found.");
			}
			rs.close();
			stmt.close();
			conn.close();
		}
		
		public static void fillOrder(int orderNum)
		{
			// Select stock# and quantity from Orders where orderNum
			
			// for ea in rs update inventory quantity -= quantity where stock#
			// query inventory where quan < min && same manufacturer if count >=3 send replenishment order for all same manu quan < max
		}
		
		
		public static void search() throws SQLException
		{
			System.out.println("Time to search");
			String stockNum=null;
			String category=null;
			String manufacturer=null;
			String modelNum=null; 
			String priceString=null;
			double price=-1.0; 
			String warrantyString=null;
			int warranty=-1;
			
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
			System.out.print("Warranty: ");
			warrantyString=reader.nextLine();

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
			if(warrantyString.trim().length() != 0)
			{
				warranty = Integer.parseInt(warrantyString);
				query+="warranty=" + warranty + " AND ";
			}
			query+="1=1";
			
			//Execute Query
			System.out.println("Stock#  Category            Manufacturer  	Model#			Price Warranty");
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery (query);
			 //int i=0;
			 while(rs.next())
			 {	 //System.out.print(i + ": ");
				 System.out.print(rs.getString(1) + " ");
				 System.out.print(rs.getString(2) + " ");
				 System.out.print(rs.getString(3) + " ");
				 System.out.print(rs.getString(4) + " ");
				 System.out.print(rs.getFloat(5) + " 	");
				 System.out.println(rs.getInt(6) + " ");
				 //i++;
				 
			 }
			 rs.close();
			 stmt.close();
			 conn.close();
			
			
		}		
		public static void searchDescription(String attribute, String value) throws SQLException
		{
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery ("SELECT * FROM Catalog WHERE stockNum IN (SELECT stockNum FROM Description WHERE attribute='" + attribute + "' AND value='" + value + "')");
			 //int i=0;
			 while(rs.next())
			 {	 //System.out.print(i + ": ");
				 System.out.print(rs.getString(1) + " ");
				 System.out.print(rs.getString(2) + " ");
				 System.out.print(rs.getString(3) + " ");
				 System.out.print(rs.getString(4) + " ");
				 System.out.print(rs.getFloat(5) + " 	");
				 System.out.println(rs.getInt(6) + " ");
				 //i++;
				 
			 }
			 rs.close();
			 stmt.close();
			 conn.close();
		}
		//TODO: Elaborate accessory search and description search
		public static void searchAccessory(String stockNum) throws SQLException
		{
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery ("SELECT * FROM Catalog C WHERE C.stockNum IN (SELECT A.childStockNum FROM Accessories A WHERE A.parentStockNum='" + stockNum + "')" );
			 //int i=0;
			 while(rs.next())
			 {	 //System.out.print(i + ": ");
				 System.out.print(rs.getString(1) + " ");
				 System.out.print(rs.getString(2) + " ");
				 System.out.print(rs.getString(3) + " ");
				 System.out.print(rs.getString(4) + " ");
				 System.out.print(rs.getFloat(5) + " 	");
				 System.out.println(rs.getInt(6) + " ");
				 //i++;
				 
			 }
			 rs.close();
			 stmt.close();
			 conn.close();
		}
		
		public static int getCartQuantity(String cID, String stockNum) throws SQLException {
			int quantity = -1;
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement s = conn.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM Cart WHERE cID ='" + cID + "' AND stockNum = '" + stockNum + "'");
			if(rs.next()){
				quantity = rs.getInt("quantity");
				System.out.println("ITEM EXISTS! Quantity: " + quantity);
			}
			
			s.close();
			conn.close();
			return quantity;
		}
		public static double getPrice(String stockNum) throws SQLException {
			double price = -1;
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement s = conn.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM Catalog WHERE stockNum ='" + stockNum + "'");
			if(rs.next()){
				price = rs.getDouble("price");
				System.out.println("ITEM EXISTS! Price: " + price);
			}
			
			s.close();
			conn.close();
			return price;
		}
		//TODO: ADD TOTAL PRICE TO CART!!!!
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
			//reader.close();
			
			int oldquantity = getCartQuantity(cID, stockNum);
			if(oldquantity > 0){
				System.out.println("Old quantity: " + oldquantity + " Adding: " + quantity);
				quantity += oldquantity;
				System.out.println("NEW: " + quantity);
			}	
			
			double price = getPrice(stockNum);
			System.out.println("Price: " + price);
			if(price == -1)
			{
				System.out.println("Item does not exist in catalog!");
				return;
			}
			
			String updateCartSQL;
			double tprice=price*quantity;

			if(oldquantity > 0){ 
				System.out.println("TPrice: " + tprice);
				updateCartSQL = "UPDATE Cart SET quantity=" + quantity + ", tprice=" + tprice + " WHERE cID='" + cID + "' AND stockNum='" + stockNum + "'";
				System.out.println("Updating quantity and tprice of item!");
				conn = DriverManager.getConnection(strConn,strUsername,strPassword);
				PreparedStatement p = conn.prepareStatement(updateCartSQL);
				//p.setInt(1, quantity);
				//p.setDouble(2, price*quantity);
				//p.setString(3, cID);
				//p.setString(4, stockNum);
				p.executeUpdate();
				p.close();
				conn.close();
				System.out.println("Added to Cart");
				/*conn = DriverManager.getConnection(strConn,strUsername,strPassword);
				Statement s = conn.createStatement();
				System.out.println("BEFORE");
				ResultSet rs=s.executeQuery("UPDATE Cart SET quantity=" + quantity + ", tprice=" + price*quantity + " WHERE cID='" + cID + "' AND stockNum='" + stockNum + "'");
				System.out.println("AFTER");
				rs.close();
				conn.close();
				System.out.println("Added to Cart");*/
			}
			else {
				updateCartSQL = "INSERT INTO Cart(cID, stockNum, quantity, tprice) VALUES (?, ?, ?, ?)";
				System.out.println("Adding new item to cart!");
				conn = DriverManager.getConnection(strConn,strUsername,strPassword);
				PreparedStatement p = conn.prepareStatement(updateCartSQL);
				p.setString(1, cID);
				p.setString(2, stockNum);
				p.setInt(3, quantity);
				p.setDouble(4, tprice);
				p.executeUpdate();	
				p.close();
				conn.close();
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
				if(quantity > 0){ 
					double tprice=price*quantity;
					System.out.println("TPrice: " + tprice);
					updateCartSQL = "UPDATE Cart SET quantity=" + quantity + ", tprice=" + tprice + " WHERE cID='" + cID + "' AND stockNum='" + stockNum + "'";
					System.out.println("Updating quantity and tprice of item!");
					conn = DriverManager.getConnection(strConn,strUsername,strPassword);
					PreparedStatement p = conn.prepareStatement(updateCartSQL);
					//p.setInt(1, quantity);
					//p.setDouble(2, price*quantity);
					//p.setString(3, cID);
					//p.setString(4, stockNum);
					p.executeUpdate();
					p.close();
					conn.close();
					System.out.println("Removed from Cart");
				}
				else {
					updateCartSQL = "DELETE FROM Cart WHERE cID='" + cID + "' AND stockNum='" + stockNum + "'";
					System.out.println("Removing item from cart!");
					conn = DriverManager.getConnection(strConn,strUsername,strPassword);
					PreparedStatement p = conn.prepareStatement(updateCartSQL);
					//p.setString(1, cID);
					//p.setString(2, stockNum);
					
					p.executeUpdate();
					
					p.close();
					conn.close();
					System.out.println("Deleted from Cart");
				}
				
			
			}
		public static void viewCart(String cID) throws SQLException {
			double subtotal=0;
			double cost=0;
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement s = conn.createStatement();
			System.out.println("Stock Num	Quantity	Cost");
			ResultSet rs = s.executeQuery("SELECT * FROM Cart WHERE cID ='" + cID + "'");
			while(rs.next())
			 {	 //System.out.print(i + ": ");
				 //System.out.print(rs.getString(1) + "	");
				 System.out.print(rs.getString(2) + "		");
				 System.out.print(rs.getInt(3) + "	");
				 cost=rs.getDouble(4);
				 System.out.println(cost);
				 subtotal+=cost;
			 }
			System.out.println("Subtotal: "+subtotal);
			System.out.println("");
			 rs.close();
			 s.close();
			 conn.close();
		}
		public static void checkout(String cID) throws SQLException {
			double subtotal=0;
			double discount=0;
			double shipping=0;
			double totalcost=0;
			String status=null;
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement s = conn.createStatement();
			
			//Get subtotal
			//TODO: Add to orders table???
			ResultSet rs = s.executeQuery("SELECT * FROM Cart WHERE cID ='" + cID + "'");
			while(rs.next())
			 {	 
				subtotal+=rs.getDouble(4);			 
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
			System.out.println("Total Cost: " + totalcost);
			
			//Remove from cart
			//Add to orders table (gotta create)
			//Update customer number of purchases, purchase (0, 1, 2), and status
			//Add to history
			
			System.out.println("");
			 rs.close();
			 s.close();
			 conn.close();
		}
		public static String getStatus(String cID) throws SQLException {
			int numP=-1;
			String status=null;
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement s = conn.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM Customers WHERE cID ='" + cID + "'");
			if(rs.next()){
				status = rs.getString("status");
				numP = rs.getInt("numP");
				if(numP == 0)
				{
					System.out.println("NEW CUSTOMER! Gets gold discount on first purchase");
				}
			}
			else
			{
				System.out.println("SOMETHING WENT WRONG!!!!!");
			}
			rs.close();
			s.close();
			conn.close();
			return status;
		}

		public static double getDiscount(String status, Double subtotal)throws SQLException {
			double discount=0;
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement s = conn.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM Discount WHERE status ='" + status + "'");
			if(rs.next()){
				discount = rs.getDouble("percent");
			}
			rs.close();
			s.close();
			conn.close();
			return (discount*subtotal);
			
		}
		public static double getShipping(Double subtotal) throws SQLException{
			double minShipping=99999;
			double percent=0;
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Discount WHERE status ='Shipping'");
			if(rs.next()){
				minShipping = rs.getDouble("min");
				System.out.println("Minimum subtotal needed to waive shipping: " + minShipping);
				percent = rs.getDouble("percent");
				System.out.println("S&H Percent: " + percent);
			}
			if(subtotal>minShipping){
				return 0;
			}
			else{
				return (subtotal*percent);
			}
		}
		public static void updateCustomer(String cID, Double purchase)throws SQLException {
			
		}
}
