package DBDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import CouponSystem.ConnectionPool;
import DAO.interfaces.JoinedTableDAO;
import DataTypes.Company;
import DataTypes.Coupon;
import DataTypes.CouponType;
import DataTypes.Customer;
import Exceptions.DAOException;
import Utiles.DateConvertUtils;
import Utiles.ProjectUtils;

/**
 * <p>
 * JoinedTableDBDAO implementation of @link JoinedTablesDAO which enable to
 * contact with the 'CouponDB' database easily and safely. The channels of
 * communication between the database and the class are: <br>
 * 
 * {@link Company}, {@link Customer}, {@link Coupon}<br>
 * All connections to the DB are given by the the connectionPool instance and
 * then returned to it.
 */
public class JoinedTableDBDAO implements JoinedTableDAO {
	/**
	 * The instance of this singleton.
	 */
	private static JoinedTableDBDAO instance = new JoinedTableDBDAO();

	/**
	 * get the instance of the singleton.
	 * 
	 * @return instance
	 */
	public static JoinedTableDBDAO getInstance() {
		if (instance == null) {
			instance = new JoinedTableDBDAO();
		}
		return instance;

	}

	/**
	 * CTOR
	 */
	public JoinedTableDBDAO() {
	}

	/**
	 * get the connection instance
	 */
	private ConnectionPool pool = ConnectionPool.getInstance();

	@Override
	public void deleteCoupon(Coupon coupon) throws DAOException {
		// check if the coupon not exist in the DB
		if (!CouponDBDAO.getInstance().isCouponExistById(coupon.getId())) {
			System.out.println("Coupon " + coupon.getId() + " Do Not Exist In The DB");
			return;
		}
		String sql = "DELETE FROM Company_Coupon WHERE Coupon_ID=?";
		String sql2 = "DELETE FROM Customer_Coupon WHERE Coupon_ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, coupon.getId());
			statement.execute();

			PreparedStatement statement2 = connection.prepareStatement(sql2);
			statement2.setLong(1, coupon.getId());
			statement2.execute();
			System.out.println("Coupon " + coupon.getTitle() + " Was Deleted From The DB");
		} catch (SQLException e) {
			throw new DAOException("delete Coupon Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void deleteCouponFromCustomer(Customer customer, Coupon coupon) throws DAOException {
		// create a newcustomerDBDAO instance
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		// check if the customer not exist in the DB
		if (!customerDBDAO.isCustomerExistById(customer.getId())) {
			System.out.println("Customer " + customer.getCustName() + "Do Not Exist In The DB");
			return;
		} else if (!CouponDBDAO.getInstance().isCouponExistById(coupon.getId())) {
			System.out.println("Coupon " + coupon.getTitle() + "Do Not Exist In The DB");
			return;
		} else {
			String sql = "DELETE FROM Customer_Coupon WHERE Customer_ID=? AND Coupon_ID=?";
			/**
			 * get a connection from connection pool
			 */
			Connection connection = pool.getConnection();
			// Creating a statement object which holds the SQL we're about to
			// execute
			try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setLong(1, customer.getId());
				statement.setLong(2, coupon.getId());
				statement.execute();
				System.out.println(
						"Coupon " + coupon.getId() + " Was Deleted From " + customer.getCustName() + " In The DB");
			} catch (SQLException e) {
				throw new DAOException("delete failed!", e);
			} finally {
				/**
				 * return the connection to the pool
				 */
				pool.returnConnection(connection);
			}
		}
	}

