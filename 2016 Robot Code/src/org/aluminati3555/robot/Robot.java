package org.aluminati3555.robot;

import org.aluminati3555.I2C.sensors.I2C_DigitalSensor;
import org.aluminati3555.I2C.sensors.I2C_Encoder;
import org.aluminati3555.control.input.JoysickMappings.LogitechAttack3_Axis;
import org.aluminati3555.control.input.JoysickMappings.LogitechAttack3_Button;
import org.aluminati3555.control.input.JoystickBase;
import org.aluminati3555.control.input.LinerJoystick;
import org.aluminati3555.control.motor.MotorGroup;
import org.aluminati3555.control.motor.MotorGroupSyncronizer;
import org.aluminati3555.robot.I2C_Request_Addresses.DriveRequests;
import org.aluminati3555.robot.I2C_Request_Addresses.ShooterRequests;
import org.aluminati3555.tables.KeyTable;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {
    public Robot() {
    	initDrive();
    	initBallLoader();
    	initShooter();
    } 
    
    private double lastJoyL, lastJoyR;
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
        	rawUpdate();
        	
        	// Gives Robot time to process input properly
            Timer.delay(0.005);
        }
    }
    
/** *********************************************************************************************************************** **\
 * 	- - - - - - - - - - - - - - - - - - - - - - - - - - Update Loop - - - - - - - - - - - - - - - - - - - - - - - - - - - -  *
\** *********************************************************************************************************************** **/
    
    public void update() {
    	updateBallLoader();
    	updateShooter();
    	updateDrive();
    	
    	updateJoystick();
    }
    
    public void rawUpdate() {
    	// Gets Input from the two drive Joysticks
    	double joyL_Value = joyL.getValue(LogitechAttack3_Axis.Y);
    	double joyR_Value = joyR.getValue(LogitechAttack3_Axis.Y);

    	// Determines if the speed has changed
    	boolean joyL_Changed = lastJoyL != joyL_Value;
    	boolean joyR_Changed = lastJoyR != joyR_Value;
    	lastJoyL = joyL_Value; lastJoyR = joyR_Value;
    	
    	// If Speed changed update Motors' Speed
    	if(joyL_Changed || joyR_Changed) {
    		// Use one to detonate top speed is full-speed
    		driveSync.setSpeedPercentage(1, joyL_Value, joyR_Value);
    	}
    	
    	// Sets the Speed of the flywheel to the value of the Operator Joystick
    	flywheelSync.setSpeedPercentage(1, joyOp.getValue(LogitechAttack3_Axis.Y));
    	
    	// Turn the ball loader on Forward if the trigger is pressed on the Operator Joystick
    	if(joyOp.isButtonPressed(LogitechAttack3_Button.Trigger))
    		ballLoader.set(Value.kForward);
    	// Turn the ball loader on Backwards if the trigger is pressed on the Operator Joystick
    	else if(joyOp.isButtonPressed(LogitechAttack3_Button.Top_Lower))
    		ballLoader.set(Value.kReverse);
    	// Otherwise turn off the Motor
    	else ballLoader.set(Value.kOff);
    }

/** *********************************************************************************************************************** **\
 * 	- - - - - - - - - - - - - - - - - - - - - - - - - - - Joysick - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  *
\** *********************************************************************************************************************** **/

	// Joystick Constants
	private static final double JOYSTICK_DEADZONE = 0.05;
	
	// Joystick Objetcs
	private JoystickBase joyL, joyR;
	private JoystickBase joyOp;
	
    public void intiJoystick() {
    	joyL  = new LinerJoystick(0, JOYSTICK_DEADZONE);
    	joyR  = new LinerJoystick(1, JOYSTICK_DEADZONE);
    	joyOp = new LinerJoystick(2, JOYSTICK_DEADZONE);
    }
    
    public void updateJoystick() {
    	shooterTop = shooterBottom = joyOp.getValue(LogitechAttack3_Axis.Y);
    	
    	tankDriveLeft = joyL.getRawValue(LogitechAttack3_Axis.Y);
    	tankDriveRight = joyR.getRawValue(LogitechAttack3_Axis.Y);
    }
    
/** *********************************************************************************************************************** **\
 * 	- - - - - - - - - - - - - - - - - - - - - - - - - - - Shooter - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  *
\** *********************************************************************************************************************** **/

	// Shooter Constants
	private static final double FLYWHEEL_TOP_SPEED = 1;
	private static final double FLYWHEEL_BOTTOM_SPEED = 1;
	
	private static final int FLYWHEEL_TOP_INDEX = 1;
	private static final int FLYWHEEL_BOTTOM_INDEX = 1;
	
    private MotorGroup topFlywheel;
	private MotorGroup bottomFlywheel;
	private MotorGroupSyncronizer flywheelSync;

    private double shooterTop;
    private double shooterBottom;
    
    private KeyTable shooterDistancesTable;
    private double shootDistance;
	
	public void initShooter() {
		shooterDistancesTable = new KeyTable("Top Wheel", "Bottom Speed");
		shooterDistancesTable.load("/shooterTable.csv");
		
    	topFlywheel = new MotorGroup(new I2C_Encoder(ShooterRequests.TopEncoder), 
    			FLYWHEEL_TOP_SPEED, new Talon(FLYWHEEL_TOP_INDEX));
    	bottomFlywheel = new MotorGroup(new I2C_Encoder(ShooterRequests.BottomEncoder), 
    			FLYWHEEL_BOTTOM_SPEED, new Talon(FLYWHEEL_BOTTOM_INDEX));
    	flywheelSync = new MotorGroupSyncronizer(topFlywheel, bottomFlywheel).setDefaultModifiers(1, -1);
	}
	
	public void updateShooter() {
		shootDistance:
		if(shootDistance > 0) {
			double[] speeds = shooterDistancesTable.getClosestRow(shootDistance, 2);
			
			if(speeds == null) {
				DriverStation.reportError("Can not shoot from a distance of: " + shootDistance, false);
				break shootDistance;
			}
			
			shooterTop = speeds[0];
			shooterBottom = speeds[1];
		}
		
    	flywheelSync.setSpeedPercentage(1, shooterTop, shooterBottom);
	}
    
