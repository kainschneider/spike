
package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team7287.robot.Drive;
import org.usfirst.frc.team7287.robot.ClawHeightSensor;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;

public class Robot extends IterativeRobot {
	private DifferentialDrive spike;
	private Joystick stick;
	private Joystick gantryController;
	Timer timer;
	double turnSpeed = 0.46;
	double linearSpeed = 0.5;
	int timeFactor;
	int timeFix;
	private Drive drive;
	double teleopSpeed;
	ClawHeightSensor clawHeightSensor;
	TalonSRX clawMotor;
	TalonSRX verticalMotor;
	boolean shouldGrab; 
	DigitalInput bottomLimit;
	DigitalInput topLimit;
	
	// strings and things
	String startingPositon = "L"; //LEFT = L, R = RIGHT, MIDDLE = M
	String closeSwitchSide;
	String farSwitchSide;
	String scaleSide;
	String scaleAndSwitchSides;
	
	
	
	
	@Override
	public void robotInit() {
		spike = new DifferentialDrive(new Spark(0), new Spark(1));
		stick = new Joystick(0);
		gantryController = new Joystick(1);
		timer = new Timer();
		drive = new Drive(spike, false);
		teleopSpeed = 1.0;
		clawHeightSensor = new ClawHeightSensor(0);
		clawMotor = new TalonSRX(0);
		verticalMotor = new TalonSRX(1);

		shouldGrab = false;
		bottomLimit = new DigitalInput(0);
		topLimit = new DigitalInput (1);
		
		scaleAndSwitchSides = DriverStation.getInstance().getGameSpecificMessage();
		closeSwitchSide = scaleAndSwitchSides(0);
		scaleSide = scaleAndSwitchSides(1);
		farSwitchSide = scaleAndSwitchSides(2);
	}
	

	private void upDown(double move){
		
		verticalMotor.set(ControlMode.PercentOutput,-move);
	}	
	private void grab(double speed) {
		clawMotor.set(ControlMode.PercentOutput, -speed);
	}
	private void drop() {
		clawMotor.set(ControlMode.PercentOutput, 0);
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
		if (startingPosition == "M") {
			drive.stop();
		} else {
			initialAutonomous();
		}


	}
	
//		Autonomous initial cube drop procedure, moves robot forwards 10' and drops cube into claws
	private void initialAutonomous() {
		double superSpeed = 1.0;
		if (timer.get() < 0.5) {
			drive.forward(superSpeed * 0.6);
		} else if(timer.get() < 1.0 && timer.get() > 0.5) {
			drive.forward(superSpeed * 0.80); 
		} else if(timer.get() > 1.0 && timer.get() < 2.2) {
			drive.forward(superSpeed * 0.35);
		} else if (timer.get() > 2.2 && timer.get < 2.7 && startingPosition == "L") {
			if (closeSwitchSide == "L") {
				drive.turn(right, 0.46);
			} else {
				drive.stop();
				}
		} else if (timer.get() > 2.2 && timer.get < 2.7 && startingPosition == "R") {
			if (closeSwitchSide == "R") {
				drive.turn("left", 0.46);
			} else {
				drive.stop();
			}
		} else {
			drive.stop();
		}
	}
	
//	private void autonomousLeft() {
//		if (timer.get < 0.25) {
//			drive.forward(1.0);
//		} else if (timer.get < )
//	}
	
	private void calibrate() {
		if (timer.get() > timeFactor + timeFix - 1.0 && timer.get() < timeFactor + timeFix) {
			drive.forward(linearSpeed);
		} else if (timer.get() > timeFactor + timeFix && timer.get() < timeFactor + timeFix + 0.5) {
			drive.turn("left",turnSpeed);
		} else if (timer.get()> timeFactor + timeFix + 0.5 && timer.get() < timeFactor + timeFix + 1.0) {
			drive.stop();
		} else {
			drive.stop();
			timeFix++;
			timeFactor++;
		}
	}
	
	@Override
	public void teleopPeriodic() {
		//	gantry/claw control
		if (gantryController.getRawAxis(1) != 0) {
			grab(gantryController.getRawAxis(1));
		}
		else {
			grab(0);
		}

		if (gantryController.getRawAxis(3) != 0){
			
			upDown(gantryController.getRawAxis(3));
		}
		else {
			upDown(0);
		}
		
		spike.arcadeDrive(stick.getY()*teleopSpeed, stick.getRawAxis(2)*teleopSpeed);
		}
	}
