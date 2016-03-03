package org.aluminati3555.control.input;

import org.aluminati3555.control.input.JoysickMappings.Axis;
import org.aluminati3555.control.input.JoysickMappings.Button;

import edu.wpi.first.wpilibj.Joystick;

public abstract class JoystickBase {
	private Joystick joystick;
	private double deadzone;
	
	public JoystickBase(int inputIndex, double deadzone) {
		joystick = new Joystick(inputIndex);
		this.deadzone = deadzone;
	}
	
	public abstract double getValue(Axis axis);
	
	public final double getRawValue(Axis axis) { 
		double rawValue = joystick.getRawAxis(axis.getIndex());
		return Math.abs(rawValue) < deadzone ? 0 : rawValue;
	}
	
	public boolean isButtonPressed(Button button) { 
		return joystick.getRawButton(button.getIndex());
	}
	
	public Joystick getJoystick() { return joystick; }
}
