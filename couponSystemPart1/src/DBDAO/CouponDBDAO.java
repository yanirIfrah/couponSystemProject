package DBDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import CouponSystem.ConnectionPool;
import DAO.interfaces.CouponDAO;
import DataTypes.Coupon;
import DataTypes.CouponType;
import DataTypes.Customer;
import Exceptions.DAOException;
import Utiles.DateConvertUtils;
import Utiles.ProjectUtils;

/**
 * CouponDBDAO implementation of {@link CouponDAO} which enables to contact with
 * the CouponDB DB easily and safely.<br>
 * The channel of communication between the database and the class is
 * {@link Coupon} class.<br>
 * All connections to the DB are given by the the connectionPool instance , and
 * then returned to it back.
 */
public class CouponDBDAO implements CouponDAO {
	/**
	 * The instance of this singleton.
	 */
	private static CouponDBDAO instance = new CouponDBDAO();

	/**
	 * get the instance of the singleton.
	 * 
	 * @return instance
	 */
	public static CouponDBDAO getInstance() {
		if (instance == null) {
			instance = new CouponDBDAO();
		}
		return instance;
	}

	/**
	 * CTOR
	 */
	public CouponDBDAO() {
	}

	/**
	 * get the connection instance
	 */
	private ConnectionPool pool = ConnectionPool.getInstance();

