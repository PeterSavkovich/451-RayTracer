import java.awt.Color;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONParser {
	
	public static Vector parseVector(JSONObject toParse) {
		try {
			Double x = toParse.getDouble("x");
			Double y = toParse.getDouble("y");
			Double z = toParse.getDouble("z");
			
			return new Vector(x, y, z);
		} catch (JSONException e) {
		}
		return null;
	}
	
	public static Color parseColor(JSONObject toParse) {
		try {
			return new Color((float) toParse.getDouble("r"),
							 (float) toParse.getDouble("g"),
							 (float) toParse.getDouble("b"));
		} catch (JSONException e) {
		}
		return null;
	}
	
	public static Material parseMaterial(JSONObject toParse) {
		try {
			return new Material(toParse.getDouble("r"),
								toParse.getDouble("g"),
								toParse.getDouble("b"),
								toParse.getDouble("spec"),
								toParse.getDouble("diff"),
								toParse.getDouble("ambi"),
								toParse.getDouble("shin"),
								toParse.getDouble("refl"));
		} catch (JSONException e) {
		}
		return null;
	}
	
	public static Sphere parseSphere(JSONObject toParse, HashMap<String, Material> materials) {
		try {
			String matName = toParse.getString("material");
			if (materials.containsKey(matName)) {
				return new Sphere(parseVector(toParse.getJSONObject("center")),
								  toParse.getDouble("radius"),
								  materials.get(matName));
			}
		} catch (JSONException e) {
		}
		return null;
	}

}
