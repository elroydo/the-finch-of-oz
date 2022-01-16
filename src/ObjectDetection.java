import edu.cmu.ri.createlab.terk.robot.finch.Finch; //Finch functionality 
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane; 
import javax.swing.JScrollPane; 
import javax.swing.JTextArea; 

public class objectDetection {
	static Finch finch = new Finch(); //instantiating the Finch robot as "finch"
	
	static long timeBegan = System.currentTimeMillis(), modeTimeBegan; //timeBegan initialised to current system time, and modeTimeBegan is declared
	static int mode;
	
	public static void main(String[] args) throws InterruptedException {
		int objects = 0; 
		
		String status = ""; //used throughout the program to determine which option/function is ran
		
		do {
			isFinchLevel(); //run isFinchLevel function to determine whether the Finch robot is level or not
			
			status = menu(); //status is assigned the value returned from the menu function
			if (status.equals("Any")) objects = anyMode(); //if status equals "Any", the anyMode function is ran and its return value is assigned to objects
			if (status.equals("Curious")) objects = curiousMode(); //if status equals "Curious, the curiousMode function is ran and its return value is assigned to objects
			if (status.equals("Scaredy")) objects = scaredyMode(); //if status equals "Scaredy", the scaredymode function is ran and its return value is assigned to objects
			if (status.equals("Help")) help(); //if status equals "Help", the help function is ran
			if (mode != 0) {log(objects);}; //if mode does not equal to 0, the log function is ran with objects passed through as a parameter
		} while (!(status.equals("Quit"))); //once the block of code is ran above, check whether status does not equal "Quit" to continue looping, else exit loop
		
		quit(); //run the quit function
	}
	
	private static String menu() {
		ImageIcon icon = new ImageIcon("src\\images\\finch.png"); //image loaded as an icon from the project's local directory to be used in the GUI interface
		Object[] option = {"Any", "Curious", "Scaredy", "Help", "Quit"}; //several objects are assigned to the option array, i.e., selectable options from the drop down menu
		//the GUI interface itself with all the components loaded, where the option selected is initialised to the menuStatus String variable
		String menuStatus = (String)JOptionPane.showInputDialog(null, "The test of courage or cowardice!\n\nWhat will the finch encounter on \nthe yellow brick road?\n\nYou decide:\n","The Finch of Oz", JOptionPane.PLAIN_MESSAGE, icon, option, "Any"); 
		if (menuStatus == null || menuStatus.length() == 0) menuStatus = "Quit"; //if menuStatus equates to null or the length to 0, then menuStatus is set to "Quit"
		return(menuStatus); //the menuStatus variable is returned to the main function
	}
	
