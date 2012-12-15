// Note: Throughout this program, when a boolean is used to refer to the type of tumor
// true = MALIGNANT
// false = BENIGN

/**This class models a Tumor object, with an integer idNumber, boolean type
 * (true for malignant, false for benign), and either an array of doubles for 
 * the 30 other characteristics of the tumor or one double for the distance
 * that Tumor is from another Tumor (this type of Tumor is created by the 
 * NearestNeighbor.SortByDistanceFrom method, based on another tumor given to
 * that method, which then sorts the tumors by their distances). 
 */
public class Tumor implements Comparable<Tumor> {
	// Initialize an integer variable idNumber to store the ID number of the 
	// tumor from the data file
	private int idNumber;
	// Initialize a boolean variable type to store the type of tumor (malignant
	// or benign)
	private boolean type;
	// Initialize an array of doubles characteristics to store the remaining
	// characteristics of each tumor
	private double[] characteristics;
	// Initialize a double variable distance to store the distance of this 
	// tumor from another tumor
	private double distance;
	
	/**This constructor creates a Tumor object with an idNumber, type 
	 * (malignant or benign) and array of doubles for its other characteristics
	 * @param idNumberIn The ID number for this tumor from the data file
	 * @param typeIn The type of this tumor from the data file (true for 
	 * malignant, false for benign)
	 * @param characteristicsIn The array of doubles for the other 
	 * characteristics of the tumor
	 */
	public Tumor(int idNumberIn, boolean typeIn, 
			double[] characteristicsIn) {
		// Sets the Tumor's idNumber variable to the actual parameter input
		idNumber = idNumberIn;
		// Sets the Tumor's type boolean to the actual parameter input
		type = typeIn;
		// Sets the Tumor's characteristics array of doubles to the actual
		// parameter input
		characteristics = characteristicsIn;
	}
	
	/**This second constructor creates a Tumor object with only three 
	 * variables: its ID number, type, and distance from some other tumor; this
	 * is for when we're sorting tumors by distance in the 
	 * NearestNeighbor.sortByDistanceFrom static method and we don't care about
	 * all the components anymore
	 * @param idNumberIn The ID number for this tumor obtained from the 
	 * corresponding Tumor(int, boolean, double[]) object
	 * @param typeIn The type for this tumor obtained from the corresponding
	 * Tumor(int, boolean, double[]) object
	 * @param distanceIn The distance from the tumor we are testing to this 
	 * tumor, calculated in NearestNeighbor.SortByDistanceFrom by the Tumor's
	 * Dist method 
	 */
	public Tumor(int idNumberIn, boolean typeIn, double distanceIn) {
		// Sets the Tumor's idNumber variable to the actual parameter input
		idNumber = idNumberIn;
		// Sets the Tumor's type boolean to the actual parameter input
		type = typeIn;
		// Sets the Tumor's distance double to the actual parameter input
		distance = distanceIn;
	}
	
	/** This compareTo method compares Tumors by their distance variables, 
	 * which should have been set by a method that calculated their distances
	 * from another Tumor; it won't work if you try to sort an array of Tumors
	 * created by the first constructor that don't have a distance variable, 
	 * but this is never done -- we only sort Tumors after calculating their
	 * distance variable
	 * @param other The other tumor to compare distance to
	 * @return 1 if this tumor's distance is greater, -1 if it's smaller, 0 if
	 * they are tied
	 */
	public int compareTo(Tumor other) {
		// If the distance variable for the current is greater than the 
		// distance variable for the tumor we are comparing to...
		if (this.distance > other.distance)
			// return 1 to signify it is "bigger"
			return 1;
		// Or if the distance variable for the current tumor is less than the
		// distance variable for the tumor we are comparing to...
		else if (this.distance < other.distance)
			// return -1 to signify it is "smaller"
			return -1;
		// Otherwise (i.e. if the distances are the same) return 0 to signify
		// the tumors are the "same size"
		else return 0;
	}
	
	/**This simple accessor method returns the idNumber variable of a Tumor
	 * @return The idNumber integer of a Tumor
	 */
	public int getID() {
		return idNumber;
	}
	
	/**This simple accessor method returns the type boolean of a Tumor
	 * @return The type boolean of a Tumor
	 */
	public boolean getType() {
		return type;
	}
	
	/**This accessor method returns all the other characteristics of a Tumor
	 * in an array of doubles
	 * @return The Tumor's characteristics array of doubles
	 */
	public double[] getCharacteristics() {
		return characteristics;
	}
	
	
	/**This method calculates the distance from another supplied Tumor to this
	 * tumor by using the Pythagorean Theorem for however many coordinates 
	 * there are in the characteristics array of the Tumors
	 * @param other The other Tumor to calculate the distance between
	 * @return The double for the distance between the Tumors
	 */
	public double Dist(Tumor other) {
		// Initializes a double for the square of the distance
		double distSquared = 0;
		// For loop to run through the characteristics arrays running the 
		// Pythagorean Theorem for all the characteristics
		for (int i = 0; i < this.characteristics.length; i++) {
			// Adds onto the distSquared double the square of the difference
			// between the two characteristics
			distSquared += (Math.pow(this.characteristics[i] - 
				other.getCharacteristics()[i], 2));
		} // End of for loop to run through characteristics array and doing
		  // Pythagorean Theorem for the characteristics
		// Takes the square root of distSquared and returns it
		return Math.sqrt(distSquared);
	} // End of Dist method	
}