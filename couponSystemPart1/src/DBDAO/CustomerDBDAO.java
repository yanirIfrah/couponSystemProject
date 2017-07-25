package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import CouponSystem.ConnectionPool;
import DAO.interfaces.CustomerDAO;
import DataTypes.Coupon;
import DataTypes.Customer;
import Exceptions.DAOException;
import Utiles.ProjectUtils;

/**
 * <p>
 * CustomerDBDAO implementation of {@link CustomerDAO} which enable to contact
 * with the CouponDB database easily and safely.
 * </p>
 * The channels of communication between the database and the class are :
 * 
 * {@link Customer} and {@link Coupon} classes.<br>
 * All connections to the DB are given by the the connectionPool instance , and
 * then returned to it back.
 */
public class CustomerDBDAO implements CustomerDAO {
	/**
	 * The instance of this singleton.
	 */
	private static CustomerDBDAO instance = new CustomerDBDAO();

	/**
	 * get the instance of the singleton.
	 * 
	 * @return instance
	 */
	public static CustomerDBDAO getInstance() {
		if (instance == null) {
			instance = new CustomerDBDAO();
		}
		return instance;
	}

	/**
	 * CTOR
	 */
	public CustomerDBDAO() {
	}

	/**
	 * get the connection instance
	 */
	private ConnectionPool pool = ConnectionPool.getInstance();

