package Facade;

import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import DBDAO.CompanyDBDAO;
import DBDAO.CouponDBDAO;
import DBDAO.JoinedTableDBDAO;
import DataTypes.Company;
import DataTypes.Coupon;
import DataTypes.CouponType;
import Exceptions.DAOException;
import Exceptions.FacadeException;

/**
 * A CompanyFacade class which contains all the methods that can be executed by
 * the company user. Any instance represents a single company, that can execute
 * the methods by this object, whose given by the login static method.
 */
public class CompanyFacade extends CouponClientFacade {
	/**
	 * Company object containing the attributes of the company
	 */
	private Company company;
	/**
	 * A reference to the CompanyDBDAO instance.
	 */
	private CompanyDBDAO companyDBDAO = CompanyDBDAO.getInstance();
	/**
	 * A reference to the CouponDBDAO instance.
	 */
	private CouponDBDAO couponDBDAO = CouponDBDAO.getInstance();
	/**
	 * A reference to the JoinedTablesDBDAO instance.
	 */
	private JoinedTableDBDAO joinedTableDBDAO = JoinedTableDBDAO.getInstance();

	/**
	 * Private CTOR
	 * 
	 * @param company
	 *            this company
	 */
	public CompanyFacade(Company company) {
		this.company = company;

	}

	/**
	 * Get the company details of the company
	 * 
	 * @return The current company.
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * createCoupon method is creating a new coupon in the DB<br>
	 * the new added coupon will be storeg in the coupon table and the
	 * company_Coupon join table.(coupon title and id must be a unique values)
	 * 
	 * @param coupon
	 *            The new added Coupon object
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void createCoupon(Coupon coupon) throws FacadeException {

		try {
			if (companyDBDAO.isCouponExistByTitle(coupon) == true) {
				System.out.println("Coupon Is Aleady Exist");
				return;
			}
			// create a coupon in COupon table
			couponDBDAO.createCoupon(coupon);
			// create coupon in Company_Coupon table
			joinedTableDBDAO.createCompanyCoupon(this.company, coupon);
		} catch (DAOException e) {
			throw new FacadeException("Create Coupon Failed", e);
		}
	}

	/**
	 * deleteCoupon method is deleting a coupon from all the DB, owned by this
	 * company<br>
	 * delete the specific coupon from the coupon table, from all the customer
	 * that purchased the coupon (from the customer_Coupon join table) and from
	 * the join tables Company_coupon.
	 * 
	 * @param coupon
	 *            the specific coupon to delete
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void deleteCoupon(Coupon coupon) throws FacadeException {
		try {
			// delete coupon from the join table Company_coupon and
			// Customer_coupon
			joinedTableDBDAO.deleteCoupon(coupon);
			// delete coupon
			couponDBDAO.deleteCoupon(coupon);
		} catch (DAOException e) {
			throw new FacadeException("Delete Coupon Failed", e);
		}
	}

	/**
	 * updateCoupon method is updating a coupon attribute in the DB.<br>
	 * update only the price and end date of the coupon as required in this
	 * specific project.
	 * 
	 * @param coupon
	 *            the coupo to update
	 * 
	 * @throws FacadeException
	 *             if connection failed
	 */
	public void updateCoupon(Coupon coupon) throws FacadeException {
		try {
			// update coupon
			couponDBDAO.updateCoupon(coupon);
		} catch (DAOException e) {
			throw new FacadeException("Update Coupon Failed", e);
		}
	}

	/**
	 * currentCompany method is getting the company details of the company
	 * 
	 * @return The current company.
	 */

	public Company currentCompany() {
		System.out.println("Current Company Info : " + this.company.toString());
		return this.company;

	}

