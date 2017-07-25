package DAO.interfaces;

import java.util.Collection;

import DataTypes.Coupon;
import DataTypes.Customer;
import Exceptions.DAOException;

public interface CustomerDAO {
	/**
	 * <strong>createCustomer method is creating a new customer in the
	 * DB</strong><br>
	 * <blockquote>* create the customer in the Customer table</blockquote>
	 * 
	 * @param customer
	 *            The customer to add
	 * @throws DAOException
	 *             if connection failed
	 */
	public void createCustomer(Customer customer) throws DAOException;

	/**
	 * <strong>deleteCustomer method is deleting a customer from the
	 * DB </strong><br>
	 * <blockquote>* remove the customer from the Customer table</blockquote>
	 * 
	 * @param customer
	 *            The customer to delete
	 * @throws DAOException
	 *             if connection failed
	 */
	public void deleteCustomer(Customer customer) throws DAOException;

	/**
	 * <strong>updateCustomer method is updating the customer in the
	 * DB </strong> (only password required)<br>
	 * 
	 * <blockquote>* update the Customer in the customer table</blockquote>
	 * <blockquote>* update only the Password of the customer as demended in
	 * this specific project</blockquote>
	 * 
	 * @param customer
	 *            The customer to update.
	 * @throws DAOException
	 *             if connection failed
	 */
	public void updateCustomer(Customer customer) throws DAOException;

	/**
	 * <strong>getCustomer method is getting a specific customer from the
	 * DB</strong><br>
	 * <blockquote>* getting the Customer from the customer table</blockquote>
	 * 
	 * @param id
	 *            The customer id to search for his details
	 * @return Customer the customer detailes
	 * @throws DAOException
	 *             if connection failed
	 */
	public Customer getCustomer(long id) throws DAOException;

	/**
	 * <strong>getAllCustomers method is getting all customers from the Customer
	 * table in the DB</strong><br>
	 * <blockquote>* getting the collection from the Customer table</blockquote>
	 * 
	 * @return A collection of all customers
	 * 
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Customer> getAllCustomers() throws DAOException;

	/**
	 * <strong>getCoupons method is getting of all coupons of a specific
	 * customer from the DB</strong><br>
	 * <blockquote>* getting the collection from the Customer_Coupon
	 * table</blockquote>
	 * 
	 * @param customer
	 *            the specific customer to search for his coupons
	 * 
	 * @return A collection of all coupons by a specific customer
	 * 
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getPurchasedCouponsByCustomer(Customer customer) throws DAOException;

	/**
	 * <strong>login method is Verifies a customer user credentials</strong>
	 * 
	 * @param custName
	 *            The customer name.
	 * @param password
	 *            The customer password.
	 * 
	 * @return True - if verification resulted true. <br>
	 *         False - if verification resulted false.
	 * 
	 * @throws DAOException
	 *             if connection failed
	 */
	public Boolean login(String custName, String password) throws DAOException;

	/**
	 * <strong>isCustomerExsistByName method is checking if the customer exist
	 * by given name</strong>
	 * 
	 * @param customer
	 *            the specific customer (only name matter)
	 * 
	 * @return True - if customer name exist. <br>
	 *         False - if customer name isn't exist.
	 * 
	 * @throws DAOException
	 *             if connection failed
	 */
	public boolean isCustomerExistByName(String customer) throws DAOException;

	/**
	 * <strong>isCustomerExistById method is checking if the customer exist by
	 * given id</strong>
	 * 
	 * @param id
	 *            The accepted customer id
	 * 
	 * @return True - if customer exist. <br>
	 *         False - if customer isn't exist.
	 * 
	 * @throws DAOException
	 *             if connection failed
	 */
	public boolean isCustomerExistById(long id) throws DAOException;

	/**
	 * <strong>getCustomerByName method is getting a specific customer by a
	 * given name from the DB</strong><br>
	 * <blockquote>* getting the details from the Customer table</blockquote>
	 * 
	 * @param custName
	 *            The accepted customer name
	 * @return Customer the customer details
	 * @throws DAOException
	 *             if connection failed
	 */
	Customer getCustomerByName(String custName) throws DAOException;

	/**
	 * <strong>isCustomerBoughtThisCoupon method is checking if a specific
	 * customer purchased a specific coupon</strong> <br>
	 * 
	 * @param customer
	 *            The specific customer
	 * @param coupon
	 *            The coupon to search for
	 * @return True - if the customer bought the specific coupon<br>
	 *         Fales - if the customer isn't bought the specific coupon
	 * @throws DAOException
	 *             if connection failed
	 */
	boolean isCustomerBoughtThisCoupon(Customer customer, Coupon coupon) throws DAOException;

}
