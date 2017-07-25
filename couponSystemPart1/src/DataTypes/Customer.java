package DataTypes;

import java.util.Collection;

public class Customer {
	/**
	 * Customers ID attribute. The primary key of a customer
	 */
	private long id;
	/**
	 * Customers name attribute. A unique name to this customer. <br>
	 * used in order to login the Coupon System.
	 */
	private String custName;
	/**
	 * Customers password attribute.<br>
	 * used in order to login the Coupon System.
	 */
	private String password;
	/**
	 * List of coupons the customer buy.
	 */
	private Collection<Coupon> coupons;

	/**
	 * CTOR of a <b>Customer</b> object
	 */
	public Customer() {
		super();
	}

	/**
	 * Allocates a <b>Customer</b> object and initializes it so that it
	 * represents a customer with given values.
	 * 
	 * @param id
	 *            Customer ID.
	 * @param custName
	 *            Customer name.
	 * @param password
	 *            Customer password.
	 */
	public Customer(long id, String custName, String password) {
		super();
		this.id = id;
		this.custName = custName;
		this.password = password;
	}

	public Customer(long id, String custName, String password, Collection<Coupon> coupons) {
		this(id, custName, password);
		this.coupons = coupons;
	}

	/**
	 * @return Customers ID.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set a value to Customers ID. block the option the set or update an
	 * existant ID for customer's (ID is defined as primary key)
	 * 
	 * @param id
	 *            The value to set.
	 */
	public void setId(long id) {
		if (this.getId() == id || this.getId() == 0) {
			this.id = id;
		} else {
			System.out.println("Cannot Update ID - Primary Key");
			return;
		}
	}

	/**
	 * @return Customers password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set a value to Customers password
	 * 
	 * @param password
	 *            The value to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Customers coupons.
	 */
	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * Set customers coupons.
	 * 
	 * @param coupons
	 *            The coupons to set.
	 */
	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	/**
	 * @return Customers name.
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * Set a value to Customers name
	 * 
	 * @param custName
	 *            The value to set.
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	@Override
	public String toString() {
		return "\nCUSTOMER ID: " + id + "\nCUSTOMER NAME: " + custName + "\nPASSWORD: " + password + "\n**********";
		// , coupons: " + coupons
		// + coupons.toString() + "]";
	}

}
