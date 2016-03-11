package org.aluminati3555.control.structure;

import org.aluminati3555.I2C.sensors.I2C_DigitalSensor;
import org.aluminati3555.robot.I2C_Request_Addresses.ShooterRequests;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;

public class BallLoader extends BaseStructure {

	// Ball Loader Constants
	private static final int BALL_LOADER_INDEX = 2;  //change to appropriate spot of the real wire 

	private Relay ballLoader;
	private I2C_DigitalSensor photogate;
	
    private BallLoaderState loaderState;
    
    //creats a list of possible states that the loader could be in
    private static enum BallLoaderState {
    	Empty, LoadingBall, BallLoaded, EjectingBall, FiringBall;
    }
    
    //initializes ball loader objects
    public void initBallLoader() {
    	
    }
    
    //starts method to load the ball
    public void loadBall() { 
    	if(loaderState == BallLoaderState.Empty) {
    		loaderState = BallLoaderState.LoadingBall; 
    	}
	}

   //starts method to eject the ball 
    public void ejectBall() { 
    	if(loaderState != BallLoaderState.Empty) {
    		loaderState = BallLoaderState.EjectingBall; 
    	}
	}
    
    //starts the method to fire the ball
    public void fireBall() { 
    	if(loaderState != BallLoaderState.Empty) {
    		loaderState = BallLoaderState.FiringBall; 
    	}
	}
    
    //checks if the loader is empty
    public boolean isBallLoaderEmpty() { return loaderState == BallLoaderState.Empty; }
	    
	
	public BallLoader() {
		super(10);
		
		ballLoader = new Relay(BALL_LOADER_INDEX);
    	photogate = new I2C_DigitalSensor(ShooterRequests.Photogate1);
		
	}

	//updates the ball loader state to the current state
	protected void updateMethod() {
    	ballLoading:
    	if(loaderState == BallLoaderState.LoadingBall) {
    		if(photogate.isPressed()) {
    			loaderState = BallLoaderState.BallLoaded;
    			break ballLoading;
    		}
    	}
    	
    	  //------- No Code Beyond Switch -------\\
    	// switch returns from method in all cases
	    switch (loaderState) {
			case EjectingBall: 
	    		ballLoader.set(Value.kReverse);
	    	return;
			
			case FiringBall:
			case LoadingBall: 
	    		ballLoader.set(Value.kForward);
	    	return;

			case Empty: 
			case BallLoaded:
			default: 
    			ballLoader.set(Value.kOff);
    		return;
		}
	}

	public void disable() {

	}

	public Relay getBallLoader() { return ballLoader; }
	public void setLoaderState(BallLoaderState loaderState) { this.loaderState = loaderState; }
}
