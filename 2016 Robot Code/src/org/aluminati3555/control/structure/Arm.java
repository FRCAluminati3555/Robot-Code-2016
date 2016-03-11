package org.aluminati3555.control.structure;

import edu.wpi.first.wpilibj.CANTalon;

public class Arm extends BaseStructure {
    //18 = 1 inch travel, 14 inches needed on threaded rod, encoder posibly 2
	
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
		
		armLeft.set(leftArmSpeed);
		armRight.set(rightArmSpeed);
	}

	public void disable() {

	}

	public void setLeftArmSpeed(double leftArmSpeed) { this.leftArmSpeed = leftArmSpeed; }
	public void setRightArmSpeed(double rightArmSpeed) { this.rightArmSpeed = rightArmSpeed; }
	public void setAngleMotorSpeed(double angleMotorSpeed) { this.angleMotorSpeed = angleMotorSpeed; }
}
