package org.aluminati3555.control.motor;

import org.aluminati3555.tables.Table;

import edu.wpi.first.wpilibj.DriverStation;

public class MotorTable {
	private Table table;
	private String path;
	
	/**
	 * Creates a new Motor Table and loads it into memory
	 * 
	 * @param averageCount
	 * @param path
	 * @param keys
	 */
	public MotorTable(int averageCount, String path, String... keys) {
		this.table = new Table(averageCount, keys);
		this.path = path; table.load(path);
	}

	public double addValue(double value, double... keyValues) {
		return table.addValue(value, keyValues);
	}

	public double getAverage(double... keyValues) {
		return table.getAverage(keyValues);
	}

	public void save() { table.save(path); }
	public MotorTable load() { table.load(path); return this; }
	
	public static double getVoltage() {
		return DriverStation.getInstance().getBatteryVoltage();
	}
}
