// CSE 331, Homework 3 (Restaurant)
// Instructor-provided code; do not modify.
package restaurant;
/**
 * This class contains the main method to run the overall program.
 * 
 * @author Marty Stepp
 * @version Spring 2011 v1.0
 */
public class RestaurantMain {
	public static void main(String[] args) {
		// read restaurant data and then show main menu
		RestaurantTextUI textUI = new RestaurantTextUI();
		if (textUI.readRestaurantData()) {
			textUI.mainMenu();
			System.out.println();
			System.out.println("Goodbye!");
		} else {
			System.out.println("Exiting.");
		}
	}
}