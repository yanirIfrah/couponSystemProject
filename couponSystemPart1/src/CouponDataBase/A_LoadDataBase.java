package CouponDataBase;

// DriverLoading
public class A_LoadDataBase {
	/**
	 * The instance of the A_LoadDataBase
	 */
	private static A_LoadDataBase instance = new A_LoadDataBase();

	/**
	 * Get the instance of the A_LoadDataBase
	 * 
	 * @return instance
	 */
	public static A_LoadDataBase getInstance() {
		if (instance == null) {
			instance = new A_LoadDataBase();
		}
		return instance;
	}

	// **********************************************************************************/////
	public static void main(String[] args) {
		/**
		 * Call the loadDataBase method
		 */
		A_LoadDataBase loadDataBase = new A_LoadDataBase();
		loadDataBase.loadDataBase();
	}

	/**
	 * loadDataBase is loading the driver name
	 */
	public void loadDataBase() {
		String driverName = "org.apache.derby.jdbc.ClientDriver";

		try {
			Class.forName(driverName);
			System.out.println("Loading Driver: " + driverName + "\n**********");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
