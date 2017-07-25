package DBDAOTest;

import java.util.Iterator;

import DBDAO.CustomerDBDAO;
import DataTypes.Customer;
import Exceptions.DAOException;

/**
 * A class of testing for all company methods, and their operation on the data
 * base by calling all the methods. The test customerDBDAOTest was created for
 * the programmer to examine all methods and all program capabilities<br>
 * The companyDAOTest test reset and create all the data base by calling the
 * relevant methods @see C_Statements<br>
 * also, the testing class create new object's like : company, customer and
 * coupon's in the Date base.
 */
public class customerDBDAOTest {

	public static void main(String[] args) {
		// ************************************************************
		// A_LoadDataBase.getInstance().loadDataBase();
		// B_ConnectToDataBase.getInstance().connectToDateBase();
		// C_Statements.getInstance().resetTables();
		// ************************************************************
		// Creating An Object Of Customer Class (Worked)
		Customer customer1 = new Customer(1, "Test1", "Pass1");
		Customer customer2 = new Customer(2, "Test2", "Pass2");
		Customer customer3 = new Customer(3, "Test3", "Pass3");
		// ************************************************************
		// Creating An Object Of CompanyDBDAO Class (Worked)
		CustomerDBDAO customerDBDAO = new CustomerDBDAO();
		// ************************************************************
		// Applying The createCompany Method (Worked)
		try {
			customerDBDAO.createCustomer(customer1);
			customerDBDAO.createCustomer(customer2);
			customerDBDAO.createCustomer(customer3);
			// ************************************************************
			// Applying The updateCompany Method (Worked)
			customer1.setPassword("updated1");
			customerDBDAO.updateCustomer(customer1);
			// *************************************
			// Deleting Specific Company (Worked)
			customerDBDAO.deleteCustomer(customer2);
			// ************************************************************
			// Getting A Collection Of All The Customers In The DB and Print
			// Them In
			// The Console (Worked)
			Iterator<Customer> it = customerDBDAO.getAllCustomers().iterator();
			while (it.hasNext()) {
				System.out.println(it.next().toString());
			}

			// ************************************************************
			// Get Customer From The DB By Her ID and Prints It To The Console
			// (Worked)
			Customer id = customerDBDAO.getCustomer(1);
			System.out.println(id.toString());
			// ************************************************************
			// Get Customer From The DB By Her Name and Prints It To The Console
			// (Worked)
			Customer name = customerDBDAO.getCustomerByName("Test3");
			System.out.println(name.toString());
			// ************************************************************
			// Checks If The Login Info Is Valid Or Not (Worked)
			boolean log = customerDBDAO.login("Test3", "PassTest3");
			System.out.println(log);
			// ************************************************************
			// Get A Collection Of All Specific Customer Coupons (Worked)
			boolean group = customerDBDAO.getPurchasedCouponsByCustomer(customer3).isEmpty();
			System.out.println(group);
			// ************************************************************
			// Deleting All The Customers From The Database
			// ************************************************************
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
