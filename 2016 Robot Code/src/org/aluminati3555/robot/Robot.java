package org.aluminati3555.robot;

import org.aluminati3555.control.input.JoysickMappings.LogitechAttack3_Axis;
import org.aluminati3555.control.input.JoysickMappings.LogitechAttack3_Button;
import org.aluminati3555.control.input.JoysickMappings.LogitechExtreme3D_Axis;
import org.aluminati3555.control.input.JoysickMappings.LogitechExtreme3D_Button;
import org.aluminati3555.control.input.JoystickBase;
import org.aluminati3555.control.input.LinerJoystick;
import org.aluminati3555.control.structure.Arm;
import org.aluminati3555.control.structure.BallLoader;
import org.aluminati3555.control.structure.DriveBase;
import org.aluminati3555.control.structure.Shooter;
import org.aluminati3555.vision.CrossPlacer;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	private DriveBase driveBase;
	private BallLoader ballLoader;
	private Shooter shooter;
	private Arm arm;
	
	private CrossPlacer crossPlacer;
	
    public Robot() {
    	driveBase = new DriveBase(); //creates talon objects with the index
    	ballLoader = new BallLoader(); //creates i2c digital sensor and uses the photogate
    	shooter = new Shooter(); // creates key table and requests all of the neccecary encoders for the shooter
    	arm = new Arm();
    	
    	crossPlacer = new CrossPlacer();
    	autonomousInit();
    	intiJoystick();
    } 
    
//    private double lastJoyL, lastJoyR;
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
        	update();
        	
        	// Gives Robot time to process input properly
            Timer.delay(0.005);
        }
    }
    
    public void disabled() {
    	driveBase.disable();
    	ballLoader.disable();
    	shooter.disable();
    	arm.disable();
    	
    	crossPlacer.disable();
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
    	
    	crossPlacer.update(joyOp.isButtonPressed(LogitechExtreme3D_Button.Button_9) ? joyOp.getJoystick() : null);
    	
    	updateJoystick(); //updates the value of the Y axis value of the joystick
    	lastTime = System.currentTimeMillis();
    }
    
//    public void rawUpdate() {
//    	// Gets Input from the two drive Joysticks
//    	double joyL_Value = joyL.getValue(LogitechAttack3_Axis.Y);
//    	double joyR_Value = joyR.getValue(LogitechAttack3_Axis.Y);
//
//    	// Determines if the speed has changed
//    	boolean joyL_Changed = lastJoyL != joyL_Value;
//    	boolean joyR_Changed = lastJoyR != joyR_Value;
//    	lastJoyL = joyL_Value; lastJoyR = joyR_Value;
//    	
//    	// If Speed changed update Motors' Speed
//    	if(joyL_Changed || joyR_Changed) {
//    		// Use one to detonate top speed is full-speed
//    		driveBase.getDriveSync().setSpeedPercentage(1, joyL_Value, joyR_Value);
//    	}
//    	
//    	// Sets the Speed of the flywheel to the value of the Operator Joystick
//    	shooter.getFlywheelSync().setSpeedPercentage(1, joyOp.getValue(LogitechAttack3_Axis.Y));
//    	
//    	// Turn the ball loader on Forward if the trigger is pressed on the Operator Joystick
//    	if(joyOp.isButtonPressed(LogitechAttack3_Button.Trigger))
//    		ballLoader.getBallLoader().set(Value.kForward);
//    	// Turn the ball loader on Backwards if the trigger is pressed on the Operator Joystick
//    	else if(joyOp.isButtonPressed(LogitechAttack3_Button.Top_Lower))
//    		ballLoader.getBallLoader().set(Value.kReverse);
//    	// Otherwise turn off the Motor
//    	else ballLoader.getBallLoader().set(Value.kOff);
//    }

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
    	if(joyOp.isPOVPressed(180)) {
    		ballLoader.loadBall();
    	} else if(joyOp.isPOVPressed(0)) {
    		ballLoader.ejectBall();
    	} else {
    		ballLoader.stopLoading();
    	}
    	
    	shooter.setShooterTop((1 - (joyOp.getValue(LogitechExtreme3D_Axis.Slider) + 1) / 2) * .8);
    	shooter.setShooterBottom((1 - (joyOp.getValue(LogitechExtreme3D_Axis.Slider) + 1) / 2) * .8);
    	
    	SmartDashboard.putNumber("Shooter Speed: ", shooter.getFlywheelSync().getMotorGroups()[0].getSpeedPercentage());
    	
    	if(joyOp.isButtonPressed(LogitechExtreme3D_Button.Thumb) && Math.abs(joyOp.getRawValue(LogitechExtreme3D_Axis.Y)) > JOYSTICK_DEADZONE)
    		arm.setAngleMotorSpeed(joyOp.getRawValue(LogitechExtreme3D_Axis.Y));
    	
    	arm.setLeftArmSpeed(joyOp.isButtonPressed(LogitechAttack3_Button.Bottom_Right_Front) ? 0.5 : joyOp.isButtonPressed(LogitechAttack3_Button.Bottom_Left_Front) ? -.5 : 0);
    	arm.setRightArmSpeed(joyOp.isButtonPressed(LogitechAttack3_Button.Bottom_Right_Front) ? 0.5 : joyOp.isButtonPressed(LogitechAttack3_Button.Bottom_Left_Front) ? -.5 : 0);
    	
    	driveBase.setTankDriveLeft(joyL.getRawValue(LogitechAttack3_Axis.Y));
    	driveBase.setTankDriveRight(joyR.getRawValue(LogitechAttack3_Axis.Y));
    }
    