/** *********************************************************************************************************************** **\
 * 	- - - - - - - - - - - - - - - - - - - - - - - - - - Basic Drive - - - - - - - - - - - - - - - - - - - - - - - - - - - -  *
\** *********************************************************************************************************************** **/
	private static final double LEFT_DRIVE_TOP_SPEED = 1;
	private static final double RIGHT_DRIVE_TOP_SPEED = 1;
	
	private static final int DRIVE_LEFT_1_INDEX = 0;
	private static final int DRIVE_LEFT_2_INDEX = 0;
	private static final int DRIVE_RIGHT_1_INDEX = 0;
	private static final int DRIVE_RIGHT_2_INDEX = 0;
    
	// Drive Speed Controllers
	private MotorGroup leftSideDrive;
	private MotorGroup rightSideDrive;
	private MotorGroupSyncronizer driveSync;
	
	private void initDrive() {
		leftSideDrive = new MotorGroup(new I2C_Encoder(DriveRequests.LeftEncoder),  
    			LEFT_DRIVE_TOP_SPEED, new Talon(DRIVE_LEFT_1_INDEX), new Talon(DRIVE_LEFT_2_INDEX));
    	rightSideDrive = new MotorGroup(new I2C_Encoder(DriveRequests.RightEncoder),  
    			RIGHT_DRIVE_TOP_SPEED, new Talon(DRIVE_RIGHT_1_INDEX), new Talon(DRIVE_RIGHT_2_INDEX));
    	driveSync = new MotorGroupSyncronizer(leftSideDrive, rightSideDrive).setDefaultModifiers(1, -1);
	}
    
    private double tankDriveLeft;
    private double tankDriveRight;

    // --------------------------------------- Distance Control ------------------------------------------ \\
    
    private static final double WHEEL_RADIUS = 6;
    private static final double CLOSE_ENOUGH = 0.1;
    private static final double TRAVEL_SPEED = 0.5;
    private double travelDistance;
    
    public void driveDistance(double inches) {
    	travelDistance += inches;
    }
    
    public void updateDrive() {
    	updateDistance:
    	if(Math.abs(travelDistance) > 0) {
	    	if(Math.abs(travelDistance) < CLOSE_ENOUGH) {
	    		travelDistance = 0;
	    		break updateDistance;
	    	}
	    	
	    	travelDistance -= leftSideDrive.getEncoder().getTotal() * (travelDistance > 0 ? 1 : -1) * WHEEL_RADIUS;
	    	leftSideDrive.getEncoder().reset();
    	}
    
    	tankDriveLeft = tankDriveRight = TRAVEL_SPEED * (travelDistance > 0 ? 1 : travelDistance < 0 ? -1 : 0);
    	driveSync.setSpeedPercentage(1, tankDriveLeft, tankDriveRight);
    }
    
/** *********************************************************************************************************************** **\
 * 	- - - - - - - - - - - - - - - - - - - - - - - - - - Ball Loader - - - - - - - - - - - - - - - - - - - - - - - - - - - -  *
\** *********************************************************************************************************************** **/

    // Ball Loader Constants
	private static final int BALL_LOADER_INDEX = 1;
    
	private Relay ballLoader;
	private I2C_DigitalSensor photogate;
	
    private BallLoaderState loaderState;
    private static enum BallLoaderState {
    	Empty, LoadingBall, BallLoaded, EjectingBall, FiringBall;
    }
    
    public void initBallLoader() {
    	ballLoader = new Relay(BALL_LOADER_INDEX);
    	photogate = new I2C_DigitalSensor(ShooterRequests.Photogate1);
    }
    
    public void loadBall() { 
    	if(loaderState == BallLoaderState.Empty) {
    		loaderState = BallLoaderState.LoadingBall; 
    	}
	}
    
    public void ejectBall() { 
    	if(loaderState != BallLoaderState.Empty) {
    		loaderState = BallLoaderState.EjectingBall; 
    	}
	}
    
    public void fireBall() { 
    	if(loaderState != BallLoaderState.Empty) {
    		loaderState = BallLoaderState.FiringBall; 
    	}
	}
    
    public boolean isBallLoaderEmpty() { return loaderState == BallLoaderState.Empty; }
    
    private void updateBallLoader() {
    	ballLoading:
    	if(loaderState == BallLoaderState.LoadingBall) {
    		if(photogate.isPressed()) {
    			loaderState = BallLoaderState.BallLoaded;
    			break ballLoading;
    		}
    	}
    	
    	//  ------- No Code Beyond Switch -------
    	// switch returns from method in all cases
	    switch (loaderState) {
			case EjectingBall: 
	    		ballLoader.set(Value.kReverse);
	    	return;
			
			case FiringBall:
			case LoadingBall: 
	    		ballLoader.set(Value.kForward);
	    	return;

			case Empty: 
			case BallLoaded:
			default: 
    			ballLoader.set(Value.kOff);
    		return;
		}
    }
}
