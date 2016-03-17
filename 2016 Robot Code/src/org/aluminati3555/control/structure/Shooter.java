package org.aluminati3555.control.structure;

import org.aluminati3555.I2C.sensors.I2C_Encoder;
import org.aluminati3555.control.motor.MotorGroup;
import org.aluminati3555.control.motor.MotorGroupSyncronizer;
import org.aluminati3555.robot.I2C_Request_Addresses.ShooterRequests;

import edu.wpi.first.wpilibj.Talon;

public class Shooter extends BaseStructure {

	// Shooter Constants
	private static final double FLYWHEEL_TOP_SPEED = 1;
	private static final double FLYWHEEL_BOTTOM_SPEED = 1;
	
	private static final int FLYWHEEL_TOP_INDEX = 5;     //change to appropriate spot of the real wire 
	private static final int FLYWHEEL_BOTTOM_INDEX = 9;  //change to appropriate spot of the real wire 
	
    private MotorGroup topFlywheel;
	private MotorGroup bottomFlywheel;
	private MotorGroupSyncronizer flywheelSync;

    private double shooterTop;
    private double shooterBottom;
    
//    private KeyTable shooterDistancesTable;
//    private double shootDistance;
	
    //key table is a class that represents a table that can acess values by providing a key
	public Shooter() {
		super(10);
		
//		shooterDistancesTable = new KeyTable("Top Wheel", "Bottom Speed");
//		shooterDistancesTable.load("/shooterTable.csv");
		
    	topFlywheel = new MotorGroup(new I2C_Encoder(ShooterRequests.TopEncoder), 
    			FLYWHEEL_TOP_SPEED, new Talon(FLYWHEEL_TOP_INDEX));
    	bottomFlywheel = new MotorGroup(new I2C_Encoder(ShooterRequests.BottomEncoder), 
    			FLYWHEEL_BOTTOM_SPEED, new Talon(FLYWHEEL_BOTTOM_INDEX));
    	flywheelSync = new MotorGroupSyncronizer(topFlywheel, bottomFlywheel).setDefaultModifiers(1, -1);
	}

	//updates the shooter and checks distance
	protected void updateMethod() {
//		shootDistance:
//		if(shootDistance > 0) {
//			double[] speeds = shooterDistancesTable.getClosestRow(shootDistance, 2);
//			
//			if(speeds == null) {
//				DriverStation.reportError("Can not shoot from a distance of: " + shootDistance, false);
//				break shootDistance;
//			}
//			
//			shooterTop = speeds[0];
//			shooterBottom = speeds[1];
//		}
		
    	flywheelSync.setSpeedPercentage(1, shooterTop, shooterBottom);
	}

	public void disable() {

	}

	public MotorGroupSyncronizer getFlywheelSync() { return flywheelSync; }
	public void setShooterTop(double shooterTop) { this.shooterTop = shooterTop; }
	public void setShooterBottom(double shooterBottom) { this.shooterBottom = shooterBottom; }
//	public void setShootDistance(double shootDistance) { this.shootDistance = shootDistance; }
}

