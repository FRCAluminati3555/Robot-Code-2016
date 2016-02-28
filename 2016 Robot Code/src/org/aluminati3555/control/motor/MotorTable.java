package org.aluminati3555.control.motor;

import org.aluminati3555.tables.Table;

public class MotorTable {
	private Table table;
	private String path;
	
	public MotorTable(int averageCount, String path) {
		this.table = new Table(averageCount, "Battery Voltage", "Value");
		this.path = path; table.load(path);
	}

	public double addValue(double value, double... keyValues) {
		return table.addValue(value, keyValues);
	}

	public double getAverage(double... keyValues) {
		return table.getAverage(keyValues);
	}

	public void save() { table.save(path); }
	public void load() { table.load(path); }
}
