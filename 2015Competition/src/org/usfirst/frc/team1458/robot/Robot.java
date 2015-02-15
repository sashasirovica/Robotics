package org.usfirst.frc.team1458.robot;

import org.usfirst.frc.team1458.robot.Levels.CarryObject;
import org.usfirst.frc.team1458.robot.Levels.LevelMod;
import org.usfirst.frc.team1458.robot.Levels.LevelMode;
import org.usfirst.frc.team1458.robot.Levels.MainLevel;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Gyro;

public class Robot extends SampleRobot {
	Joystick right = new Joystick(0);
	Joystick left = new Joystick(1);
	Joystick buttonPanel = new Joystick(2);

	Timer timer = new Timer();
	Timer timer2 = new Timer();

	Encoder leftEncoder = new Encoder(0, 1);// need proper channels
	Encoder rightEncoder = new Encoder(2, 3);// need proper channels
	Encoder centreEncoder = new Encoder(4, 5);

	I2CGyro gyro = new I2CGyro();

	I2CMagnetometer maggie = new I2CMagnetometer();

	Infrared toteCheck = new Infrared(0, 0);
	/*
	 * Infrared seeRight = new Infrared(3, 0); Infrared seeLeft = new
	 * Infrared(4, 1);
	 */

	LED ledStrip = new LED();

	Compressor compressor = new Compressor(0);
	DoubleSolenoid hSolenoid = new DoubleSolenoid(0, 1);
	DoubleSolenoid intakeSolenoid = new DoubleSolenoid(2, 3);

	PowerDistributionPanel pdp = new PowerDistributionPanel();

	RobotFunctions robot;

	Elevator elevator;

	Elevator.ElevatorMode elevatorMode = Elevator.ElevatorMode.CARRY;

	private final double kDriveToInches = 1;

	public Robot() {
		// initialise all of the things, for the elevator, robotfunctions, gyro,
		// etc.
		robot = new RobotFunctions();
		elevator = new Elevator();

	}

	/**
	 * Autonomous mode
	 */
	public void autonomous() {

	}

	public void waitSasha(double time) {
		Timer t = new Timer();
		while (t.get() < time) {
			// Party Time!
		}
	}

