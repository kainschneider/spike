package org.usfirst.frc.team7287.robot;
//All page numbers referencing FRC Java Programing Manual
//import came from page 185, 168 on newer version
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogInput;

public class ClawHeightSensor{
	
	private AnalogInput analogInput;
	
	ClawHeightSensor(int port) {
		analogInput = new AnalogInput(port);
	}
	
	public void readClawVoltage() {
		//Constructing an Analog Sensor coming from page 199
		//Raw and voltage values, we can decide which we like more
		//Coming from page 200
		int rawValue = analogInput.getValue();
		double voltsValue = analogInput.getVoltage();

		//sending values to console for comparision
		for(int i=0; i < 10; i++) {
			System.out.println("raw value: "+ rawValue);
			System.out.println("Voltage value: "+ voltsValue);
		}
	}
}