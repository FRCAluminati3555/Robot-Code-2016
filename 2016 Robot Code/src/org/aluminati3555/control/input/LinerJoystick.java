package org.aluminati3555.control.input;

import org.aluminati3555.control.input.JoysickMappings.Axis;

public class LinerJoystick extends JoystickBase {
	public LinerJoystick(int inputIndex, double deadzone) {
		super(inputIndex, deadzone);
	}

	public double getValue(Axis axis) {
		return getRawValue(axis);
	}
}