	/**
	 * operator controlled mode
	 */
	public void operatorControl() {
		
		Timer intakeTimer = new Timer();

		compressor.start();

		gyro.reset();
		gyro.update();
		double gyroAngle;
		double gyroRate;

		boolean manualIntake = true;

		// seeRight.reset();

		maggie.zero();

		rightEncoder.setDistancePerPulse(1.0);
		leftEncoder.setDistancePerPulse(1.0);
		centreEncoder.setDistancePerPulse(1.0);

		timer.start();
		timer2.start();

		while (isEnabled() && isOperatorControl()) {
			SmartDashboard.putNumber("totCheck IR", toteCheck.getDistance());
			SmartDashboard.putNumber("test encoder", leftEncoder.get());

			SmartDashboard.putNumber("Update speed (Hz)", 1 / timer.get());
			timer.reset();
			SmartDashboard.putNumber("Timer 2", timer2.get());
			/*
			 * if (timer2.get() >= 0.1) {
			 * SmartDashboard.putNumber("Infrared test distance",
			 * seeRight.getDistance()); timer2.reset(); }
			 */

			if (maggie.isReady()) {
				maggie.update();

			}
			SmartDashboard.putNumber("Direction", maggie.getAngle());

			gyroAngle = gyro.getAngle();
			gyroRate = gyro.getRate();
			System.out.println("Gyro Angle: " + gyroAngle);
			System.out.println("Gyro Rate: " + gyroRate);
			SmartDashboard.putNumber("Gyro Angle: ", gyroAngle);
			SmartDashboard.putNumber("Gyro Rate: ", gyroRate);

			gyro.update();

			elevator.update();
			robot.edrive(elevator.motorMovement);
			if (manualIntake) {
				if (buttonPanel.getRawButton(14)) {
					elevator.setLevelMod(Levels.LevelMod.LOAD);
				} else {
					elevator.setLevelMod(Levels.LevelMod.UNLOAD);
				}
			} else {
				// new intake code
				if (buttonPanel.getRawButton(9)) {
					// start intake
					elevatorMode = Elevator.ElevatorMode.INTAKELIFT;
					elevator.setMainLevel(Levels.MainLevel.TWO);
					elevator.setLevelMod(Levels.LevelMod.LOAD);
					elevator.update();

				} else if (buttonPanel.getRawButton(10)) {
					// start outtake
					elevatorMode = Elevator.ElevatorMode.OUTTAKE;
				}

				if (elevatorMode == Elevator.ElevatorMode.INTAKELIFT) {
					// intake lift loop
					elevator.update();
					if (elevator.getAtHeight()) {
						elevatorMode = Elevator.ElevatorMode.INTAKESUCK;
						intakeSolenoid.set(DoubleSolenoid.Value.kForward);
						robot.aLeftdrive(1);
						robot.aRightdrive(-1);
						intakeTimer.reset();
						intakeTimer.start();
						
					}

				} else if (elevatorMode == Elevator.ElevatorMode.INTAKESUCK) {
					if(intakeTimer.get()>1.5) {
						intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
					}

					if (toteCheck.getDistance()>1.23) {
						// set to drop
						elevatorMode = Elevator.ElevatorMode.INTAKEDROP;
						elevator.setMainLevel(Levels.MainLevel.ONE);
						elevator.setLevelMod(Levels.LevelMod.UNLOAD);
					}

				} else if (elevatorMode == Elevator.ElevatorMode.INTAKEDROP) {
					//drop loop
					if ((true) && elevator.getAtHeight()) {
						// intake end
						elevator.setLevelMod(Levels.LevelMod.LOAD);
					}
				} else if (elevatorMode == Elevator.ElevatorMode.OUTTAKE) {
					// outtake loop
					if (true) {
						// outtake end
					}
				} else if (elevatorMode == Elevator.ElevatorMode.CARRY) {
					// carry loop
				}
			}

			/*
			 * // intake of a stack n high if (buttonPanel.getRawButton(9)) { //
			 * raise elevator to level n+1-done by humans elevatorMode =
			 * elevatorMode.INTAKE;
			 * elevator.setLevelMod(elevatorMode.getLevelMod()); // run intake
			 * // left 1, right -1
			 * 
			 * intakeSolenoid.set(DoubleSolenoid.Value.kForward);
			 * robot.aLeftdrive(1); robot.aRightdrive(-1);
			 * 
			 * // lower elevator to level 1 //
			 * 
			 * } else if (buttonPanel.getRawButton(10)) {
			 * intakeSolenoid.set(DoubleSolenoid.Value.kForward);
			 * elevator.setMainLevel(Levels.MainLevel.ONE); elevator.update();
			 * 
			 * // raise elevator to level n+1 // lower elevator to level n //
			 * disengage // drive straight back a little bit
			 * 
			 * } // intake loop stuff if (elevatorMode ==
			 * Elevator.ElevatorMode.INTAKE) {
			 * intakeSolenoid.set(DoubleSolenoid.Value.kReverse); if
			 * (toteCheck.getDistance() < 1.23) {
			 * elevator.setMainLevel(Levels.MainLevel.ONE);
			 * elevator.setLevelMod(Levels.LevelMod.UNLOAD); elevator.update();
			 * }
			 * 
			 * } else if (elevatorMode == Elevator.ElevatorMode.OUTTAKE) {
			 * 
			 * } // check if complete if ((elevatorMode ==
			 * Elevator.ElevatorMode.INTAKE) && elevator.getAtHeight() &&
			 * (toteCheck.getDistance() > 1.23)) { elevatorMode =
			 * Elevator.ElevatorMode.CARRY; } else if
			 * ((elevatorMode==Elevator.ElevatorMode
			 * .OUTTAKE)&&elevator.getAtHeight()&&(toteCheck.getDistance()<0.5))
			 * { elevatorMode = Elevator.ElevatorMode.CARRY; }
			 */

			// outtake to n
			if (right.getRawButton(1)) {
				robot.tankdrive(-right.getAxis(Joystick.AxisType.kY), -right.getAxis(Joystick.AxisType.kY));
			} else if (!robot.hMode) {
				robot.tankdrive(-right.getAxis(Joystick.AxisType.kY), -left.getAxis(Joystick.AxisType.kY));
			}
			if (right.getRawButton(3)) {
				// deploy H
				hSolenoid.set(DoubleSolenoid.Value.kForward);
				robot.hMode = true;
				// robot.hdrive(right.getAxis(Joystick.AxisType.kX));
			}
			if (right.getRawButton(4)) {
				// retract H
				hSolenoid.set(DoubleSolenoid.Value.kReverse);
				robot.hMode = false;
			}
			if (robot.hMode) {
				robot.hdrive(right.getAxis(Joystick.AxisType.kX));
			}
			/*
			 * robot.ldrive(-left.getAxis(Joystick.AxisType.kY));
			 * robot.rdrive(-right.getAxis(Joystick.AxisType.kY));
			 */
			if (buttonPanel.getRawButton(1)) {
				elevator.setMainLevel(Levels.MainLevel.ONE);
			} else if (buttonPanel.getRawButton(2)) {
				elevator.setMainLevel(Levels.MainLevel.TWO);
			} else if (buttonPanel.getRawButton(3)) {
				elevator.setMainLevel(Levels.MainLevel.THREE);
			} else if (buttonPanel.getRawButton(4)) {
				elevator.setMainLevel(Levels.MainLevel.FOUR);
			}

			if (buttonPanel.getRawButton(5)) {
				elevator.setLevelMode(Levels.LevelMode.FLOOR);
				elevator.setManual(false);
			} else if (buttonPanel.getRawButton(6)) {
				elevator.setLevelMode(Levels.LevelMode.PLATFORM);
				elevator.setManual(false);
			} else if (buttonPanel.getRawButton(7)) {
				elevator.setLevelMode(Levels.LevelMode.STEP);
				elevator.setManual(false);
			} else if (buttonPanel.getRawButton(8)) {
				elevator.setManual(true);
			}

			if (!buttonPanel.getRawButton(13)) {
				elevator.setCarryObject(Levels.CarryObject.TOTE);
			} else {
				elevator.setCarryObject(Levels.CarryObject.CONTAINER);
			}

			if (buttonPanel.getRawButton(11) && elevator.getManual()) {
				elevator.manualUp();
			} else if (buttonPanel.getRawButton(12) && elevator.getManual()) {
				elevator.manualDown();
			} else if (elevator.getManual()) {
				elevator.stop();
			}

			if (!elevator.getManual()) {
				if (elevator.levelMode == Levels.LevelMode.FLOOR) {
					buttonPanel.setOutput(1, false);
					buttonPanel.setOutput(2, true);
					buttonPanel.setOutput(3, true);
					buttonPanel.setOutput(4, true);
				} else if (elevator.levelMode == Levels.LevelMode.PLATFORM) {
					buttonPanel.setOutput(1, true);
					buttonPanel.setOutput(2, false);
					buttonPanel.setOutput(3, true);
					buttonPanel.setOutput(4, true);
				} else if (elevator.levelMode == Levels.LevelMode.STEP) {
					buttonPanel.setOutput(1, true);
					buttonPanel.setOutput(2, true);
					buttonPanel.setOutput(3, false);
					buttonPanel.setOutput(4, true);
				}
			} else {
				buttonPanel.setOutput(1, true);
				buttonPanel.setOutput(2, true);
				buttonPanel.setOutput(3, true);
				buttonPanel.setOutput(4, false);
			}
		}
	}

