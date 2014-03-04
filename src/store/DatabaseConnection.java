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
		 conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		 Statement stmt = conn.createStatement();
		 ResultSet rs = stmt.executeQuery ("SELECT * FROM Inventory");
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
		}
	
	

}
