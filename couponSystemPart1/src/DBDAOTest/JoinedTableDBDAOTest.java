package DBDAOTest;

import java.util.Iterator;

import DBDAO.CompanyDBDAO;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerDBDAO;
import DBDAO.JoinedTableDBDAO;
import DataTypes.Company;
import DataTypes.Coupon;
import DataTypes.CouponType;
import DataTypes.Customer;
import Exceptions.DAOException;

/**
 * A class of testing for all company methods, and their operation on the data
 * base by calling all the methods. The test JoinedTableDBDAOTest was created
 * for the programmer to examine all methods and all program capabilities<br>
 * The companyDAOTest test reset and create all the data base by calling the
 * relevant methods @see C_Statements<br>
 * also, the testing class create new object's like : company, customer and
 * coupon's in the Date base.
 */
public class JoinedTableDBDAOTest {

	public static void main(String[] args) {
		// ************************************************************
		// A_LoadDataBase.getInstance().loadDataBase();
		// B_ConnectToDataBase.getInstance().connectToDateBase();
		// C_Statements.getInstance().resetTables();
		// ************************************************************
		// Creating An Object Of Coupon Class (Worked)
		Coupon coupon1 = new Coupon(123, "t001", "17/2/2017", "19/3/2012", 12, CouponType.CAMPING, "TEA", 1, "1.img");
		Coupon coupon2 = new Coupon(132, "t002", "10/2/2017", "20/4/2015", 43, CouponType.ELECTRICITY, "TV", 2,
				"2.img");
		Coupon coupon3 = new Coupon(25, "t01", "16/1/2017", "19/4/2019", 54, CouponType.FOOD, "TEL", 3, "3.img");
		// ************************************************************
		// Creating An Object Of CouponDBDAO Class (Worked)
		CouponDBDAO couponDBDAO = new CouponDBDAO();
		// ************************************************************
		// Applying The createCoupon Method (Worked)
		try {
			couponDBDAO.createCoupon(coupon1);
			couponDBDAO.createCoupon(coupon2);
			couponDBDAO.createCoupon(coupon3);
			// ************************************************************
			// Creating An Object Of Customer Class (Worked)
			Customer customer1 = new Customer(1, "Test1", "PassTest1");
			Customer customer2 = new Customer(2, "Test2", "PassTest2");
			Customer customer3 = new Customer(3, "Test3", "PassTest3");
			// ************************************************************
			// Creating An Object Of CompanyDBDAO Class (Worked)
			CustomerDBDAO customerDBDAO = new CustomerDBDAO();
			// ************************************************************
			// Applying The createCompany Method (Worked)
			customerDBDAO.createCustomer(customer1);
			customerDBDAO.createCustomer(customer2);
			customerDBDAO.createCustomer(customer3);
			// ************************************************************
			// Creating An Object Of Company Class (Worked)
			Company company1 = new Company(9, "Test9", "passrd1", "test1@test.com");
			Company company2 = new Company(98, "Test98", "passrd2", "test2@test.com");
			Company company3 = new Company(987, "Test987", "passrd3", "test3@test.com");
			// ************************************************************
			// Creating An Object Of CompanyDBDAO Class (Worked)
			CompanyDBDAO companyDBDAO = new CompanyDBDAO();
			// ************************************************************
			// Applying The createCompany Method
			companyDBDAO.createCompany(company1);
			companyDBDAO.createCompany(company2);
			companyDBDAO.createCompany(company3);
			// ************************************************************
			// Creating An Object Of JoinedTableDBDAO Class (Worked)
			JoinedTableDBDAO joinedTableDBDAO = new JoinedTableDBDAO();
			// ************************************************************
			// Adds New Coupon To A Specific Company (Worked)
			joinedTableDBDAO.createCompanyCoupon(company1, coupon1);
			joinedTableDBDAO.createCompanyCoupon(company2, coupon2);
			joinedTableDBDAO.createCompanyCoupon(company1, coupon3);
			joinedTableDBDAO.createCompanyCoupon(company2, coupon2);
			joinedTableDBDAO.createCompanyCoupon(company3, coupon3);
			// ************************************************************
			// Get A Collection Of All Coupons Of A Specific Company By Max Date
			// ,And Print Them To The Console (Worked)
			Iterator<Coupon> maxDateIt = joinedTableDBDAO.getAllCouponsOfCompanyByMaxEndDate(company1, "17/4/2017")
					.iterator();
			while (maxDateIt.hasNext()) {
				System.out.println(maxDateIt.next().toString());
			}
			// ************************************************************
			// Specific Customer Purchases A Specific Coupon (Worked)
			joinedTableDBDAO.customerPurchaseCoupon(customer1, coupon1);
			joinedTableDBDAO.customerPurchaseCoupon(customer1, coupon2);
			joinedTableDBDAO.customerPurchaseCoupon(customer1, coupon3);
			joinedTableDBDAO.customerPurchaseCoupon(customer3, coupon3);
			// ************************************************************
			// Get All The Purchased Coupons Out Of The DB By Specific Max Price
			// Of
			// Specific Customer And Prints Them In The Console (Worked)
			customerDBDAO.createCustomer(new Customer(6, "cust6", "1234"));
			Iterator<Coupon> coupIt = joinedTableDBDAO
					.getAllPurchasedCouponOfCustomerByMaxPrice(new Customer(1, "Test1", "PassTest1"), 1).iterator();
			while (coupIt.hasNext()) {
				System.out.println(coupIt.next().toString());
			}
			// ************************************************************
			// Get All The Purchased Coupons Out Of The DB By Specific Max Price
			// Of
			// Specific Company And Prints Them In The Console (Worked)
			companyDBDAO.createCompany(new Company(1, "Test1", "PassTest1", "EmailTest1"));
			Iterator<Coupon> coupIter = joinedTableDBDAO
					.getAllCouponsOfCompanyByMaxPrice(new Company(1, "t001", "PassTest1", "EmailTest1"), 3).iterator();
			while (coupIter.hasNext()) {
				System.out.println(coupIter.next().toString());
			}
			// ************************************************************
			// Deletes Specific Coupon From All The Tables In The DB (Worked)
			joinedTableDBDAO.deleteCoupon(coupon3);
			// ************************************************************
			// Deletes Specific Coupon From Customer_Coupon Table In The DB
			// (Worked)
			joinedTableDBDAO.deleteCouponFromCustomer(customer1, coupon1);
			// ************************************************************
			// Deletes All Purchase Coupons By Specific Customer In The DB
			// ID (Worked)
			joinedTableDBDAO.deleteCustomerPurchaseHistory(customerDBDAO.getCustomer(1));
			// ************************************************************
			// Deletes All Purchase Coupons By Specific Customer In The DB
			// ID (Worked)
			joinedTableDBDAO.deleteCustomerPurchaseHistory(customer1);
			// ************************************************************
			// Gets All Purchased Coupon From The DB By Specific Customer ID
			// (Worked)
			Iterator<Coupon> it = joinedTableDBDAO.getAllPurchasedCouponByCustomerId(1).iterator();
			while (it.hasNext()) {
				System.out.println(it.next().toString());
			}
			// //
			// ************************************************************
			// Deleting All Specific Company Coupons (Worked)
			joinedTableDBDAO.deleteAllCompanyCouponsInDB(company1);
			// ************************************************************

		} catch (

		DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
