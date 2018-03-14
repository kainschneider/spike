
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
	boolean shouldGrab; 
	DigitalInput limitSwitch;
	
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
		shouldGrab = false;
		limitSwitch = new DigitalInput(0);
	}
	
	public static double mapRange(double analogLow, double analogHigh, double digitalLow, double digitalHigh, double input){
		return digitalLow + ((input - analogLow)*(digitalHigh - digitalLow))/(analogHigh - analogLow);
	}

//	private void clawVerticalSafteyCheck(DigitalInput topSwitch, DigitalInput bottomSwitch, CANTalon motor) {
//		if(topswitch.get() || bottomswitch.get()) {
//			motor.set(0.0);
//			}
//		}
	
	private void grab(double speed) {
		clawMotor.set(ControlMode.PercentOutput, -speed);
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
	}
	
	
	@Override
	public void autonomousPeriodic() {
		initialCubeDrop();
//		clawVerticalSafteyCheck(bottomSwitch, topSwitch, verticalMotor);
	}
	
//		Autonomous initial cube drop procedure, moves robot forwards 10' and drops cube into claws
	private void initialCubeDrop() {
		double driveTime = 2.5;
		double superSpeed = 1.0;
		double fallTime = 0.25;
		if (timer.get() < driveTime * 0.1) {
			drive.forward(superSpeed);
		} else if(timer.get() < driveTime * fallTime && timer.get() > driveTime * 0.1) {
			drive.stop(); 
		} else if(timer.get() > driveTime * fallTime && timer.get() < driveTime * 1.0) {
			drive.forward(0.5);
		} else {
			drive.stop();
		}
	}
	
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
		System.out.println("was pushed: " + limitSwitch.get());
//		clawHeightSensor.readClawValues();

//	gantry/claw control
		if (gantryController.getRawAxis(1) != 0) {
			grab(gantryController.getRawAxis(1));
		}
		else {
			grab(0);
		}
//	Gantry Vertical control WILL ONLY WORK ONCE MERGED
//		if (gantryController.getRawAxis(3)){
//		verticalMotor(gantryController.getRawAxis(1));
//		}
		

//		Speed gearing system to swap between precision speed and high speed when right bumper is pressed
//		if (stick.getRawButton(6)) {
//			teleopSpeed = (teleopSpeed == 0.50) ? 1.0 : 0.65;
//		}
		spike.arcadeDrive(stick.getY()*teleopSpeed, stick.getRawAxis(2)*teleopSpeed);
//		spike.arcadeDrive(-stick.getY()*teleopSpeed, stick.getX()*teleopSpeed);
//		clawVerticalSafteyCheck(bottomSwitch, topSwitch, verticalMotor);
	}
}