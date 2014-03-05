package store;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;


public class DatabaseConnection {
	
	static Connection conn;
	static String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	static String strUsername = "asarraf";
	static String strPassword = "4913604";

	public static void main(String[] args) throws SQLException{

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
		searchModel("a01");
		
		}
		
		
		public static void searchStockNum(String stockNum) throws SQLException
		{
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery ("SELECT * FROM Catalog WHERE stockNum='" + stockNum + "'");
			 System.out.println("result:");
			 while(rs.next())
			 {	 
				 System.out.print("Stock Number: ");
				 System.out.print(rs.getString(1) + " ");
				 System.out.print("Category: ");
				 System.out.print(rs.getString(2) + " ");
				 System.out.print("Manufacturer: ");
				 System.out.print(rs.getString(3) + " ");
				 System.out.print("Model#: ");
				 System.out.print(rs.getString(4) + " ");
				 System.out.print("Price: ");
				 System.out.print(rs.getFloat(5) + " ");
				 System.out.print("Warranty Years: ");
				 System.out.println(rs.getInt(6) + " ");

				 
			 }
			 rs.close();
			 stmt.close();
			 conn.close();
		}
		
		public static void searchCategory(String category) throws SQLException
		{
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery ("SELECT * FROM Catalog WHERE category='" + category + "'");
			 System.out.println("result:");
			 while(rs.next())
			 {	 
				 System.out.print("Stock Number: ");
				 System.out.print(rs.getString(1) + " ");
				 System.out.print("Category: ");
				 System.out.print(rs.getString(2) + " ");
				 System.out.print("Manufacturer: ");
				 System.out.print(rs.getString(3) + " ");
				 System.out.print("Model#: ");
				 System.out.print(rs.getString(4) + " ");
				 System.out.print("Price: ");
				 System.out.print(rs.getFloat(5) + " ");
				 System.out.print("Warranty Years: ");
				 System.out.println(rs.getInt(6) + " ");

				 
			 }
			 rs.close();
			 stmt.close();
			 conn.close();
		}
	
		public static void searchModel(String model) throws SQLException
		{
			conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery ("SELECT * FROM Catalog WHERE modelNum='" + model + "'");
			 System.out.println("result:");
			 while(rs.next())
			 {	 
				 System.out.print("Stock Number: ");
				 System.out.print(rs.getString(1) + " ");
				 System.out.print("Category: ");
				 System.out.print(rs.getString(2) + " ");
				 System.out.print("Manufacturer: ");
				 System.out.print(rs.getString(3) + " ");
				 System.out.print("Model#: ");
				 System.out.print(rs.getString(4) + " ");
				 System.out.print("Price: ");
				 System.out.print(rs.getFloat(5) + " ");
				 System.out.print("Warranty Years: ");
				 System.out.println(rs.getInt(6) + " ");

				 
			 }
			 rs.close();
			 stmt.close();
			 conn.close();
		}

}
