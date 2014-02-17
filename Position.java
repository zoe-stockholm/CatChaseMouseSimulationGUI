public class Position {

	// Represent a position (radius, angle) in polar coordinates.
	// All angles are in radians.
	// The internal representation of an angle is always at least 0
	// and less than 2 * PI. Also, the radius is always at least 1.

	private double myRadius;
	private double myAngle;

	// Constructors.
	public Position() {
		myRadius = 0;
		myAngle = 0;
	}

	public Position(Position p) {
		myRadius = p.myRadius;
		myAngle = p.myAngle;
	}

	public Position(double r, double theta) {
		myRadius = r;
		myAngle = theta;
	}

	// Return a printable version of the position.
	public String toString() {
		return "(" + myRadius + "," + myAngle + ")";
	}

	// Update the current position according to the given increments.
	// Preconditions: thetaChange is less than 2 * PI and greater than -2 * PI;
	// one of rChange and thetaChange is 0.
	public void update(double rChange, double thetaChange) {
		myRadius = myRadius + rChange;
		if (Math.abs(thetaChange) < 2*Math.PI) {
			myAngle = myAngle + thetaChange;
		} else {
			while (Math.abs(thetaChange) > 2*Math.PI) {
				thetaChange = thetaChange - 2*Math.PI;
			}
			myAngle = myAngle + thetaChange;
		}
		
	}

	public double getMyRadius ()  {
		return myRadius;
	}
	
	public double getMyAngle () {
		return myAngle;
	}
	
	public void setMyRadius (double radius) {
		myRadius = radius;
	}
	
	public void setMyAngle (double angle) {
		myAngle = angle;
	}

}