	@Override
	public void createCustomer(Customer customer) throws DAOException {
		// check if the customer exist in the DB
		if (isCustomerExistById(customer.getId())) {
			System.out.println("Customer " + customer.getCustName() + ",ID: " + customer.getId()
					+ " Is Already Exist\n**********");
			return;
		}
		String sql = "INSERT INTO Customer VALUES(?,?,?)";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			statement.setString(2, customer.getCustName());
			statement.setString(3, customer.getPassword());
			statement.execute();
			System.out.println("Customer :" + customer.getCustName() + " Successfully Insert To The DB");
		} catch (Exception e) {
			throw new DAOException("Creating Customer Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void deleteCustomer(Customer customer) throws DAOException {
		// check if the customer not exist in the DB
		if (!isCustomerExistById(customer.getId())) {
			System.out.println("Cannot Delete This Specific Customer. Isn't Exist In The DB.");
			return;
		}
		String sql = "DELETE FROM Customer WHERE ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			statement.execute();
			System.out.println("Customer Deleted From The DB");
		} catch (Exception e) {
			throw new DAOException("Delete Customer Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void updateCustomer(Customer customer) throws DAOException {
		// check if the customer not exist in the DB
		if (!isCustomerExistById(customer.getId())) {
			System.out.println("Cannot Delete This Specific Customer. Isn't Exist In The DB.");
			return;
		}
		String sql = "UPDATE Customer SET PASSWORD=? WHERE ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			if (CustomerDBDAO.instance.isCustomerExistById(customer.getId())) {
				statement.setString(1, customer.getPassword());
				statement.setLong(2, customer.getId());
				statement.executeUpdate();
				System.out.println("Customer " + customer.getId() + " was updated");
			} else {
				System.out.println("This customer id : " + customer.getId() + " isn't exist");
			}
		} catch (Exception e) {
			throw new DAOException("Update Customer " + customer.getId() + " Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Customer getCustomer(long id) throws DAOException {
		// create a new customer object
		Customer customer = new Customer();
		// check if the customer not exist in the DB
		if (!isCustomerExistById(id)) {
			System.out.println("Customer: " + id + " - Is Not Exist");
			return customer;
		}
		String sql = "SELECT * FROM Customer WHERE ID = ?";
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
				customer.setId(id);
				customer.setCustName(Rs.getString(2));
				customer.setPassword(Rs.getString(3));
				Rs.close();
				return customer;
			}
		} catch (SQLException e) {
			throw new DAOException("Get Customer Failed!", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
		return customer;
	}

	@Override
	public Collection<Customer> getAllCustomers() throws DAOException {

		String sql = "SELECT * FROM Customer";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		/**
		 * create a collection of coupons (customers) which eventually will be
		 * returns
		 */
		Collection<Customer> customers = new ArrayList<>();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				// create a new customer for the collection
				Customer customer = new Customer();
				customer.setId(Rs.getLong(1));
				customer.setCustName(Rs.getString(2));
				customer.setPassword(Rs.getString(3));
				// add the new customer to the collection
				customers.add(customer);
			}
			Rs.close();
			if (customers.isEmpty()) {
				System.out.println("There Is No Customers In The DB");
			}
			return customers;
		} catch (SQLException e) {
			throw new DAOException("Get All Customers Failed!", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getPurchasedCouponsByCustomer(Customer customer) throws DAOException {
		/**
		 * create a collection of coupons (coupons) which eventually will be
		 * returns
		 */
		Collection<Coupon> coupons = new ArrayList<>();
		// check if the customer not exist in the DB
		if (!isCustomerExistById(customer.getId())) {
			System.out.println("This Customer ID-" + customer.getId() + " Isn't Exist In The DB");
			return coupons;
		}
		String sql = "SELECT Coupon_ID FROM Customer_Coupon WHERE Customer_ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, customer.getId());
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {

				String Tempsql = "SELECT * FROM Coupon WHERE ID=?";
				PreparedStatement Tempstatement = connection.prepareStatement(Tempsql);
				Tempstatement.setLong(1, Rs.getLong(1));
				ResultSet sqlCoupon = Tempstatement.executeQuery();
				while (sqlCoupon.next()) {
					/**
					 * create a ner coupon
					 */
					Coupon newCoupon = ProjectUtils.extractCouponFromResultSet(sqlCoupon);
					// add newCoupon to collection
					coupons.add(newCoupon);
				}
				sqlCoupon.close();
			}
			Rs.close();
			return coupons;
		} catch (SQLException e) {
			throw new DAOException("Coupon was not found ", e);
		} finally {
			/**
			 * return the connection to the npool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Boolean login(String custName, String password) throws DAOException {

		String sql = "SELECT PASSWORD FROM Customer WHERE CUST_NAME=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			//
			if (!isCustomerExistByName(custName)) {
				System.out.println("Customer " + custName + " Name Isn't Exist In The System");
				return false;
			}
			statement.setString(1, custName);
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				if (Rs.getString(1).equals(password)) {
					System.out.println(custName + " Password Is Correct");
					return true;
				} else {
					System.out.println("Sorry! " + custName + " Password is Incorrect!");
					return false;
				}
			}
			System.out.println(" There Is No Such " + custName + " In Database.");
			return false;
		} catch (SQLException e) {
			throw new DAOException("Login ERROR ! please try again", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public boolean isCustomerExistByName(String custName) throws DAOException {

		String sql = "SELECT * FROM Customer WHERE CUST_NAME=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, custName);
			ResultSet Rs = statement.executeQuery();
			if (Rs.next()) {
				System.out.println("Customer Is Found!");
				return true;
			} else {
				System.out.println("Customer was Not Found!");
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Connection Error!", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public boolean isCustomerExistById(long id) throws DAOException {

		String sql = "SELECT * FROM Customer WHERE ID=?";
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
			// if (!(Rs == null)) {
			if (Rs.next()) {
				// System.out.println("Customer ID: " + id + " Found");
				return true;
			} else {
				// System.out.println("Customer was'nt Found");
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Connection Error!", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Customer getCustomerByName(String custName) throws DAOException {
		// check if the customer not exist in the DB
		if (!isCustomerExistByName(custName)) {
			System.out.println("This Customer " + custName + " Isn't Exist In The DB");
		}
		String sql = "SELECT * FROM Customer WHERE CUST_NAME = ?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, custName);
			ResultSet Rs = statement.executeQuery();
			Customer customer = new Customer();
			if (Rs.next()) {
				customer.setId(Rs.getLong(1));
				customer.setCustName(custName);
				customer.setPassword(Rs.getString(3));
				Rs.close();
				return customer;
			} else {
				Rs.close();
				throw new DAOException("Customer: " + custName + " Is Not Exist");
			}
		} catch (SQLException e) {
			throw new DAOException("Get Customer By Name Failed!", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public boolean isCustomerBoughtThisCoupon(Customer customer, Coupon coupon) throws DAOException {

		String sql = "SELECT * FROM Customer_Coupon WHERE Customer_ID=? AND Coupon_ID=?";
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
			ResultSet Rs = statement.executeQuery();
			if (Rs.next()) {
				System.out.println("Customer Already Bought This Coupon");
				return true;
			} else {
				System.out.println("Coupon Is Available For Purchase");
				return false;
			}

		} catch (SQLException e) {
			throw new DAOException("Connection Error", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

}
