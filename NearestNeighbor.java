// Note: Throughout this program, when a boolean is used to refer to the type 
// of tumor, true means malignant and false means benign.

// Importing classes for Scanner, Arrays (to sort) and IO (to read file)
import java.util.Scanner;
import java.util.Arrays;
import java.io.*;

/**Repository of static methods used for the Nearest Neighbor algorithm.
 */
public class NearestNeighbor {
	
	/**Returns a list of tumors (without their characteristics array 
	 * but with distance variables) sorted by their distance from a tumor t1
	 * @arg t1 The tumor to calculate distance from
	 * @arg tList The array of Tumors to sort by their distance from t1
	 * @return tDistList The array of Tumors, sorted by their distance from t1
	 */
	public static Tumor[] sortByDistanceFrom(Tumor t1, Tumor[] tList) {
		// Creates a new array of Tumor objects the same length as the input
		// array of tumors
		Tumor[] tDistList = new Tumor[tList.length];
		// For loop to populate the new array
		for (int i = 0; i < tList.length; i++) {
			// For every tumor in tList, creates a new tumor with the same ID 
			// and type of the corresponding tumor in tList and with distance
			// from t1 determined by the Dist method in Tumor, and adds all 
			// these to the array tDistList. 
			tDistList[i] = new Tumor(tList[i].getID(), 
					tList[i].getType(), t1.Dist(tList[i]));
			// tDistList will now contain an array of Tumors *without* their 
			// 30 characteristics, but we don't really care about those anymore
			// because we only needed them to find the distance.
		} // End of for loop to populate tDistList array
		
		// Sorts tumors in tDistList by their distance variable, which was 
		// determined (above) by their distance from t1
		Arrays.sort(tDistList);
		
		// Returns the array tDistList, which has been sorted by distance from
		// t1
		return tDistList;
	}
	
	
	/**Finds type -- malignant (true) or benign (false) -- of the Nth nearest
	 * neighbor to a Tumor t1 from a list of tumors tList (so if N is 1, it 
	 * will find the closest; if N is 2, it will find the second closest, etc.)
	 * @param N Which neighbor to find: 1 for closest, 2 for second closest, 
	 * etc.
	 * @param t1 The Tumor to find the nearest neighbor of
	 * @param tList The array of Tumors to pick the closest one to t1 out of
	 * @return True if the Nth nearest neighbor is malignant; false if it is
	 * benign.
	 */
	public static boolean FindTypeOfNthNearestNeighbor(int N, Tumor t1, 
			Tumor[] tList) {
		// Sorts the array of Tumors tList by their distance from the tumor t1
		// using the sortByDistanceFrom method and stores the sorted array in 
		// a new array of Tumors, sortedTumors
		Tumor[] sortedTumors = sortByDistanceFrom(t1, tList);
		// Returns the type (obtained from the getType() method of the Tumor)
		// of the Tumor located at position N-1 in the sortedTumors array
		// (so it will be the first if N is 1, the second if N is 2, etc.)
		return sortedTumors[N-1].getType();	
	}

	
	/**Runs the nearest neighbor algorithm on an array of Tumors tListIn by
	 * splitting the array into the first 80% (for training data) and the last
	 * 20% (for testing), finding the k nearest neighbors in the training
	 * data of each Tumor in the testing data, and having those neighbors vote
	 * on whether the tumor from the testing data is malignant or benign.
	 * @param k How many nearest neighbors to find and then have vote on 
	 * whether the tumor is malignant or benign
	 * @param tListIn An array of Tumors to use as training and testing data
	 * for the nearest neighbor algorithm
	 * @return An array of doubles representing the percent accuracy of the 
	 * nearest neighbor algorithm for this particular run as well as some other
	 * information
	 */
	public static double[] Accuracy(int k, Tumor[] tListIn) {
		// First we take tListIn and shuffle it up a bit:
		for (int i = 0; i < 10000; i++) {
			// Pick a random tumor between 0 and one less than the number of 
			// Tumors in tListIn
			int randomTumor1 = (int) (Math.random() * tListIn.length);
			// Pick a second random number as above. Now we have two tumors
			// to exchange.
			int randomTumor2 = (int) (Math.random() * tListIn.length);
			// Create a new temporary tumor to hold the first tumor we are 
			// swapping
			Tumor t1Temp = tListIn[randomTumor1];
			// Replace the first tumor we are swapping with the second
			tListIn[randomTumor1] = tListIn[randomTumor2];
			// Replace the second tumor with the (original) first from the 
			// temporary tumor t1Temp
			tListIn[randomTumor2] = t1Temp;	
		} // End of for loop for shuffling the array of Tumors
		
		// Now we take the first 80% of our list into a new array
		// Initialize the new array with .8 times the number of values in the
		// original array
		Tumor[] trainingData = new Tumor[(int) (tListIn.length * .8)];

		// Initialize index variable for upcoming for loop (outside of that for
		// loop so its final value can be accessed later)
		int i = 0;
		
		// Use a for loop to fill trainingData[] with the first values of 
		// tListIn
		for (i = 0; i < trainingData.length; i++) {
			// Copy current value of i of tListIn into trainingData
			trainingData[i] = tListIn[i];
		}
		
		// Now we take the last 20% (starting with value i) into a new array 
		// for testing
		// Create a new array with length equal to the remaining number of 
		// tumors in trainingData
		Tumor[] testData = new Tumor[tListIn.length - trainingData.length];
		// For loop to populate this new array with the remaining Tumors
		for (int j = 0; j < testData.length; j++) {
			// Copies the (i + j)th value of tListIn into testData[j] (since we
			// took up to tListIn[i] for the training data, we start at i and 
			// count from there by adding on j)
			testData[j] = tListIn[i + j];
		} // End of for loop to populate testing data array
		
		// Initialize variables for upcoming calculation of accuracy
		int countCorrect = 0, countUnanimous = 0, countUnanimousCorrect = 0;
		int countNotUnanimousCorrect = 0;
		
		// For loop: for each Tumor in testData, feed that Tumor along with 
		// the entire array of Tumors trainingData to the 
		// findTypeOfNthNearestNeighbor method (with N = k), which will guess
		// the result based on the nearest neighbor algorithm. Then check the
		// result. 
		for (int j = 0; j < testData.length; j++) {
			// Initialize a boolean for the guess
			boolean guess;
			// Initialize counter variables for the number of benign and 
			// malignant tumors
			int numBenign = 0, numMalignant = 0;
			// Inner for loop to find the types of the closest k neighbors
			for (int count = 1; count <= k; count++) {
				// If the kth-closest nearest neighbor is malignant, increment
				// numMalignant
				if (FindTypeOfNthNearestNeighbor(count, testData[j], 
						trainingData) == true)
					numMalignant++;
				// Otherwise increment numBenign
				else numBenign++;
			} // End of inner for loop to find types of closest k neighbors
			
			// If there were more malignants than benigns among the k closest
			// neighbors, then set our guess to true (for malignant)
			if (numMalignant > numBenign) guess = true;
			// Otherwise set our guess to false (for benign)
			else guess = false;
			
			// If the tumors were all of one type... 
			if ((numMalignant == 0) || (numBenign == 0))
				// ...we increment countUnanimous
				countUnanimous++;
			
			// Now we check if our guess was correct by comparing it to the 
			// actual type of that Tumor from testData
			// If we were correct...
			if (guess == testData[j].getType()) {
				// ...we increment countCorrect
				countCorrect++;
				// If we were correct and all of the neighbors were one type...
				if ((numMalignant == 0) || (numBenign == 0))
					// ...we also increment countUnanimousCorrect
					countUnanimousCorrect++;
				// Or if we were correct and the neighbors were not all of one
				// type, we increment countNotUnanimousCorrect
				else countNotUnanimousCorrect++;
			} // End of if statement checking if we were correct
			
		} // End of for loop checking the guess for each value in testData
		  // against the actual type
		
		// Set a new double percentAccurate to the times we were correct
		// divided by the number of Tumors in testData we checked (times 100)
		double percentAccurate = (((double) countCorrect / 
				(double) testData.length) * 100);
		// Set a new double percentUnanimous to the times all the neighbors
		// had the same type (times 100)
		double percentUnanimous = 100 *
			((double) countUnanimous / (double) testData.length);
		// Set a new double percentUnanimousAccuracy to the number of times
		// we were correct and the neighbors were unanimous divided by the 
		// times the neighbors were unanimous (times 100)
		double percentUnanimousAccuracy = 100 * 
			((double) countUnanimousCorrect / (double) countUnanimous);
		// Set a new double percentNotUnanimousAccuracy to the number of times
		// we were correct and the neighbors were not unanimous divided by the 
		// times the neighbors were not unanimous (times 100)
		double percentNotUnanimousAccuracy = 100 * 
			((double) countNotUnanimousCorrect / 
					(double) (testData.length - countUnanimous));
		
		// Comment tags around this can be removed to show the result of each
		// testing of 20% of the Tumors
		/*
		System.out.println("Out of " + testData.length + " tumors, " +
				"NearestNeighbor guessed correctly " + countCorrect + 
				" times, for " + percentAccurate + "% accuracy.");
		*/
		
		// Create a new array of doubles, results, with length 4 for all the 
		// results we want to return
		double[] results = new double[4];
		// Set the first value of results to overall percent accuracy for this
		// trial
		results[0] = percentAccurate;
		// Set the second value of results to the percent of the time the 
		// neighbors were unanimous for this trial
		results[1] = percentUnanimous;
		// Set the third value of results to percent accuracy when the 
		// neighbors were unanimous for this trial
		results[2] = percentUnanimousAccuracy;
		// Set the fourth value of results to percent accuracy when the
		// neighbors were not unanimous for this trial
		results[3] = percentNotUnanimousAccuracy;

		// Return the array results
		return results;
	}
	
	
	/**Runs 100 trials of the Accuracy method and keeps track of the 
	 * accuracy percentages
	 * @param tListIn An array of Tumors to use as training and testing data
	 * for the nearest neighbor algorithm (probably extracted from the data 
	 * file; passed to Accuracy method)
	 * @param numNeighbors How many nearest neighbors to find and then have 
	 * vote on whether the tumor is malignant or benign (passed to Accuracy
	 * method as k)
	 * @return An array of doubles representing the overall percent accuracy 
	 * of the nearest neighbor algorithm for all 100 runs as well as some other
	 * information
	 */
	public static double[] Accuracy100Trials(Tumor[] tListIn, 
			int numNeighbors) {
		// Now we run NearestNeighbor.Accuracy on tListIn 100 times.
		// That static method divides the list up 80-20 each time it is
		// run, runs the Nearest Neighbor algorithm on the 20% using 
		// the 80% as training data, and reports back an array of 
		// doubles containing percent accuracy as well as other 
		// information, like how often the neighbors were unanimous
		// and how accurate it was when they were and weren't. 
		// We do this 100 times and take the average of the 
		// various results.
		
		// Initialize variables for the sum of the percentAccuracy and 
		// percentUnanimousAccuracy percentages that will be reported back from
		// the Accuracy method
		double totalAccuracy = 0, totalUnanimousAccuracy = 0;
		// Same as above for the sum of the percentNotUnanimousAccuracy 
		double totalNotUnanimousAccuracy = 0;
		// Same as above for the sum of the percentUnanimous
		double totalUnanimousPercent = 0;
		
		// Initialize a final int for the number of trials, which will be 100
		final int NUM_OF_TRIALS = 100;
		
		// For loop to run Accuracy method 100 times
		for (int i = 0; i < NUM_OF_TRIALS; i++) {
			// Runs Accuracy method for the array of Tumors tListIn and the
			// value of k for k-nearest neighbor numNeighbors
			double[] resultsOfAccuracy = 
				NearestNeighbor.Accuracy(numNeighbors, tListIn);
			// Adds the result from the Accuracy method's return array for
			// overall accuracy onto totalAccuracy
			totalAccuracy += resultsOfAccuracy[0];
			// Adds the result from the Accuracy method's return array for
			// unanimous percentage onto totalUnanimousPercent
			totalUnanimousPercent += resultsOfAccuracy[1];
			// Adds the result from the Accuracy method's return array for
			// unanimous accuracy onto totalUnanimousAccuracy
			totalUnanimousAccuracy += resultsOfAccuracy[2];
			// Adds the result from the Accuracy method's return array for
			// non-unanimous accuracy onto totalNotUnanimousAccuracy
			totalNotUnanimousAccuracy += resultsOfAccuracy[3];
		} // End of for loop to run Accuracy method 100 times 
		
		// Creates double avgAccuracy for the average of the overall accuracy
		// of each run
		double avgAccuracy = totalAccuracy / (double) NUM_OF_TRIALS;
		// Creates double avgUnanimousPercent for the average of the percentage
		// of times the neighbors were unanimous of each run
		double avgUnanimousPercent = 
			totalUnanimousPercent / (double) NUM_OF_TRIALS;
		// Creates double avgUnanimousPercent for the average of the accuracy
		// when the neighbors were unanimous of each run
		double avgUnanimousAccuracy = 
			totalUnanimousAccuracy / (double) NUM_OF_TRIALS;
		// Creates double avgNotUnanimousPercent for the average of the accuracy
		// when the neighbors were not unanimous of each run
		double avgNotUnanimousAccuracy = 
			totalNotUnanimousAccuracy / (double) NUM_OF_TRIALS;
		
		// Creates a new array of doubles, results, with length 4 for all the 
		// results we want to return
		double[] results = new double[4];
		// Set the first value of results to the overall accuracy of nearest 
		// neighbor for 100 trials
		results[0] = avgAccuracy;
		// Set the second value of results to the overall percentage of times
		// the neighbors were unanimous
		results[1] = avgUnanimousPercent;
		// Set the third value of results to the overall percentage of times
		// the guesses were accurate when the neighbors were unanimous
		results[2] = avgUnanimousAccuracy;
		// Set the third value of results to the overall percentage of times
		// the guesses were accurate when the neighbors were not unanimous
		results[3] = avgNotUnanimousAccuracy;
		
		// Return the array of results
		return results;
	} // End of method Accuracy100Trials
	
	
	/**Imports a file and generates an array of Tumor objects based on the
	 * contents of the file
	 * @param fileName The name of the file to try opening
	 * @return The array of Tumors gotten from the file
	 * @throws IOException Throws this exception if the fileName is not good
	 * or there is some other problem with I/O; dealt with in the main methods
	 * that invoke this method
	 */
	public static Tumor[] Import(String fileName) throws IOException {
		// Creates a new File object based on the file name the user gives
		File inFile = new File(fileName);
		// Creates a new Scanner object to scan the File object
		Scanner fileScanner = new Scanner(inFile);
		// Sets a final int for the number of tumors to 569 because that's 
		// how many tumors are in the data file
		final int NUM_OF_TUMORS = 569;
				
		// Creates a new array for all the tumors with size equal to the number
		// of tumors
		Tumor[] tListIn = new Tumor[NUM_OF_TUMORS];
		
		// For loop to populate the array tListIn with the tumors from
		// the input file; runs until there are no more lines in the file
		for (int i = 0; fileScanner.hasNextLine(); i++) {
			// Makes a string of each line of the file, from the scanner 
			// fileScanner
			String line = fileScanner.nextLine();
			// Creates a new scanner to scan the strings made by the previous
			// line
			Scanner lineScanner = new Scanner(line);
			// Sets the delimiter of this scanner to a comma for separating 
			// the values in the input file
			lineScanner.useDelimiter(",");

			// Initializes an integer idNumber for the ID number of the tumor
			// obtained from the first item in the line returned by lineScanner
			int idNumber = lineScanner.nextInt();
			// Obtains a string (which should be "M" or "B") from the second 
			// item in the line returned by lineScanner
			String typeString = lineScanner.next();
			// Initializes a boolean type for the type of the tumor (M or B)
			boolean type;
			// Sets the boolean to true if the string obtained above is "M"...
			if (typeString.equals("M")) type = true;
			// ... or false if it's not.
			else type = false;

			// Creates a new array of doubles for the remaining 
			// characteristics of each tumor
			double[] characteristics = new double[30];
			
			// Fills the characteristics array with the rest of the doubles 
			// making up the Tumor's characteristics from lineScanner
			for (int j = 0; lineScanner.hasNextDouble(); j++) {
				characteristics[j] = lineScanner.nextDouble();
			}
			
			// Fills tListIn array with Tumor objects, which each have 
			// an int idNumber, boolean type (true for malignant, false
			// for benign) and array of 30 doubles for their other 
			// characteristics
			tListIn[i] = new Tumor(idNumber, type, characteristics);
		} // End of for loop creating tListIn array of tumors
		
		// Returns the array of Tumors that we just made from the input file
		return tListIn;
	} // End of import method

} // End of class
