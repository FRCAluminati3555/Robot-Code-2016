package org.aluminati3555.control.input;

import org.aluminati3555.control.input.JoysickMappings.Axis;

public class LogarithmicJoystick extends JoystickBase {
	public LogarithmicJoystick(int inputIndex) {
		super(inputIndex);
	}

	public double getValue(Axis axis) {
		double value = getRawValue(axis);
		double log = Math.log(1 - Math.abs(value));
		return value < 0 ? Math.max(-1, log) : Math.max(1, -log);
	}
}
