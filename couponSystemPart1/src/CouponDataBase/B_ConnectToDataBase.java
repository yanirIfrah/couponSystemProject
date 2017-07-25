package CouponDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class B_ConnectToDataBase {
	/**
	 * The instance of the B_ConnectToDataBase
	 */
	private static B_ConnectToDataBase instance = new B_ConnectToDataBase();

	/**
	 * Get the instance of the B_ConnectToDataBase
	 * 
	 * @return B_ConnectToDataBase
	 */
	public static B_ConnectToDataBase getInstance() {
		if (instance == null) {
			instance = new B_ConnectToDataBase();
		}
		return instance;
	}

	// *****************************************************
	public static void main(String[] args) {
		/**
		 * called the connectToDateBase method
		 */
		B_ConnectToDataBase connectToDataBase = new B_ConnectToDataBase();
		connectToDataBase.connectToDateBase();
	}

	/**
	 * Connecting to the DateBase
	 */
	public void connectToDateBase() {

		String DBURL = "jdbc:derby://localhost:1527/CouponDB;create=true";

		try (Connection con = DriverManager.getConnection(DBURL);) {
			System.out.println("Connected To Driver...\n**********");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
