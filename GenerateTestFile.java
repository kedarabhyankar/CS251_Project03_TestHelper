import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 * Custom test file generation for CS25100-Spring2020 Project 03
 *
 * @author kedarabhyankar
 * @version 3/14/2020
 */
public class GenerateTestFile {

	@SuppressWarnings("FieldCanBeLocal")
	private static final int MAXIMUM_INPUT_GENERATION = (int) 1e6;
	private static final int MAXIMUM_PRODUCT_NAME_LENGTH = 6;
	private static final int NUM_DEPARTMENT_RATINGS = 4;
	private static final int MAXIMUM_DEPARTMENT_RATING = 9;
	private static final int MINIMUM_DEPARTMENT_RATING = 1;
	private static final String[] vendors = {"apple", "apricot", "avocado", "banana", "berry", "cantaloupe",
			"cherry", "citron", "citrus", "coconut", "date", "fig", "grape", "guava", "kiwi", "lemon",
			"lime", "mango", "melon", "mulberry", "nectarine", "orange", "papaya", "peach", "pear",
			"pineapple", "plum", "prune", "raisin", "raspberry", "tangerine"};
	private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String FILENAME_TO_GENERATE = "custom_test.txt";
	private static String[] ratings;


	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		ratings = new String[NUM_DEPARTMENT_RATINGS];
		fillRatings();
		System.out.println("How many products would you like to generate?");
		String numToGenerate = scan.nextLine();
		if (verifyNumberWithinBounds(numToGenerate)) {
			processTestGeneration(Integer.parseInt(numToGenerate));
			System.out.println("Products were generated and have been output to a file called " + FILENAME_TO_GENERATE);
		} else {
			System.out.println("error, retry.");
		}
	}

	/**
	 * Verifies a given number is between 1 and a specific constant upper bound defined as {@code
	 * MAXIMUM_INPUT_GENERATION}. Takes a String, parses it, validates it, and returns the validity of the String
	 * numeral being in bounds.
	 *
	 * @param num the number to verify, as a String
	 * @return whether the String passed in constitutes as a number betwen specified bounds.
	 */
	private static boolean verifyNumberWithinBounds(String num) {
		try {
			Integer.parseInt(num);
		} catch (NumberFormatException E) {
			System.out.println("NaN!");
			return false;
		}

		int n = Integer.parseInt(num);
		if (n < 1) {
			System.out.println("Too little input!");
			return false;
		} else if (n > MAXIMUM_INPUT_GENERATION) {
			System.out.println();
			return false;
		}

		return true;
	}


	/**
	 * Core of the code - generates inputs, adds to an arraylist, then prints the array list to a file.
	 *
	 * @param numInputs the number of inputs to output to the file
	 */
	private static void processTestGeneration(int numInputs) {
		File outFile = new File(FILENAME_TO_GENERATE);
		ArrayList<String> output = new ArrayList<>();
		for (int i = 0; i < numInputs; i++) {
			output.add(generateInput());
		}

		output.add(0, "Name, Vendor, Price, Department Rate");
		try {
			Files.write(outFile.toPath(), output, Charset.defaultCharset());
		} catch (IOException e) {
			System.out.println("Error in writing file, check permissions");
			e.printStackTrace();
		}
	}

	/**
	 * Creates products that are output to the file using helper methods
	 *
	 * @return a single product-representative String
	 */
	private static String generateInput() {
		String productName = generateProductName();
		String vendorName = pickVendorName();
		double price = generatePrice();
		String[] ratings = generateRatings();

		StringBuilder sb = new StringBuilder();
		sb.append(productName);
		sb.append("; ");
		sb.append(vendorName);
		sb.append("; ");
		sb.append("$");
		sb.append(String.format("%.2f", price));
		sb.append("; ");
		sb.append("[");
		for (int i = 0; i < ratings.length; i++) {
			sb.append(ratings[i]);
			if (i != ratings.length - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Generates a pseudo-random name from the alphabet, to a specific number of digits
	 *
	 * @return the pseudo-random product name generated
	 */
	private static String generateProductName() {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < MAXIMUM_PRODUCT_NAME_LENGTH; i++) {
			sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		}

		return sb.toString();
	}

	/**
	 * Picks a pseudo random vendor name from the vendor array and returns it
	 *
	 * @return the pseudo-randomly picked name from the vendor array
	 */
	private static String pickVendorName() {
		Random rand = new Random();
		return vendors[rand.nextInt(vendors.length)];
	}

	/**
	 * Generates a price for the product between 100 and 1000 both inclusive.
	 *
	 * @return the pseudo-randomly generated product price
	 */
	private static double generatePrice() {
		Random rand = new Random();
		double dollar = rand.nextInt((900) + 1) + 100;
		double cent = rand.nextInt(100) / 100.0;
		assert Double.compare(cent, 1.0) < 0 && Double.compare(0.0, cent) > 0;
		return dollar + cent;
	}

	/**
	 * Generates a String array of ratings. The number of ratings is defined in {@code NUM_DEPARTMENT_RATINGS}, and the
	 * maximum and minimum ratings are defined in {@code MAXIMUM_DEPARTMENT_RATING} and {@code
	 * MINIMUM_DEPARTMENT_RATING} respectively.
	 *
	 * @return The String array representative of the pseudo-randomly generated product department ratings.
	 */
	private static String[] generateRatings() {
		ArrayList<String> preShuffle = new ArrayList<>();
		//noinspection ManualArrayToCollectionCopy
		for (String rating : ratings) {
			//noinspection UseBulkOperation
			preShuffle.add(rating);
		}
		Collections.shuffle(preShuffle);
		for (int i = 0; i < ratings.length; i++) {
			ratings[i] = preShuffle.get(i);
		}
		return ratings;
	}


	/**
	 * Fills the ratings array once, with a randomized input - the input will be shuffled on each product's generation,
	 * but the original input will be generated once, at the start of the program's execution.
	 * <p>
	 * Added based on Anonymous Comp's comment, accessible on Piazza @503, comment _f1
	 */
	private static void fillRatings() {
		Random rand = new Random();
		for (int i = 0; i < ratings.length; i++) {
			ratings[i] = Integer.toString(
					rand.nextInt((MAXIMUM_DEPARTMENT_RATING - MINIMUM_DEPARTMENT_RATING) + 1) +
							MINIMUM_DEPARTMENT_RATING);
		}
	}


}
