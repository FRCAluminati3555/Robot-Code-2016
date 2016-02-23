package org.aluminati3555.sensors;

public class I2C_Encoder extends I2C_SensorBase {
	private static final int GET_TOTAL_ADDRESS = 0;
	private static final int GET_SPEED_ADDRESS = 1;

	private static final int TICKS_PER_REVOLUTION = 100;
	
	private double startValue;
	
	public I2C_Encoder(I2C_Request request) {
		super(request);
	}

	public void postRegister() { reset(); }
	
	// Radinas
	public double getAngle() {
		return getTotal() / TICKS_PER_REVOLUTION % 1 * 2*Math.PI;
	}
	
	// inches / second
	public double getSpeed() {
		return (double) requestRead(GET_SPEED_ADDRESS, 4).getIntValue() / 10.0;
	}
	
	public double getTotal() {
		return (double) requestRead(GET_TOTAL_ADDRESS, 4).getIntValue() / 10.0 - startValue;
	}
	
	// NOTE: Only recodes the distance on ONE Encoder (a.k.a One Side)
	//							 Inches	
	public double getDistance(double radius) {
		return getTotal() / TICKS_PER_REVOLUTION * radius * Math.PI * 2;
	}
	
	public void reset() {
		startValue = requestRead(GET_TOTAL_ADDRESS, 4).getIntValue() / 10.0;
	} 
}
