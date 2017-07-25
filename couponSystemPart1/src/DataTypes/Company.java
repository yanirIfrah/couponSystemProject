package DataTypes;

import java.util.Collection;

/**
 * A class representing company attributes.
 */
public class Company {
	/**
	 * company ID. The primary key of a company.
	 */
	private long id;
	/**
	 * Company name. A unique name to this company.<br>
	 * used in order to login the Coupon System.
	 */
	private String compName;
	/**
	 * Company password.<br>
	 * used in order to login the Coupon System.
	 */
	private String password;
	/**
	 * Company email.
	 */
	private String email;
	/**
	 * List of coupons the company sells.
	 */
	Collection<Coupon> coupons;

	/**
	 * Allocates a <b>Company</b> object and initializes it so that it
	 * represents a company with default values. ID value must be set before
	 * using this object, Otherwise it will be useless.
	 */
	public Company() {
		super();
	}

	/**
	 * Allocates a <b>Company</b> object and initializes it so that it
	 * represents a company with given values.
	 * 
	 * @param id
	 *            Company ID.
	 * @param compName
	 *            Company name.
	 * @param password
	 *            Company password.
	 * @param email
	 *            Company email.
	 */
	public Company(long id, String compName, String password, String email) {
		super();
		this.id = id;
		this.compName = compName;
		this.password = password;
		this.email = email;
	}

	/***
	 * 
	 * Allocates a <b>Company</b> object and initializes it so that it
	 * represents a company with given values.
	 * 
	 * @param id
	 *            Company ID.
	 * @param compName
	 *            Company name.
	 * @param password
	 *            Company password.
	 * @param email
	 *            Company email.
	 * @param coupons
	 *            A collection of all coupons by the company
	 */
	public Company(long id, String compName, String password, String email, Collection<Coupon> coupons) {
		this(id, compName, password, email);
		this.coupons = coupons;
	}

	/**
	 * @return company ID.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set a value to company ID.
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
	 * @return company name.
	 */
	public String getCompName() {
		return compName;
	}

	/**
	 * Set a value to company name.
	 * 
	 * @param compName
	 *            The value to set.
	 */
	public void setCompName(String compName) {
		this.compName = compName;
	}

	/**
	 * @return company password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set a value to company password.
	 * 
	 * @param password
	 *            The value to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return company email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set a value to company email.
	 * 
	 * @param email
	 *            The value to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return company coupons.
	 */
	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * Set company coupons.
	 * 
	 * @param coupons
	 *            The coupons to set.
	 */
	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return "\n**********\nCOMPANY ID: " + id + "\nCOMPANY NAME: " + compName + "\nPASSWORD: " + password
				+ "\nEMAIL: " + email + "\n**********";
	}

}
