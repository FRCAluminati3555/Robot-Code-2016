package org.aluminati3555.IO;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class CSV_Table {
	private ArrayList<Object[]> table;
	private Class<?>[] collumnTypes;
	
	public CSV_Table(Class<?>... collumnTypes) {
		table = new ArrayList<>();
		this.collumnTypes = collumnTypes;
	}
	
	public boolean addEntry(Object... elements) {
		for(int i = 0; i < elements.length; i ++)
			if(!elements[i].getClass().equals(collumnTypes[i]))
				throw new IllegalArgumentException("Type " + elements[i].getClass().getSimpleName() + " is not accepted for collumn " + i + "; "
						+ "only Objects of type " + collumnTypes[i].getSimpleName() + " are accepted in collumn " + i);
		
		return table.add(elements);
	}
	
	public void saveTable(String path) {
		int index = 0;
		String[] strings = new String[table.size()];
		
		for(Object[] row : table) {
			String buildString = "";
			for(Object element : row) {
				buildString += element.toString() + ", ";
			}
			
			strings[index ++] = buildString.substring(0, buildString.length() - 2) + "\n";
		}
		
		File_IO.write(path, false, strings);
	}
	
	public static CSV_Table loadTable(String path, Class<?>... collumnTypes) {
		CSV_Table table = new CSV_Table(collumnTypes);
		
		ByteBuffer buffer = File_IO.read(path);
		byte[] data = buffer.array();
		int offset = 0, length = 0;
		
		ArrayList<String> rows = new ArrayList<String>();
		while(offset + length < data.length) {
			if(data[offset + length] == '\n') {
				rows.add(new String(Arrays.copyOfRange(data, offset, offset + length)));
				offset += length;
				length = 0;
			}
			
			length ++;
		}
		
		for(String row : rows) {
			String[] elements = row.split(", ");
			Object[] rowElements = new Object[elements.length];
			
			for(int i = 0; i < elements.length; i ++) {
				try { rowElements[i] = collumnTypes[i].getConstructor(String.class).newInstance(elements[i]); } 
				catch (InstantiationException | IllegalAccessException | IllegalArgumentException | 
						InvocationTargetException | NoSuchMethodException | SecurityException e) {}
			}
			
			table.table.add(rowElements);
		}
		
		return table;
	}
	
	public Object getElement(int row, int collumn) {
		return table.get(row)[collumn];
	}
}
