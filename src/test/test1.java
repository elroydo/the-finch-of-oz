package test;
import edu.cmu.ri.createlab.terk.robot.finch.Finch; //importing the library package for the functionality of the finch
import java.util.Random; //importing the library package responsible for generating random values
import javax.swing.JOptionPane; //importing the UI library package

public class test2 {
	static Finch finch = new Finch(); //instantiating the finch robot, naming it "finch"
	
	static long timeBegan = System.currentTimeMillis(), modeTimeBegan;
	static int mode;
	
	public static void main(String[] args) throws InterruptedException {
		int curiousObjects = 0, scaredObjects = 0;
		
		String status = ""; //used throughout the program to determine which function is ran

		do {
			isFinchLevel();
			
			status = menu();
			if (status.equals("Any")) anyMode();
			if (status.equals("Curious")) curiousObjects = curiousMode();
			if (status.equals("Scaredy")) scaredObjects = scaredyMode();
			if (mode != 0) {log(curiousObjects, scaredObjects);};
		} while (!(status.equals("Quit")));
		
		quit();
	}
	
	private static String menu() {
		Object[] option = {"Any", "Curious", "Scaredy", "Quit"};
		String menuStatus = (String)JOptionPane.showInputDialog(null, "Is the Finch courageous or cowardly?\n------------------------------------------------------------\nWhich path will the Finch venture?\nYou decide:\n","The Finch of Oz", JOptionPane.PLAIN_MESSAGE, null, option, "Begin");
		if (menuStatus == null || menuStatus.length() == 0) menuStatus = "Quit";
		return(menuStatus);
	}
	
