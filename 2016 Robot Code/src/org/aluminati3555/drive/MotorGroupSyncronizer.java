package org.aluminati3555.drive;

public class MotorGroupSyncronizer {
	private MotorGroup[] groups;
	
	public MotorGroupSyncronizer(MotorGroup... groups) {
		this.groups = groups;
	}
	
	public void setSpeedPercentage(double speed, double... associations) {
		if(associations.length < groups.length) 
			throw new IllegalArgumentException("Not enough associations passed");
		for(int i = 0; i < groups.length; i ++)
			groups[i].setSpeedPercentage(speed * associations[i]);
	}
	
	public void setSpeed(double speed, double... associations) {
		if(associations.length < groups.length) 
			throw new IllegalArgumentException("Not enough associations passed");
		for(int i = 0; i < groups.length; i ++)
			groups[i].setSpeed(speed * associations[i]);
	} 
}
