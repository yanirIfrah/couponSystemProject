package DAO.interfaces;

import java.util.Collection;

import DataTypes.Company;
import DataTypes.Coupon;
import DataTypes.CouponType;
import Exceptions.DAOException;

public interface CompanyDAO {
	/**
	 * createCustomer method is creating a new company in the Company table in
	 * the DB.
	 * 
	 * @param company
	 *            The company to add.
	 * @throws DAOException
	 *             if connection failed
	 */
	public void createCompany(Company company) throws DAOException;

	/**
	 * deleteCompany method is deleting a company from the Company table in the
	 * DB.<br>
	 * check if company with the given id exists
	 * 
	 * @param company
	 *            The company to delete.
	 * @throws DAOException
	 *             if connection failed
	 */
	public void deleteCompany(Company company) throws DAOException;

	/**
	 * updateCompany method is updating a company attribute of specific company.
	 * <br>
	 * (set only Passwords and Email)
	 * 
	 * @param company
	 *            The company to update
	 * @throws DAOException
	 *             if connection failed
	 */
	public void updateCompany(Company company) throws DAOException;

	/**
	 * getCompany method is getting a company with the given id from the company
	 * table in db.
	 * 
	 * @param id
	 *            The requested company's ID.
	 * @return Company <br>
	 *         The company from DB with the given ID.
	 * @throws DAOException
	 *             if connection failed
	 */
	public Company getCompany(long id) throws DAOException;

	/**
	 * getAllCompanies method is getting all the companies from the DB.
	 * 
	 * @return A collection of all companies from DB.
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Company> getAllCompanies() throws DAOException;

	/**
	 * getCoupons method is getting a coupon from the Company_Coupon table,
	 * associated with the given company id.
	 * 
	 * @param company
	 *            The company for which to search coupons for.
	 * @return A collection of all coupons of the given company.
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getCoupons(Company company) throws DAOException;

	/**
	 * login method Verifies a company user credentials.
	 * 
	 * @param compName
	 *            The company name.
	 * @param password
	 *            The company password.
	 * @return True - if verification resulted true. <br>
	 *         False - if verification resulted false.
	 * @throws DAOException
	 *             if connection failed
	 */
	public boolean login(String compName, String password) throws DAOException;

	/**
	 * isCompanyExistByCompanyName check if the company exist by a given company
	 * name.
	 * 
	 * @param company
	 *            The company for which to search for.
	 * @return True - if the company is exists<br>
	 *         Fales - if tha company isn't exists
	 * @throws DAOException
	 *             if connection failed
	 */
	public boolean isCompanyExistByCompanyName(String company) throws DAOException;

	/**
	 * isCouponExistByTitle check if the coupon title exists in DB.
	 * 
	 * @param coupon
	 *            The coupon for which to search for by title.
	 * @return True - if the company is exists<br>
	 *         Fales - if tha company isn't exists
	 * @throws DAOException
	 *             if connection failed
	 */
	public boolean isCouponExistByTitle(Coupon coupon) throws DAOException;

	/**
	 * getCouponsByType fet all coupons by a specific type from the DB
	 * 
	 * @param CouponType
	 *            The type of coupon for which to search for.
	 * @return collection of all coupons bt type
	 * 
	 * @throws DAOException
	 *             if connection failed
	 */
	public Collection<Coupon> getCouponsByType(CouponType CouponType) throws DAOException;

	/**
	 * isCompanyExistByCompanyId check if the company exist by a given company
	 * id.
	 * 
	 * @param id
	 *            The company id for which to search for.
	 * @return True - if the company is exists<br>
	 *         Fales - if tha company isn't exists
	 * @throws DAOException
	 *             if connection failed
	 */
	boolean isCompanyExistByCompanyId(long id) throws DAOException;

	/**
	 * getCompanyByName method is getting a specific company by given company
	 * name
	 * 
	 * @param name
	 *            The company name for which to search for.
	 * @return The specific Company detail with the given name
	 * @throws DAOException
	 *             if connection failed
	 */
	public Company getCompanyByName(String name) throws DAOException;

	/**
	 * isCompanyAlreadyCreatedThisCoupon method is checking if the company
	 * allready created a specific coupon
	 * 
	 * @param company
	 *            The company for which to search for.
	 * @param coupon
	 *            The coupon for which to search for.
	 * @return True - if the company allready created the specific coupon<br>
	 *         False - if the company Did'nt created the specific coupon
	 * @throws DAOException
	 *             if connection failed
	 */
	public boolean isCompanyAlreadyCreatedThisCoupon(Company company, Coupon coupon) throws DAOException;

}
