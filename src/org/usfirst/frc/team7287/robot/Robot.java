
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
	String switchAndScaleSides = DriverStation.getInstance().getGameSpecificMessage();
	String closeSwitchSide;
	int autoState;
	
	@Override
	public void robotInit() {
		spike = new DifferentialDrive(new Spark(0), new Spark(1));
		stick = new Joystick(0);
		gantryController = new Joystick(1);
		timer = new Timer();
		drive = new Drive(spike, false);
		teleopSpeed = 1;
		clawHeightSensor = new ClawHeightSensor(0);
		clawMotor = new TalonSRX(0);
		verticalMotor = new TalonSRX(1);

		shouldGrab = false;
		bottomLimit = new DigitalInput(0);
		topLimit = new DigitalInput (1);
		this.closeSwitchSide = String.valueOf(this.switchAndScaleSides.charAt(0));
		System.out.println("Our side of each is: " + switchAndScaleSides);
		System.out.println("Our side of the close switch is: " + closeSwitchSide);
	}
	

	private void upDown(double move){	
		move = -move; //invert move directions
		if (move > 0) {
			move = move * 0.5;
		}
		verticalMotor.set(ControlMode.PercentOutput, move);
	}	
	
	private void grab(double speed) {
		clawMotor.set(ControlMode.PercentOutput, speed * 0.7);
	}
	
	private void drop() {
//		clawMotor.stopMotor();
		clawMotor.set(ControlMode.PercentOutput, 0);
	}
	@Override
	public void autonomousInit() {
		timeFactor = 1;
		timeFix = 0;
		timer.reset();
		timer.start();
		autoState = 0;
	}
	
	@Override
	public void autonomousPeriodic() {
		System.out.println(autoState);		
		System.out.println("CHEESE"+closeSwitchSide);
		double turnTime = 0.5;
		switch (autoState) {
				case 0:
					if (closeSwitchSide.equals("L")) {
						autoState = 3;
					}
					else {
						autoState = 1;
					}
					break;
				case 1:
					//Drives straight into switch to deliver cube(middle right)
					drive.forward(0.5);
					if (timer.get() >= 1) {
						timer.reset();
						autoState = 10 ;
					}
					break;
				case 2: //turns 90 degrees let
					drive.turn("left", 0.5);					
					if (timer.get() >= turnTime) {
						timer.reset();
						autoState = 3 ;
					}
					break;
				case 3: // drive forward to align with left side of switch
					drive.smoothTurning(0.5);
					if (timer.get() >= 3.5) {
						timer.reset();
						autoState = 10 ;
					}
					break;
				case 4: // turn right 90 degrees 
					drive.turn("right", turnTime);
					if (timer.get() >= 0.5) {
						timer.reset();
						autoState = 1 ;
					}
					break;
				case 10: // Stop
					drive.stop();
					break;
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

