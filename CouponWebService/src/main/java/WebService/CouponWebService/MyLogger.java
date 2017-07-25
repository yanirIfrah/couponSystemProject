package WebService.CouponWebService;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

	private static MyLogger instance;
	public static Logger logger = null;
	private FileHandler fileHandler;
	private SimpleFormatter simpleFormatter;

	// location to file MyLogger - C:\spring\sts-bundle\sts-3.8.4.RELEASE
	public MyLogger() {
		logger = Logger.getLogger(MyLogger.class.getName());
		try {
			fileHandler = new FileHandler("MyLogger.log");
			logger.addHandler(fileHandler);
			simpleFormatter = new SimpleFormatter();
			fileHandler.setFormatter(simpleFormatter);

			ConsoleHandler cHandler = new ConsoleHandler();
			cHandler.setLevel(Level.INFO);
			cHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(cHandler);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized MyLogger getInstance() {
		if (instance == null)
			instance = new MyLogger();
		return instance;
	}

	public Logger getLogger() {
		return logger;
	}

}
