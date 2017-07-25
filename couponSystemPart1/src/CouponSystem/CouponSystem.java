package CouponSystem;

import java.sql.SQLException;

import CouponDataBase.A_LoadDataBase;
import CouponDataBase.B_ConnectToDataBase;
import CouponDataBase.C_Statements;
import DBDAO.CompanyDBDAO;
import DBDAO.CustomerDBDAO;
import Exceptions.CouponSystemException;
import Exceptions.DAOException;
import Facade.AdminFacade;
import Facade.ClientType;
import Facade.CompanyFacade;
import Facade.CouponClientFacade;
import Facade.CustomerFacade;

/**
 * A singleton That function as the top layer of the application, starting the
 * connectionPool instance and closing it.
 */
public class CouponSystem {

	// private CompanyFacade companyFacade;
	// private CustomerFacade customerFacade;
	// private AdminFacade adminFacade;
	/**
	 * The instance of this singleton class
	 */
	private static CouponSystem instance;

	/**
	 * Initializes the instance of this singleton class.
	 * 
	 * @throws CouponSystemException
	 *             When The driver of database could not load properly.
	 */
	public static void initialize() throws CouponSystemException {
		try {
			instance = new CouponSystem();
		} catch (SQLException e) {
			throw new CouponSystemException("Error in initializing the Coupon systam" + e.getMessage());
		}
	}

	/**
	 * A safe way to use the instance (cannot be changed).
	 * 
	 * @return The instance of this singleton.
	 */
	public static CouponSystem getInstance() {
		return instance;
	}

	ConnectionPool connectionPool = ConnectionPool.getInstance();

	/**
	 * Initializes the daily thread
	 */

	static DailyCouponExpirationTask cleanerThread = new DailyCouponExpirationTask("Coupon Cleaner");

	// Private constructor to avoid clients applications creating instance.
	private CouponSystem() throws SQLException {
		// Load The Driver
		A_LoadDataBase.getInstance().loadDataBase();
		// Check The Connection To The Server
		B_ConnectToDataBase.getInstance().connectToDateBase();
		// Check If Tables Exists In The DB, And If Not - Creates Them
		if (!C_Statements.getInstance().checkIfTablesExistInTheDB()) {
			C_Statements.getInstance().createTableAtDB();
		}
		// Run The Expired Coupons Delete Thread
		cleanerThread.start();
	}

	/**
	 * Login action performed by client with specific credentials.
	 * 
	 * @param userName
	 *            The name of the client.
	 * @param password
	 *            The password of the client.
	 * @param clientType
	 *            The client type of which to try and return facade instance
	 *            from.
	 * @return A facade of each type of client
	 * @throws CouponSystemException
	 *             When login failed, or the client type is not given.
	 * @throws DAOException
	 *             if connection failed
	 */
	public CouponClientFacade login(ClientType clientType, String userName, String password)
			throws CouponSystemException, DAOException {
		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		switch (clientType) {
		case ADMIN:
			if (userName.equals("admin")) {
				if (password.equals("1234")) {
					return AdminFacade.getInstance();
				}
				System.out.println("Admin Password Is Incorrect!");
			}
			System.out.println("Admin UserName Is Incorrect!");
			break;
		case COMPANY:
			if (companyDBDAO.login(userName, password)) {
				return new CompanyFacade(companyDBDAO.getCompanyByName(userName));
			}
			break;
		case CUSTOMER:
			if (customerDBDAO.login(userName, password)) {
				return new CustomerFacade(customerDBDAO.getCustomerByName(userName));
			}
			break;
		}
		throw new CouponSystemException("Wrong Login Info");
	}

	/**
	 * ShutDown method is cloesing The Daily Thread And The Connection Pool
	 */
	public void shutDown() {
		/**
		 * shut down the daily thread delete expired coupons
		 */
		cleanerThread.shutdownThread();
		/**
		 * close the connection when shut down the system
		 */
		connectionPool.closeAllConnections();
	}
}
