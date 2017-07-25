package CouponSystem;

import DBDAO.CouponDBDAO;
import Exceptions.DAOException;

public class DailyCouponExpirationTask extends Thread {

	private String threadName;
	/**
	 * Set a boolean flag for keeping the run() method process until the
	 * shutdownThread() is called. A running method which contains the process
	 */
	private static boolean KeepWorking = true;

	public DailyCouponExpirationTask(String name) {
		this.threadName = name;
		System.out.println(name + " Thread Is Created");
	}

	/**
	 * A run method of the thread that responsibal to delete all expierd coupons
	 * in the Date Base
	 */
	@Override
	public void run() {
		while (KeepWorking) {
			CouponDBDAO couponDBDAO = new CouponDBDAO();
			try {
				couponDBDAO.deleteExpiredCoupons();
				/**
				 * put thread in Blocked pool for 24 hours
				 */
				Thread.sleep(1000 * 60 * 60 * 24);
			} catch (DAOException | InterruptedException e1) {
				e1.getMessage();
				KeepWorking = false;
				System.out.println("Connection Failes - Thread Is About To Stop !");
			}
		}
		System.out.println("Thread : " + threadName + " Exiting!");
	}

	/**
	 * A shutdownThread() method that stop the run() method by setting the bExit
	 * to False
	 */
	public void shutdownThread() {
		KeepWorking = false;
		System.out.println(threadName + " Was Shut Down!");
		interrupt();
	}
}
