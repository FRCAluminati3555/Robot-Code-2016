package org.aluminati3555.vision;

import static com.ni.vision.NIVision.imaqMeasureParticle;

import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.MeasurementType;

public class ShapeMatcher {
	public static class AABB {
		private int x, y;
		private int width, height;
		private int centerX, centerY;
		
		public AABB(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			
			this.centerX = x + width / 2;
			this.centerY = y + height / 2;
		}
		
		public static AABB loadAABB(Image image, boolean connectivity8, int particleIndex) {
			double width = imaqMeasureParticle(image, particleIndex, connectivity8 ? 1 : 0, MeasurementType.MT_BOUNDING_RECT_WIDTH);
			double height = imaqMeasureParticle(image, particleIndex, connectivity8 ? 1 : 0, MeasurementType.MT_BOUNDING_RECT_HEIGHT);
			double x = imaqMeasureParticle(image, particleIndex, connectivity8 ? 1 : 0, MeasurementType.MT_BOUNDING_RECT_LEFT);
			double y = imaqMeasureParticle(image, particleIndex, connectivity8 ? 1 : 0, MeasurementType.MT_BOUNDING_RECT_TOP);
			
			return new AABB((int) x, (int) y, (int) width, (int) height);
		}

		public int getX() { return x; }
		public int getY() { return y; }
		public int getWidth()  { return width; }
		public int getHeight() { return height; }
		public int getCenterX() { return centerX; }
		public int getCenterY() { return centerY; }

		public String toString() {
			return "AABB [x=" + x + ", y=" + y + ", width=" + width
					+ ", height=" + height + ", centerX=" + centerX
					+ ", centerY=" + centerY + "]";
		}
	}
}
