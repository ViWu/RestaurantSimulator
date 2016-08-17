// CSE 331, Homework 3 (Restaurant)
// Instructor-provided code.
// You SHOULD heavily modify this file to make it interface with your own classes.
package restaurant;
import java.io.*;
import java.lang.*;
import java.util.*;
// import java.util.*;


/**
 * This class represents the text user interface (UI) for the restaurant
 * program, allowing the user to view and manage the restaurant and its objects.
 * 
 * @author Marty Stepp
 * @version Spring 2011 v1.0
 */
public class RestaurantTextUI {
	// file name from which to read the restaurant data
	private static final String DEFAULT_RESTAURANT_FILENAME = "tables.txt";
	Restaurant restaurant;
	WaitList waitlist;
	/**
	 * Constructs a new text user interface for managing a restaurant.
	 */
	public RestaurantTextUI() {
		System.out.println("CSE 331 Restaurant Simulator");

		// TODO: initialization code can go here
		restaurant = new Restaurant();
		waitlist = new WaitList();
		//crash("initialization code");
	}
	
	/**
	 * Reads the information about the restaurant from the default restaurant
	 * file.
	 * @return true if the data was read successfully; false if there were any errors
	 */
	public boolean readRestaurantData() {
		File restaurantFile = ValidInputReader.getValidFile(
				"File name for restaurant data [" + DEFAULT_RESTAURANT_FILENAME + "]?",
				DEFAULT_RESTAURANT_FILENAME);

		// TODO: read restaurant info from tables file;
		// return true if it was successful and false if not
		try{
			Scanner s = new Scanner(restaurantFile);
			String line = s.nextLine();
			ArrayList<Integer> al = new ArrayList<Integer>();
			int value,size,count=0;
			while(s.hasNext()){
				line = s.next();
				size=line.length();
				for(int i = 0; i < line.length(); i++) {
					value = line.charAt(i) - '0';
					al.add(value);
					value =al.get(i)*(int)Math.pow(10,size-1);
				    size--;
				}
				value=0;
				for(int i=al.size()-1;i>=0;i--){
					value += al.get(i)*(int)Math.pow(10,count);
					count++;
				}
				restaurant.tables.add(new Table(value));
				//System.out.println(value);
				count=0;
				al.clear();			 
			}
			//System.out.println();
			s.close();
			return true;
		}
		catch(IOException e){
			System.out.println("Unable to read restaurant data: file not found.");		
			return false;
		}
		//crash("read restaurant info from tables file: " + restaurantFile);
		// when there is an error reading the file,	
	}
	
	/**
	 * Displays the main menu of choices and prompts the user to enter a choice.
	 * Once a valid choice is made, initiates other code to handle that choice.
	 */
	public void mainMenu() {
		// main menu
		displayOptions();
		while (true) {
			String choice = ValidInputReader.getValidString(
					"Main menu, enter your choice:",
					"^[sSaAdDrRpPtTcCwWqQ!?]$").toUpperCase();
			if (choice.equals("S")) {
				serversOnDuty();
			} else if (choice.equals("A")) {
					addServer();
			} else if (choice.equals("D")) {
				dismissServer();
			} else if (choice.equals("R")) {
				cashRegister();
			} else if (choice.equals("P")) {
				partyToBeSeated();
			} else if (choice.equals("T")) {
				tableStatus();
			} else if (choice.equals("C")) {
				checkPlease();
			} else if (choice.equals("W")) {
				waitingList();
			} else if (choice.equals("Q")) {
				break;
			} else if (choice.equals("?")) {
				displayOptions();
			} else if (choice.equals("!")) {
				rickRoll();
			}
			System.out.println();
		}
	}
	
	// Displays the list of key commands the user can use.
	private void displayOptions() {
		System.out.println();
		System.out.println("Main System Menu");
		System.out.println("----------------");
		System.out.println("S)ervers on duty");
		System.out.println("A)dd server");
		System.out.println("D)ismiss server");
		System.out.println("R)egister");
		System.out.println("P)arty has arrived");
		System.out.println("T)ables status");
		System.out.println("C)heck, please");
		System.out.println("W)aiting list");
		System.out.println("?) display this menu of choices again");
		System.out.println("Q)uit");
	}
	
	// Called when S key is pressed from main menu.
	// Displays all servers who are currently working.
	private void serversOnDuty() {
		System.out.println("Servers currently on duty:");
		for(int i=0;i<restaurant.servers.size();i++){
			System.out.print("Server #"+restaurant.servers.get(i).number+" ($");
			 System.out.printf("%1$.2f", restaurant.servers.get(i).tip);
			 System.out.println(" in total tips)");
		}
		System.out.println("server count: "+restaurant.servers.size());
	}
	
