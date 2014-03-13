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
	//custMenu("sarrafGUY");
	deleteOld();
	
	}
		
	public static void custMenu(String cID) throws SQLException
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
			System.out.println("");
			System.out.print("Choice: ");

			//Read values
			choice=reader.nextInt();
			reader.nextLine(); //Get rid of unread new line

			switch (choice) 
			{
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
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
		conn.close();
	}
		
	public static void receiveShipment(int sID) throws SQLException
	{
		// Select relevent info from Shipment
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
		conn.close();
	}	
	
	public static void checkQuantity(String stockNum) throws SQLException
	{
		if(stockNum.length() > 7)
		{
			System.out.println("ERROR: length of stockNum must be 7");
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
	
	public static int getSID() throws SQLException
	{
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT value FROM Defined WHERE type = 'SID'");
		if(rs.next())
		{
			int result = rs.getInt(1);
			stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE Defined SET value = value + 1 WHERE type = 'SID'");
			return result;
		}
		else
		{
			System.out.println("ERROR: no SID in table Defined");
			return -1;
		}
	}
	
	public static int getStockNum() throws SQLException
	{
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT value FROM Defined WHERE type = 'stockNum'");
		if(rs.next())
		{
			int result = rs.getInt(1);
			stmt = conn.createStatement();
			stmt.executeUpdate("UPDATE Defined SET value = value + 1 WHERE type = 'stockNum'");
			return result;
		}
		else
		{
			System.out.println("ERROR: no stockNum in table Defined");
			return -1;
		}
	}
	
	public static void fillOrder(int orderNum) throws SQLException
	{
		// Select stock# and quantity from Orders where orderNum
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
				String modelNum = rs3.getString(1);
				processShippingNotice(SID, "Replenishment!", manufacturer, modelNum, quantity);
				System.out.println("Sent replenishment order for man: " + manufacturer + " mod: " + modelNum);
			}
			rs3.close();
		}
		rs2.close();		
		rs.close();
		stmt.close();
		conn.close();
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
		System.out.print("Accessories? (y/n) ");
		accessories=reader.nextLine();
		
		//TODO: Descriptions
		

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
			 String tempStock=rs.getString(1);
			 System.out.print(tempStock + " ");
			 System.out.print(rs.getString(2) + " ");
			 System.out.print(rs.getString(3) + " ");
			 System.out.print(rs.getString(4) + " ");
			 System.out.print(rs.getFloat(5) + " 	");
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
		checkQuantity(stockNum);
		
		double price = getPrice(stockNum);
		System.out.println("Price: " + price);
		if(price == -1)
		{
			System.out.println("Item does not exist in catalog!");
			return;
		}
		
		String updateCartSQL;
		double tprice=price*quantity;

		if(oldquantity > 0)
		{ 
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
		else 
		{
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
			if(quantity > 0)
			{ 
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
			else 
			{
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
	public static void viewCart(String cID) throws SQLException 
	{
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
	public static void checkout(String cID) throws SQLException 
	{	//TODO: Test if no items in cart
		double subtotal=0;
		double discount=0;
		double shipping=0;
		double totalcost=0;
		
		//Temp values to fill history table
		String stockNum;
		int quantity;
		double tprice;
		
		String status=null;
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
		
		//TODO: SEND SHIPPING NOTICE TO EDEPOT???

		System.out.println("");
		rs.close();
		s.close();
		conn.close();
	}
	public static String getStatus(String cID) throws SQLException 
	{
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
				System.out.println("NEW CUSTOMER! ");
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

	public static double getDiscount(String status, Double subtotal)throws SQLException 
	{
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
	public static double getShipping(Double subtotal) throws SQLException
	{
		double minShipping=99999;
		double percent=0;
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
		return orderNum;
	}
	
	//Updates customer purchases and status
	public static void updateCustomer(String cID) throws SQLException {
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
			total+=rs.getInt(1);
			i++;
		}
		status=getNewStatus(total);
		s=conn.createStatement();
		s.executeUpdate("UPDATE Customer SET status='" + status + "' WHERE cID='" + cID + "'");
		
		/*
		
		//Status values
		String status="Green";
	
		//Get customer data
		ResultSet rs = s.executeQuery("SELECT * FROM Customers WHERE cID ='" + cID + "'");
		if(rs.next()){
			numP=rs.getInt("numP");
			p0=rs.getDouble("p0");
			p1=rs.getDouble("p1");
			p2=rs.getDouble("p2");
			lastPurchase=numP%3; //Determines where to record purchase in customer data (either p0, p1, or p2)
			if(lastPurchase == 0){
				total=purchase+p1+p2;
				status=getNewStatus(total);
				numP++;
				PreparedStatement p = conn.prepareStatement("UPDATE Customers SET p0=" + purchase + ", numP=" + numP + ", status='" + status + "' WHERE cID='" + cID + "'");
				p.executeUpdate();
				p.close();
			}
			else if(lastPurchase == 1){
				total=purchase+p0+p2;
				status=getNewStatus(total);
				PreparedStatement p = conn.prepareStatement("UPDATE Customers SET p1=" + purchase + ", numP=" + numP+1 + ", status='" + status + "' WHERE cID='" + cID + "'");
				p.executeUpdate();
				p.close();
			}
			else if(lastPurchase == 2){
				total=purchase+p0+p1;
				status=getNewStatus(total);
				PreparedStatement p = conn.prepareStatement("UPDATE Customers SET p2=" + purchase + ", numP=" + numP+1 + ", status='" + status + "' WHERE cID='" + cID + "'");
				p.executeUpdate();
				p.close();
			}
		}
		else{
			System.out.println("ERROR: Customer doesn't exist!!!");
		}
		*/
		rs.close();
		s.close();
		conn.close();
	}
	
	public static String getNewStatus(double total) throws SQLException
	{
		System.out.println("Getting new status...");
		System.out.println("Total: " + total);
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
			System.out.println("DEBUG: Current status is " + statusCheck);
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
		conn.close();
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
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
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
		conn.close();
	}
	
	public static void printSummary() throws SQLException
	{
		conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT stockNum FROM Catalog");
		System.out.println("1");
		while(rs.next())
		{
			System.out.println("2");

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
		conn.close();
	}
}
