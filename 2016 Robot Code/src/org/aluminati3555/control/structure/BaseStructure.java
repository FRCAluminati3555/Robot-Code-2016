package org.aluminati3555.control.structure;

public abstract class BaseStructure {
	private int updateDelayMilli;
	private int sumDelay;
	
	public BaseStructure(int updateDelayMilli) {
		this.updateDelayMilli = updateDelayMilli;
	}
	
	public final void update(int deltaMilli) {
//		sumDelay += deltaMilli;
//		if(sumDelay >= updateDelayMilli) {
			sumDelay = 0; updateMethod();
//		}
	}
	
	protected abstract void updateMethod();
	public abstract void disable();
}
