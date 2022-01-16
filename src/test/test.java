package test;

import java.util.Random;

import edu.cmu.ri.createlab.terk.robot.finch.Finch;

public class test1 {
	static public void main(String args[]) throws InterruptedException {
		Finch finch = new Finch();
		
		int curiousObjects = 0;
		long timer = System.currentTimeMillis();

		System.out.println("The Finch is curious . . .");
		
		while(!finch.isBeakUp()) {
			timer = System.currentTimeMillis();
			
			finch.setLED(0, 0, 255);
			finch.setWheelVelocities(randomGen(), randomGen());
			
			while(!finch.isBeakUp() && (System.currentTimeMillis() - timer) <= 5000) {
				if(finch.isObstacleLeftSide() && finch.isObstacleRightSide()) {
					System.out.println("Ahead of me!");
					while(finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						timer = System.currentTimeMillis();
						finch.setLED(0, 0, 255);
						finch.stopWheels();
					}
					finch.setLED(0, 255, 0);
					finch.setWheelVelocities(70, 70);
				}else if(finch.isObstacleLeftSide()) {
					System.out.println("On my left!");
					while(finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						timer = System.currentTimeMillis();
						finch.setLED(0, 0, 255);
						finch.stopWheels();
					}
					finch.setLED(0, 255, 0);
					finch.setWheelVelocities(50, 70);
				}else if(finch.isObstacleRightSide()) {
					System.out.println("On my right!");
					while(finch.isObstacle() && (System.currentTimeMillis() - timer) <= 5000) {
						timer = System.currentTimeMillis();
						finch.setLED(0, 0, 255);
						finch.stopWheels();
					}
					finch.setLED(0, 255, 0);
					finch.setWheelVelocities(70, 50);
				}
			}
			System.out.println("Where did it go?");
			curiousObjects++;
			finch.stopWheels();
			Thread.sleep(1000);
		}
		
		System.out.println("FINISHED. Objects = " + curiousObjects);
		finch.setLED(0, 0, 0);
		finch.stopWheels();
	}
	
	private static int randomGen() {
		Random randNum = new Random();
		int minVelocity = 30, maxVelocity = 100;

	    return randNum.nextInt(maxVelocity - minVelocity) + minVelocity;
	}
}
