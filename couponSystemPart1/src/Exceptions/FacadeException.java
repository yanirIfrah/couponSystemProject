package Exceptions;

public class FacadeException extends CouponSystemException {
	/**
	 * for serializetion which not used, but inherited from Exception.
	 */
	private static final long serialVersionUID = 1L;

	public FacadeException() {
		super();
	}

	public FacadeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructs a new FacadeException with the specified detail message and
	 * cause.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public FacadeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new FacadeException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 */
	public FacadeException(String message) {
		super(message);
	}

	/**
	 * Constructs a new FacadeException with the specified detail cause.
	 * 
	 * @param cause
	 *            the detail cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method).
	 */
	public FacadeException(Throwable cause) {
		super(cause);
	}
}
