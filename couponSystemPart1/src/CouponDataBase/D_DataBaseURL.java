package CouponDataBase;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class D_DataBaseURL {

	public static void main(String[] args) {
		File dir = new File("files");
		dir.mkdir();
		/**
		 * creating a folder files thet holds the connection url to database.
		 */
		// ARM
		try (FileWriter out = new FileWriter("files/DataBaseURL.txt");) {
			String URL = "jdbc:derby://localhost:1527/CouponDB";
			out.write(URL);
			System.out.println("end");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}