	@Override
	public void customerPurchaseCoupon(Customer customer, Coupon coupon) throws DAOException {
		// create a new CustomerDBDAO instence
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		// create a new CouponDBDAO instence
		CouponDBDAO couponDBDAO = new CouponDBDAO();
		// check if the customer not exist in the DB
		if (!customerDBDAO.isCustomerExistById(customer.getId())) {
			throw new DAOException("Cannot Purchase Coupon For This Customer. Customer Doesn't Exist In The DB");
		} else if (!couponDBDAO.isCouponExistById(coupon.getId())) {
			throw new DAOException("Cannot Purchase Coupon For This Customer. Coupon Doesn't Exist In The DB");
		} else if (customerDBDAO.isCustomerBoughtThisCoupon(customer, coupon)) {
			throw new DAOException("Coupon Already Purchased");
		} else {
			String sql = "INSERT INTO Customer_Coupon VALUES (?,?)";
			/**
			 * get a connection from connection pool
			 */
			Connection connection = pool.getConnection();
			Coupon coup = couponDBDAO.getCoupon(coupon.getId());
			int currentAmount = coup.getAmount();
			if (currentAmount == 0) {
				throw new DAOException("The coupon was not purchased since it is out of stock");
			}

			// Creating a statement object which holds the SQL we're about to
			// execute
			try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setLong(1, customer.getId());
				statement.setLong(2, coupon.getId());
				statement.execute();
				coup.setAmount(currentAmount - 1);
				couponDBDAO.updateCouponAmount(coup);
				System.out.println("Customer " + customer.getCustName() + " Added Coupon " + coupon.getId()
						+ " To The Company_Coupon Table");
			} catch (SQLException e) {
				throw new DAOException("Purchasing Of Customer_Coupon Error", e.getCause());
			} finally {
				/**
				 * return the connection to the pool
				 */
				pool.returnConnection(connection);
			}
		}
	}

	@Override
	public void createCompanyCoupon(Company company, Coupon coupon) throws DAOException {
		// create a new CompanyDBDAO instence
		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		// create a new CouponDBDAO instence
		CouponDBDAO couponDBDAO = new CouponDBDAO();
		// check if the company not exist in the DB
		if (!companyDBDAO.isCompanyExistByCompanyId(company.getId())) {
			System.out.println("Cannot Add Coupon For This Company. Company Doesn't Exist In The DB");
			return;
		} else if (!couponDBDAO.isCouponExistById(coupon.getId())) {
			System.out.println("Cannot Add Coupon For This Company. Coupon Doesn't Exist In The DB");
			return;
		} else if (companyDBDAO.isCompanyAlreadyCreatedThisCoupon(company, coupon)) {
			return;
		} else {
			String sql = "INSERT INTO Company_Coupon VALUES (?,?)";
			/**
			 * get a connection from connection pool
			 */
			Connection connection = pool.getConnection();
			// Creating a statement object which holds the SQL we're about
			// to execute
			try {
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setLong(1, company.getId());
				statement.setLong(2, coupon.getId());
				statement.execute();
				System.out.println("Company " + company.getCompName() + " Added Coupon " + coupon.getId()
						+ " To The Company_Coupon Table");
			} catch (SQLException e) {
				throw new DAOException("Creating A New Company_coupon Row Failed!");
			} finally {
				/**
				 * return the connection to the pool
				 */
				pool.returnConnection(connection);
			}
		}
	}

	@Override
	public Collection<Coupon> getAllPurchasedCouponByCustomerId(long custId) throws DAOException {
		/**
		 * create a collection of coupons (CustomerPurchasedCoupon) which
		 * eventually will be returns
		 */
		Collection<Coupon> customerPurchasedCoupon = new ArrayList<>();
		// check if the customer not exist in the DB
		if (!CustomerDBDAO.getInstance().isCustomerExistById(custId)) {
			System.out.println("Customer " + custId + " Do Not Exist In The DB");
			return customerPurchasedCoupon;
		}
		String sql = "SELECT * FROM Coupon JOIN Customer_Coupon ON Coupon.ID=Customer_Coupon.Coupon_ID WHERE Customer_ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, custId);
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				// add coupon to collection
				customerPurchasedCoupon.add(coupon);
			}
			Rs.close();
			if (customerPurchasedCoupon.isEmpty()) {
				System.out.println("There Are No Purchased Coupons By Customer " + custId);
			} else {
				System.out.println("Got All Customer " + custId + " Purchased Coupons");
			}
			return customerPurchasedCoupon;
		} catch (SQLException e) {
			throw new DAOException("Connection Failed!", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void deleteCustomerPurchaseHistory(Customer customer) throws DAOException {
		// create a new customerDBDAO instence
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		// check if the customer not exist in the DB
		if (!customerDBDAO.isCustomerExistById(customer.getId())) {
			System.out.println("Cannot Purchase Coupon For This Customer. Customer Doesn't Exist In The DB");
			return;
		}
		String sql = "DELETE FROM Customer_Coupon WHERE Customer_ID=?";
		Connection connection = pool.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			statement.execute();
			System.out.println("All " + customer.getCustName() + " Purchase History Was Erased");
		} catch (SQLException e) {
			throw new DAOException("Delete Customer Purchase History from Customer_Coupon Table Error", e);
		} finally {
			pool.returnConnection(connection);

		}
	}

	@Override
	public Collection<Coupon> getAddedCouponsByCompany(Company company) throws DAOException {
		/**
		 * create a collection of coupons (allCompanyCoupons) which eventually
		 * will be returns
		 */
		Collection<Coupon> allCompanyCoupons = new ArrayList<>();
		// check if the Company not exist in the DB
		if (!CompanyDBDAO.getInstance().isCompanyExistByCompanyId(company.getId())) {
			System.out.println("Company " + company.getCompName() + " Do Not Exist In The DB");
			return allCompanyCoupons;
		}
		String sql = "SELECT * FROM Coupon JOIN Company_Coupon ON Coupon.ID=Company_Coupon.Coupon_ID WHERE Company_ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				allCompanyCoupons.add(coupon);
			}
			Rs.close();
			if (allCompanyCoupons.isEmpty()) {
				System.out.println("There Are No Coupons By company " + company.getCompName());
			} else {
				System.out.println("Got All company " + company.getCompName() + " Added Coupons");
			}
			return allCompanyCoupons;
		} catch (SQLException e) {
			throw new DAOException("Connection Failed!", e);

		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getAllPurchasedCouponOfCustomerByMaxPrice(Customer customer, double maxPrice)
			throws DAOException {
		/**
		 * create a collection of coupons (allPurchasedCouponByPrice) which
		 * eventually will be returns
		 */
		Collection<Coupon> allPurchasedCouponByPrice = new ArrayList<>();
		// check if the Customer not exist in the DB
		if (!CustomerDBDAO.getInstance().isCustomerExistById(customer.getId())) {
			System.out.println("This Entered Customer Isn't Exist");
			return allPurchasedCouponByPrice;
		}

		String sql = "SELECT * FROM Coupon INNER JOIN Customer_Coupon on Coupon.ID = Customer_Coupon.Coupon_ID WHERE Customer_Coupon.Customer_ID =? AND Coupon.PRICE <= ?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			statement.setDouble(2, maxPrice);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(rs);
				allPurchasedCouponByPrice.add(coupon);
			}
			rs.close();
			if (allPurchasedCouponByPrice.isEmpty()) {
				System.out.println("There Is No Purchased Coupons At This Max Price From This Customer");
			}
			// return the collection with all the purchased coupons by price
			return allPurchasedCouponByPrice;
		} catch (SQLException e) {
			throw new DAOException("Retrieving Coupons From DB Error. Please Try Again. ", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getAllCouponsOfCompanyByMaxPrice(Company company, double maxPrice) throws DAOException {
		/**
		 * create a collection of coupons (AllCouponsOfCompanyByMaxPrice) which
		 * eventually will be returns
		 */
		Collection<Coupon> AllCouponsOfCompanyByMaxPrice = new ArrayList<>();
		// create a companyDBDAO new instence
		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		// check if the company not exist in the DB
		if (!companyDBDAO.isCompanyExistByCompanyId(company.getId())) {
			System.out.println("This Entered Company Isn't Exist");
			return AllCouponsOfCompanyByMaxPrice;
		}

		String sql = "SELECT * FROM Coupon JOIN Company_Coupon on Coupon.ID = Company_Coupon.Coupon_ID WHERE Company_Coupon.Company_ID =? AND Coupon.PRICE <= ?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			statement.setDouble(2, maxPrice);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(rs);
				AllCouponsOfCompanyByMaxPrice.add(coupon);
			}
			rs.close();
			if (AllCouponsOfCompanyByMaxPrice.isEmpty()) {
				System.out.println("There Is No Coupons At This Max Price-" + maxPrice + " From This Company - "
						+ company.getCompName());
			}
			System.out.println(
					"Got Coupons At This Max Price-" + maxPrice + " From This Company - " + company.getCompName());
			// return the collection with all the purchased coupons by price
			return AllCouponsOfCompanyByMaxPrice;
		} catch (SQLException e) {
			throw new DAOException("Retrieving Coupons From DB Error. Please Try Again. ", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getAllCouponsOfCompanyByMaxEndDate(Company company, String maxEndDate)
			throws DAOException {
		/**
		 * create a collection of coupons (couponOfCompanyByMaxEndDate) which
		 * eventually will be returns
		 */
		Collection<Coupon> couponOfCompanyByMaxEndDate = new ArrayList<>();

		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		// check if the company not exist in the DB
		if (!companyDBDAO.isCompanyExistByCompanyId(company.getId())) {
			System.out.println("Company: " + company.getId() + " - Is Not Exist");
			return couponOfCompanyByMaxEndDate;
		}
		String sql = "SELECT * FROM Coupon JOIN Company_Coupon on Coupon.ID = Company_Coupon.Coupon_ID WHERE Company_Coupon.Company_ID =? AND Coupon.END_DATE <= ?";
		/**
		 * get a connection from pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			statement.setDate(2, DateConvertUtils.convertToSql(DateConvertUtils.stringToDate(maxEndDate)));
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				// create a new coupon
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				// add the new coupon to the collection
				couponOfCompanyByMaxEndDate.add(coupon);
			}
			Rs.close();
			if (couponOfCompanyByMaxEndDate.isEmpty()) {
				System.out.println("There Is Coupons At This MaxEndDate-" + maxEndDate + " From This Company - "
						+ company.getCompName());
			}
			System.out.println(
					"Got Coupons At This MaxEndDate-" + maxEndDate + " From This Company - " + company.getCompName());
			// return the collection of Coupons By MaxEndDate
			return couponOfCompanyByMaxEndDate;
		} catch (SQLException e) {
			throw new DAOException("Retrieving Coupons From DB Error. Please Try Again. ", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getAllCouponsOfCompanyByMaxEndDate(Company company, Date maxEndDate) throws DAOException {
		/**
		 * create a collection of coupons (allPurchasedCouponByPrice) which
		 * eventually will be returns
		 */
		Collection<Coupon> allPurchasedCouponByPrice = new ArrayList<>();
		// create a companyDBDAO new instence
		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		// check if the company not exist in the DB
		if (!companyDBDAO.isCompanyExistByCompanyId(company.getId())) {
			System.out.println("This Entered Company Isn't Exist");
			return allPurchasedCouponByPrice;
		}

		String sql = "SELECT * FROM Coupon JOIN Company_Coupon on Coupon.ID = Company_Coupon.Coupon_ID WHERE Company_Coupon.Company_ID =? AND Coupon.END_DATE <= ?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			statement.setDate(2, maxEndDate);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(rs);
				allPurchasedCouponByPrice.add(coupon);
			}
			rs.close();
			if (allPurchasedCouponByPrice.isEmpty()) {
				System.out.println("There Is Coupons At This MaxEndDate-" + maxEndDate + " From This Company - "
						+ company.getCompName());
			}
			// return the collection with all the purchased coupons by price
			System.out.println(
					"Got Coupons At This MaxEndDate-" + maxEndDate + " From This Company - " + company.getCompName());
			return allPurchasedCouponByPrice;
		} catch (SQLException e) {
			throw new DAOException("Retrieving Coupons From DB Error. Please Try Again. ", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void deleteAllCompanyCouponsInDB(Company company) throws DAOException {
		// create a companyDBDAO new instence
		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		// check if the company not exist in the DB
		if (!companyDBDAO.isCompanyExistByCompanyId(company.getId())) {
			System.out.println("Cannot Delete This Specific Company's Coupons. Company Isn't Exist In The DB.");
			return;
		}
		String select = "SELECT Coupon_ID FROM Company_Coupon WHERE Company_ID= ?";
		String delete = "DELETE FROM Customer_Coupon WHERE Coupon_ID=?";
		String deleteCoupon = "DELETE FROM Coupon WHERE ID=?";
		String delComCoup = "DELETE FROM Company_Coupon WHERE Company_ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statementSelect = connection.prepareStatement(select);
			// select all the company coupons from the company coupon table
			PreparedStatement statementDelete = connection.prepareStatement(delete);

			PreparedStatement statementDeleteCoupon = connection.prepareStatement(deleteCoupon);
			// delete all the company coupons from the company coupon table
			PreparedStatement stmt = connection.prepareStatement(delComCoup);
			stmt.setLong(1, company.getId());
			stmt.executeUpdate();
			// select all the company coupons from the company coupon table
			statementSelect.setLong(1, company.getId());
			ResultSet Rs = statementSelect.executeQuery();
			while (Rs.next()) {
				long id = Rs.getLong(1);
				// delete all the company coupons from the Customer_Coupon table
				statementDelete.setLong(1, id);
				statementDelete.execute();
				// delete all the company coupons from the Coupon table
				statementDeleteCoupon.setLong(1, id);
				statementDeleteCoupon.execute();
			}
			Rs.close();
			System.out.println("Deleted All Company-" + company.getCompName() + " Coupons From All DB");
		} catch (Exception e) {
			throw new DAOException("Deleting Company " + company.getCompName() + " Coupons Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponByTypeOfSpecificCompany(Company company, CouponType type) throws DAOException {

		String sql = "SELECT * FROM Coupon INNER JOIN Company_Coupon ON Coupon.ID=Company_Coupon.Coupon_ID WHERE Company_Coupon.Company_ID = ? AND Coupon.TYPE=?";

		/**
		 * create a collection of coupons (coupons) which eventually will be
		 * returns
		 */
		Collection<Coupon> coupons = new ArrayList<>();
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			statement.setString(2, type.toString());
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {

				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				// add the new coupon to the collection
				coupons.add(coupon);
			}
			Rs.close();
			// return the collection of Coupons By Type
			return coupons;
		} catch (SQLException e) {
			throw new DAOException("Ge all coupons by type Failed");
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

}
