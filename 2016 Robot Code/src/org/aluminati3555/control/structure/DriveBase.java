package org.aluminati3555.control.structure;

import org.aluminati3555.control.motor.MotorGroup;
import org.aluminati3555.control.motor.MotorGroupSyncronizer;
import org.aluminati3555.control.motor.MotorTable;

public class DriveBase extends BaseStructure {
	private MotorTable speedTable;
	
	private MotorGroupSyncronizer motorSync;
	private MotorGroup leftSideDrive;
	private MotorGroup rightSideDrive;
	
	private double speedLeft, speedRight;
	
	public DriveBase(String path, MotorGroup leftSideDrive, MotorGroup rightSideDrive) {
		super(10);
		
		this.leftSideDrive = leftSideDrive;
		this.rightSideDrive	 = rightSideDrive;
		
		motorSync = new MotorGroupSyncronizer(leftSideDrive, rightSideDrive);
		speedTable = new MotorTable(10, path).load();	
	}
	
	public void setSpeed(double speedLeft, double speedRight) {
		this.speedLeft = speedLeft;
		this.speedRight = speedRight;
		
		motorSync.setSpeed(1, speedLeft, speedRight);
	}
	
	public void setSpeedPercenatge(double speedLeft, double speedRight) {
		this.speedLeft = speedLeft;
		this.speedRight = speedRight;
		
		motorSync.setSpeedPercentage(1, speedLeft, speedRight);
	}
	
	public void updateMethod() {
		
	}

	public void disable() {
		speedTable.save();
	}

	public MotorTable getSpeedTable() { return speedTable; }
	public MotorGroupSyncronizer getMotorSync() { return motorSync; }
	
	public MotorGroup getLeftSideDrive() { return leftSideDrive; }
	public MotorGroup getRightSideDrive() { return rightSideDrive; }
	
	public double getSpeedLeft() { return speedLeft; }
	public double getSpeedRight() { return speedRight; }
}
