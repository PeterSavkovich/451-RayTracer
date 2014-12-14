import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;


public class Material {
	public double r, g, b, specular, diffuse, ambient, shininess, reflectivity;
	
	public Material(double r, double g, double b, double spec, double diff, double ambi, double shin, double refl) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.specular = spec;
		this.diffuse = diff;
		this.ambient = ambi;
		this.shininess = shin;
		this.reflectivity = refl;
	}
	
	public Color Shade(Vector point, Vector normal,
					   Vector viewerDir, ArrayList<Vector> lights,
					   ArrayList<SceneObject> objects, Color bg) {
		Iterator lightIter = lights.iterator();
		
		double r = 0;
		double g = 0;
		double b = 0;
		while (lightIter.hasNext()) {
			Vector light = (Vector) lightIter.next();
			//Figure out ambient light
			Vector lDir = light.difference(point);
			lDir.normalize();
			//Ignoring directional lights
			
			Vector offset = point.sum(lDir.scale(0.001));
			Ray shadowRay = new Ray(offset, lDir);
			if (!shadowRay.trace(objects)) {
				double lambert = lDir.dot(normal);
				double diffuse = this.diffuse * lambert;
				r += this.r * diffuse;
				g += this.g * diffuse;
				b += this.b * diffuse;
				
				Vector rDir = normal.scale(2 * lambert).difference(lDir);
				double specular = this.specular * Math.pow(rDir.dot(viewerDir), this.shininess);
				r += specular;
				g += specular;
				b += specular;
			}
		}
		
		if (this.reflectivity > 0) {
			double t = viewerDir.dot(normal);
			if (t > 0) {
				t *= 2;
				Vector reflect = normal.scale(t).difference(viewerDir);
				Vector offset = point.sum(reflect.scale(0.001));
				Ray reflectedRay = new Ray(offset, reflect);
				if (reflectedRay.trace(objects)) {
					//THIS IS WHERE I LEFT OFF
				}
			}
		}
		
		return null;
	}
}
