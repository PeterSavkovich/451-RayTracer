import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;


public class Ray {
	Vector origin;
	Vector direction;
	double time;
	SceneObject closestObject;
	
	public Ray(Vector camera, Vector direction) {
		this.origin = camera;
		this.direction = direction;
		this.time = 0;
	}
	
	public boolean trace(ArrayList<SceneObject> objects) {
		Iterator<SceneObject> objIter = objects.iterator();
		this.time = Double.MAX_VALUE;
		this.closestObject = null;
		while (objIter.hasNext()) {
			SceneObject obj = objIter.next();
			obj.intersect(this);
		}
		return this.closestObject != null;
	}
	
	public Color Shade(ArrayList<Vector> lights, ArrayList<SceneObject> objects, Color bg) {
		return this.closestObject.Shade(this, lights, objects, bg);
	}
	
	public Vector getPointOfIntersection() {
		
		return null;
	}
}
