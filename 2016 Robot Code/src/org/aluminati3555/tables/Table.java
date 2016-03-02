package org.aluminati3555.tables;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;

public class Table {
	private static final int DECIMAL_PERCITION = 1;
	private static final double DECIMAL_SHIFT = Math.pow(10, DECIMAL_PERCITION);
	
	private ArrayList<double[]> table;
	private String[] keys;	// keys.length == Index of Average
	private int averageCount;
	
	public Table(int averageCount, String... keys) {
		this.keys = keys; this.averageCount = averageCount;
		table = new ArrayList<>();
	}
	
	public double addValue(double value, double... keyValues) {
		double[] row = getRow(keyValues);
		row[keys.length + (int) row[row.length - 1] ++ % averageCount] = value; 
		return recalcAverage(row);
	}
	
	public double recalcAverage(double[] row) {
		double total = 0;
		
		for(int i = 0; i < averageCount; i ++)
			total += row[keys.length + i];
		total /= averageCount;
		
		return row[keys.length] = total;
	}
	
	public double getAverage(double... keyValues) {
		return getRow(keyValues)[keys.length];
	}
	
	public double[] getRow(double... keyValues) {
		if(keyValues.length < keys.length)
			throw new IllegalArgumentException("No Enough Key Values");
		keyValues = groupKeyValues(keyValues);
		
		rowLoop:
		for(double[] row : table) {
			for(int i = 0; i < keys.length; i ++) {
				if(row[i] != keyValues[i]) {
					continue rowLoop;
				}
			}
			
			return row;
		}
		
		double[] row = new double[keys.length + 1 + averageCount + 1];
		for(int i = 0; i < keys.length; i ++) {
			row[i] = keyValues[i];
		} table.add(row);
		
		return row;
	}
	
	public double[] groupKeyValues(double... keyValues) {
		double[] newKeys = new double[keyValues.length];
		int index = 0; 
		for(double key : keyValues) {
			double shiftedKey = key * DECIMAL_SHIFT;
			double decimalsLeft = shiftedKey % 1;
			newKeys[index ++] = (double) ((int) shiftedKey + Math.round(decimalsLeft)) / DECIMAL_SHIFT;
		}
		
		return newKeys;
	}
	
	private void addRow(Object[] row) {
		double[] doubleRow = new double[row.length];
		int index = 0;
		
		for(Object object : row) {
			if(object instanceof Double)
				doubleRow[index ++] = ((Double) object).doubleValue();
			else if(object instanceof String)
				doubleRow[index ++] = Double.parseDouble((String) object);
			else
				DriverStation.reportError("Invalid Format for Table; Skipping value " + index, false); 
		}
		
		table.add(doubleRow);
	}
	
	public void save(String path) {
		CSV_Table csv = createCSV();
		csv.saveTable(path);
	}
	
	public Table load(String path) { 
		Table t = Table.load(path, this); 
		if(t != this)
			DriverStation.reportError("Failed to Load: " + path, false);
		return this;
	}
	
	public static Table load(String path, Table table) {
		Class<?>[] columnFormat = new Class<?>[table.keys.length + table.averageCount + 1];
		for(int i = 0; i < columnFormat.length; i ++)
			columnFormat[i] = String.class;
		CSV_Table csv = CSV_Table.loadTable(path, columnFormat);
		if(csv == null) return null;
		for(int i = 1; i < csv.getNumberOfRows(); i ++)
			table.addRow(csv.getRow(i));
		return table;
	}
	
	public CSV_Table createCSV() {
		Class<?>[] columnFormat = new Class<?>[keys.length + averageCount + 1];
		for(int i = 0; i < columnFormat.length; i ++)
			columnFormat[i] = String.class;
		CSV_Table table = new CSV_Table(columnFormat);
		
		table.addEntry((Object[]) keys);
		for(double[] row : this.table) {
			Object[] objectArray = new String[row.length];
			for(int i = 0; i < row.length; i ++)
				objectArray[i] = row[i] + "";
			table.addEntry(objectArray);
		}
		
		return table;
	}
}
