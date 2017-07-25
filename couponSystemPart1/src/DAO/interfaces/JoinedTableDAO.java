package DAO.interfaces;

import java.sql.Date;
import java.util.Collection;

import DataTypes.Company;
import DataTypes.Coupon;
import DataTypes.CouponType;
import DataTypes.Customer;
import Exceptions.DAOException;

public interface JoinedTableDAO {
	/**
	 * <strong>deleteCoupon method is deleting a coupon</strong><br>
	 * <blockquote>* delete the coupon from Company_coupon and Customer_Coupon
	 * tables</blockquote>
	 * 
	 * @param coupon
	 *            The coupon to remove.
	 * @throws DAOException
	 *             if connection failed
	 */
	public void deleteCoupon(Coupon coupon) throws Exceptions.DAOException;

	/**
	 * <strong>deleteCouponFromCustomer method is deleting a specific customer
	 * and specific coupon from the DB</strong><br>
	 * <blockquote>* delete a row in the join table Customer_Coupon</blockquote>
	 * 
	 * @param customer
	 *            The customer with the ID
	 * @param coupon
	 *            The coupon with the ID
	 * @throws DAOException
	 *             if connection failed
	 */
	public void deleteCouponFromCustomer(Customer customer, Coupon coupon) throws DAOException;

	/**
	 * <strong>customerPurchaseCoupon add new row in Customer_Coupon
	 * table</strong><br>
	 * <blockquote>* create coupon in the Customer_Coupon table</blockquote>
	 * <blockquote>* Updates the amount (-1) in the Coupon table </blockquote>
	 * 
	 * @param customer
	 *            The customer (only customer id matter)
	 * @param coupon
	 *            The coupon (only customer id matter)
	 * @throws DAOException
	 *             if connection failed
	 */
	public void customerPurchaseCoupon(Customer customer, Coupon coupon) throws DAOException;

	/**
	 * <strong>createCompanyCoupon add new row for Company_Coupon table</strong>
	 * <br>
	 * <blockquote>* create coupons in the Company_Coupon table</blockquote>
	 * 
	 * @param company
	 *            The company for the row.
	 * @param coupon
	 *            The coupon for the row.
	 * @throws DAOException
	 *             if connection failed
	 */
	public void createCompanyCoupon(Company company, Coupon coupon) throws DAOException;

	/**
	 * <strong>getAllPurchasedCouponByCustomerId is getting all purchase coupons
	 * of a specific customer</strong><br>
	 * 
	 * @return A collection of purchase coupon of the specific customer
	 * 
	 * @param Custid
	 *            The customer id.
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getAllPurchasedCouponByCustomerId(long Custid) throws DAOException;

	/**
	 * <strong>getAddedCouponsByCompany method is getting a coupons that added
	 * by a specific company by the company</strong><br>
	 * 
	 * @param company
	 *            the Specific company (only ID matter)
	 * @return A collection of coupons the Added By a specific Companyor
	 * @throws DAOException
	 *             if connection failed
	 */
	Collection<Coupon> getAddedCouponsByCompany(Company company) throws DAOException;

	/**
	 * <strong>this method is getting all the coupons that purchased by a
	 * specific Customer by a limit max price</strong><br>
	 * 
	 * @param customer
	 *            the Specific Customer (only ID matter)
	 * @param maxPrice
	 *            the specific maxPrice
	 * @return A collection of coupons the below or equals to a limited maximum
	 *         price
	 * @throws DAOException
	 *             if connection failed
	 */
	Collection<Coupon> getAllPurchasedCouponOfCustomerByMaxPrice(Customer customer, double maxPrice)
			throws DAOException;

	/**
	 * <strong>getAllCouponsOfCompanyByMaxPrice method is getting all specific
	 * company's coupons from DB limited by a maximum price (maxPrice)</strong>
	 * <br>
	 * <blockquote>* get coupons from the Company_Coupon table</blockquote>
	 * 
	 * @param company
	 *            the company that own the coupon
	 * @param maxPrice
	 *            the max price limit of the coupon
	 * @return A collection of All coupons below and equal to the given max
	 *         price.
	 * @throws DAOException
	 *             if connection failed
	 */
	Collection<Coupon> getAllCouponsOfCompanyByMaxPrice(Company company, double maxPrice) throws DAOException;

	/**
	 * <strong>getAllCouponsOfCompanyByMaxEndDate method is getting all coupons
	 * from DB limited by a specific coupon End date</strong><br>
	 * <blockquote>* get date as java.util.Date and convert it to
	 * sql</blockquote><br>
	 * <blockquote>* get coupons from the Company_Coupon table</blockquote>
	 * 
	 * @param company
	 *            the company that own the coupon
	 * @param maxEndDate
	 *            the coupon end Date limit
	 * @return A collection of all coupons below and equal a specific EndDate
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getAllCouponsOfCompanyByMaxEndDate(Company company, Date maxEndDate) throws DAOException;

	/**
	 * <strong>getAllCouponsOfCompanyByMaxEndDate method is getting all coupons
	 * from DB limited by a a specific coupon End date </strong><br>
	 * <blockquote>* get date as string and convert it to sql</blockquote>
	 * 
	 * @param company
	 *            the company that own the coupon
	 * @param maxEndDate
	 *            the coupon max end Date limit
	 * @return A {@link Collection} of all coupons from database matching a
	 *         specific EndDate.
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getAllCouponsOfCompanyByMaxEndDate(Company company, String maxEndDate)
			throws DAOException;

	/**
	 * <strong>getCouponByTypeOfSpecificCompany method is getting a specific
	 * coupon by the coupon type, belongs to a specific company</strong><br>
	 * <blockquote>* getting the coupons from the Company_Coupon
	 * table</blockquote>
	 * 
	 * @param company
	 *            The specific company
	 * @param type
	 *            The type of coupon to search for
	 * @return A Collection of coupons
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getCouponByTypeOfSpecificCompany(Company company, CouponType type) throws DAOException;

	/**
	 * <strong>deleteAllCompanyCouponsInDB method is deleting all the coupons of
	 * a specific Company from the DB</strong><br>
	 * <blockquote>* delete the coupons from the Coupon table</blockquote>
	 * <blockquote>* delete the coupons from the Company_Coupon
	 * table</blockquote> <blockquote>* delete the coupons from the
	 * Customer_Coupon table</blockquote>
	 * 
	 * @param company
	 *            The specific Company
	 * @throws DAOException
	 *             if connection failed
	 */
	void deleteAllCompanyCouponsInDB(Company company) throws DAOException;

	/**
	 * <strong>deleteCustomerPurchaseHistory method is deleting all the coupons
	 * that purchased by a specific customer </strong><br>
	 * <blockquote>* delete the coupons from the Customer_Coupon
	 * table</blockquote>
	 * 
	 * @param customer
	 *            The specific customer
	 * @throws DAOException
	 *             if connection failed
	 */
	void deleteCustomerPurchaseHistory(Customer customer) throws DAOException;

}
