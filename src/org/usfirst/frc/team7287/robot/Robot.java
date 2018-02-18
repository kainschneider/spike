/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends IterativeRobot {
	private DifferentialDrive m_myRobot;
	private Joystick m_leftStick;
	Timer timer;
	boolean shouldRamp = false;
	double turnSpeed = 0.46;
	double linearSpeed = 0.5;
	int timeFactor;
	int timeFix;
	
	@Override
	public void robotInit() {
		m_myRobot = new DifferentialDrive(new Spark(0), new Spark(1));
		m_leftStick = new Joystick(0);
		timer = new Timer();
	}
	
	@Override
	public void autonomousInit() {
		timeFactor = 1;
		timeFix = 0;
		timer.reset();
		timer.start();
	}
	
	
	@Override
	public void autonomousPeriodic() {
		double driveTime = 2.5;
		double superSpeed = 1.0;
		if (timer.get() < driveTime * 0.2) {
			forward(superSpeed);
		} else if(timer.get() < driveTime * 0.4 && timer.get() > driveTime * 0.2) {
			stop(); 
		} else if(timer.get() > driveTime * 0.4 && timer.get() < driveTime * 1.0) {
			forward(0.5);
		} else {
			stop();
		}
		
		
	}
	
	private void calibrate() {
		if (timer.get() > timeFactor + timeFix - 1.0 && timer.get() < timeFactor + timeFix) {
			forward(linearSpeed);
		} else if (timer.get() > timeFactor + timeFix && timer.get() < timeFactor + timeFix + 0.5) {
			turn("left",turnSpeed);
		} else if (timer.get()> timeFactor + timeFix + 0.5 && timer.get() < timeFactor + timeFix + 1.0) {
			stop();
		} else {
			stop();
			timeFix++;
			timeFactor++;
		}
	}

	
	private void forward(double speed) {
		System.out.print("Forward with speed " + speed);
		m_myRobot.tankDrive(speed, speed, shouldRamp);
	}
	
	private void stop() {
		System.out.print("Stop nuff said");
		m_myRobot.tankDrive(0.0, 0.0, shouldRamp);
	}
	
	private void reverse(double speed) {
		System.out.print("Reverse with speed " + speed);
		m_myRobot.tankDrive(-speed, -speed, shouldRamp);
	}
	
	private void turn(String direction, double speed) {
		System.out.print("Turn with direction "+ direction + "and speed " + speed);
		switch(direction.toLowerCase()) {
		case "right": 
			m_myRobot.tankDrive(speed, -speed, shouldRamp);
		case "left":
			m_myRobot.tankDrive(-speed, speed, shouldRamp);
		default:
		}
	}
	
	@Override
	public void teleopPeriodic() {
		m_myRobot.arcadeDrive(-m_leftStick.getY()*(0.4*m_leftStick.getRawAxis(3)+0.6), m_leftStick.getRawAxis(4)*(0.4*m_leftStick.getRawAxis(3)+0.6));
	}
}
