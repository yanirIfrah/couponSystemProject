package Exceptions;

public class DAOException extends Exception {

	/**
	 * for serializetion which not used, but inherited from Exception.
	 */
	private static final long serialVersionUID = 1L;

	public DAOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Constructs a new DAOException with the specified detail message and
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
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new DAOException with the specified detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 */
	public DAOException(String message) {
		super(message);
	}

	/**
	 * Constructs a new DAOException with the specified detail cause.
	 * 
	 * @param cause
	 *            the detail cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method).
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}

}