	private static void isFinchLevel() {
		while (!finch.isFinchLevel()) {
			JOptionPane.showMessageDialog(null, "\nPlease ensure the finch is level on the ground to begin the program.\n", "Finch Notice",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private static void anyMode() throws InterruptedException {
		mode = randomGen(0);
		
		if (mode == 1) {
			curiousMode();
		}else if (mode == 2){
			scaredyMode();
		}
	}
	
	private static int curiousMode() throws InterruptedException {
		int curiousObjects = 0;
		long timer;
		
		modeTimeBegan = System.currentTimeMillis();
		mode = 1;
		
		System.out.println("The Finch is curious . . .");
		
		finch.setWheelVelocities(randomGen(1), randomGen(1));
		finch.setLED(0, 0, 255);
		while (!finch.isBeakUp()) {
			timer = System.currentTimeMillis();
			while(!finch.isBeakUp() && (System.currentTimeMillis() - timer) <= 5000) {
				if(finch.isObstacleLeftSide() && finch.isObstacleRightSide()) {
					System.out.println("In front of me!");
					finch.setLED(0, 255, 0);
					finch.setWheelVelocities(65, 65);
					timer = System.currentTimeMillis();
					while(!finch.isBeakUp() && finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						curiousObjects++;
						Thread.sleep(1000);
						finch.setLED(0, 0, 255);
						finch.stopWheels();
					}
					System.out.println("Where did it go?");
					finch.setWheelVelocities(-100, 100, 1500);
				}else if(finch.isObstacleLeftSide()) {
					System.out.println("On my left!");
					finch.setLED(0, 255, 0);
					finch.setWheelVelocities(25, 75);
					timer = System.currentTimeMillis();
					while(!finch.isBeakUp() && finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						curiousObjects++;
						Thread.sleep(1000);
						finch.setLED(0, 0, 255);
						finch.stopWheels();
					}
					System.out.println("Where did it go?");
					finch.setWheelVelocities(-100, 100, 1500);
				}else if(finch.isObstacleRightSide()) {
					System.out.println("On my right!");
					finch.setLED(0, 255, 0);
					finch.setWheelVelocities(75, 25);
					timer = System.currentTimeMillis();
					while(!finch.isBeakUp() && finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						curiousObjects++;
						Thread.sleep(1000);
						finch.setLED(0, 0, 255);
						finch.stopWheels();
					}
					System.out.println("Where did it go?");
					finch.setWheelVelocities(100, -100, 1500);
				}else if ((System.currentTimeMillis() - timer) > 4000){
					Thread.sleep(1000);
					finch.setLED(0, 0, 255);
					finch.setWheelVelocities(randomGen(1), randomGen(1));
				}else {
					finch.setLED(0, 0, 255);
				}
			}
		}
		finch.setLED(0, 0, 0);
		finch.stopWheels();
		return curiousObjects;
	}
	
	private static int scaredyMode () {
		int scaredObjects = 0;
		
		modeTimeBegan = System.currentTimeMillis();
		mode = 2;
		
		System.out.println("The Finch is scared . . .");
		
		finch.setWheelVelocities(randomGen(1), randomGen(1));
		finch.setLED(0, 255, 0);
		while(!finch.isBeakUp()) {
			if(finch.isObstacleLeftSide() && finch.isObstacleRightSide()) {
				System.out.println("Nope.");
				finch.buzzBlocking(1000, 100);
				scaredObjects++;
				finch.setLED(255, 0, 0);
				finch.stopWheels();
				finch.setWheelVelocities(-100, 100, 1500);
				finch.setWheelVelocities(randomGen(1), randomGen(1), 3000);
			}else if(finch.isObstacleLeftSide()) {
				System.out.println("SAVE MY LEFT WING!");
				finch.buzzBlocking(1000, 100);
				scaredObjects++;
				finch.setLED(255, 0, 0);
				finch.stopWheels();
				finch.setWheelVelocities(100, -100, 1500);
				finch.setWheelVelocities(randomGen(1), randomGen(1), 3000);
			}else if(finch.isObstacleRightSide()) {
				System.out.println("SAVE MY RIGHT WING!");
				finch.buzzBlocking(1000, 100);
				scaredObjects++;
				finch.setLED(255, 0, 0);
				finch.stopWheels();
				finch.setWheelVelocities(-100, 100, 1500);
				finch.setWheelVelocities(randomGen(1), randomGen(1), 3000);
			}else {
				finch.setLED(0, 255, 0);
			}
		}
		finch.setLED(0, 0, 0);
		finch.stopWheels();
		return scaredObjects;
	}
	
	private static int randomGen(int context) {
		Random randNum = new Random();
		int minVelocity = 30, maxVelocity = 100;
		
		if (context == 0) {
			return randNum.nextInt(2)+1;
		}else if(context == 1) {
	        return randNum.nextInt(maxVelocity - minVelocity) + minVelocity;
		}
		return 0;
	}
	
	private static void log(int curiousObjects, int scaredObjects) throws InterruptedException {
		long timeEnd = System.currentTimeMillis(), totalElapsedTime = timeEnd - timeBegan, modeElapsedTime = timeEnd - modeTimeBegan;
		String logMessage = "", totalTime = String.format("%02d:%02d:%02d.%d", (totalElapsedTime/(1000*60*60))%24, (totalElapsedTime/(1000*60))%60, (totalElapsedTime/1000)%60, totalElapsedTime%1000), modeTime = String.format("%02d:%02d:%02d.%d", (modeElapsedTime/(1000*60*60))%24, (modeElapsedTime/(1000*60))%60, (modeElapsedTime/1000)%60, modeElapsedTime%1000);
		
		if(mode == 1) {
			logMessage += "Mode ran: Curious\n" + "Mode time: " + modeTime + "ms\nTotal time: " + totalTime + "ms\nObjects encountered = " + curiousObjects;
		}else if(mode == 2) {
			logMessage += "Mode ran: Scaredy\n" + "Mode time: " + modeTime + "ms\nTotal time: " + totalTime + "ms\nObjects avoided = " + scaredObjects;
		}
		
		int confirm = JOptionPane.showConfirmDialog(null, "Would you like to view the execution log?", "Log Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if (confirm == JOptionPane.YES_OPTION) {
			JOptionPane.showMessageDialog(null, logMessage + "\n");
		}else {
			mode = 0;
			main(null);
		}
		
		curiousObjects = 0;
		scaredObjects = 0;
		mode = 0;
	}
	
	private static void quit() throws InterruptedException {
		System.out.print("\nExiting");
		Thread.sleep(500);
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