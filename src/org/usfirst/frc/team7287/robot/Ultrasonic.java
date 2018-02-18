//All page numbers referencing FRC Java Programing Manual
//import came from page 185
import edu.wpi.first.wpilibj.Ultrasonic;

//temperary on switch
int oN=1;

public class clawHeight{
//Cunstrucing an Analog Sensor coming from page 199

  AnalogInput clawHeight = new AnalogInput(0);
//Number is port on roborio

 //Raw and voltage values, we can decide which we like more
 //Coming from page 200
 int rawValue = clawHeight.getValue();
 double voltsValue = clawHeight.getVoltage();
  
 //sending values to console for comparision
while(oN > 0){
System.out.println("raw value"+ rawValue);
System.out.println("Voltage value"+ voltsValue);
   oN++
   }
}
