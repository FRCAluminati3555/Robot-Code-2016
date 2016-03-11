package org.aluminati3555.robot;

import org.aluminati3555.control.input.JoysickMappings.LogitechAttack3_Axis;
import org.aluminati3555.control.input.JoysickMappings.LogitechAttack3_Button;
import org.aluminati3555.control.input.JoystickBase;
import org.aluminati3555.control.input.LinerJoystick;
import org.aluminati3555.control.structure.Arm;
import org.aluminati3555.control.structure.BallLoader;
import org.aluminati3555.control.structure.DriveBase;
import org.aluminati3555.control.structure.Shooter;

import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends SampleRobot {
	private DriveBase driveBase;
	private BallLoader ballLoader;
	private Shooter shooter;
	private Arm arm;
	
    public Robot() {
    	driveBase = new DriveBase(); //creates talon objects with the index
    	ballLoader = new BallLoader(); //creates i2c digital sensor and uses the photogate
    	shooter = new Shooter(); // creates key table and requests all of the neccecary encoders for the shooter
    	arm = new Arm();
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
    
    private long lastTime;
    
    public void update() {
    	int delta = (int) (System.currentTimeMillis() - lastTime);
    	
    	driveBase.update(delta); //checks if the ball is still loading from the photogate, if the ball is loaded break the load method
    	shooter.update(delta);
    	ballLoader.update(delta);
    	arm.update(delta);
    	
    	updateJoystick(); //updates the value of the Y axis value of the joystick
    	lastTime = System.currentTimeMillis();
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
    		driveBase.getDriveSync().setSpeedPercentage(1, joyL_Value, joyR_Value);
    	}
    	
    	// Sets the Speed of the flywheel to the value of the Operator Joystick
    	shooter.getFlywheelSync().setSpeedPercentage(1, joyOp.getValue(LogitechAttack3_Axis.Y));
    	
    	// Turn the ball loader on Forward if the trigger is pressed on the Operator Joystick
    	if(joyOp.isButtonPressed(LogitechAttack3_Button.Trigger))
    		ballLoader.getBallLoader().set(Value.kForward);
    	// Turn the ball loader on Backwards if the trigger is pressed on the Operator Joystick
    	else if(joyOp.isButtonPressed(LogitechAttack3_Button.Top_Lower))
    		ballLoader.getBallLoader().set(Value.kReverse);
    	// Otherwise turn off the Motor
    	else ballLoader.getBallLoader().set(Value.kOff);
    }

/** *********************************************************************************************************************** **\
 * 	- - - - - - - - - - - - - - - - - - - - - - - - - - - Joysick - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  *
\** *********************************************************************************************************************** **/

	// Joystick Constants
	private static final double JOYSTICK_DEADZONE = 0.05;
	
	// Joystick Objetcs
	private JoystickBase joyL, joyR;
	private JoystickBase joyOp;
	
	//initializes joystic objects
    public void intiJoystick() {
    	joyL  = new LinerJoystick(0, JOYSTICK_DEADZONE);
    	joyR  = new LinerJoystick(1, JOYSTICK_DEADZONE);
    	joyOp = new LinerJoystick(2, JOYSTICK_DEADZONE);
    }
    
    //checks the value of the joystck and updates it to the robot coorisponding to its purpose
    public void updateJoystick() {
    	shooter.setShooterTop(joyOp.getValue(LogitechAttack3_Axis.Y));
    	shooter.setShooterBottom(joyOp.getValue(LogitechAttack3_Axis.Y));
    	
    	arm.setAngleMotorSpeed(joyOp.isButtonPressed(LogitechAttack3_Button.Top_Left) ? 0.5 : joyOp.isButtonPressed(LogitechAttack3_Button.Top_Right) ? -.5 : 0);
    	
    	arm.setLeftArmSpeed(joyOp.isButtonPressed(LogitechAttack3_Button.Bottom_Left_Front) ? 0.5 : joyOp.isButtonPressed(LogitechAttack3_Button.Bottom_Left_Back)  ? -.5 : 0);
    	arm.setRightArmSpeed(joyOp.isButtonPressed(LogitechAttack3_Button.Bottom_Right_Front) ? 0.5 : joyOp.isButtonPressed(LogitechAttack3_Button.Bottom_Right_Back) ? -.5 : 0);
    	
    	driveBase.setTankDriveLeft(joyL.getRawValue(LogitechAttack3_Axis.Y));
    	driveBase.setTankDriveRight(joyR.getRawValue(LogitechAttack3_Axis.Y));
    }
}
