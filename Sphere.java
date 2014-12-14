import java.awt.Color;
import java.util.ArrayList;


public class Sphere implements SceneObject {
	Vector center;
	double radius;
	Material mat;
	
	public Sphere(Vector center, double radius, Material mat) {
		this.center = center;
		this.radius = radius;
		this.mat = mat;
	}
	
	public boolean intersect(Ray r) {
		Vector d = this.center.difference(r.origin);
		double v = r.direction.dot(d);
		
		if (v - this.radius > r.time) {
			return false;
		}
		
		double t = this.radius*this.radius + v*v - d.x*d.x - d.y*d.y - d.z*d.z;
		if (t < 0) {
			return false;
		}
		
		t = v - Math.sqrt(t);
		if (t > r.time || t < 0) {
			return false;
		}
		
		r.time = t;
		r.closestObject = this;
		return true;
	}

	public Color Shade(Ray r, ArrayList<Vector> lights,
					   ArrayList<SceneObject> objects, Color background) {
		Vector intersection = r.origin.sum(r.direction.scale(r.time));
		Vector normal = intersection.difference(this.center);
		normal.normalize();
		Vector viewer = r.direction.invert();
		
		return this.mat.Shade(intersection, normal, viewer, lights, objects, background);
	}
}
