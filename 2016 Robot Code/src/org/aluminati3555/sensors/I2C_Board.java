package org.aluminati3555.sensors;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class I2C_Board {
	private I2C i2c;
	private ArrayList<I2C_SensorBase> sensors;
	
	public I2C_Board(int address) {
		i2c = new I2C(Port.kOnboard, address);
		sensors = new ArrayList<>();
	}
	
	public void registerSensor(I2C_SensorBase sensor) {
		sensor.register(i2c);
		sensors.add(sensor);
	}
}