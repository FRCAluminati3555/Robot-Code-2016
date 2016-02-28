package org.aluminati3555.I2C.sensors;

import org.aluminati3555.I2C.I2C_SensorBase;

public class I2C_Potentieometer extends I2C_SensorBase {
	private static final double MIN_ANGLE = 0;
	private static final double MAX_ANGLE = 300;
	private static final double MAX_VALUE = 1023;
	
	public I2C_Potentieometer(I2C_Request request) {
		super(request);
	}

	public double getAngle() {
		return (double) requestRead(0, 4).getIntValue() / MAX_VALUE * MAX_ANGLE + MIN_ANGLE;
	}
}
