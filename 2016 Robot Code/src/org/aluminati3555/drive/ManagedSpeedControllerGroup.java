package org.aluminati3555.drive;

import edu.wpi.first.wpilibj.SpeedController;

public class ManagedSpeedControllerGroup {
	private SpeedController[] speedControllers;
	private double groupTopSpeed;
	private double speed;
	
	// 									 Inches / Second
	public ManagedSpeedControllerGroup(double groupTopSpeed, SpeedController... speedControllers) {
		this.groupTopSpeed = groupTopSpeed;
		this.speedControllers = speedControllers;
	}
	
	public double getSpeed() { return speed; }
	public void stop() { setSpeedPercentage(0); }
	
	public void setSpeedPercentage(double value) {
		speed = value;
		for(SpeedController speedController : speedControllers)
			speedController.set(value);
	}
	
	public void setSpeed(double inchPerSecond) {
		setSpeedPercentage(Math.abs(inchPerSecond) > groupTopSpeed ? inchPerSecond / groupTopSpeed : 1);
	}
	
}
