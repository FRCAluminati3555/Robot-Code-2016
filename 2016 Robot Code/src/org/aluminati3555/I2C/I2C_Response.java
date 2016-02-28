package org.aluminati3555.I2C;

public class I2C_Response {
	private byte[] readData;
	public I2C_Response(byte[] readData) {
		this.readData = readData;
	}
	
	public char getCharValue() {
		if(readData.length < 1) {
			throw new IllegalStateException("Can not create Char with only " + readData.length + " bytes."
					+ " At least 1 byte is needed");
		}
		
		return (char) readData[0];
	}
	
	public byte getByteValue() {
		if(readData.length < 1) {
			throw new IllegalStateException("Can not create Byte with only " + readData.length + " bytes."
					+ " At least 1 byte is needed");
		}
		
		return readData[0];
	}
	
	public short getShortValue() {
		if(readData.length < 2) {
			throw new IllegalStateException("Can not create Short with only " + readData.length + " bytes."
					+ " At least 2 byte is needed");
		}
		
		return (short) (((short) readData[0] << 8 & 0xFF00) | ((short)readData[1] & 0x00FF));
	}
	
	public int getIntValue() {
		if(readData.length < 4) {
			throw new IllegalStateException("Can not create Int with only " + readData.length + " bytes."
					+ " At least 4 byte is needed");
		}
		
		return (readData[0] << 24 & (0xFF << 24)) | (readData[1] << 16 & (0xFF << 16))
				| (readData[2] << 8 & (0xFF << 8)) | (readData[3] & (0xFF << 0));
	}
	
	public float getFloatValue() {
		throw new IllegalStateException("This system does not suppot floting point numbers");
	}
	
	public double getDoubleValue() {
		throw new IllegalStateException("This system does not suppot floting point numbers");
	}
	
	public boolean getBooleanValue() {
		if(readData.length < 1) {
			throw new IllegalStateException("Can not create Boolean with only " + readData.length + " bytes."
					+ " At least 1 byte is needed");
		}
		
		return readData[0] != 0;
	}
}
