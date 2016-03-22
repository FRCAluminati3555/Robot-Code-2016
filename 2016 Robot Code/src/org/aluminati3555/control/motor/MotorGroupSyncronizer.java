package org.aluminati3555.control.motor;

import java.util.Arrays;

public class MotorGroupSyncronizer {
	private MotorGroup[] groups;
	private double[] defaultModifiers;
	
	public MotorGroupSyncronizer(MotorGroup... groups) {
		this.groups = groups;
		this.defaultModifiers = new double[groups.length];
		Arrays.fill(defaultModifiers, 1);
	}
	
	public MotorGroupSyncronizer setDefaultModifiers(double... defaultModifiers) {
		if(defaultModifiers.length != this.defaultModifiers.length)
			throw new IllegalArgumentException("Default modifier list must contain the same numbe of modifiers as the number of motor group");
		this.defaultModifiers = defaultModifiers;
		return this;
	}
	
	public void setSpeedPercentage(double speed, double... associations) {
		if(associations.length < groups.length) 
			throw new IllegalArgumentException("Not enough associations passed");
		for(int i = 0; i < groups.length; i ++)
			groups[i].setSpeedPercentage(speed * associations[i] * defaultModifiers[i]);
	}
	
	public void setSpeed(double speed, double... associations) {
		if(associations.length < groups.length) 
			throw new IllegalArgumentException("Not enough associations passed");
		for(int i = 0; i < groups.length; i ++)
			groups[i].setSpeed(speed * associations[i]);
	} 
	
	public MotorGroup[] getMotorGroups() { return groups; }
}
