package Facade;

import java.util.Collection;

import DBDAO.CompanyDBDAO;
import DBDAO.CustomerDBDAO;
import DBDAO.JoinedTableDBDAO;
import DataTypes.Company;
import DataTypes.Customer;
import Exceptions.DAOException;
import Exceptions.FacadeException;

/**
 * A AdminFacade class which contains all the methods that can be executed by
 * the admin user. Any instance represents a single admin, that can execute the
 * methods by this object, whose given by the login static method.
 */
public class AdminFacade extends CouponClientFacade {
	/**
	 * A reference to the CompanyDBDAO instance.
	 */
	private CompanyDBDAO companyDBDAO = CompanyDBDAO.getInstance();
	/**
	 * A reference to the CouponDBDAO instance.
	 */
	private CustomerDBDAO customerDBDAO = CustomerDBDAO.getInstance();
	/**
	 * A reference to the JoinedTablesDBDAO instance.
	 */
	private JoinedTableDBDAO joinedTableDBDAO = JoinedTableDBDAO.getInstance();

	/**
	 * Private CTOR
	 */
	public AdminFacade() {
		// username - admin
		// password - 1234
	}

	/**
	 * creata an instance
	 */
	private static AdminFacade instance = new AdminFacade();

	/**
	 * get the instance of the AdminFacade
	 * 
	 * @return instance
	 */
	public static AdminFacade getInstance() {
		if (instance == null) {
			instance = new AdminFacade();
		}
		return instance;
	}

	/**
	 * createCompany method is creating a company in the DB by administrator
	 * (checking first if the company is already exist by company name)
	 * 
	 * @param company
	 *            The Company object (only name meter)
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void createCompany(Company company) throws FacadeException {
		try {
			if (companyDBDAO.isCompanyExistByCompanyName(company.getCompName()) == true) {
				System.out.println("Company Name Already Exist In The DB. Please Enter Different Company Name");
			} else {
				companyDBDAO.createCompany(company);
			}
		} catch (DAOException e) {
			throw new FacadeException("Create Company " + company.getCompName() + "  Failed", e);
		}
	}

	/**
	 * deleteCompany method is deleting a company in the DB by administrator
	 * Also delete all the coupons of the specific company from the coupon
	 * table, the CompanyCoupon join table and from the CustomerCoupon join
	 * table
	 * 
	 * @param company
	 *            The Company object
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void deleteCompany(Company company) throws FacadeException {
		try {
			if (!(companyDBDAO.isCompanyExistByCompanyId(company.getId()))) {
				System.out.println("This Company Doesn't Exist In The DB");
				return;
			}
			// delete all company coupons in the DB
			joinedTableDBDAO.deleteAllCompanyCouponsInDB(company);
			// delete the specific company
			companyDBDAO.deleteCompany(company);
		} catch (DAOException e) {
			throw new FacadeException("Delete Company Failed", e);
		}
	}

	/**
	 * updateCompany method is updating a company in the DB by administrator (do
	 * not update the company name as øequired in this specific project)
	 * 
	 * @param company
	 *            The Company object
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void updateCompany(Company company) throws FacadeException {
		try {
			companyDBDAO.updateCompany(company);
		} catch (DAOException e) {
			throw new FacadeException("Update Company Failed" + e.getMessage());

		}
	}

	/**
	 * getCompany method is getting all the company detail from the DB by
	 * administrator.
	 * 
	 * @param id
	 *            The specific Company with the given id
	 * @return Company
	 * @throws FacadeException
	 *             if connection failed
	 */
	public Company getCompany(long id) throws FacadeException {
		Company company = null;
		try {
			if (!companyDBDAO.isCompanyExistByCompanyId(id)) {
				System.out.println("This Company Doesn't Exist In The DB");
			} else {
				company = companyDBDAO.getCompany(id);
				System.out.println("Demended Company: " + company.toString());
			}
			return company;
		} catch (DAOException e) {
			throw new FacadeException("Get Company Is Failed!", e);
		}
	}

	/**
	 * getAllCompany method is getting a collections of all companies from the
	 * DB by administrator.
	 * 
	 * @return Collection of all companies
	 * @throws FacadeException
	 *             if connection failed
	 */
	public Collection<Company> getAllCompanies() throws FacadeException {
		try {
			return companyDBDAO.getAllCompanies();
		} catch (DAOException e) {
			throw new FacadeException("Getting Companies Failed", e);
		}
	}

	/**
	 * createCustomer method is creating a customer in the DB by administrator
	 * checking if the customer exist by name
	 * 
	 * @param customer
	 *            The customer object (only custName meter)
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void createCustomer(Customer customer) throws FacadeException {
		try {
			if (customerDBDAO.isCustomerExistByName(customer.getCustName())) {
				System.out.println("Customer Is Already Exist");
				return;
			} else {
				// Creating the Customer
				customerDBDAO.createCustomer(customer);
			}
		} catch (DAOException e) {
			throw new FacadeException("Creating Customer Failed!", e);
		}
	}

	/**
	 * deleteCustomer method is deleting a customer from the DB by
	 * administrator. checking if the customer exist by id. also deletes all the
	 * purchased coupons of the specific customer from the customse_Coupon join
	 * table
	 * 
	 * @param customer
	 *            The customer object (only custName meter)
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void deleteCustomer(Customer customer) throws FacadeException {
		try {
			if (!customerDBDAO.isCustomerExistById(customer.getId())) {
				System.out.println("This Customer isn't Exist In DB.");
				return;
			}
			// delete the customer and all of his purchase coupons from the
			// Custoner_coupon table
			joinedTableDBDAO.deleteCustomerPurchaseHistory(customer);
			// delete the customer from the DB
			customerDBDAO.deleteCustomer(customer);
			System.out.println("Customer " + customer.getCustName() + "Is Deleted From The DB");
		} catch (DAOException e) {
			throw new FacadeException("Delete Customer Failed!", e);
		}
	}

	/**
	 * updetCustomer method is updating a customer in the DB by administrator.
	 * do not update the customer name as requierd in this specific project
	 * 
	 * @param customer
	 *            The customer object (only custName meter)
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void updetCustomer(Customer customer) throws FacadeException {
		try {
			customerDBDAO.updateCustomer(customer);
		} catch (DAOException e) {
			throw new FacadeException("Update Customer Failed!", e);
		}
	}

	/**
	 * getCustomer method is getting all the customer detail from the DB by
	 * administrator. checking if the customer exist by Name.do not update the
	 * customer name
	 * 
	 * @param id
	 *            The specific customer id
	 * @return The Specific Customer
	 * @throws FacadeException
	 *             if connection failed
	 */
	public Customer getCustomer(long id) throws FacadeException {

		try {
			return customerDBDAO.getCustomer(id);
		} catch (DAOException e) {
			throw new FacadeException("Get Customer Failed", e);
		}
	}

	/**
	 * getAllCustomer method is getting a collections of customers from the DB
	 * by administrator.
	 * 
	 * @return Collection of all customers from the DB
	 * @throws FacadeException
	 *             if connection failed
	 */
	public Collection<Customer> getAllCustomers() throws FacadeException {
		try {
			return customerDBDAO.getAllCustomers();
		} catch (DAOException e) {
			throw new FacadeException("Get all Customers Failed", e);
		}
	}

}
