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
		case "left":
			robot.tankDrive(-speed, speed, shouldRamp);
		default:
		}
	}
	public void manualTurning(double speedL, double speedR) {
		robot.tankDrive(speedL,speedR,shouldRamp);
	}
	public void smoothTurning(double speedOfLeftSide) {
		//OKAY, I know this looks odd so let me explain
		//It means that the power is divided up between the sides,
		//so the speed you input wil go to the left side and what ever is left over
		//will go to the other side
		robot.tankDrive(speedOfLeftSide, 1 - speedOfLeftSide, shouldRamp);
		}
	}
