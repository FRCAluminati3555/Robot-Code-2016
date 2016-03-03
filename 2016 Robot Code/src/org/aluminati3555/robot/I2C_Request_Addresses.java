package org.aluminati3555.robot;

import org.aluminati3555.I2C.I2C_SensorBase.I2C_Request;

public class I2C_Request_Addresses {
	public static enum DriveRequests implements I2C_Request {
		LeftEncoder(0x00), RightEncoder(0x01),
		
		LateralLeftEncoder(0x02), LateralRightEncoder(0x03),
		LateralLeftPotentiometer(0x04), LateralRightPotentiometer(0x05);
		
		private int address;
		DriveRequests(int address) { this.address = address; }
		public int id() { return address; }
		public int getBoardAddress() { return 0x0A; }
	}
	
	public static enum ShooterRequests implements I2C_Request {
		TopEncoder(0x00), BottomEncoder(0x01),
		Photogate1(0x02), Photogate2(0x03);
		
		private int address;
		ShooterRequests(int address) { this.address = address; }
		public int id() { return address; }
		public int getBoardAddress() { return 0x0B; }
	}
	
	public static enum ArmRequests implements I2C_Request {
		;
		
		private int address;
		ArmRequests(int address) { this.address = address; }
		public int id() { return address; }
		public int getBoardAddress() { return 0x0C; }
	}
}
