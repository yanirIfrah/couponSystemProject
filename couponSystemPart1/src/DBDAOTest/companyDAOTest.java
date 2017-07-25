package DBDAOTest;

import java.util.Iterator;

import CouponDataBase.A_LoadDataBase;
import CouponDataBase.B_ConnectToDataBase;
import CouponDataBase.C_Statements;
import DBDAO.CompanyDBDAO;
import DBDAO.JoinedTableDBDAO;
import DataTypes.Company;
import Exceptions.DAOException;

/**
 * A class of testing for all company methods, and their operation on the data
 * base by calling all the methods. The test companyDAOTest was created for the
 * programmer to examine all methods and all program capabilities<br>
 * The companyDAOTest test reset and create all the data base by calling the
 * relevant methods. @see C_Statements<br>
 * also, the testing class create new object's like : company, customer and
 * coupon's in the Date base.
 */
public class companyDAOTest {

	public static void main(String[] args) {
		// ************************************************************
		A_LoadDataBase.getInstance().loadDataBase();
		B_ConnectToDataBase.getInstance().connectToDateBase();
		C_Statements.getInstance().resetTables();
		// ************************************************************
		// Creating An Object Of Company Class (Worked)
		Company company1 = new Company(01, "Test111", "pass1", "test1@test.com");
		Company company2 = new Company(02, "Test112", "pass2", "test2@test.com");
		Company company3 = new Company(03, "Test113", "pass3", "test3@test.com");
		// ************************************************************
		// Creating An Object Of CompanyDBDAO Class **********(Worked)
		CompanyDBDAO companyDBDAO = new CompanyDBDAO();
		// ************************************************************
		// Applying The createCompany Method **********(Worked)
		try {
			companyDBDAO.createCompany(company1);
			companyDBDAO.createCompany(company2);
			companyDBDAO.createCompany(company3);
			// ************************************************************
			// Applying The updateCompany Method **********(Worked)
			company1.setPassword("123456");
			company1.setEmail("yanir@gmail.com");
			companyDBDAO.updateCompany(company1);
			// ************************************************************
			// Deleting Specific Company **********(Worked)
			companyDBDAO.deleteCompany(company1);
			companyDBDAO.deleteCompany(company2);
			companyDBDAO.deleteCompany(company3);
			// ************************************************************
			// Deleting All Specific Company Coupons **********(Worked)
			JoinedTableDBDAO.getInstance().deleteAllCompanyCouponsInDB(company1);
			// ************************************************************
			// Getting A Collection Of All The Companies In The DB and Print
			// Them In
			// The Console ****************(Worked)
			Iterator<Company> it = companyDBDAO.getAllCompanies().iterator();
			while (it.hasNext()) {
				System.out.println(it.next().toString());

				// ************************************************************
				// Get Company From The DB By the ID and Prints It To The
				// Console
				// **************(Worked)
				Company id = companyDBDAO.getCompany(6);
				System.out.println(id.toString());
				// ************************************************************
				// Get Company From The DB By Her Name and Prints It To The
				// Console
				// **************(Worked)
				Company name = companyDBDAO.getCompanyByName("Test12");
				System.out.println(name.toString());
				// ************************************************************
				// Checks If The Login Info Is Valid Or Not
				// *************(Worked)
				boolean log = companyDBDAO.login("Test12", "pass2");
				System.out.println(log);
				// ************************************************************
				// ************************************************************
				// Get A Collection Of All Specific Company Coupons (Worked)
				boolean group = companyDBDAO.getCoupons(company1).isEmpty();
				System.out.println(group);
				// ************************************************************
				// Deletes All The Companies Exists In The Database
				companyDBDAO.getAllCompanies();
			}
		} catch (

		DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