	private static void isFinchLevel() {
		while (!finch.isFinchLevel()) { //while the Finch is not level, run this loop
			 //warning dialog message to the user prompting the to ensure the Finch is level on the ground
			JOptionPane.showMessageDialog(null, "\nPlease ensure the finch is level on the ground to begin the program.\nThen click 'OK' to continue.", "Finch Notice",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private static int anyMode() throws InterruptedException {
		mode = randomGen(0); //assign the returned value from the randomGen function with 0 as a parameter to mode
		
		if (mode == 1) { //if mode equals 1
			return curiousMode(); //run the curiousMode and return
		}else if (mode == 2){ //if mode equals 2
			return scaredyMode(); //run the scaredyMode and return
		}
		return 0; //else return 0
	}
	
	private static int curiousMode() throws InterruptedException {
		int curiousObjects = 0; 
		Boolean found = false; 
		long timer; 
		
		modeTimeBegan = System.currentTimeMillis(); //assign the current system time to modeTimeBegan
		mode = 1; 
		
		System.out.println("The Finch is curious . . ."); 
		
		while(!finch.isBeakUp()) { //while the finch beak is not up
			timer = System.currentTimeMillis(); //set timer to current system time
			finch.setLED(0, 255, 0); //set LED colour
			finch.setWheelVelocities(randomGen(1), randomGen(1)); //set random wheel velocities for Finch
			while(!finch.isBeakUp() && (System.currentTimeMillis() - timer) <= 5000) { //while beak is not up and timer is less than 5 seconds
				if(finch.isObstacleLeftSide() && finch.isObstacleRightSide()) { //if the left and right sensors detect and object (in front)
					System.out.println("Ahead of me!"); 
					found = true;
					//while the beak is not up, the Finch detects an object, and the timer is less than 5 seconds
					while(!finch.isBeakUp() && finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						timer = System.currentTimeMillis(); 
						finch.setLED(0, 255, 0); 
						finch.stopWheels(); //stop the Finch wheels
					}
					finch.setLED(0, 0, 255); 
					finch.setWheelVelocities(70, 70); //set wheel velocities
				}else if(finch.isObstacleLeftSide()) { //if left sensor detects an object
					System.out.println("On my left!"); 
					found = true; 
					
					while(!finch.isBeakUp() && finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						timer = System.currentTimeMillis(); 
						finch.setLED(0, 255, 0); 
						finch.stopWheels(); 
					}
					finch.setLED(0, 0, 255); 
					finch.setWheelVelocities(50, 70); 
				}else if(finch.isObstacleRightSide()) { //if the right sensor detects an object
					System.out.println("On my right!"); 
					found = true; 
					
					while(!finch.isBeakUp() && finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						timer = System.currentTimeMillis(); 
						finch.setLED(0, 255, 0); 
						finch.stopWheels(); 
					}
					finch.setLED(0, 0, 255); 
					finch.setWheelVelocities(70, 50); 
				}
			}
			if(found) {
				System.out.println("Where did it go?"); 
				curiousObjects++; //increment curiousObjects
				System.out.println("Hmm . . . " + curiousObjects + " object(s) so far."); 
				found = false; 
				finch.stopWheels(); //stop the wheels
				finch.sleep(1000); //put the Finch to sleep for 1 second
			}
		}
		finch.setLED(0, 0, 0); //turn off the LED
		finch.stopWheels(); 
		return curiousObjects; //return the value of curiousObjects 
	}
	
	private static int scaredyMode () {
		int scaredObjects = 0; //declare and initialise scaredObjects as integer to 0
		
		modeTimeBegan = System.currentTimeMillis(); //set current system time to modeTimeBegan
		mode = 2; //set mode to 2
		
		System.out.println("The Finch is scared . . ."); 
		
		finch.setWheelVelocities(randomGen(1), randomGen(1)); //set random wheel velocities
		finch.setLED(0, 255, 0); 
		while(!finch.isBeakUp()) { //while the beak is not up
			if(finch.isObstacleLeftSide() && finch.isObstacleRightSide()) { //if the left and right sensors detect an object (in front)
				System.out.println("Nope."); 
				finch.buzzBlocking(10000, 100); //beep at a frequency of 10000 for 100 milliseconds
				scaredObjects++; //increment scaredObjects
				System.out.println("That makes " + scaredObjects + " object(s)!"); 
				finch.setLED(255, 0, 0); //set LED to red
				finch.stopWheels(); 
				finch.setWheelVelocities(-100, -100, 1000); 
				finch.setWheelVelocities(150, -150, 1500); 
				finch.setWheelVelocities(100, 100, 3000); 
				finch.setWheelVelocities(randomGen(1), randomGen(1)); //set random wheel velocities
			}else if(finch.isObstacleLeftSide()) { //if an object is detected in the left sensor
				System.out.println("SAVE MY LEFT WING!"); 
				finch.buzzBlocking(10000, 100); //beep at a frequency of 10000 for 100 milliseconds
				scaredObjects++; //increment scaredObjects
				System.out.println("That makes " + scaredObjects + " object(s)!"); 
				finch.setLED(255, 0, 0); 
				finch.stopWheels(); 
				finch.setWheelVelocities(-100, -100, 1000); 
				finch.setWheelVelocities(150, -150, 1500); 
				finch.setWheelVelocities(100, 100, 3000); 
				finch.setWheelVelocities(randomGen(1), randomGen(1)); //set random wheel velocities
			}else if(finch.isObstacleRightSide()) { //if an object is detected in the right sensor
				System.out.println("SAVE MY RIGHT WING!"); 
				finch.buzzBlocking(10000, 100); //beep at a frequency of 10000 for 100 milliseconds
				scaredObjects++; //increment scaredObjects
				System.out.println("That makes " + scaredObjects + " object(s)!"); 
				finch.setLED(255, 0, 0); 
				finch.stopWheels();
				finch.setWheelVelocities(-100, -100, 1000); 
				finch.setWheelVelocities(-150, 150, 1500); 
				finch.setWheelVelocities(100, 100, 3000); 
				finch.setWheelVelocities(randomGen(1), randomGen(1)); //set random wheel velocities
			}else { //if all other conditions fail to meet
				finch.setLED(0, 255, 0); //set the LED to green
			}
		}
		finch.setLED(0, 0, 0); //turn off the LED
		finch.stopWheels(); 
		return scaredObjects; //return the number of objects encountered during the execution of this mode
	}
	
	private static int randomGen(int context) {
		Random randNum = new Random();
		int minVelocity = 50, maxVelocity = 100; //minVelocity to 50 and maxVelocity to 100
		
		if (context == 0) { //if parameter equals 0
			return randNum.nextInt(2)+1; //compute a random number between 1 and 2 and return the value
		}else if(context == 1) { //if parameter equals 1
	        return randNum.nextInt(maxVelocity - minVelocity) + minVelocity; //compute and return a random velocity value based on this formula
		}
		return 0; 
	}
	
	private static void help() {
		JTextArea helpText = new JTextArea(15, 40); //new TextArea object is created and assigned the dimension of 15x40
		helpText.setText("-Detect Object-\n"
				+ "--------------------------------------------------------------------------------------------------------------\n"
				+ "Please ensure the Finch is connected and level to achieve this application's full functionality.\n\n"
				+ "--------------------------------------------------------------------------------------------------------------\n"
				+ "-Main screen-\n"
				+ "--------------------------------------------------------------------------------------------------------------\n"
				+ "To begin, select a mode from the dropdown menu, including:\n"
				+ "1) Any - Run a random mode.\n"
				+ "2) Curious - Run the Curious mode; the Finch will actively look for objects.\n"
				+ "3) Scaredy - Run the Scaredy mode; the Finch will move away from objects. \n"
				+ "4) Help - Display this help screen.\n"
				+ "5) Quit - Exit the program.\n"
				+ "Once an option has been selected, proceed with the 'OK' button, else you can exit via the 'Cancel' button.\n\n"
				+ "--------------------------------------------------------------------------------------------------------------\n"
				+ "-Log-\n"
				+ "--------------------------------------------------------------------------------------------------------------\n"
				+ "After a mode has completed execution, a prompt to display the log will appear, click 'Yes' or 'No' to continue.\n\n"
				+ "--------------------------------------------------------------------------------------------------------------\n"
				+ "Click 'OK' to continue.");
		//help text set to JavaTextArea help text object
		
		helpText.setWrapStyleWord(true); //prevents wrapping of letters and instead wraps entire words in given dimensions
		helpText.setLineWrap(true); //lines are wrapped according to the dimensions of the TextArea box
		helpText.setEditable(false); //prevents the help text from being edited
		
		JOptionPane.showMessageDialog(null, new JScrollPane(helpText), "Help", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static void log(int objects) throws InterruptedException {
		long timeEnd = System.currentTimeMillis(), totalElapsedTime = timeEnd - timeBegan, modeElapsedTime = timeEnd - modeTimeBegan;
		String logMessage = "", totalTime = "", modeTime = "";
		
		totalTime = String.format("%02d:%02d.%d", (totalElapsedTime/(1000*60))%60, (totalElapsedTime/1000)%60, totalElapsedTime%1000); //convert totalElapsedTime to minutes, seconds, and milliseconds and assign the formatted values to totalTime
		modeTime = String.format("%02d:%02d.%d", (modeElapsedTime/(1000*60))%60, (modeElapsedTime/1000)%60, modeElapsedTime%1000); //convert modeElapsedTime to minutes, seconds, and milliseconds and assign the formatted values to modeTime
		
		if(mode == 1) { //if the mode equals 1
			logMessage += "Mode ran: Curious\n" + "Mode time: " + modeTime + "ms\nTotal time: " + totalTime + "ms\nObjects encountered = " + objects; //modeTime, totalTime, and objects to logMessage
		}else if(mode == 2) { //if the mode equals 2
			logMessage += "Mode ran: Scaredy\n" + "Mode time: " + modeTime + "ms\nTotal time: " + totalTime + "ms\nObjects avoided = " + objects; //modeTime, totalTime, and objects to logMessage
		}
		
		int confirm = JOptionPane.showConfirmDialog(null, "Would you like to view the execution log?", "Log Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); //display a prompt to the user querying whether they would like to display the execution log or not, and assign the input to confirm, declared as an int
		
		if (confirm == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(null, logMessage + "\n\nClick OK to continue.\n"); //output execution log
		}
		
		objects = 0; 
		mode = 0;
	}
	
	//terminate
	private static void quit() throws InterruptedException {
		System.out.print("\nExiting"); 
		Thread.sleep(500); //sleep for 500 milliseconds
		System.out.print(" ."); 
		Thread.sleep(500); 
		System.out.print(" ."); 
		Thread.sleep(500); 
		System.out.print(" ."); 
		Thread.sleep(500); 
		System.out.println("\nGoodbye!"); 
		
		finch.quit();
		System.exit(0); 
	}
}