package org.usfirst.frc.team1458.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {

	// Elevator.Level.ONE.TOTE.CARRY
	// LIP=lip of tote
	// CARRY= carry level
	// PLACE = placing level
	Levels.MainLevel mainLevel = Levels.MainLevel.ONE;
	Levels.CarryObject carryObject = Levels.CarryObject.TOTE;
	Levels.LevelMode levelMode = Levels.LevelMode.FLOOR;
	Levels.LevelMod levelMod = Levels.LevelMod.LIP;

	//Infrared elevatorBottom = new Infrared(0, 1);
	//Infrared elevatorTop = new Infrared(1, 1);
	
	Encoder elevatorEncoder = new Encoder(6,7);
	
	DigitalInput topLimit = new DigitalInput(4);
	DigitalInput bottomLimit = new DigitalInput(5);

	double elevatorHeight;
	double desiredElevatorHeight;

	boolean isManual = true;
	boolean canMove = true;

	Levels levelHandler = new Levels();

	public double motorMovement;

	public double getElevatorHeight() {
		return elevatorHeight;
	}

	public void setMainLevel(Levels.MainLevel mainLevel) {
		this.mainLevel = mainLevel;
		update();
	}

	public void setLevelMode(Levels.LevelMode levelMode) {
		this.levelMode = levelMode;
		update();
	}

	public void setCarryObject(Levels.CarryObject carryObject) {
		this.carryObject = carryObject;
		update();
	}

	public void setLevelMod(Levels.LevelMod levelMod) {
		this.levelMod = levelMod;
		update();
	}

	public Levels.LevelMode getLevelMode() {
		return levelMode;
	}

	public void update() {
		if(topLimit.get()||bottomLimit.get()) {
			canMove= false;
		}
		desiredElevatorHeight = levelHandler.getHeight(mainLevel, levelMode,
				carryObject, levelMod);
		SmartDashboard
				.putNumber("desiredElevatorHeight", desiredElevatorHeight);
		seeHeight();
		goTowardsDesired();
		
	}

	public void goTowardsDesired() {
		if (canMove&&!isManual) {
			// code
			motorMovement=0.1*(desiredElevatorHeight-elevatorHeight);//0.1 is coeffecient assumes need to move up
		} else {
			//do nothing
		}

	}

	public void stop() {
		motorMovement = 0;
	}

	public void manualUp() {
		if (canMove&&isManual) {
			motorMovement = 1;
		}

	}

	public void manualDown() {
		if (canMove&&isManual) {
			motorMovement = -1;
		}

	}
	
	public void manualAmount(double power) {
		if (canMove&&isManual) {
			motorMovement = power;
		}
	}

	public void setManual(boolean manual) {
		this.isManual = manual;
	}

	public boolean getManual() {
		return isManual;
	}

	public void seeHeight() {
		/*
		elevatorHeight = elevatorBottom.getDistance() - 0;
		if (elevatorHeight > 75) {
			elevatorHeight = elevatorTop.getDistance() - 0;
		}
		*/
		elevatorHeight=1*elevatorEncoder.get();

	}
}