	/**
	 * getCoupon method is getting a specific coupon details from the DB owned
	 * by this specific company.<br>
	 * 
	 * @param id
	 *            The specific coupon id
	 * @return The specific coupon
	 * @throws FacadeException
	 *             if connection failed
	 */
	public Coupon getCoupon(long id) throws FacadeException {
		try {
			Coupon coupon = null;

			Iterator<Coupon> it = joinedTableDBDAO.getAddedCouponsByCompany(this.company).iterator();
			while (it.hasNext()) {
				Coupon coup = it.next();
				if (id == coup.getId()) {
					System.out.println(
							"Got Coupon " + id + " From The DB, owns by company :" + this.company.getCompName());
					coupon = coup;
					return coupon;
				}
			}
			System.out.println("There Is No " + id + " Coupon In " + this.company.getCompName() + " DB");
			return coupon;
		} catch (DAOException e) {
			throw new FacadeException("Get Coupon Was Failed", e);
		}
	}

	/**
	 * getAllCoupons method is getting All added coupons owned by the specific
	 * company.<br>
	 * 
	 * @return A collection of all coupons owned by the current company
	 * @throws FacadeException
	 *             if connection failed
	 */
	public Collection<Coupon> getAllCoupons() throws FacadeException {

		try {
			return joinedTableDBDAO.getAddedCouponsByCompany(this.company);
		} catch (DAOException e) {
			throw new FacadeException("Get All Coupon Is Failed", e);
		}
	}

	/**
	 * getCouponsByType method is getting a collection of all coupons owned by
	 * the specific company with a specific type of coupon.
	 * 
	 * @param type
	 *            The type of the specific coupon
	 * 
	 * @return A collection of all coupons owned by the current company, with a
	 *         specific type
	 * @throws FacadeException
	 *             if connection failed
	 */
	public Collection<Coupon> getCouponsByType(CouponType type) throws FacadeException {
		try {
			return joinedTableDBDAO.getCouponByTypeOfSpecificCompany(this.company, type);
		} catch (DAOException e) {
			throw new FacadeException("Get Coupons By Type Failed", e);
		}
	}

	public Collection<Coupon> getCouponsByTypeOfCompany(CouponType type) throws FacadeException {

		try {
			return joinedTableDBDAO.getCouponByTypeOfSpecificCompany(this.company, type);
		} catch (DAOException e) {
			throw new FacadeException("Failed to get Coupons By Type", e);
		}
	}

	/**
	 * getCouponsyMaxPrice method is getting a collection of all coupons owned
	 * by the specific company by a specific maxPrice of coupon. return an empty
	 * or full collection maxPrice
	 * 
	 * @param maxPrice
	 *            The maximum price of all the coupons.
	 * 
	 * @return A collection of coupons owned by current company, with price
	 *         which doesn't exceeds the given maximum price (maxPrice).
	 * @throws FacadeException
	 *             if connection failed.
	 */
	public Collection<Coupon> getCouponsByMaxPrice(double maxPrice) throws FacadeException {
		try {
			Collection<Coupon> CouponsByMaxPrice = joinedTableDBDAO.getAllCouponsOfCompanyByMaxPrice(company, maxPrice);
			if (CouponsByMaxPrice.isEmpty()) {
				System.out.println("There IS No Coupon By This Price In The DB");
			}
			System.out.println("Found Coupons Of Max Price-" + maxPrice + " In Your Company DB");
			return CouponsByMaxPrice;
		} catch (DAOException e) {
			throw new FacadeException("Got Coupons Failed", e);
		}
	}

	/**
	 * getCouponsByEndDate method is getting a collection of all coupons owned
	 * by the current company by a specific end Date of coupons. return an empty
	 * or full collection maxPrice
	 * 
	 * @param maxEndDate
	 *            The maximum maxEndDate of all the coupons.
	 * 
	 * @return A collection of coupons owned by the current company , with
	 *         expiration date which doesn't exceeds the given Date (EndDate).
	 * @throws FacadeException
	 *             if connection failed.
	 */
	public Collection<Coupon> getCouponsByEndDate(Date maxEndDate) throws FacadeException {
		try {
			Collection<Coupon> CouponsByEndDate = joinedTableDBDAO.getAllCouponsOfCompanyByMaxEndDate(this.company,
					maxEndDate);
			if (CouponsByEndDate.isEmpty()) {
				System.out.println("There IS No Coupon By This End Date In The DB");
			}
			return CouponsByEndDate;
		} catch (DAOException e) {
			throw new FacadeException(" Get Coupons By This Date Failed", e);
		}
	}

}
