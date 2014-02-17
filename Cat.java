public class Cat {
	private Position myPosition;

	// Constructors.
	public Cat() {
		myPosition = new Position();
	}

	public Cat(Position p) {
		myPosition = p;
	}

	// An access function.
	public Position getPosition() {
		return myPosition;
	}
	
	public void setPosition( Position p ) {
		myPosition = p;
	}

	// Move the cat around the statue:
	// one meter toward the statue if the cat sees the mouse (or up to the
	// statue
	// if the cat is closer to the statue than one meter away), or
	// 1.25 meters counterclockwise around the statue if the cat doesn't see the
	// mouse.
	// Return true if the cat eats the mouse during the move, false otherwise.
	public boolean move (Position mousePosition) {
		boolean eaten = false;
		if (this.myPosition.getMyRadius() >= 1) {
			if (this.myPosition.getMyRadius() * Math.cos(this.myPosition.getMyAngle() - mousePosition.getMyAngle()) >= 1) {
				if (this.myPosition.getMyRadius() >= 2) {
					this.myPosition.update(-1.0, 0.0);
					
				} else {
					this.myPosition.update(-this.myPosition.getMyRadius() % 1, 0.0);
				}
				
			} else {
				this.myPosition.update(0.0, 1.25);
			}
			
			if (this.myPosition.getMyRadius() == 1 && mousePosition.getMyAngle() < this.myPosition.getMyAngle()) {
				eaten = true;
			} else {
				eaten = false;
			}
		}
		return eaten;
	}
	

}