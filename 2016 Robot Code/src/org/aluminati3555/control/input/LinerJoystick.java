package org.aluminati3555.control.input;

import org.aluminati3555.control.input.JoysickMappings.Axis;

public class LinerJoystick extends JoystickBase {
	public LinerJoystick(int inputIndex) {
		super(inputIndex);
	}

	public double getValue(Axis axis) {
		return getRawValue(axis);
	}
}
