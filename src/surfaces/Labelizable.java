package surfaces;
/**
 * Assigns labels to the instances of the objects.
 */
public interface Labelizable {
	
	/**
	 * Assign a label to an object.
	 */
	void assignLabel();
	
	/**
	 * Returns the label of an object.
	 * @return the label of the object.
	 */
	String getLabel();
}
