
package org.usfirst.frc.team1458.robot;


import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Gyro;


public class Robot extends SampleRobot {
	Joystick right = new Joystick(0);
	Joystick left = new Joystick(1);
	Joystick buttonPanel = new Joystick(2);
	//Casement/David input stuff
	Victor rightDriveFront = new Victor(1);
	Victor rightDriveRear = new Victor(0);
	Victor leftDriveFront = new Victor(3);
	Victor leftDriveRear = new Victor(2);
	Talon centreDrive = new Talon(4);
	Talon arm1 = new Talon(5);
	Talon arm2 = new Talon(6);
	Talon elevator = new Talon(7);
	
	
	Encoder leftEncoder = new Encoder(0,1);//need proper channels
	Encoder rightEncoder = new Encoder(2,3);//need proper channels
	
	
	
	I2CGyro gyro = new I2CGyro();
	
	Infrared elevatorBottom = new Infrared(0);
	Infrared elevatorTop = new Infrared(1);
	Infrared toteCheck = new Infrared(2);
	Infrared seeRight = new Infrared(3);
	Infrared seeLeft = new Infrared(4);

	LED ledStrip = new LED();
    
    public Robot() {
    
    }
    /*
    public double ultrasonicDistance()
    {
        SmartDashboard.putDouble("Ultrasonic Distance", (ultrasonic.getVoltage() * 3.47826087) - 0.25);
    }
    */

    /**
     * Autonomous mode
     */
    public void autonomous() {
    
    }

    /**
     * operator controlled mode
     */
    public void operatorControl() {
    	
    	gyro.reset();
    	gyro.update();
    	double gyroAngle;
    	double gyroRate;
    	/*
    	rightEncoder.setDistancePerPulse(1.0);
    	leftEncoder.setDistancePerPulse(1.0);
    	*/
    	while(isEnabled()) {
    		gyroAngle = gyro.getAngle();
    		gyroRate = gyro.getRate();
    		System.out.println("Gyro Angle: " + gyroAngle);
    		System.out.println("GyroRate: " + gyroRate);
    		SmartDashboard.putNumber("Gyro Angle: ", gyroAngle);
    		SmartDashboard.putNumber("Gyro Rate: ", gyroRate);
    		SmartDashboard.putNumber("Gyro Angle Graph: ", gyroAngle);
    		SmartDashboard.putNumber("Gyro Rate Graph: ", gyroRate);
    		gyro.update();
    		
    	}
    }

    /**
     * Runs during test mode
     */
    public void test() {
    	
    }
}
/*
double lDp=0;//left Drive power
double rDp=0;//right Drive power
double cDp=0;//centre Drive power

final double kPGyro=0.0;
final double kIGyro=0.0;
final double kDGyro=0.0;

final double kPEncoder=0.0;
final double kIEncoder=0.0;
final double kDEncoder=0.0;

double adjustPGyro;
double adjustIGyro;
double adjustDGyro;

double adjustPEncoder;
double adjustIEncoder;
double adjustDEncoder;

double adjustGyro;
double adjustEncoder;
double adjustTotal;

double idealGyroAngle = 0;

double power;
*/

/*
power=right.getY();

adjustPGyro=kPGyro*(gyro.getAngle()-idealGyroAngle);
adjustIGyro=kIGyro*0;//not applicable but included for consistency
adjustDGyro=kDGyro*gyro.getRate();//based on rotation to right in degrees/second
adjustGyro=adjustPGyro+adjustIGyro+adjustPGyro;

adjustPEncoder=kPEncoder*(rightEncoder.get()-leftEncoder.get());
adjustIEncoder=kIEncoder*0;//also not applicable
adjustDEncoder=kDEncoder*(leftEncoder.getRate()-rightEncoder.getRate());
adjustEncoder=adjustPEncoder+adjustIEncoder+adjustPEncoder;

adjustTotal=adjustGyro+adjustEncoder;
rDp=power-adjustTotal;
lDp=power+adjustTotal;


rightDriveFront.set(rDp);
rightDriveRear.set(rDp);
leftDriveFront.set(lDp);
leftDriveRear.set(lDp);
centreDrive.set(cDp);

System.out.println("Right: " + rDp);
SmartDashboard.putNumber("Right: ", rDp);

System.out.println("Left: " + lDp);
SmartDashboard.putNumber("Left: ", lDp);

System.out.println("Centre: "+cDp);
SmartDashboard.putNumber("Centre:",cDp);


System.out.println("Right encoder: "+rightEncoder.getRaw());
SmartDashboard.putNumber("Right encoder: ",rightEncoder.getRaw());

System.out.println("Left encoder: "+leftEncoder.getRaw());
SmartDashboard.putNumber("Left encoder: ",leftEncoder.getRaw());
*/