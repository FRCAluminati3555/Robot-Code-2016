package org.aluminati3555.tables;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;

public class KeyTable {
	private static final int DECIMAL_PERCITION = 1;
	private static final double DECIMAL_SHIFT = Math.pow(10, DECIMAL_PERCITION);
	
	private ArrayList<double[]> table;
	private String[] keys;	
	
	public KeyTable(String... keys) {
		this.keys = keys;
		table = new ArrayList<>();
	}
	
	public double[] addValue(double value, double... keyValues) {
		double[] row = getRow(keyValues);
		row[keys.length] = value; 
		return row;
	}
	
	public double getValue(double... keyValues) {
		return getRow(keyValues)[keys.length];
	}
	
	public ArrayList<double[]> getRows(double value) {
		ArrayList<double[]> rows = new ArrayList<>();
		for(double[] row : table) {
			if(row[keys.length] == value) {
				rows.add(row);
			}
		}
		
		return rows;
	}
	
	public double[] getClosestRow(double value, int index) {
		double differance = Double.MAX_VALUE;
		int bestCaseIndex = -1;
		
		for(int i = 0; i < table.size(); i ++) {
			double checkVal = Math.abs(table.get(i)[index] - value);
			if(checkVal < differance) {
				bestCaseIndex = i;
				differance = checkVal;
			}
		}
		
		return table.get(bestCaseIndex);
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
		
		double[] row = new double[keys.length + 1];
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
	
	public KeyTable load(String path) { 
		KeyTable t = KeyTable.load(path, this); 
		if(t != this)
			DriverStation.reportError("Failed to Load: " + path, false);
		return this;
	}
	
	public static KeyTable load(String path, KeyTable table) {
		Class<?>[] columnFormat = new Class<?>[table.keys.length + 1];
		for(int i = 0; i < columnFormat.length; i ++)
			columnFormat[i] = String.class;
		CSV_Table csv = CSV_Table.loadTable(path, columnFormat);
		if(csv == null) return null;
		for(int i = 1; i < csv.getNumberOfRows(); i ++)
			table.addRow(csv.getRow(i));
		return table;
	}
	
	public CSV_Table createCSV() {
		Class<?>[] columnFormat = new Class<?>[keys.length + 1];
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
