package org.aluminati3555.control.input;

import org.aluminati3555.control.input.JoysickMappings.Axis;
import org.aluminati3555.control.input.JoysickMappings.Button;

import edu.wpi.first.wpilibj.Joystick;

public abstract class JoystickBase {
	private Joystick joystick;
	
	public JoystickBase(int inputIndex) {
		joystick = new Joystick(inputIndex);
	}
	
	public abstract double getValue(Axis axis);
	
	public double getRawValue(Axis axis) { 
		return joystick.getRawAxis(axis.getIndex());
	}
	
	public boolean isButtonPressed(Button button) { 
		return joystick.getRawButton(button.getIndex());
	}
	
	public Joystick getJoystick() { return joystick; }
}
