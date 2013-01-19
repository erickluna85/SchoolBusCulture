package at.genetic.culture.schoolbus;

public class BusStop {
	protected int x;
	protected int y;
	protected int numberOfPupils;

	public BusStop(int x, int y, int numberOfPupils) {
		this.x = x;
		this.y = y;
		this.numberOfPupils = numberOfPupils;
	}
	
	public String toString () {
		return "(" + x + ", " + y + ") with " + numberOfPupils + " kids.";
	}
}