/** *********************************************************************************************************************** **\
 * 	- - - - - - - - - - - - - - - - - - - - - - - - - - Autonomous  - - - - - - - - - - - - - - - - - - - - - - - - - - - -  *
\** *********************************************************************************************************************** **/
    
	private SendableChooser autonmusChooser;
	
	public void autonomousInit() {
    	autonmusChooser = new SendableChooser();
    	
    	autonmusChooser.addDefault(AutonomousOptions.None.toString(), AutonomousOptions.None);
    	for(AutonomousOptions options : AutonomousOptions.values()) {
    		if(options != AutonomousOptions.None)
    	    	autonmusChooser.addDefault(options.toString(), options);
    	}
	}
	
	public static enum AutonomousOptions {
		None, 
		Dirve_25$prc, Dirve_50$prc, Dirve_75$prc, Dirve_100$prc;
		
		public String toString() {
			String raw = super.toString();
			String full = "";
			
			int escapedAt = -1;
			String escapeString = "";
			for(int i = 0; i < raw.length(); i ++) {
				char c = raw.charAt(i);
				
				if(escapedAt != -1) {
					escapeString += c;
					
					if(escapedAt - i == 3) {
						escapedAt = -1;
						full += escape(escapeString);
					}
					
					continue;
				}
				
				
				if(c == '_') {
					full += " ";
					continue;
				}
				
				if(c == '$') {
					escapedAt = i;
					escapeString = "";
					continue;
				}
				
				full += c;
			}
			
			if(escapedAt != -1) {
				full += escape(escapeString);
 			}
			
			return full;		
		}
		
		public String escape(String escapeChars) {
			if(escapeChars.equals("prc"))
				return "%";
			
			if(escapeChars.startsWith("$"))
				return escapeChars;
			
			return "";
		}
	}
	
	public void autonomous() {
		driveBase.setTankDriveLeft(0);
		driveBase.setTankDriveRight(0);
		
		switch((AutonomousOptions) autonmusChooser.getSelected()) {
			case Dirve_25$prc:
				autonomousDrive(.25);
			break;
				
			case Dirve_50$prc:
				autonomousDrive(.5);
			break;
				
			case Dirve_75$prc: 
				autonomousDrive(.75);
			break;
			
			case Dirve_100$prc:
				autonomousDrive(1);
			break;
				
			case None:
			default:
				break;
		}
	}
	
	public void autonomousDrive(double percentage) {
		driveBase.setTankDriveLeft(percentage);
		driveBase.setTankDriveRight(percentage);
		
		Timer.delay(3);
		
		driveBase.setTankDriveLeft(0);
		driveBase.setTankDriveRight(0);
	}
}
