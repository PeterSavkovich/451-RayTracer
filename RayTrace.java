import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;


public class RayTrace {
	Image out;
	Graphics g;
	ArrayList<SceneObject> objects;
	ArrayList<Vector> lights;
	Material currentObject;
	
	Vector viewer, viewDir, up;
	
	double field;
	
	Color bg;
	
	int width, height;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	public void setPixel(int row, int col) {
		Vector dir = new Vector();
		Ray ray = new Ray(this.viewer, dir);
		if (ray.trace(this.objects)){
			this.g.setColor(ray.shade(lights, objects, bg));
		}
	}
}
