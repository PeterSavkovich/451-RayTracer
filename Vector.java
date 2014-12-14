
public class Vector {
	public double x, y, z;
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double magnitude() {
		return (double) Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}
	
	public double dot(Vector other) {
		return (this.x*other.x + this.y*other.y + this.z*other.z);
	}
	
	public Vector cross(Vector other) {
		return new Vector(this.y*other.z - this.z*other.y,
						  this.z*other.x - this.x*other.z,
						  this.x*other.y - this.y*other.x);
	}
	
	public Vector sum(Vector other) {
		return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
	}
	
	public Vector difference(Vector other) {
		return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
	}
	
	public Vector scale(double factor) {
		return new Vector(this.x * factor, this.y * factor, this.z * factor);
	}
	
	public Vector invert() {
		return this.scale(-1);
	}
	
	public double distanceTo(Vector other) {
		return this.difference(other).magnitude();
	}
	
	public void normalize() {
		double magnitude = this.magnitude();
		this.x /= magnitude;
		this.y /= magnitude;
		this.z /= magnitude;
	}
}