	@Override
	public void createCoupon(Coupon coupon) throws DAOException {
		// check if the coupon exist in the DB
		if (isCouponExistById(coupon.getId())) {
			System.out.println("Coupon id : " + coupon.getId() + " Is Already Exist");
			return;
		}
		String sql = "INSERT INTO Coupon VALUES(?,?,?,?,?,?,?,?,?)";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, coupon.getId());
			statement.setString(2, coupon.getTitle());
			statement.setDate(3, DateConvertUtils.convertToSql(coupon.getStartDate()));
			statement.setDate(4, DateConvertUtils.convertToSql(coupon.getEndDate()));
			statement.setInt(5, coupon.getAmount());
			statement.setString(6, coupon.getType().toString());
			statement.setString(7, coupon.getMessage());
			statement.setDouble(8, coupon.getPrice());
			statement.setString(9, coupon.getImage());
			statement.execute();
			System.out.println("New Coupon ID: " + coupon.getId() + " Was Inserted To The DB\n**********");

		} catch (SQLException e) {
			throw new DAOException("Create Coupon Failed! A coupon with ID '" + coupon.getId() + "' already exist!", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void deleteCoupon(Coupon coupon) throws DAOException {
		// check if the coupon not exist in the DB
		if (!isCouponExistById(coupon.getId())) {
			System.out.println("Can't Delete This Specific Coupon. Isn't Exist In The DB.");
			return;
		}
		String sql = "DELETE FROM Coupon WHERE ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, coupon.getId());
			statement.executeUpdate();
			System.out.println("Coupon '" + coupon.getTitle() + "' Was Deleted From The DB");

		} catch (Exception e) {
			throw new DAOException("Delete coupon :" + coupon.getId() + " failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void updateCoupon(Coupon coupon) throws DAOException {
		// check if th coupon exist in the DB
		if (!isCouponExistById(coupon.getId())) {
			System.out.println("Cannot Update This Specific Coupon. Isn't Exist In The DB.");
			return;
		}
		String sql = "UPDATE Coupon SET END_DATE=?,PRICE=? WHERE ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDate(1, DateConvertUtils.convertToSql(coupon.getEndDate()));
			statement.setDouble(2, coupon.getPrice());
			statement.setLong(3, coupon.getId());
			statement.execute();
			System.out.println("Coupon " + coupon.getTitle() + " Info Was Updated");

		} catch (Exception e) {
			throw new DAOException("Update Coupon Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void updateCouponAmount(Coupon coupon) throws DAOException {
		// check if th coupon exist in the DB
		if (!isCouponExistById(coupon.getId())) {
			System.out.println("Cannot Update This Specific Coupon. Isn't Exist In The DB.");
			return;
		}
		String sql = "UPDATE Coupon SET AMOUNT=? WHERE ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, coupon.getAmount());
			statement.setDouble(2, coupon.getId());
			statement.execute();
			System.out.println("Coupon " + coupon.getTitle() + " Info Was Updated");

		} catch (Exception e) {
			throw new DAOException("Update Coupon amount Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Coupon getCoupon(long id) throws DAOException {
		Coupon coupon = new Coupon();
		// check if th coupon exist in the DB
		if (!isCouponExistById(id)) {
			System.out.println("Coupon: " + id + " - Is Not Exist");
			return coupon;
		}
		String sql = "SELECT * FROM  Coupon WHERE ID = ?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			ResultSet Rs = statement.executeQuery();
			if (Rs.next()) {
				// coupon was found. creating a new coupon
				coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				System.out.println("Fetching Coupon " + id + " In The DB");
			}
			Rs.close();
			return coupon;
		} catch (SQLException e) {
			throw new DAOException("Get Coupon " + id + " Failed");
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getAllCoupon() throws DAOException {

		String sql = "SELECT * FROM Coupon";
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
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				/**
				 * create a new coupon
				 */
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				/**
				 * add the new coupon to the collection
				 */
				coupons.add(coupon);
			}
			if (coupons.isEmpty()) {
				System.out.println("There Is No Coupons In The DB");
			}
			Rs.close();
			return coupons;
		} catch (SQLException e) {
			throw new DAOException("Ge all coupons Failed");
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getAllPurchasedCouponByType(long customerId, CouponType type) throws DAOException {
		// check if the customer exist in the DB
		if (!CustomerDBDAO.getInstance().isCustomerExistById(customerId)) {
			System.out.println("Customer: " + customerId + " - Is Not Exist");
		}
		String sql = "SELECT * FROM Coupon JOIN Customer_Coupon on Coupon.ID = Customer_coupon.Coupon_ID WHERE Customer_Coupon.Customer_ID =? AND Coupon.TYPE = ?";

		/**
		 * create a collection of coupons (PurchasedCouponByType) which
		 * eventually will be returns
		 */
		Collection<Coupon> purchasedCouponByType = new ArrayList<>();
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, customerId);
			statement.setString(2, type.toString());
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				// add coupon to the collection
				purchasedCouponByType.add(coupon);
			}
			Rs.close();
			if (purchasedCouponByType.isEmpty()) {
				System.out.println("There Is No Coupons In The DB Of This Specific Customer And Type");
			}
			return purchasedCouponByType;
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
	public Collection<Coupon> getAllPurchasedCouponByPrice(Customer customer, double price) throws DAOException {

		String sql = "SELECT * FROM Coupon JOIN Customer_Coupon ON Coupon.ID = Customer_Coupon.Coupon.ID WHERE Customer.ID =? AND Coupon.PRICE <= ?";
		/**
		 * create a collection of coupons (AllPurchasedCouponBtPrice) which
		 * eventually will be returns
		 */
		Collection<Coupon> AllPurchasedCouponBtPrice = new ArrayList<>();
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			statement.setDouble(2, price);
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				// add coupon to the collection
				AllPurchasedCouponBtPrice.add(coupon);
			}
			Rs.close();
			return AllPurchasedCouponBtPrice;
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
	public boolean isCouponAvailableForPurchase(Coupon coupon) throws DAOException {

		String sql = "SELECT * FROM Coupon WHERE ID=?, AMOUNT=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, coupon.getId());
			statement.setInt(2, coupon.getAmount());
			statement.execute();
			if (coupon.getAmount() > 0) {
				System.out.println("The Coupon Is Available For Purchase");
				return true;
			} else {
				System.out.println("The Coupon Is Unvailable For Purchase");
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Retrieving Coupon From DB Error. Please Try Again. ", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponsByMaxPrice(double maxPrice) throws DAOException {

		String sql = "SELECT * FROM Coupon WHERE PRICE<=?";
		/**
		 * create a collection of coupons (couponMaxPrice) which eventually will
		 * be returned
		 */
		Collection<Coupon> couponMaxPrice = new ArrayList<>();
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDouble(1, maxPrice);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {

				Coupon coup = ProjectUtils.extractCouponFromResultSet(rs);
				// add the new coupon to the collection
				couponMaxPrice.add(coup);
			}
			rs.close();
			if (couponMaxPrice.isEmpty()) {
				System.out.println("There Is No Coupons In The DB By This Max Price");
			}
			// return the collection of Coupons By MaxPrice
			return couponMaxPrice;
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
	public Collection<Coupon> getCouponsByMaxEndDate(String maxEndDate) throws DAOException {

		String sql = "SELECT * FROM Coupon WHERE END_DATE<=?";
		/**
		 * create a collection of coupons (couponMaxEndDate) which eventually
		 * will be returns
		 */
		Collection<Coupon> couponMaxEndDate = new ArrayList<>();
		/**
		 * converting date from sql date to string
		 */
		Date maxDate = DateConvertUtils.convertToSql(DateConvertUtils.stringToDate(maxEndDate));
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setDate(1, maxDate);
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				// create a new coupon
				Coupon coup = ProjectUtils.extractCouponFromResultSet(Rs);
				// add the new coupon to the collection
				couponMaxEndDate.add(coup);
				// return the collection of Coupons By MaxEndDate
			}
			Rs.close();
			// check if the collection is empty
			if (couponMaxEndDate.isEmpty()) {
				System.out.println("There Is No Coupon By This Max Date");
			}
			return couponMaxEndDate;
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
	public void deleteExpiredCoupons() throws DAOException {
		/**
		 * create a referencce to the current time
		 */
		Date today = new Date(System.currentTimeMillis());

		String couponSql = "SELECT * FROM COUPON WHERE END_DATE<'" + today + "'";
		String delCompany_CouponSql = "Delete FROM Company_Coupon WHERE Coupon_ID = ? ";
		String delCouponSql = "Delete FROM Coupon WHERE ID = ? ";
		String delCustomer_CouponSql = "Delete FROM Customer_Coupon WHERE Coupon_ID = ? ";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(couponSql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				long couponId = rs.getLong(1);
				PreparedStatement statement1 = connection.prepareStatement(delCompany_CouponSql);
				statement1.setLong(1, couponId);
				statement1.execute();
				PreparedStatement statement2 = connection.prepareStatement(delCouponSql);
				statement2.setLong(1, couponId);
				statement2.execute();
				PreparedStatement statement3 = connection.prepareStatement(delCustomer_CouponSql);
				statement3.setLong(1, couponId);
				statement3.execute();
			}
			System.out.println("*Trashed Expired Coupons*");
		} catch (SQLException e) {
			throw new DAOException("Failed to delete Company due to :" + e.getMessage());
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getPurchasedCouponByTypeAndCustId(long customerId, CouponType type) throws DAOException {
		/**
		 * create a collection of coupons (purchasedCouponByType) which
		 * eventually will be returns
		 */
		Collection<Coupon> purchasedCouponByType = new ArrayList<>();
		// check if the customer exist
		if (!CustomerDBDAO.getInstance().isCustomerExistById(customerId)) {
			System.out.println("Customer: " + customerId + " - Is Not Exist");
		}
		String sql = "SELECT * FROM Coupon JOIN Customer_Coupon on Coupon.ID = Customer_coupon.Coupon_ID WHERE Customer_Coupon.Customer_ID =? AND Coupon.TYPE = ?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, customerId);
			statement.setString(2, type.toString());
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				purchasedCouponByType.add(coupon);
			}
			// close the result set
			Rs.close();
			if (purchasedCouponByType.isEmpty()) {
				System.out.println("There Is No Coupons In The DB Of This Specific Customer And Type");
			}
			// return the collection with all the purchased coupons by Type
			return purchasedCouponByType;
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
	public boolean isCouponExistById(long id) throws DAOException {

		String sql = "SELECT * FROM Coupon WHERE ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			ResultSet Rs = statement.executeQuery();
			if (!(Rs == null)) {
				if (Rs.next()) {
					// System.out.println("Coupon Was Found");
					return true;
				} else {
					System.out.println("Coupon ID:" + id + " Wasn't Found");
					return false;
				}
			}
			return false;
		} catch (SQLException e) {
			throw new DAOException("Connection Error");
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public boolean isCouponExistByCouponType(CouponType couponType) throws DAOException {

		String sql = "SELECT * FROM Coupon WHERE TYPE=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, couponType.toString());
			ResultSet Rs = statement.executeQuery();
			if (!(Rs == null)) {
				if (Rs.next()) {
					System.out.println("Coupon Was Found");
					return true;
				} else {
					System.out.println("Coupon Wasn't Found");
					return false;
				}
			}
			return false;
		} catch (SQLException e) {
			throw new DAOException("Connection Error" + e.getMessage());
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public boolean isCouponExistByTitle(Coupon coupon) throws DAOException {

		String sql = "SELECT * FROM Coupon WHERE TITLE=?";
		/**
		 * get a connection from the pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, coupon.getTitle());
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				System.out.println("There Is A Company With This Title In The System");
				rs.close();
				return true;
			} else {
				System.out.println("There Isn't A Company With This Title In The System");
				rs.close();
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Failed To Check If Company Exist By ID");
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType type) throws DAOException {

		String sql = "SELECT * FROM Coupon WHERE TYPE=?";
		/**
		 * create a collection of coupons (coupons) which eventually will be
		 * returned
		 */
		Collection<Coupon> coupons = new ArrayList<>();
		/**
		 * get a connection from pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, type.toString());
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {

				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				// add the new coupon to the collection
				coupons.add(coupon);
				// return the collection of Coupons By Type
				return coupons;

			}
			Rs.close();
			return coupons;
		} catch (SQLException e) {
			throw new DAOException("Get Coupon By Type Failed");
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getPurchasedCouponsByCustAndMaxPrice(Customer customer, double maxPrice)
			throws DAOException {
		// create a new customerDBDAO object
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		// check if customer exists
		if (!customerDBDAO.isCustomerExistById(customer.getId())) {
			System.out.println("Customer: " + customer.getId() + " - Is Not Exist");
		}
		/**
		 * create a collection of coupons (purchasedCouponByMaxPrice) which
		 * eventually will be returns
		 */
		Collection<Coupon> purchasedCouponByMaxPrice = new ArrayList<>();
		String sql = "SELECT * FROM Coupon JOIN Customer_Coupon on Coupon.ID = Customer_coupon.Coupon_ID WHERE Customer_Coupon.Customer_ID =? AND Coupon.Price <= ?";
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
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				// creae a new coupon
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				purchasedCouponByMaxPrice.add(coupon);
			}
			// close the result set
			Rs.close();
			if (purchasedCouponByMaxPrice.isEmpty()) {
				System.out.println("There Is No Coupons In The DB Of This Specific Customer And Max Price");
			}
			// return the collection with all the purchased coupons by Type
			return purchasedCouponByMaxPrice;
		} catch (SQLException e) {
			throw new DAOException("Retrieving Coupons From DB Error. Please Try Again. ", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

}
