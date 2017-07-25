package DAO.interfaces;

import java.util.Collection;

import CouponSystem.DailyCouponExpirationTask;
import DataTypes.Coupon;
import DataTypes.CouponType;
import DataTypes.Customer;
import Exceptions.DAOException;

/**
 * Interface that allows the application to preform all essential methods that
 * involve communication with the DB Coupon table.
 */
public interface CouponDAO {
	/**
	 * <strong>createCoupon method is creating a new coupon in the Coupon table
	 * in the DB.</strong>
	 * 
	 * @param coupon
	 *            The coupon to add.
	 * @throws DAOException
	 *             if connection failed
	 */
	public void createCoupon(Coupon coupon) throws DAOException;

	/**
	 * <strong>deleteCoupon method is deleting a coupon from coupon table from
	 * the DB.</strong> <br>
	 * <blockquote>check if the coupon with the given id exists.</blockquote>
	 * 
	 * @param coupon
	 *            The coupon to delete(only id meter).
	 * @throws DAOException
	 *             if connection failed
	 */
	public void deleteCoupon(Coupon coupon) throws DAOException;

	/**
	 * <strong>updateCoupon method is updating attribute in specific coupon in
	 * coupon table in the DB.</strong> <br>
	 * <blockquote>* update only the End Date and the price of the coupon as
	 * demended in this specific project</blockquote>
	 * 
	 * @param coupon
	 *            The coupon to update
	 * @throws DAOException
	 *             if connection failed
	 */
	public void updateCoupon(Coupon coupon) throws DAOException;

	/**
	 * <strong>updateCoupon method is updating attribute in specific coupon in
	 * coupon table in the DB.</strong> <br>
	 * <blockquote>* update only the amount of the coupon as demended in this
	 * specific project</blockquote>
	 * 
	 * @param coupon
	 *            The coupon to update
	 * @throws DAOException
	 *             if connection failed
	 */
	public void updateCouponAmount(Coupon coupon) throws DAOException;

	/**
	 * <strong>getCoupon method is getting a coupon with the given id from
	 * DB.</strong>
	 * 
	 * @param id
	 *            The requested coupon's ID.
	 * @return The coupon from database with the given ID.
	 * @throws DAOException
	 *             if connection failed
	 */
	public Coupon getCoupon(long id) throws DAOException;

	/**
	 * <strong>getAllCoupon method is getting all coupons from Coupon table in
	 * DB.</strong>
	 * 
	 * @return A collection of all coupons from DB.
	 * 
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getAllCoupon() throws DAOException;

	/**
	 * <strong>getCouponByType method is getting all coupons matching a specific
	 * coupon type from DB.</strong>
	 * 
	 * @param type
	 *            The coupon type
	 * 
	 * @return A collection of All coupons with a specific Coupon Type
	 * @throws DAOException
	 *             if connection failed
	 * @see CouponType
	 */
	public Collection<Coupon> getCouponByType(CouponType type) throws DAOException;

	/**
	 * <strong>isCouponAvailableForPurchase method is check for the amount of a
	 * specific coupon.</strong>
	 * 
	 * @return True - if the amount is in stock and availble to purchase<br>
	 *         False - if the amount is not in stock
	 * @param coupon
	 *            the coupon attribute (only coupon id and amount mater)
	 * @throws DAOException
	 *             if connection failed
	 */

	public boolean isCouponAvailableForPurchase(Coupon coupon) throws DAOException;

	/**
	 * <strong>deleteExpiredCoupons method is deleting all expired coupons from
	 * the DB.</strong><br>
	 * <blockquote>uses in the Daily Coupon Thread
	 * {@link DailyCouponExpirationTask}</blockquote>
	 * 
	 * @throws DAOException
	 *             if connection failed
	 */
	public void deleteExpiredCoupons() throws DAOException;

