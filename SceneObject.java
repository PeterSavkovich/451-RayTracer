import java.awt.Color;
import java.util.ArrayList;


public interface SceneObject {
	public boolean intersect(Ray r);
	public Color shade(Ray r, ArrayList<Vector> lights, ArrayList<SceneObject> objects, Color background);
}
