package org.aluminati3555.tables;

import java.util.ArrayList;


public class Table {
	private static final int DECIMAL_PERCITION = 1;
	private static final double DECIMAL_SHIFT = Math.pow(10, DECIMAL_PERCITION);
	
	private ArrayList<double[]> table;
	private String[] keys;
	private int averageCount;
	
	public Table(int averageCount, String... keys) {
		this.keys = keys; this.averageCount = averageCount;
		table = new ArrayList<>();
	}
	
	public double addValue(double value, double... keyValues) {
		double[] row = getRow(keyValues);
		row[keys.length + 1 + (int) row[row.length - 1] ++ % averageCount] = value; 
		return recalcAverage(row);
	}
	
	public double recalcAverage(double[] row) {
		double total = 0;
		
		for(int i = 0; i < averageCount; i ++)
			total += row[keys.length + 1 + i];
		total /= averageCount;
		return row[keys.length] = total;
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
			newKeys[index ++] = (double) ((int) shiftedKey) / DECIMAL_SHIFT + Math.round(decimalsLeft);
		}
		
		return newKeys;
	}
}
