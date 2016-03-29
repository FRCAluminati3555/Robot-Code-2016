package org.aluminati3555.control.structure;

import edu.wpi.first.wpilibj.CANTalon;

public class Arm extends BaseStructure {
    //18 = 1 inch travel, 14 inches needed on threaded rod, encoder posibly 2
	
	private static final double MAX_DISTANCE = 1; // ? = 15 + width of bot
	private static final double MIN_EXTENTION = 32.5; // Inches
	
	private CANTalon angleMotor;
	private CANTalon armLeft;
	private CANTalon armRight;
	
	public Arm() {
		super(10);
		
		angleMotor = new CANTalon(42);
		armLeft = new CANTalon(43);
		armRight = new CANTalon(44);
	}

	private double leftArmSpeed;
	private double rightArmSpeed;
	private double angleMotorSpeed;
	
	protected void updateMethod() {
		angleMotor.set(angleMotorSpeed);
		
//		if(calcMaxExtention(angle) * EXTEND_DIRECTION > distanceEncode.getDistance()) {
//			if(leftArmSpeed * EXTEND_DIRECTION > 0) {
//				leftArmSpeed = 0;
//				rightArmSpeed = 0;
//			}
//			
//		} else {
//			leftArmSpeed = EXTEND_DIRECTION * 0.1;
//			rightArmSpeed = EXTEND_DIRECTION * 0.1;
//		}
		
		armLeft.set(leftArmSpeed);
		armRight.set(rightArmSpeed);
	}

	public void disable() {

	}

	public double calcMaxExtention(double angle) {
		return MAX_DISTANCE / Math.cos(Math.toRadians(angle)) - MIN_EXTENTION;
	}
	
	public void setLeftArmSpeed(double leftArmSpeed) { this.leftArmSpeed = leftArmSpeed; }
	public void setRightArmSpeed(double rightArmSpeed) { this.rightArmSpeed = rightArmSpeed; }
	public void setAngleMotorSpeed(double angleMotorSpeed) { this.angleMotorSpeed = angleMotorSpeed; }
}
