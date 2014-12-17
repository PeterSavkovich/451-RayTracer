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
	
	public Color shade(Vector point, Vector normal, Vector viewerDir,
					   Color ambient, ArrayList<Vector> lights,
					   ArrayList<SceneObject> objects, Color bg) {
		Iterator lightIter = lights.iterator();
		
		double r = this.ambient * this.r * ambient.getRed()/255.0;
		double g = this.ambient * this.g * ambient.getGreen()/255.0;
		double b = this.ambient * this.b * ambient.getBlue()/255.0;
		while (lightIter.hasNext()) {
			Vector light = (Vector) lightIter.next();
			Vector lDir = light.difference(point).normalize();
			
			Vector offset = point.sum(lDir.scale(0.001));
			Ray shadowRay = new Ray(offset, lDir);
			if (!shadowRay.trace(objects)) {
				double lambert = lDir.dot(normal);
				double diffuse = this.diffuse * lambert;
				r += this.r * diffuse;
				g += this.g * diffuse;
				b += this.b * diffuse;
				
				Vector rDir = normal.scale(2 * lambert).difference(lDir);
				double spec = rDir.dot(viewerDir);
				if (spec > 0) {
					spec = this.specular * Math.pow(spec, this.shininess);
					r += spec;
					g += spec;
					b += spec;
				}
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
					Color reflectedColor = reflectedRay.shade(ambient, lights, objects, bg);
					r += this.reflectivity * reflectedColor.getRed()/255.0;
					g += this.reflectivity * reflectedColor.getGreen()/255.0;
					b += this.reflectivity * reflectedColor.getBlue()/255.0;
				} else {
					r += this.reflectivity * bg.getRed()/255.0;
					g += this.reflectivity * bg.getGreen()/255.0;
					g += this.reflectivity * bg.getBlue()/255.0;
				}
			}
		}
		
		if (r > 1) { r = 1; }
		if (g > 1) { g = 1; }
		if (b > 1) { b = 1; }
		if (r < 0 && r > -1) { r = 0; }
		if (g < 0 && g > -1) { g = 0; }
		if (b < 0 && b > -1) { b = 0; }
		if (r < 0 || g < 0 || b < 0) { System.out.println("r:" + r + " g:" + g + " b:" + b); }
		return new Color((float)r, (float)g, (float)b);
	}
}
