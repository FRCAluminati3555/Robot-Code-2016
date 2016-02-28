package org.aluminati3555.I2C;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;

public class I2C_SensorBase {
	private I2C i2c;
	private I2C_Request request;
	
	public I2C_SensorBase(I2C_Request request) {
		this.request = request;
	}
	
	public void register(I2C i2c) { 
		this.i2c = i2c; postRegister();
	}
	
	public void sendRequest(int requestAddress) {
		if(i2c == null) throw new UnregisteredI2CException();
    	i2c.write(requestAddress, request.id());
    }
    
    public I2C_Response readData(int size) {
		if(i2c == null) throw new UnregisteredI2CException();
    	byte[] data = new byte[size];
    	i2c.readOnly(data, size);
    	return new I2C_Response(data);
    }
    
    public I2C_Response requestRead(int requestAddress, int size) {
    	sendRequest(requestAddress);
    	Timer.delay(0.01);
    	return readData(size);
    }
    
    protected void postRegister() {}
    
    protected static interface I2C_Request { public int id(); }
    public static final class UnregisteredI2CException extends RuntimeException {
		private static final long serialVersionUID = -5308586672027533658L;
    }
}
