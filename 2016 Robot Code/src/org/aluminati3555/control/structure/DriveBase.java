package org.aluminati3555.control.structure;

import org.aluminati3555.I2C.sensors.I2C_Encoder;
import org.aluminati3555.control.motor.MotorGroup;
import org.aluminati3555.control.motor.MotorGroupSyncronizer;
import org.aluminati3555.robot.I2C_Request_Addresses.DriveRequests;

import edu.wpi.first.wpilibj.Talon;

public class DriveBase extends BaseStructure {
	
	private static final double LEFT_DRIVE_TOP_SPEED = 1;
	private static final double RIGHT_DRIVE_TOP_SPEED = 1;
	
	private static final int DRIVE_LEFT_1_INDEX = 0; //change to appropriate spot of the real wire 
	private static final int DRIVE_LEFT_2_INDEX = 1; //change to appropriate spot of the real wire 
	private static final int DRIVE_RIGHT_1_INDEX = 2;//change to appropriate spot of the real wire 
	private static final int DRIVE_RIGHT_2_INDEX = 3;//change to appropriate spot of the real wire 
    
	// Drive Speed Controllers
	private MotorGroup leftSideDrive;
	private MotorGroup rightSideDrive;
	private MotorGroupSyncronizer driveSync;

	public DriveBase() {
		super(10);
		
		leftSideDrive = new MotorGroup(new I2C_Encoder(DriveRequests.LeftEncoder),  
    			LEFT_DRIVE_TOP_SPEED, new Talon(DRIVE_LEFT_1_INDEX), new Talon(DRIVE_LEFT_2_INDEX));
    	rightSideDrive = new MotorGroup(new I2C_Encoder(DriveRequests.RightEncoder),  
    			RIGHT_DRIVE_TOP_SPEED, new Talon(DRIVE_RIGHT_1_INDEX), new Talon(DRIVE_RIGHT_2_INDEX));
    	driveSync = new MotorGroupSyncronizer(leftSideDrive, rightSideDrive).setDefaultModifiers(1, -1);
	}
	
	private double tankDriveLeft;
    private double tankDriveRight;
    
    private static final double WHEEL_RADIUS = 6;
    private static final double CLOSE_ENOUGH = 0.1; //when robot drives itself, this is the amount is can be off by
    private static final double TRAVEL_SPEED = 0.5;
    private double travelDistance;
    
    public void driveDistance(double inches) {
    	travelDistance += inches;
    }

	protected void updateMethod() {
	    //updates travel distance
    	updateDistance:
    	if(Math.abs(travelDistance) > 0) {
	    	if(Math.abs(travelDistance) < CLOSE_ENOUGH) {
	    		travelDistance = 0;
	    		break updateDistance;
	    	}
	    	
	    	travelDistance -= leftSideDrive.getEncoder().getTotal() * (travelDistance > 0 ? 1 : -1) * WHEEL_RADIUS;
	    	leftSideDrive.getEncoder().reset();
    	}
    
//	TODO: FIX
//    	tankDriveLeft = tankDriveRight = TRAVEL_SPEED * (travelDistance > 0 ? 1 : travelDistance < 0 ? -1 : 0);
    	driveSync.setSpeedPercentage(1, tankDriveLeft, tankDriveRight);
    	
	}

	public void disable() {

	}

	public MotorGroupSyncronizer getDriveSync() { return driveSync; }
	public void setTankDriveLeft(double tankDriveLeft) { this.tankDriveLeft = tankDriveLeft; }
	public void setTankDriveRight(double tankDriveRight) { this.tankDriveRight = tankDriveRight; }
}