	// Called when A key is pressed from main menu.
	// Adds one more server to the system.
	private void addServer() {
		System.out.println("Adding a new server to our workforce:");
		restaurant.servers.add(new Servers(restaurant.totalServers+1));
		System.out.println("server count: "+restaurant.servers.size());
		restaurant.totalServers++;
	}
	
	// Called when D key is pressed from main menu.
	// Sends one server home for the night (if possible).
	private void dismissServer() {
		// when there are no servers,
		if(restaurant.servers.size()==0){
			System.out.println("No servers to dismiss.");
			return;
		}
		// when only one server remains with tables remaining,
		if(restaurant.servers.size()==1 && restaurant.activeTables!= 0){
			System.out.println("Sorry, the server cannot cash out now;");
			System.out.println("there are still tables remaining and this is the only server left.");
		}
		// when the server is able to be dismissed,
		else{			
			if(restaurant.turn > restaurant.servers.size())
				restaurant.turn =1;
			int dismissed = restaurant.servers.get(restaurant.turn-1).number;
			System.out.println("Dismissing a server:");
			System.out.print("Server #"+(dismissed)+" cashes out with $");
			System.out.printf("%1$.2f", restaurant.servers.get(restaurant.turn-1).tip);
			System.out.println(" in total tips");
			restaurant.servers.remove(restaurant.turn-1);
			System.out.println("Servers now available: "+ restaurant.servers.size());
			//Now let other servers take over the tables
			for(int i=0;i<restaurant.tables.size();i++){
				if(restaurant.tables.get(i).open != true){
					if(restaurant.tables.get(i).server.number == dismissed){
						if(restaurant.turn > restaurant.servers.size())
							restaurant.turn =1;
						restaurant.tables.get(i).server = restaurant.servers.get(restaurant.turn-1);
						restaurant.turn++;
					}
				}
			}
		}
		// TODO: cash out server and display current count of servers
		// Server #2 cashes out with $47.95 in total tips.
		// Servers now available: 3
	}
	
	// Called when R key is pressed from main menu.
	// Displays how much money is in the restaurant's cash register.
	private void cashRegister() {
		System.out.println("Cash register:");
		System.out.print("Total money earned = $");
		System.out.printf("%.2f", restaurant.cashRegister);
		System.out.println();
	}
	
	// Called when T key is pressed from main menu.
	// Displays the current status of all tables.
	private void tableStatus() {
		System.out.println("Tables status:");	
		// TODO: show restaurant's table statuses, e.g.:
		Iterator<Table> it = restaurant.tables.iterator();
		int count=1;
		Table t;
		while(it.hasNext()){
			t=it.next();
			System.out.print("Table "+count+" ("+t.size+"-top): ");
			if(t.open==true)
				System.out.println("empty");
			else{
				System.out.println(t.party.name+" party of "+t.party.size+" - Server #"+t.server.number);
			}
			++count;
		}
	}
	
	// Called when C key is pressed from main menu.
	// Helps process a party's check to leave the restaurant.
	private void checkPlease() {
		boolean open = false;
		System.out.println("Send the check to a party that has finished eating:");
		String partyName = ValidInputReader.getValidString("Party's name?", "^[a-zA-Z '-]+$");		
		// when such a party is sitting at a table in the restaurant,
		for(int i=0;i<restaurant.tables.size();i++){
			if(restaurant.tables.get(i).open != true){
				if(partyName.equalsIgnoreCase(restaurant.tables.get(i).party.name)){
					double subtotal = ValidInputReader.getValidDouble("Bill subtotal?", 0.0, 9999.99);
					double tip = ValidInputReader.getValidDouble("Tip?", 0.0, 9999.99);
					restaurant.tables.get(i).server.tip += tip;
					restaurant.cashRegister += (subtotal*1.1);
					System.out.print("Gave tip of $");
					System.out.printf("%.2f", tip);
					System.out.println(" to Server #"+restaurant.tables.get(i).server.number);
					System.out.print("Gave a total of $");
					System.out.printf("%.2f", (subtotal*1.1));
					System.out.println(" to the cash register");
					restaurant.tables.get(i).server = null;					//party leaves table after paying check
					restaurant.tables.get(i).open = true;
					restaurant.tables.get(i).party=null;
					restaurant.activeTables--;
					open = true;
				}
			}			
		}
		if(open){
			System.out.println("Seating from waiting list:");		//seat from waiting list
			for(int j=0;j < waitlist.parties.size();j++){
				for(int k=0;k<restaurant.tables.size();k++){
					if(restaurant.tables.get(k).open==true && restaurant.tables.get(k).size >= waitlist.parties.get(j).size ){
						if(restaurant.turn > restaurant.servers.size())
							restaurant.turn =1;
						restaurant.tables.get(k).party = new Party(waitlist.parties.get(j).name, waitlist.parties.get(j).size);
						restaurant.tables.get(k).open=false;
						restaurant.tables.get(k).server = restaurant.servers.get(restaurant.turn-1);	
						restaurant.turn++;
						restaurant.activeTables++;
						waitlist.parties.remove(waitlist.parties.get(j));
						return;
					}					
				}
			}
		}
		// when such a party is NOT sitting at a table in the restaurant,
		if(!open)
			System.out.println("There is no party by that name.");
	}
	
