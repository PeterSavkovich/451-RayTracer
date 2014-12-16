import java.awt.Color;
import java.util.ArrayList;


public class Sphere implements SceneObject {
	Vector center;
	double radius;
	Material material;
	
	public Sphere(Vector center, double radius, Material material) {
		this.center = center;
		this.radius = radius;
		this.material = material;
	}
	
	public boolean intersect(Ray r) {
		Vector d = this.center.difference(r.origin);
		double v = r.direction.dot(d);
		
		if (v - this.radius > r.time) {
			//System.out.println("Not closest object");
			return false;
		}
		
		double t = this.radius*this.radius + v*v - d.x*d.x - d.y*d.y - d.z*d.z;
		if (t < 0) {
			//System.out.println("Ray missed");
			return false;
		}
		
		t = v - Math.sqrt(t);
		if (t > r.time || t < 0) {
			//System.out.println("Not closest object or behind");
			return false;
		}

		//System.out.println("Hit");
		r.time = t;
		r.closestObject = this;
		return true;
	}

	public Color shade(Ray r, Vector ambient, ArrayList<Vector> lights,
					   ArrayList<SceneObject> objects, Color background) {
		Vector intersection = r.origin.sum(r.direction.scale(r.time));
		Vector normal = intersection.difference(this.center).normalize();
		Vector viewer = r.direction.invert();
		
		return this.material.shade(intersection, normal, viewer, ambient, lights, objects, background);
	}
}
