package Facade;

import java.util.Collection;

import DBDAO.CouponDBDAO;
import DBDAO.CustomerDBDAO;
import DBDAO.JoinedTableDBDAO;
import DataTypes.Coupon;
import DataTypes.CouponType;
import DataTypes.Customer;
import Exceptions.DAOException;
import Exceptions.FacadeException;

/**
 * A CustomerFacade class which contains all methods that can be executed by the
 * customer user.<br>
 * Any instance represents a single customer, that can execute the methods by
 * this object, whose given by the login static method.
 */
public class CustomerFacade extends CouponClientFacade {
	/**
	 * Customer object containing the attributes of the customer
	 */
	private Customer customer;
	/**
	 * A reference to the CustomerDAO instance.
	 */
	private CustomerDBDAO customerDBDAO = CustomerDBDAO.getInstance();
	/**
	 * A reference to the CouponDBDAO instance.
	 */
	private CouponDBDAO couponDBDAO = CouponDBDAO.getInstance();
	/**
	 * A reference to the JoinedTableDBDAO instance.
	 */
	private JoinedTableDBDAO joinedTableDBDAO = JoinedTableDBDAO.getInstance();

	/**
	 * CTOR
	 * 
	 * @param customer
	 *            the specific customer
	 */
	public CustomerFacade(Customer customer) {
		this.customer = customer;
	}

	/**
	 * purchaseCoupon method is purchase a coupon for this customer client.<br>
	 * purchesed a coupon by the customer is automatically adds a row in the
	 * customerCoupon join table
	 * 
	 * @param coupon
	 *            The coupon to purchase
	 * @throws FacadeException
	 *             When this customer already purchased this coupon,or coupon
	 *             out of stock or connection failed.
	 */
	public void purchaseCoupon(Coupon coupon) throws FacadeException {
		try {
			joinedTableDBDAO.customerPurchaseCoupon(this.customer, coupon);
		} catch (DAOException e) {
			throw new FacadeException("Sorry, We Can't Purchase This Coupon ", e.getCause());
		}
	}

	/**
	 * getAllPurchasedCoupon method is getting a collection of all Purchased
	 * Coupons that bought by this customer.
	 * 
	 * @return A collection of Purchased Coupons which this customer bought.
	 * 
	 * @throws FacadeException
	 *             When connection with the database failed.
	 */
	public Collection<Coupon> getAllPurchasedCoupon() throws FacadeException {
		try {
			Collection<Coupon> purchasedCoupon = customerDBDAO.getPurchasedCouponsByCustomer(this.customer);
			if (purchasedCoupon.isEmpty()) {
				System.out.println("There Is No Purchased Coupons of" + customer.getCustName());
			} else {
				// return the collection with all purchased coupons of this
				// customer
				System.out.println("Got All Purchased Coupons of Customer : " + customer.getCustName());
			}
			return purchasedCoupon;
		} catch (DAOException e) {
			throw new FacadeException("Get All Purchased Coupon Failed", e);
		}
	}

	/**
	 * getAllPurchasedCouponByType method is getting a collection of all coupons
	 * bought by this customer with a specific type of coupon.
	 * 
	 * @param type
	 *            The specific type of coupons
	 * 
	 * @return A collection of Purchased Coupons which this customer bought with
	 *         a specific type of coupon.
	 * @throws FacadeException
	 *             if connection failed.
	 */
	public Collection<Coupon> getAllPurchasedCouponByType(CouponType type) throws FacadeException {

		try {
			Collection<Coupon> purchasedCouponByType = couponDBDAO.getAllPurchasedCouponByType(this.customer.getId(),
					type);
			if (purchasedCouponByType.isEmpty()) {
				System.out.println("There Is No Purchased Coupons Of This Type");
			} else {
				// return the collection with all purchased coupons of this
				// customer
				System.out.println("Got All Purchased Coupons Of This Type");
			}
			return purchasedCouponByType;
		} catch (DAOException e) {
			throw new FacadeException("Sorry, We Can't Get All Purchased Coupons By Their Type ", e);
		}
	}

	/**
	 * getAllPurchasedCouponBtPrice method is getting a collection of all
	 * coupons, bought by this customer with a specific max price.
	 * 
	 * @param maxPrice
	 *            the specific max price
	 * 
	 * @return A collection of Purchased Coupons which this customer bought with
	 *         a specific max price.
	 * 
	 * @throws FacadeException
	 *             if connection failed.
	 */
	public Collection<Coupon> getAllPurchasedCouponByPrice(double maxPrice) throws FacadeException {
		try {
			return couponDBDAO.getPurchasedCouponsByCustAndMaxPrice(this.customer, maxPrice);
		} catch (DAOException e) {
			throw new FacadeException("Sorry, We Can't Get All Purchased Coupons By Their Price ", e);

		}
	}
}
