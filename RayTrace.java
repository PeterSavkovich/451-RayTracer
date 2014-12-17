import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.json.*;


public class RayTrace {
	BufferedImage out;
	Color ambient;
	ArrayList<Vector> lightLocs;
	ArrayList<SceneObject> objects;
	Material currentMaterial;
	String fileName;
	
	Vector viewer, lookAt, up;
	Vector Du, Dv, Vp;
	double field;
	
	Color bg;
	
	int height, width;
	
	public static void main(String[] args) {
		RayTrace rt = new RayTrace();
		rt.init();
		for (int i = 0; i < rt.height; i++) {
			for (int j = 0; j < rt.width; j++) {
				rt.setPixel(i, j);
			}
		}
		System.out.println(rt.fileName);
		File f = new File(rt.fileName + ".png");
		System.out.println(f.getAbsolutePath());
		try {
			System.out.println(rt.out.toString());
			ImageIO.write(rt.out, "png", f);
			System.out.println("I did it");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		String json = "";
		try {
			json = this.readFile();
		} catch (IOException e) {
			System.out.println("Error reading Scene Config.txt");
			e.printStackTrace();
		}
		
		if (json.isEmpty()) {
			this.setSnowmanValues();
		} else {
			this.parseInJSON(json);
		}

		this.out = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		this.setupVectors();
	}
	
	private String readFile() throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\savkovpj\\workspace\\RayTracer\\src\\Scene Config.txt"));
        StringBuilder sb = new StringBuilder();
	    try {
	        String line = reader.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = reader.readLine();
	        }
	    } catch (Exception e) {
	    	System.out.println("Error reading Scene Config.txt");
			e.printStackTrace();
	    } finally {
	        reader.close();
	    }
        return sb.toString();
	}
	
	private void parseInJSON(String jString) {
		JSONObject json = null;
		try {
			json = new JSONObject(jString);
		} catch (JSONException e) {
			this.setSnowmanValues();
			System.out.println("Error parsing json string");
			e.printStackTrace();
		}
		if (json != null) {
			try {
				this.fileName = json.getString("fileName");
				this.height = json.getInt("height");
				this.width = json.getInt("width");
				this.field = json.getInt("fov");
				this.viewer = JSONParser.parseVector(json.getJSONObject("camera"));
				this.lookAt = JSONParser.parseVector(json.getJSONObject("focus point"));
				this.bg = JSONParser.parseColor(json.getJSONObject("background color"));
				this.ambient = JSONParser.parseColor(json.getJSONObject("ambient"));
				this.lightLocs = new ArrayList<Vector>();
				JSONArray jsonLights = json.getJSONArray("lights");
				for (int i = 0; i < jsonLights.length(); i++) {
					JSONObject currLight = jsonLights.getJSONObject(i);
					this.lightLocs.add(JSONParser.parseVector(currLight));
				}
				HashMap<String, Material> materials = new HashMap<String, Material>();
				JSONArray jsonMaterials = json.getJSONArray("materials");
				for (int i = 0; i < jsonMaterials.length(); i++) {
					JSONObject mat = jsonMaterials.getJSONObject(i);
					materials.put(mat.getString("name"),
							JSONParser.parseMaterial(mat));
				}
				this.objects = new ArrayList<SceneObject>();
				JSONArray jsonObjects = json.getJSONArray("objects");
				for (int i = 0; i < jsonObjects.length(); i++) {
					JSONObject obj = jsonObjects.getJSONObject(i);
					if (obj.getString("type").equals("sphere")) {
						this.objects.add(JSONParser.parseSphere(obj, materials));
					}
				}
			} catch (JSONException e) {
				this.setSnowmanValues();
				System.out.println("Error parsing json string");
				e.printStackTrace();
			}
		}
	}
	
	private void setupVectors() {
		Vector lookDir = this.lookAt.difference(this.viewer);
		this.up = new Vector(1, 0, 0);
		this.Du = lookDir.cross(this.up).normalize();
		this.Dv = lookDir.cross(this.Du).normalize();
		double db = this.width / (2*Math.tan(0.5*this.field*Math.PI/180));
		this.Vp = lookDir.normalize();
		this.Vp = this.Vp.scale(db).difference(this.Du.scale(this.width).sum(this.Dv.scale(this.height)).scale(0.5));
	}
	
	public void setPixel(int row, int col) {
		Vector dir = this.Du.scale(row).sum(this.Dv.scale(col)).sum(this.Vp);
		Ray ray = new Ray(this.viewer, dir);
		Color toColor;
		if (ray.trace(this.objects)) {
			toColor = ray.shade(this.ambient, this.lightLocs, this.objects, this.bg);
		} else {
			toColor = this.bg;
		}
		this.out.setRGB(col, row, toColor.getRGB());
	}
	
	private void setSnowmanValues() {
		this.height = 1000;
		this.width = 1000;
		this.field = 50;
		this.fileName = "fail";
		
		this.ambient = new Color(1, 1, 1);
		this.lightLocs = new ArrayList<Vector>();
		this.objects = new ArrayList<SceneObject>();
		
		this.lightLocs.add(new Vector(0, -30, 0));
		this.lightLocs.add(new Vector(20, -50, 60));
		//this.lights.add(new Vector(-15, -20, 70));
		
		this.currentMaterial = new Material(1, 0.55, 0, .6, .5, .2, 30, .8);
		//this.objects.add(new Sphere(new Vector(0, 0, 100), 5, this.currentMaterial));
		//this.objects.add(new Sphere(new Vector(0, 0, 100), 2, this.currentMaterial));
		//this.objects.add(new Sphere(new Vector(2, -5, 50), 2, this.currentMaterial));
		this.objects.add(new Sphere(new Vector(-5, 6, 30), 2, this.currentMaterial));
		//this.objects.add(new Sphere(new Vector(5, 2, 50), 2, this.currentMaterial));
		//this.objects.add(new Sphere(new Vector(13, 2, 50), 2, this.currentMaterial));
		this.currentMaterial = new Material(1, 0.98, 0.98, .2, 0.1, .9, 30, 0.01);
		this.objects.add(new Sphere(new Vector(10, 4, 40), 4, this.currentMaterial));
		this.objects.add(new Sphere(new Vector(10, -1.75, 40), 3, this.currentMaterial));
		this.objects.add(new Sphere(new Vector(10, -6, 40), 2, this.currentMaterial));
		
		this.objects.add(new Sphere(new Vector(-10, -10, 40), 0.1, this.currentMaterial));
		this.objects.add(new Sphere(new Vector(15, -15, 40), 0.1, this.currentMaterial));
		this.objects.add(new Sphere(new Vector(2, -3, 20), 0.1, this.currentMaterial));
		
		this.currentMaterial = new Material(205/255.0, 232/255.0, 1, .6, .5, .2, 30, .1);
		this.objects.add(new Sphere(new Vector(0, 252, 50), 245, this.currentMaterial));
		
		this.viewer = new Vector(0, 0, 0);
		this.lookAt = new Vector(2.5, 1, 40);
		this.bg = new Color(0, 0, 0);
	}
}
