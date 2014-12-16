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
		this.direction = direction.normalize();
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
	
	public Color shade(Vector ambient, ArrayList<Vector> lights, ArrayList<SceneObject> objects, Color bg) {
		return this.closestObject.shade(this, ambient, lights, objects, bg);
	}
	
//	public Vector getPointOfIntersection() {
//		
//		return null;
//	}
}
