package org.aluminati3555.drive;

import edu.wpi.first.wpilibj.SpeedController;

public class ManagedSpeedControllerGroup {
	private SpeedController[] speedControllers;
	
	public ManagedSpeedControllerGroup(int groupTopSpeed, SpeedController... speedControllers) {
		this.speedControllers = speedControllers;
	}
}
