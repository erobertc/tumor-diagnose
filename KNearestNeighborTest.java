// Importing classes for Scanner and IO (to read file)
import java.io.*;
import java.util.Scanner;

/**
 * This class runs the k-nearest neighbor algorithm using three values for k:
 * 3, 5, and 7 for the three, five, and seven nearest neighbors. It runs 100 
 * times for each value of k on test data obtained from NearestNeighbor.import
 * on a file, using 80% of the data each time as training data and the remaining 20%
 * to check accuracy, and reports the accuracy for each value of k.
 */
public class KNearestNeighborTest {

	/**
	 * @param args The command line arguments for the method (there should be
	 * none)
	 * @throws IOException This exception gets thrown by the call to 
	 * NearestNeighbor.import if the file name passed to it is not good.
	 */
	public static void main(String[] args) throws IOException {
		// Asks user for a file name
		System.out.println("Please provide a file name for the data file to " +
				"use.");
		// Creates a new scanner to read the file name
		Scanner keyboard = new Scanner(System.in);
		// Reads the file name input by the user
		String fileName = keyboard.next();

		// Sets a boolean for whether or not to try again in asking for the 
		// file name to true so we will enter the while loop
		boolean again = true;
		// While loop so we can try again if there is an exception
		while (again) {
			// Try-catch block for importing the file and catching exception
			// if the file name is bad
			try {
				// Tells the user it is importing the file
				System.out.println("Importing file...");
				// Runs the Import method from the NearestNeighbor class with
				// the fileName provided by user. If there is an exception here
				// it will jump to the catch block and ask the user to try 
				// again. 
				Tumor[] tListIn = NearestNeighbor.Import(fileName);
				
				// For loop to try multiple values of k (nearest neighbors)
				for (int numNeighbors=3; numNeighbors <= 7; numNeighbors += 2)
				{
					// Tells the user it is testing for the nearest 
					// numNeighbors neighbors (will be 3, 5, and 7)
					System.out.println("Testing accuracy for the nearest " +
							numNeighbors + " neighbors (k = " + numNeighbors +
							")...");
					// Stores the results array of the Accuracy100Trials method
					// in a creatively named new array of doubles
					double[] results = 
						NearestNeighbor.Accuracy100Trials(tListIn, 
								numNeighbors);
					// Prints the overall accuracy of the algorithm for this
					// value of k
					System.out.println("Nearest Neighbor with nearest " +
							numNeighbors + " neighbors is on average " + 
							results[0] + "% accurate.");
					// Prints how much of the time the neighbors were unanimous
					System.out.println("The nearest " + numNeighbors + 
							" neighbors were unanimous " + results[1] + 
							"% of the time.");
					// Prints the accuracy of the algorithm when the neighbors
					// were unanimous
					System.out.println("When they were unanimous, the " +
							"algorithm was " + results[2] + "% accurate; ");
					// Prints the accuracy of the algorithm when the neighbors
					// were not unanimous
					System.out.println("when they were not unanimous, it " +
							"was " + results[3] + "% accurate.\n");
				} // End of for loop to try multiple values of k
				// Sets again to false since we made it through the program 
				// without an exception and we don't want to try again
				again = false;
			} // End of try block
			
			// If there is an exception, asks user to try again
			catch(IOException e){
				// Asks user for new file name
				System.out.println("Please try again with correct input " +
						"file name.");
				// Stores user's new input as fileName
				fileName = keyboard.next();
				} // End of catch block; will jump back to beginning of try
				  // block because we were in a while loop
		} // End of while loop
	} // End of main method
} // End of class