	/**
	 * <strong>getAllPurchasedCouponByType method is getting all coupons from DB
	 * that have been purchased, by a specific type of coupon of a specific
	 * customer</strong>
	 * 
	 * @param customerId
	 *            the specific customer id
	 * @param couponType
	 *            the specific type of coupon
	 * @return A collection of all coupon by the type of the coupon, purchased
	 *         by a specific customer
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getAllPurchasedCouponByType(long customerId, CouponType couponType) throws DAOException;

	/**
	 * <strong>getAllPurchasedCouponBtPrice method is getting all coupons from
	 * DB that have been purchased, by a specific price of coupon of a specific
	 * customer.</strong>
	 * 
	 * @param customer
	 *            the customer that purchased the coupon
	 * @param price
	 *            the price of the coupon
	 * @return A collection of all purcheased coupon by price
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getAllPurchasedCouponByPrice(Customer customer, double price) throws DAOException;

	/**
	 * <strong>isCustomerExsistById method is checking if the Coupon is exist in
	 * the DB by the coupon ID</strong>
	 * 
	 * @param id
	 *            The coupon id
	 * @return True if the coupon exist<br>
	 *         False- if the coupon Dos't exist .
	 * @throws DAOException
	 *             if connection failed
	 */
	boolean isCouponExistById(long id) throws DAOException;

	/**
	 * <strong>isCouponExistByTitle method is checking if the Coupon is exist in
	 * the DB by the coupon Tytle</strong>
	 * 
	 * @param coupon
	 *            The coupon (only Tytle meter)
	 * @return True if the coupon exist<br>
	 *         False- if the coupon Dos't exist
	 * @throws DAOException
	 *             if connection failed
	 */
	boolean isCouponExistByTitle(Coupon coupon) throws DAOException;

	/**
	 * getPurchasedCouponsByCustAndMaxPrice method is getting all coupons
	 * matching a specific coupon type of a specific customer from DB.<br>
	 * 
	 * @param customer
	 *            The specific customer.
	 * @param maxPrice
	 *            The coupon maxPrice.
	 * 
	 * @return a collection of All coupons with a specific Coupon Type .
	 * @throws DAOException
	 *             if connection failed
	 * @see CouponType
	 */

	Collection<Coupon> getPurchasedCouponsByCustAndMaxPrice(Customer customer, double maxPrice) throws DAOException;

	/**
	 * <strong>isCouponExistByCouponType method is checking if the Coupon is
	 * exist in the DB by the coupon type</strong>
	 * 
	 * @param couponType
	 *            The type of the coupon to srech for
	 * @return True if the coupon exist<br>
	 *         False- if the coupon Dos't exist
	 * @throws DAOException
	 *             if connection failed
	 */
	boolean isCouponExistByCouponType(CouponType couponType) throws DAOException;

	/**
	 * <strong>getPurchasedCouponByTypeAndCustId method is getting all the
	 * coupons by type, that purchased by a specific customer </strong>
	 * 
	 * @param customerId
	 *            The customer Id that purchased the coupons
	 * @param type
	 *            The type of the coupon to srech for
	 * @return True - if the coupon exist<br>
	 *         False - if the coupon Dos't exist
	 * @throws DAOException
	 *             if connection failed
	 */
	Collection<Coupon> getPurchasedCouponByTypeAndCustId(long customerId, CouponType type) throws DAOException;

	/**
	 * <strong>getCouponsByMaxEndDate method is getting all coupons that are
	 * below a certain date</strong>
	 * 
	 * @param maxEndDate
	 *            The maximum end date of the coupons to search for
	 * @return A collection of all the coupons that much the certain End date
	 * @throws DAOException
	 *             if connection failed
	 */
	Collection<Coupon> getCouponsByMaxEndDate(String maxEndDate) throws DAOException;

	/**
	 * <strong>getCouponsByMaxPrice method is getting all coupons that are below
	 * a certain max Price</strong>
	 * 
	 * @param maxPrice
	 *            The maximum price of the coupons to search for
	 * @return A collection of all the coupons that much the certain Accepted
	 *         Price
	 * @throws DAOException
	 *             if connection failed
	 */
	Collection<Coupon> getCouponsByMaxPrice(double maxPrice) throws DAOException;
}
