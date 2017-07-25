package DBDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import CouponSystem.ConnectionPool;
import DAO.interfaces.CompanyDAO;
import DataTypes.Company;
import DataTypes.Coupon;
import DataTypes.CouponType;
import Exceptions.DAOException;
import Utiles.ProjectUtils;

/**
 * CompanyDBDAO implementation of {@link CompanyDAO} which enables to contact
 * with the CouponDB Date base easily and safely.<br>
 * The channel of communication between the database and the class is
 * {@link Company} class.<br>
 * All connections to the DB are given by the the connectionPool instance , and
 * then returned to it back.
 */
public class CompanyDBDAO implements CompanyDAO {
	/**
	 * The instance of this singleton.
	 */
	private static CompanyDBDAO instance = new CompanyDBDAO();

	/**
	 * get the instance of the singleton.
	 * 
	 * @return instance of CompanyDAO
	 */
	public static CompanyDBDAO getInstance() {
		if (instance == null) {
			instance = new CompanyDBDAO();
		}
		return instance;
	}

	/**
	 * CTOR
	 */
	public CompanyDBDAO() {

	}

	/**
	 * get the connection instance
	 */
	private ConnectionPool pool = ConnectionPool.getInstance();

	@Override
	public void createCompany(Company company) throws DAOException {
		// check if the Company exist in the DB
		if (isCompanyExistByCompanyId(company.getId())) {
			System.out.println("The Company Name: '" + company.getCompName() + "' Is Already Exist.\n**********");
			return;
		}
		String sql = "INSERT INTO Company VALUES(?,?,?,?)";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			statement.setString(2, company.getCompName());
			statement.setString(3, company.getPassword());
			statement.setString(4, company.getEmail());
			statement.execute();
			System.out.println("Company " + company.getCompName() + " Successfully Insert To The DB");
		} catch (Exception e) {
			throw new DAOException("Insert company failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void deleteCompany(Company company) throws DAOException {
		// check if the Company exist in the DB
		if (!isCompanyExistByCompanyId(company.getId())) {
			System.out.println("Can't Delete This Specific Company. Isn't Exist In The DB.");
			return;
		}
		String sql = "DELETE FROM Company WHERE ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			statement.executeUpdate();
			System.out.println("Company " + company.getId() + " Deleted From The DB.");

		} catch (Exception e) {
			throw new DAOException("delete company " + company.getCompName() + " failed" + e.getMessage());
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public void updateCompany(Company company) throws DAOException {
		// check if the Company exist in the DB
		if (!isCompanyExistByCompanyId(company.getId())) {
			System.out.println("Cannot Update This Specific company. Isn't Exist In The DB.");
			return;
		}
		String sql = "UPDATE Company SET PASSWORD=?,EMAIL=? WHERE ID=? ";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, company.getPassword());
			statement.setString(2, company.getEmail());
			statement.setLong(3, company.getId());
			statement.executeUpdate();
			System.out.println("The Company " + company.getCompName() + " Info Was Updated");
		} catch (Exception e) {
			throw new DAOException("Update Company " + company.getCompName() + " Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Company getCompany(long id) throws DAOException {
		// create a new company object
		Company company = new Company();
		// check if the Company exist in the DB
		if (!isCompanyExistByCompanyId(id)) {
			System.out.println("Company: " + id + " - Is Not Exist");
			return company;
		}
		String sql = "SELECT * FROM Company WHERE ID = ?";
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
				company.setId(id);
				company.setCompName(Rs.getString(2));
				company.setPassword(Rs.getString(3));
				company.setEmail(Rs.getString(4));
				Rs.close();
				return company;
			}
			Rs.close();
			System.out.println("Fetching Company " + id + " In The DB");
		} catch (SQLException e) {
			throw new DAOException("Get Company " + id + " Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
		return company;
	}

	@Override
	public Collection<Company> getAllCompanies() throws DAOException {

		String sql = "SELECT * FROM Company";
		/**
		 * create a collection of coupons (companeis) which eventually will be
		 * returns
		 */
		Collection<Company> companeis = new ArrayList<>();
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
				 * if we get here, thats means we find a company and create a
				 * company instance
				 */
				Company comp = new Company();
				// set the company state
				comp.setId(Rs.getLong(1));
				comp.setCompName(Rs.getString(2));
				comp.setPassword(Rs.getString(3));
				comp.setEmail(Rs.getString(4));
				// add the company to the collection set companies
				companeis.add(comp);
			}
			if (companeis.isEmpty()) {
				System.out.println("There Is No Companies In The DB");
			}
			Rs.close();
			return companeis;
		} catch (SQLException e) {
			throw new DAOException("Get all companies Failed!", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCoupons(Company company) throws DAOException {
		/**
		 * create a collection of coupons (coupons) which eventually will be
		 * returns
		 */
		Collection<Coupon> coupons = new ArrayList<>();
		// check if the Company exist in the DB
		if (!isCompanyExistByCompanyId(company.getId())) {
			System.out.println("This Company ID-" + company.getId() + " Isn't Exist In The DB");
			return coupons;
		}
		String sql = "SELECT Coupon_ID FROM Company_Coupon WHERE Company_ID = ?";
		String sql2 = "SELECT * FROM Coupon WHERE ID= ?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			ResultSet CouponIDset = statement.executeQuery(sql);
			while (CouponIDset.next()) {
				PreparedStatement TempStatement = connection.prepareStatement(sql2);
				TempStatement.setLong(1, CouponIDset.getLong(1));
				ResultSet sqlCoupon = TempStatement.executeQuery();
				while (sqlCoupon.next()) {

					Coupon newCoupon = ProjectUtils.extractCouponFromResultSet(sqlCoupon);

					/**
					 * add a mew coupon to the collection
					 */
					coupons.add(newCoupon);
					CouponIDset.close();
				}
				sqlCoupon.close();
			}
			return coupons;
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
	public boolean login(String compName, String password) throws DAOException {

		String sql = "SELECT PASSWORD FROM Company WHERE COMP_NAME =?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			// check if the Company exist in the DB
			if (!isCompanyExistByCompanyName(compName)) {
				System.out.println("This Company Name Isn't Exist In The System");
				return false;
			}
			statement.setString(1, compName);
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				/**
				 * check if the rs.getString(1)is equals to the correct
				 * password.
				 */
				if (Rs.getString(1).equals(password)) {
					System.out.println("Company " + compName + " Login Info Is Valid. Welecome!");
					return true;
				} else {
					System.out.println("Company " + compName + "Entered Password Is Incorrect. Please Try Again.");
					return false;
				}
			}
			System.out.println("The Entered Password Is Incorrect. Please Try Again.");
			return false;
		} catch (SQLException e) {
			throw new DAOException("There was an Error during connection");
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public boolean isCompanyExistByCompanyName(String compName) throws DAOException {

		String sql = "SELECT * FROM Company WHERE COMP_NAME=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, compName);
			ResultSet Rs = statement.executeQuery();
			// If there's something in the resultSet (I have at least 1 answer)
			if (Rs.next()) {
				System.out.println("Company " + compName + " Already Exsit In The DB!");
				return true;
			} else {
				System.out.println("Company " + compName + " Isn't Exsit In The DB!");
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Failed To Check if Company exists due to :" + e.getMessage());
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
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, coupon.getTitle());
			ResultSet Rs = statement.executeQuery();
			if (Rs.next()) {
				System.out.println("Coupon Title " + coupon.getTitle() + " Already Exist In DB!");
				Rs.close();
				return true;
			} else {
				System.out.println("Company With This Coupon Title " + coupon.getTitle() + " Not Exist!");
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Failed To Check If Company Exist By ID: " + e.getMessage());
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Collection<Coupon> getCouponsByType(CouponType CouponType) throws DAOException {

		String sql = "SELECT * FROM Coupon WHERE TYPE = ?";
		/**
		 * create a collection of coupons (coupons) which eventually will be
		 * returned
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
			statement.setString(1, CouponType.toString());
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				Coupon coupon = ProjectUtils.extractCouponFromResultSet(Rs);
				// add the coupon to the collection
				coupons.add(coupon);
				Rs.close();
			}
			return coupons;
		} catch (SQLException e) {
			throw new DAOException("Failed To Fetch This Coupon Type: " + e.getMessage());
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	@Override
	public Company getCompanyByName(String compName) throws DAOException {
		// create a new copany object
		Company company = new Company();
		// check if the Company exist in the DB
		if (!isCompanyExistByCompanyName(compName)) {
			System.out.println("This Company Name " + compName + " Isn't Exist In The DB");
			return company;
		}
		String sql = "SELECT * FROM Company WHERE COMP_NAME = ?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, compName);
			ResultSet Rs = statement.executeQuery();
			while (Rs.next()) {
				company.setId(Rs.getLong(1));
				company.setCompName(compName);
				company.setPassword(Rs.getString(3));
				company.setEmail(Rs.getString(4));
				Rs.close();
				System.out.println("Fetching Company Name - " + compName + " In The DB");
				return company;
			}
			Rs.close();
		} catch (SQLException e) {
			throw new DAOException("Get Company By Name : " + company.getCompName() + " Failed", e);
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
		return company;
	}

	@Override
	public boolean isCompanyExistByCompanyId(long id) throws DAOException {

		String sql = "SELECT * FROM Company WHERE ID=?";
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
			// If there's something in the resultSet (I have at least 1 answer)
			if (Rs.next()) {
				Rs.close();
				return true;
			} else {
				System.out.println("Company ID: " + id + " Don't Exist in the DB!");
				return false;
			}
		} catch (SQLException e) {
			throw new DAOException("Failed Checking If Company Exist By ID :" + e.getMessage());
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

	public boolean isCompanyAlreadyCreatedThisCoupon(Company company, Coupon coupon) throws DAOException {

		String sql = "SELECT * FROM Company_Coupon WHERE Company_ID=? AND Coupon_ID=?";
		/**
		 * get a connection from connection pool
		 */
		Connection connection = pool.getConnection();
		// Creating a statement object which holds the SQL we're about to
		// execute
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, company.getId());
			statement.setLong(2, coupon.getId());
			ResultSet Rs = statement.executeQuery();
			if (Rs.next()) {
				System.out.println("Company Already Created This Coupon");
				return true;
			} else {
				System.out.println("Coupon Is Available For Adding");
				return false;
			}

		} catch (SQLException e) {
			throw new DAOException("Connection Error");
		} finally {
			/**
			 * return the connection to the pool
			 */
			pool.returnConnection(connection);
		}
	}

}
