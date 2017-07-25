package CouponSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * A <b>singleton</b> that handles all connections of the application with the
 * database. <br>
 * That is performed by having a {@link Collection} of connections that stay
 * open until the application's shut down , Letting every client (admin,company
 * and customer, borrow a single connection from the pool and return it back to
 * the pool. that process, which avoide persistant opening and closing of
 * connections, makes the application a lot more efficient since every
 * connection takes much longer.
 * <p>
 * The ConnectionPool instance is initialized by the static method
 * <b>ConnectionPool.initialize()</b>, which executed by the
 * {@link CouponSystem} object at the begginig of the application. That
 * initialization opens all connections of the pool given the amount of
 * <b>ConnectionPool.MAX_CONNECTION</b>.
 * </p>
 * The address of the database is given by the static method
 * <b>CouponSystem.dataBaseURL()</b>.
 */
public class ConnectionPool {
	/**
	 * The instance of this singleton class
	 */
	private static ConnectionPool instance = new ConnectionPool();

	/**
	 * A safe way to use the instance
	 * 
	 * @return The instance of this singleton.
	 */
	public static ConnectionPool getInstance() {
		if (instance == null) {
			instance = new ConnectionPool();
		}
		return instance;
	}

	/**
	 * Connections of the pool represented by {@link HashSet} object
	 */
	private Collection<Connection> connections = new HashSet<>();

	/**
	 * Amount of connections that are available.
	 */
	public static final int MAX_CONNECTIONS = 10;

	/**
	 * Construct an element of ConnectionPool type. The constructor is
	 * class-private for the safety of the code, insuring that ConnectionPool
	 * will stay singleton with a single value of its kind.
	 */
	private ConnectionPool() {
		try {
			for (int i = 0; i < MAX_CONNECTIONS; i++) {
				connections.add(createConnection());
			}
			System.out.println("****[ Connection Pool Is Full And Ready For Use ]******\n");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * createConnection mathod is reading the DB URL from a file:
	 * DataBaseURL.txt. load the driver and get connection to the DB.
	 * 
	 * @return Connection
	 */
	public Connection createConnection() {
		// read from the file
		String url1 = "jdbc:derby://localhost:1527/CouponDB";
		// try (Scanner file = new Scanner(new
		// FileInputStream("files/DataBaseURL.txt"))) {
		// try (Scanner file = new Scanner(new FileInputStream(url1))) {

		// String url = file.nextLine();
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver");
			return (DriverManager.getConnection(url1));
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// } catch (FileNotFoundException | SQLException |
		// ClassNotFoundException e) {
		// e.printStackTrace();
		// return null;
		// }
		System.err.println("Connection To DataBase Is Failed");
		return null;
	}

	/**
	 * sychronized method ! for situations that there is no connection in the
	 * pool, and nust to block those who want to take a connection, by the
	 * wait() method.<br>
	 * tack connection from the pool and give it to client. The given connection
	 * Should be return to the pool at the end of the Operation of the each
	 * method that took the connection.
	 * 
	 * @return A connection from the pool.
	 */
	public synchronized Connection getConnection() {
		Iterator<Connection> it = connections.iterator();
		while (!it.hasNext()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Connection con = it.next();
		it.remove();
		notifyAll();
		return con;
	}

	/**
	 * sychronized method ! for situations that there is MAX_CONNECTION of
	 * connection in the pool, and must block those who want to return a
	 * connection, by the wait() method.<br>
	 * taking a connection back from the method that barrow him, end returned it
	 * to the pool.
	 * 
	 * @param con
	 *            The connection to return
	 */
	public synchronized void returnConnection(Connection con) {
		while (connections.size() >= MAX_CONNECTIONS) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		connections.add(con);
		notifyAll();
	}

	/**
	 * close all connections. called by the shutDown() method in CouponSystem
	 * class
	 */
	// Closing all the connection in both arrays with for each loop
	public void closeAllConnections() {
		try {
			Iterator<Connection> iterator = connections.iterator();
			while (iterator.hasNext()) {
				iterator.next().close();
			}
			System.out.println("All connections where closed");
		} catch (SQLException e) {
		}
	}
}
