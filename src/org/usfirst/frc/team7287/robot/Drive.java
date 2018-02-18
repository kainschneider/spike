package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Drive {
	private DifferentialDrive robot;
	private boolean shouldRamp;
	Drive(DifferentialDrive robot, boolean shouldRamp) {
		this.robot = robot;
		this.shouldRamp = shouldRamp;
	}
	public void forward(double speed) {
		System.out.println("Forward with speed " + speed);
		robot.tankDrive(speed, speed, shouldRamp);
	}
	public void stop() {
		System.out.println("Stop nuff said");
		robot.tankDrive(0.0, 0.0, shouldRamp);
	}
	
	public void reverse(double speed) {
		System.out.println("Reverse with speed " + speed);
		robot.tankDrive(-speed, -speed, shouldRamp);
	}
	
	public void turn(String direction, double speed) {
		System.out.println("Turn with direction "+ direction + "and speed " + speed);
		switch(direction.toLowerCase()) {
		case "right": 
			robot.tankDrive(speed, -speed, shouldRamp);
		case "left":
			robot.tankDrive(-speed, speed, shouldRamp);
		default:
		}
	}
}