	public void elevatorMotor(double power) {
		robot.edrive(power);
	}

	/**
	 * Runs during test mode
	 */
	// in inches
	private void straightDistance(double distance) {
		double marginOfError = 1;// in inches
		double distanceTraveled = 0;// in inches
		leftEncoder.reset();
		rightEncoder.reset();
		while (distanceTraveled * kDriveToInches < distance) {
			distanceTraveled = (leftEncoder.get() + rightEncoder.get()) / 2;

		}
	}

	public void superDrive(double power, double direction) {
		double pCorrection;
		double iCorrection;
		double totalCorrection;
		final double kP = 0.25, kI = 1.0;
		// d was 0.25
		direction = (direction) + gyro.getAngle();
		// straightAngle = direction;
		pCorrection = (-gyro.getRate()) * kP;
		iCorrection = (direction - gyro.getAngle()) * kI;
		totalCorrection = pCorrection + iCorrection;

		totalCorrection /= 10;
		if (totalCorrection > 1) {
			totalCorrection = 1;
		} else if (totalCorrection < -1) {
			totalCorrection = -1;
		}
		if (totalCorrection < 0) {
			robot.ldrive(power * (1 - Math.abs(totalCorrection)));
			robot.rdrive(power);
		} else if (totalCorrection > 0) {
			robot.ldrive(power);
			robot.rdrive(power * (1 - Math.abs(totalCorrection)));
		} else {
			robot.tankdrive(power, power);
		}
	}

