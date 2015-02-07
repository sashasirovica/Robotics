package org.usfirst.frc.team1458.robot;

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

	Infrared elevatorBottom = new Infrared(0, 1);
	Infrared elevatorTop = new Infrared(1, 1);

	double elevatorHeight;
	double desiredElevatorHeight;

	boolean manual;

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
		desiredElevatorHeight = levelHandler.getHeight(mainLevel, levelMode,
				carryObject, levelMod);
		SmartDashboard
				.putNumber("desiredElevatorHeight", desiredElevatorHeight);
		seeHeight();
	}

	public void goTowardsDesired() {
		if (!manual) {
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
		if (manual) {
			motorMovement = 1;
		}

	}

	public void manualDown() {
		if (manual) {
			motorMovement = -1;
		}

	}
	
	public void manualAmount(double power) {
		if (manual) {
			motorMovement = power;
		}
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

	public boolean getManual() {
		return manual;
	}

	public void seeHeight() {
		elevatorHeight = elevatorBottom.getDistance() - 0;
		if (elevatorHeight > 75) {
			elevatorHeight = elevatorTop.getDistance() - 0;
		}

	}
}