package Utiles;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import DataTypes.Coupon;
import DataTypes.CouponType;

/**
 * ProjectUtils class which contains one method that extract all the coupon
 * information- id,title,satrt date,end date,amount,type,maessage,price and
 * image from the result set<br>
 * this class is created for the convenience for all the methods that needs to
 * extract coupon from the result set and return a coupon object
 */
public class ProjectUtils {
	/**
	 * extractCouponFromResultSet method is extract all the coupon information-
	 * id,title,satrt date,end date,amount,type,maessage,price and image from
	 * the result set<br>
	 * this methos is created for our convenience (includes all the methods that
	 * needs to extract coupon from the result set).
	 * 
	 * @param resultSet
	 *            thr result set wich the coupon is extract from
	 * @return Coupon
	 * @throws SQLException
	 *             if connection failed
	 */
	public static Coupon extractCouponFromResultSet(ResultSet resultSet) throws SQLException {
		long id = resultSet.getLong("ID");
		String title = resultSet.getString("TITLE");
		Date startDate = resultSet.getDate("START_DATE");
		Date endDate = resultSet.getDate("END_DATE");
		int amount = resultSet.getInt("AMOUNT");
		CouponType couponType = CouponType.valueOf(resultSet.getString("TYPE"));
		String message = resultSet.getString("MESSAGE");
		double price = resultSet.getDouble("PRICE");
		String image = resultSet.getString("IMAGE");

		Coupon coupon = new Coupon(id, title, startDate, endDate, amount, couponType, message, price, image);

		return coupon;
	}
}
