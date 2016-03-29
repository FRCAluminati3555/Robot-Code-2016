package org.aluminati3555.vision;

import static com.ni.vision.NIVision.imaqCountParticles;
import static com.ni.vision.NIVision.imaqCreateImage;
import static com.ni.vision.NIVision.imaqDrawLineOnImage;
import static com.ni.vision.NIVision.imaqDuplicate;
import static com.ni.vision.NIVision.imaqMask;
import static com.ni.vision.NIVision.imaqSetSimpleCalibration;

import java.nio.ByteBuffer;

import org.aluminati3555.IO.File_IO;
import org.aluminati3555.vision.ShapeMatcher.AABB;

import com.ni.vision.NIVision.AxisOrientation;
import com.ni.vision.NIVision.CalibrationUnit;
import com.ni.vision.NIVision.CoordinateSystem;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.GridDescriptor;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ImageType;
import com.ni.vision.NIVision.Point;
import com.ni.vision.NIVision.PointFloat;
import com.ni.vision.NIVision.ScalingMethod;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class CrossPlacer extends SampleRobot {
	private USBCamera camera;
	private Point point;
	private double xCal, yCal;
	
	public CrossPlacer() {
		camera = new USBCamera("cam0");
		camera.setSize(320, 240);
		camera.startCapture();
		
		ByteBuffer buffer = File_IO.read("/targetPoint.txt");
		if(buffer != null) {
			String[] line = new String(buffer.array()).split(",");
			point = new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
			xCal = point.x; yCal = point.y;
		} else
			point = new Point();
	}

	public void update(Joystick joystick) {
		if(joystick != null) {
			point.x = Math.min((int) (xCal += joystick.getX() * 10), 320);
			point.y = Math.min((int) (yCal += joystick.getY() * 10), 240);
		}
		
		// Acquire Image
		Image maskImage = imaqCreateImage(ImageType.IMAGE_U8, 7);			
		Image image = imaqCreateImage(ImageType.IMAGE_U8, 7);
		camera.getImage(image);
		imaqDuplicate(maskImage, image);
		
		// Filter Image
		Image filterImage = imaqCreateImage(ImageType.IMAGE_U8, 7);
		filterImage = ImageFilter.filter(filterImage, image);
		imaqMask(maskImage, maskImage, filterImage);
		
		// "Calibrate" Image
		GridDescriptor gridDescriptor = new GridDescriptor(1, 1, CalibrationUnit.UNDEFINED);
		CoordinateSystem coordinateSystem = new CoordinateSystem(new PointFloat(0, 0), 0, AxisOrientation.INDIRECT);
		imaqSetSimpleCalibration(filterImage, ScalingMethod.SCALE_TO_FIT, 1, gridDescriptor, coordinateSystem);
		
		// Setup AABB for all particles
		int particleCount = imaqCountParticles(filterImage, 1);
		AABB[] particles = new AABB[particleCount];
		for(int i = 0; i < particleCount; i ++) {
			particles[i] = AABB.loadAABB(filterImage, true, i);
		}
		
		// For every AABB (a.k.a Every Particle)
		for(AABB aabb : particles) { // Shape Match
			imaqDrawLineOnImage(image, image, DrawMode.DRAW_VALUE,
					new Point(aabb.getCenterX(), aabb.getY()), new Point(aabb.getCenterX(), aabb.getY() + aabb.getHeight()), 255);
			imaqDrawLineOnImage(image, image, DrawMode.DRAW_VALUE,
					new Point(aabb.getX(), aabb.getCenterY()), new Point(aabb.getX() + aabb.getWidth(), aabb.getCenterY()), 255);
		}
		
		imaqDrawLineOnImage(image, image, DrawMode.DRAW_VALUE,
				new Point(point.x, 0), new Point(point.x, 240), 255);
		imaqDrawLineOnImage(image, image, DrawMode.DRAW_VALUE,
				new Point(0, point.y), new Point(320, point.y), 255);
		
		CameraServer.getInstance().setImage(image);
		
		image.free();
		maskImage.free();
		filterImage.free();
	}
	
	public void disable() {
		File_IO.write("/targetPoint.txt", false, point.x + ",", point.y + "");
	}
}