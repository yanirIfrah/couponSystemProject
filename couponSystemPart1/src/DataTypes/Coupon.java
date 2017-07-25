package DataTypes;

import java.text.ParseException;
import java.util.Date;

import Utiles.DateConvertUtils;

/**
 * A class representing coupon attributes.
 */
public class Coupon {
	/**
	 * Coupon ID. The primary key of a coupon.
	 */
	private long id;
	/**
	 * Coupon title. A unique title to this coupon.
	 */
	private String title;
	/**
	 * Coupon validity start day.
	 */
	private Date startDate;
	/**
	 * Coupon validity end day.
	 */
	private Date endDate;
	/**
	 * Coupon amount on stock.
	 */
	private int amount;
	/**
	 * Coupon type.
	 */
	private CouponType type;
	/**
	 * Coupon message.
	 */
	private String message;
	/**
	 * Coupon price.
	 */
	private double price;
	/**
	 * Coupon image location.
	 */
	private String image;

	/**
	 * Allocates a <b>Coupon</b> object and initializes it so that it represents
	 * a coupon with default values. ID value must be set before using this
	 * object, Otherwise it will be useless.
	 */
	public Coupon() {
	}

	/**
	 * Allocates a <b>Coupon</b> object and initializes it so that it represents
	 * a coupon with given values.
	 * 
	 * @param id
	 *            Coupon ID.
	 * @param title
	 *            Coupon title.
	 * @param sStartDate
	 *            Coupon start_date (String date)
	 * @param sEndDate
	 *            Coupon end_date (String date)
	 * @param amount
	 *            Coupon amount.
	 * @param type
	 *            Coupon couponType.
	 * @param message
	 *            Coupon message.
	 * @param price
	 *            Coupon price.
	 * @param image
	 *            Coupon image.
	 */
	public Coupon(long id, String title, String sStartDate, String sEndDate, int amount, CouponType type,
			String message, double price, String image) {
		super();
		this.id = id;
		this.title = title;
		this.startDate = DateConvertUtils.stringToDate(sStartDate);
		this.endDate = DateConvertUtils.stringToDate(sEndDate);
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
	}

	/**
	 * Allocates a <b>Coupon</b> object and initializes it so that it represents
	 * a coupon with given values.
	 * 
	 * @param id
	 *            Coupon ID.
	 * @param title
	 *            Coupon title.
	 * @param startDate
	 *            Coupon start_date (java.util.Date date)
	 * @param endDate
	 *            Coupon end_date (java.util.Date date)
	 * @param amount
	 *            Coupon amount.
	 * @param type
	 *            Coupon couponType.
	 * @param message
	 *            Coupon message.
	 * @param price
	 *            Coupon price.
	 * @param image
	 *            Coupon image.
	 */
	public Coupon(long id, String title, Date startDate, Date endDate, int amount, CouponType type, String message,
			double price, String image) {
		super();
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
	}

	/**
	 * @return Coupon ID.
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set a value to Coupon ID.
	 * 
	 * @param id
	 *            The value to set.
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return Coupon title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set a value to Coupon title.
	 * 
	 * @param title
	 *            The value to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return Coupon start date.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Set a value to Coupon start date.
	 * 
	 * @param startDate
	 *            The value to set.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Set a value to Coupon start date.
	 * 
	 * @param startDate
	 *            The value to set.
	 */
	public void setStartDate(String startDate) {
		this.startDate = DateConvertUtils.stringToDate(startDate);
	}

	/**
	 * Set a value to Coupon end date.
	 * 
	 * @param endDate
	 *            The value to set (String end date).
	 * @throws ParseException
	 *             If the string isn't written in the valid form
	 */
	public void setEndDate(String endDate) throws ParseException {
		this.startDate = DateConvertUtils.stringToDate(endDate);
	}

	/**
	 * @return Coupon end date.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Set a value to Coupon end date.
	 * 
	 * @param endDate
	 *            The value to set (java.util.Date date).
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return Coupon amount.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Set a value to Coupon amount.
	 * 
	 * @param amount
	 *            The value to set.
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return Coupon type.
	 */
	public CouponType getType() {
		return type;
	}

	/**
	 * Set a value to Coupon type.
	 * 
	 * @param type
	 *            The value to set.
	 */
	public void setType(CouponType type) {
		this.type = type;
	}

	/**
	 * @return Coupon message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set a value to Coupon message.
	 * 
	 * @param message
	 *            The value to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Coupon price.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Set a value to Coupon price.
	 * 
	 * @param price
	 *            The value to set.
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return Coupon image.
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Set a value to Coupon image.
	 * 
	 * @param image
	 *            The value to set.
	 */
	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "\nCOUPON ID: " + id + "\nTITLE: " + title + "\nSTART DATE: " + startDate + "\nEND DATE: " + endDate
				+ "\nAMOUNT: " + amount + "\nTYPE: " + type + "\nMESSAGE: " + message + "\nPRICE: " + price + " NIS"
				+ "\nIMAGE: " + image + "\n**********";
	}

}
