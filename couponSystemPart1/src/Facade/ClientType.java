package Facade;

/**
 * emun ClientType class that define a diffrent Type of Client<br>
 * a Client object has a Type that include one of the Type that define in this
 * class<br>
 * <blockquote><b>Type's are:</b> <br>
 * ADMIN("admin")<br>
 * COMPANY("company") <br>
 * CUSTOMER("customer")</blockquote>
 */
public enum ClientType {

	ADMIN("admin"), COMPANY("company"), CUSTOMER("customer");

	private final String name;

	/**
	 * Set a value to ClientType name.
	 * 
	 * @param name
	 *            The value to set.
	 */
	ClientType(String name) {
		this.name = name();
	}

	/**
	 * @return ClientType name.
	 */
	public String getName() {
		return name;
	}

}
