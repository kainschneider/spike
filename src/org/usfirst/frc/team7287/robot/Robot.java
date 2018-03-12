
package org.usfirst.frc.team7287.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team7287.robot.Drive;
import org.usfirst.frc.team7287.robot.ClawHeightSensor;

//Note CANTalon is deprecated and I still don't care.
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Robot extends IterativeRobot {
	private DifferentialDrive spike;
	private Joystick stick;
	Timer timer;
	double turnSpeed = 0.46;
	double linearSpeed = 0.5;
	int timeFactor;
	int timeFix;
	private Drive drive;
	double teleopSpeed;
	ClawHeightSensor clawHeightSensor;
	CANTalon clawMotor;
	CANTalon verticalMotor;
	boolean shouldGrab; 
	DigitalInput bottomLimit;
	DigitalInput topLimit;
	
	
	
	@Override
	public void robotInit() {
		spike = new DifferentialDrive(new Spark(0), new Spark(1));
		stick = new Joystick(0);
		timer = new Timer();
		drive = new Drive(spike, false);
		teleopSpeed = 0.50;
		clawHeightSensor = new ClawHeightSensor(0);
		clawMotor = new CANTalon(0);
		clawMotor.enable();
		verticalMotor = new CANTalon(1);
		verticalMotor.enable();
		int mode = CANTalon.TalonControlMode.PercentVbus.ordinal();
		clawMotor.setControlMode(mode);
		verticalMotor.setControlMode(mode);
		shouldGrab = false;
		bottomLimit = new DigitalInput(0);
		topLimit = new DigitalInput (1);
	}
	
	private void upDown(double move){
		verticalMotor.set(move);
		
		if (move > 0) {
		verticalMotor.set(move);
		System.out.println("Going up at a speed of" + move);
			
    	}
		else if (move < 0){
		verticalMotor.set(move);
		System.out.println("Going down at a speed of" + move);
		}
		else{
		verticalMotor.set(0);	
		}
	}
	
	private void grab(double speed) {
		clawMotor.set(-speed);
	}
	private void drop() {
		clawMotor.stopMotor();
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
		clawHeightSensor.readClawValues();
		if (stick.getRawButton(1)) {
			grab(0.3);
		}
		if (stick.getRawButton(2)) {
			grab(-0.3);
		}
		if (stick.getRawButton(3)){
	    	upDown(0.8);
		}
		else if(stick.getRawButton(4)){
    		upDown(-0.5);
		}
		else {
			upDown(0);
		}
	//		Speed gearing system to swap between precision speed and high speed when right bumper is pressed
		if (stick.getRawButton(6)) {
			teleopSpeed = (teleopSpeed == 0.50) ? 1.0 : 0.65;
		}
		spike.arcadeDrive(stick.getY()*teleopSpeed, stick.getRawAxis(2)*teleopSpeed);
//		spike.arcadeDrive(-stick.getY()*teleopSpeed, stick.getX()*teleopSpeed);
		}
	}
