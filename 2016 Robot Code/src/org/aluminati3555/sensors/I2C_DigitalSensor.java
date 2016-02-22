package org.aluminati3555.sensors;

public class I2C_DigitalSensor extends I2C_SensorBase {
	public I2C_DigitalSensor(I2C_Request request) {
		super(request);
	}

	public boolean isPressed() {
		return requestRead(0, 4).getBooleanValue();
	}
}
