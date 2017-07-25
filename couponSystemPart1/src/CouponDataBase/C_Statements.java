package CouponDataBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A statement class is responsible for creating / deleting / reseting the DB
 * and also checks if the tables exists.<br>
 * this class created for the convenience of the project and for testing by the
 * programmer.
 */

public class C_Statements {
	/**
	 * set the names of the tables as final for the insurence that no one wull
	 * change the names of them.
	 */
	public static final String Name_Company = "Company";
	public static final String Name_Customer = "Customer";
	public static final String Name_Coupon = "Coupon";
	public static final String Name_Customer_Coupon = "Customer_Coupon";
	public static final String Name_Company_Coupon = "Company_Coupon";

	// the url for Database
	public static String url = "jdbc:derby://localhost:1527/CouponDB";
	public String DBURL = "jdbc:derby://localhost:1527/CouponDB";// that the
																	// file
																	// reader
																	// read from
	/**
	 * Get an instance of this class C_Statements
	 */
	private static C_Statements instance = new C_Statements();

	/**
	 * An instance of this class
	 * 
	 * @return C_Statements
	 */
	public static C_Statements getInstance() {
		if (instance == null) {
			instance = new C_Statements();
		}
		return instance;
	}

	/**
	 * creataTableAtDB method is creating all the tables in the DB, as required
	 * in this specific project (this method should not be here, and created for
	 * the convenience of the project and for testing by the programmer).
	 * 
	 * @throws SQLException
	 *             when connection to DB failed
	 */
	public void createTableAtDB() throws SQLException {
		/**
		 * create a new folder in the project package CouponSystem that holds
		 * the URL connecting to ths DB. checks before if the folder already
		 * exists.
		 */
		File folderName = new File("files");
		folderName.mkdir();
		if (folderName.exists()) {
			System.out.println("create a file: \n" + folderName + " was succesfully created");
		} else {
			System.out.println(folderName + " is existed");
		}
		// wirating the url to the file
		/**
		 * creating a folder that holds the file that holds the connection URL
		 * to database using ARM for make sure that the resource will be closed
		 */
		// ARM
		try (FileWriter out = new FileWriter("files/DataBaseURL.txt");) {
			String URL = "jdbc:derby://localhost:1527/CouponDB";
			out.write(URL);
			System.out.println("Wirating URL To The File Seccseed");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/**
		 * Reading the DBURL from the file DateBaseURL.txt
		 */
		// try (BufferedReader in = new BufferedReader(new
		// FileReader("files/DataBaseURL.txt"));) {
		// DBURL = in.readLine();
		// // System.out.println("The Source Url of The Databse is: " + DBURL);
		// } catch (IOException e4) {
		// }
		/**
		 * create a connection to database
		 */
		String r = "jdbc:derby://localhost:1527/CouponDB";
		try (Connection connection = DriverManager.getConnection(r)) {

			Statement statement = connection.createStatement();

			System.out.println("***\nconnected to: " + r + "\n");
			/**
			 * create an array list for creating the tables
			 */
			ArrayList<String> tables = new ArrayList<>();
			/**
			 * creating the tables for the database and added the created tables
			 * to the array list tables
			 */
			String Company = "create table " + Name_Company
					+ "(ID BIGINT PRIMARY KEY,COMP_NAME VARCHAR(30),PASSWORD VARCHAR(20),EMAIL VARCHAR(40))";
			String Customer = "create table " + Name_Customer
					+ "(ID BIGINT PRIMARY KEY,CUST_NAME VARCHAR(30),PASSWORD VARCHAR(20))";
			String Coupon = "create table " + Name_Coupon
					+ "(ID BIGINT PRIMARY KEY,TITLE VARCHAR(30),START_DATE DATE,END_DATE DATE,AMOUNT INTEGER,TYPE VARCHAR(20),MESSAGE VARCHAR (50),PRICE DOUBLE,IMAGE VARCHAR(15))";
			String Company_Coupon = "create table " + Name_Company_Coupon
					+ "(Company_ID BIGINT,Coupon_ID BIGINT, PRIMARY KEY(Company_ID,Coupon_ID))";
			String Customer_Coupon = "create table " + Name_Customer_Coupon
					+ "(Customer_ID BIGINT,Coupon_ID BIGINT, PRIMARY KEY(Customer_ID,Coupon_ID))";

			tables.add(Company);
			tables.add(Customer);
			tables.add(Coupon);
			tables.add(Company_Coupon);
			tables.add(Customer_Coupon);
			System.out.println("Create Tables in the DB:\n**********");
			for (int i = 0; i < tables.size(); i++) {
				statement.executeUpdate(tables.get(i));
				System.out.println(tables.get(i) + " Was Created !");
			}
			System.out.println("{{{{{{{   All Tables Created !  }}}}}}}\n***********\n");
		} catch (SQLException e) {
			throw new SQLException("Connection Failed", e.getMessage());
		}
	}

	/**
	 * deleteTablesFromDB methos is delete all the tables from the DB. this
	 * method should not be here, and created for the convenience of the project
	 * and for testing by the programmer.
	 * 
	 * @throws SQLException
	 *             when connection to DB failed
	 */
	public void deleteTablesFromDB() throws SQLException {
		// /**
		// * read the DateBase URL from the file that we created before.
		// */
		// try (BufferedReader in = new BufferedReader(new
		// FileReader("files/DataBaseURL.txt"));) {
		// DBURL = in.readLine();
		// // System.out.println("The Source Url of The Databse is: " + DBURL);
		// } catch (IOException e4) {
		// }
		/**
		 * create a cnnection to DB
		 */
		String url = "jdbc:derby://localhost:1527/CouponDB";
		try (Connection connection = DriverManager.getConnection(url)) {
			System.out.println("connected to: " + DBURL + "\n");
			/**
			 * create an array list to delete all tables in DB
			 */
			ArrayList<String> DeleteTables = new ArrayList<>();
			/**
			 * add the deleted tables to the array list deleteTables !
			 */
			DeleteTables.add(Name_Company);
			DeleteTables.add(Name_Customer);
			DeleteTables.add(Name_Coupon);
			DeleteTables.add(Name_Company_Coupon);
			DeleteTables.add(Name_Customer_Coupon);

			System.out.println("Delete All Tables :\n**********");
			Statement stmt = connection.createStatement();
			for (int i = 0; i < DeleteTables.size(); i++) {
				stmt.executeUpdate("Drop table " + DeleteTables.get(i));
				System.out.println(DeleteTables.get(i) + " Is Deleted From The DB !");
			}
			System.out.println("{{{{{{{   All Tables Deleted !  }}}}}}}\n***********\n");

			System.out.println("Disconnected From " + DBURL);
		} catch (SQLException e2) {
			throw new SQLException("Connection Error", e2.getMessage());
		}
	}

	/**
	 * boolean method checkIfTablesExistInTheDB check if the DB is exist. the
	 * rational of this method is to test only one table, and if it exists, we
	 * assume that all tables are existed at the DB.
	 * 
	 * @return True- if the tables is exist<br>
	 *         False- if the tables is not exist
	 */
	public boolean checkIfTablesExistInTheDB() {

		String sql = "SELECT * FROM Company_Coupon";

		try (Connection connection = DriverManager.getConnection(url);) {
			PreparedStatement statement = connection.prepareStatement(sql);
			System.out.println("Checking DB...\n");
			if (statement.execute()) {
				System.out.println("All Tables Exists In The DB");
				statement.close();
				return true;
			} else {
				statement.close();
				return false;
			}
		} catch (SQLException e) {
			System.out.println("The Required Table Does Not Exist In The DB");
			return false;
		}
	}

	/**
	 * resetTable method is deleteing all the tables in ths DB and create a new
	 * table. this method is for testing the systam and for the use of the
	 * programmer only
	 */
	public void resetTables() {
		if (checkIfTablesExistInTheDB()) {
			try {
				deleteTablesFromDB();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			createTableAtDB();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}