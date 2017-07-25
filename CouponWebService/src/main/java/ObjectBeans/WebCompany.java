package ObjectBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

import DataTypes.Company;
import DataTypes.Coupon;

@XmlRootElement
public class WebCompany implements Serializable {

	private static final long serialVersionUID = 1L;

	// Attribute
	private long id;
	private String compName;
	private String password;
	private String email;
	Collection<Coupon> coupons;

	// CTOR
	public WebCompany() {
	}

	// CTOR for Web Coupon that run on the net without Password
	public WebCompany(Company company) {
		super();
		this.id = company.getId();
		this.compName = company.getCompName();
		this.email = company.getEmail();
	}

	// ***********************************************************************
	/**
	 * convertToCompany method are converting the company to a WebCompany that
	 * can run in the Web.
	 */
	public Company convertToCompany() {
		return new Company(this.id, this.compName, this.password, this.email);
	}

	/**
	 * convertToCompanies method are converting the WebCompany to a Company that
	 * can run in the Web.
	 */
	public static Collection<Company> convertToCompanies(Collection<WebCompany> wc) {
		Collection<Company> companies = new ArrayList<>();
		for (WebCompany webCompany : wc) {
			companies.add(webCompany.convertToCompany());
		}
		return companies;
	}

	/**
	 * convertToWebCompanies method are converting the Company from a WebCompany
	 * that can run in the Web to Company as in the DB
	 */
	public static Collection<WebCompany> convertToWebCompanies(Collection<Company> companies) {
		Collection<WebCompany> WebCompanies = new ArrayList<>();
		for (Company company : companies) {
			WebCompanies.add(new WebCompany(company));
		}
		return WebCompanies;
	}

	// ***********************************************************************
	// Getters and Setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		if (this.getId() == id || this.getId() == 0) {
			this.id = id;
		} else {
			System.out.println("Cannot Update ID - Primary Key");
			return;
		}
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

}
