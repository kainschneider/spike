package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

//		Drive script to move the robot and log it's speed and movement
public class Drive {
	private DifferentialDrive robot;
	private boolean shouldRamp;
	Drive(DifferentialDrive robot, boolean shouldRamp) {
		this.robot = robot;
		this.shouldRamp = shouldRamp;
	}
	public void forward(double speed) {
		robot.tankDrive(speed, speed, shouldRamp);
	}
	public void stop() {
		robot.tankDrive(0.0, 0.0, shouldRamp);
	}
	
	public void reverse(double speed) {
		robot.tankDrive(-speed, -speed, shouldRamp);
	}
	
	public void turn(String direction, double speed) {
		switch(direction.toLowerCase()) {
		case "right": 
			robot.tankDrive(speed, -speed, shouldRamp);
			break;
		case "left":
			robot.tankDrive(-speed, speed, shouldRamp);
			break;
		default:
		}
	}
	public void manualTurning(double speedL, double speedR) {
		robot.tankDrive(speedL,speedR,shouldRamp);
	}
	// k = ratio beteen speed of inside wheel to  the speed of the outside wheel insideWheelSpeed = k*outsideWheelSpeed
	double k = 0.75;
	public void smoothTurning(double speedOfOutside) {
		robot.tankDrive(speedOfOutside,  speedOfOutside*k, shouldRamp);
		}
}
