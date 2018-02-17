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
	
	
	@Override
	public void robotInit() {
		m_myRobot = new DifferentialDrive(new Spark(0), new Spark(1));
		m_leftStick = new Joystick(0);
		timer = new Timer();
	}
	
	@Override
	public void autonomousInit() {

		timer.reset();
		timer.start();
	}
	
	public void autonomousPeriodic() {
		//come back to timer and change time etc
		if (timer.get() <2.0) {
			m_myRobot.tankDrive(1.0,1.0);
		} else {
			m_myRobot.tankDrive(0.0,0.0 );
		
		}
		
	}
	@Override
	public void teleopPeriodic() {
		m_myRobot.arcadeDrive(-m_leftStick.getY()*(0.4*m_leftStick.getRawAxis(3)+0.6), m_leftStick.getRawAxis(4));
	}
}