	public void test() {

	}
}
/*
 * double lDp=0;//left Drive power double rDp=0;//right Drive power double
 * cDp=0;//centre Drive power
 * 
 * final double kPGyro=0.0; final double kIGyro=0.0; final double kDGyro=0.0;
 * 
 * final double kPEncoder=0.0; final double kIEncoder=0.0; final double
 * kDEncoder=0.0;
 * 
 * double adjustPGyro; double adjustIGyro; double adjustDGyro;
 * 
 * double adjustPEncoder; double adjustIEncoder; double adjustDEncoder;
 * 
 * double adjustGyro; double adjustEncoder; double adjustTotal;
 * 
 * double idealGyroAngle = 0;
 * 
 * double power;
 */

/*
 * power=right.getY();
 * 
 * adjustPGyro=kPGyro*(gyro.getAngle()-idealGyroAngle);
 * adjustIGyro=kIGyro*0;//not applicable but included for consistency
 * adjustDGyro=kDGyro*gyro.getRate();//based on rotation to right in
 * degrees/second adjustGyro=adjustPGyro+adjustIGyro+adjustPGyro;
 * 
 * adjustPEncoder=kPEncoder*(rightEncoder.get()-leftEncoder.get());
 * adjustIEncoder=kIEncoder*0;//also not applicable
 * adjustDEncoder=kDEncoder*(leftEncoder.getRate()-rightEncoder.getRate());
 * adjustEncoder=adjustPEncoder+adjustIEncoder+adjustPEncoder;
 * 
 * adjustTotal=adjustGyro+adjustEncoder; rDp=power-adjustTotal;
 * lDp=power+adjustTotal;
 * 
 * 
 * rightDriveFront.set(rDp); rightDriveRear.set(rDp); leftDriveFront.set(lDp);
 * leftDriveRear.set(lDp); centreDrive.set(cDp);
 * 
 * System.out.println("Right: " + rDp); SmartDashboard.putNumber("Right: ",
 * rDp);
 * 
 * System.out.println("Left: " + lDp); SmartDashboard.putNumber("Left: ", lDp);
 * 
 * System.out.println("Centre: "+cDp); SmartDashboard.putNumber("Centre:",cDp);
 * 
 * 
 * System.out.println("Right encoder: "+rightEncoder.getRaw());
 * SmartDashboard.putNumber("Right encoder: ",rightEncoder.getRaw());
 * 
 * System.out.println("Left encoder: "+leftEncoder.getRaw());
 * SmartDashboard.putNumber("Left encoder: ",leftEncoder.getRaw());
 */