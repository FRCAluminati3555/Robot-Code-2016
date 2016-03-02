package org.aluminati3555.control.motor;

import org.aluminati3555.I2C.sensors.I2C_Encoder;

import edu.wpi.first.wpilibj.SpeedController;

public class MotorGroup {
	private SpeedController[] speedControllers;
	private I2C_Encoder encoder;
	private double groupTopSpeed;
	private double speed;
	
	// 										Inches / Second
	public MotorGroup(I2C_Encoder encoder, double groupTopSpeed, SpeedController... speedControllers) {
		this.groupTopSpeed = groupTopSpeed;
		this.speedControllers = speedControllers;
		this.encoder = encoder;
	}
	
	public double getSpeed() { return speed; }
	public void stop() { setSpeedPercentage(0); }
	
	public void setSpeedPercentage(double value) {
		this.speed = value;
		
		for(SpeedController speedController : speedControllers)
			speedController.set(value);
	}
	
	public void setSpeed(double inchPerSecond) {
		setSpeedPercentage(Math.abs(inchPerSecond) > groupTopSpeed ? inchPerSecond / groupTopSpeed : inchPerSecond > 1 ? 1 : -1);
	}
	
	public I2C_Encoder getEncoder() { return encoder; }
}