	// Called when W key is pressed from main menu.
	// Displays the current waiting list, if any.
	private void waitingList() {
		System.out.println("Waiting list:");
		for(int i=0;i<waitlist.parties.size();i++)
			System.out.println(waitlist.parties.get(i).name+" party of " + waitlist.parties.get(i).size);
		// when there is nobody on the waiting list,
		if(waitlist.parties.size()==0)
			System.out.println("empty");
	}
	
	// Called when P key is pressed from main menu.
	// Helps seat a newly arriving party at a table in the restaurant.
	private void partyToBeSeated() {
		String partyName="";
		int partySize=0;
		boolean pass=false;
		Table bestTable= new Table(99999);
		int trackIndex=0;
		// when there are no servers,
		if(restaurant.servers.size()==0){
			System.out.println("Sorry, there are no servers here yet to seat this party");
			System.out.println("and take their orders.  Add servers and try again.");
			return;
		}
		// when there is at least one server,
		else{
				pass=false;
				while(pass != true){
					partyName = ValidInputReader.getValidString("Party's name?", "^[a-zA-Z '-]+$");
					partySize = ValidInputReader.getValidInt("How many people in the party?", 1, 99999);
					pass= true;
			// when a duplicate party name is found,
					for(int i=0;i<restaurant.tables.size();i++){
						if(restaurant.tables.get(i).open != true){
							if(restaurant.tables.get(i).party.name.equalsIgnoreCase(partyName)){
								System.out.println("We already have a party with that name in the restaurant.");
								System.out.println("Please try again with a unique party name.");
								pass=false;
							}
						}
					}
			}
		}
		// TODO: try to seat this party
		
		// when the restaurant doesn't have any tables big enough to ever seat this party,
		pass=false;
		for(int i=0;i<restaurant.tables.size();i++){			
			if(restaurant.tables.get(i).size>=partySize){
				pass=true;
				break;
			}
		}
		if(!pass){
			System.out.println("Sorry, the restaurant is unable to seat a party of this size.");
			return;
		}
		// when all tables large enough to accommodate this party are taken,
		pass=false;
		for(int i=0;i<restaurant.tables.size();i++){			
			if(restaurant.tables.get(i).size>=partySize && restaurant.tables.get(i).open==true){
				if(restaurant.tables.get(i).size<bestTable.size){
					bestTable=restaurant.tables.get(i);
					trackIndex=i;
					pass=true;
				}
			}
		}		
		if(restaurant.turn > restaurant.servers.size())
			restaurant.turn =1;
		if(pass){
			restaurant.tables.get(trackIndex).party = new Party(partyName, partySize);
			restaurant.tables.get(trackIndex).open=false;
			restaurant.tables.get(trackIndex).server = restaurant.servers.get(restaurant.turn-1);	
			restaurant.turn++;
			restaurant.activeTables++;
			}
		if(!pass){		
			System.out.println("Sorry, there is no open table that can seat this party now.");
			boolean wait = ValidInputReader.getYesNo("Place this party onto the waiting list? (y/n)");
			if(wait == true)
				waitlist.parties.add(new Party(partyName, partySize));
			// TODO: put this party on the waiting list
		}
	}
	
	
	// You know what this method does.
	private void rickRoll() {
		// TODO: tell you how I'm feeling; make you understand
		System.out.println("We're no strangers to love");
		System.out.println("You know the rules and so do I");
		System.out.println("A full commitment's what I'm thinking of");
		System.out.println("You wouldn't get this from any other guy");
		System.out.println("I just wanna tell you how I'm feeling");
		System.out.println("Gotta make you understand");
		System.out.println();
		System.out.println("Never gonna give you up");
		System.out.println("Never gonna let you down");
		System.out.println("Never gonna run around and desert you");
		System.out.println("Never gonna make you cry");
		System.out.println("Never gonna say goodbye");
		System.out.println("Never gonna tell a lie and hurt you");
	}
	
	// This helper is just put into the text UI code to mark places where you
	// will need to add or modify this file.  Crashes with a runtime exception.
	private void crash(String message) {
		// Math.random() < 10 will always be true;  so why is it there?
		// I can't just throw because Eclipse will then warn about dead code
		// for any code that occurs after a call to crash().
		// So I wrap the exception throw in an "opaque predicate" to fool it.
		if (Math.random() < 10) {
			throw new RuntimeException("Not yet implemented: " + message);
		}
	}
}