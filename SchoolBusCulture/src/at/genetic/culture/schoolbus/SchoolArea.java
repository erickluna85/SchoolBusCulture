package at.genetic.culture.schoolbus;

public class SchoolArea {

	private BusStop school;
	private BusStop[] busStops;
	
	private int busSize = 60;
	private double costPerBus = 1000;
	private double costPerKm = 0.7;

	public SchoolArea(BusStop school, BusStop[] busStops, int busSize) {
		this.school = school;
		this.busStops = busStops;
		this.busSize = busSize;
	}

	public double[] getCostsForSchedule (int[] schedule) {
		if (schedule.length != busStops.length) {
			//throw new Exception("The given schedule with " + schedule.length + " elements doesn't match the busStops " + busStops.length + ".");
		}
		
		int kmCosts = 0;
		int busCosts = 0;
		int inBus = 0;
		boolean atSchool = true;
		
		// TODO way from school to first stop
		busCosts += costPerBus; // first bus
		kmCosts += distance(school, busStops[schedule[0]]) * costPerKm;
		
		for (int i = 0; i < schedule.length; i++) {
			
			if (busStops[schedule[i]].numberOfPupils + inBus > busSize) {
				if (i-1 < 0) {
					// Exception, bus can't be full on first tour
				}
				// have to go to school cause bus is full
				kmCosts += distance(busStops[schedule[i-1]], school) * costPerKm;
				busCosts += costPerBus;
				kmCosts += distance(school, busStops[schedule[i]]) * costPerKm;
				
				// put pupils into bus
				inBus = busStops[schedule[i]].numberOfPupils;
				atSchool = false;
			} else {
				if (!atSchool) kmCosts += distance(busStops[schedule[i-1]], busStops[schedule[i]]) * costPerKm;
				inBus += busStops[schedule[i]].numberOfPupils;
				atSchool = false;
			}
			
		}

		// way from last stop to school
		kmCosts += distance(busStops[schedule[schedule.length-1]], school) * costPerKm;
		
		return new double[] {kmCosts, busCosts};
	}
	
	public int countStops() {
		return busStops.length;
	}

	private double distance(BusStop a, BusStop b) {
		//System.out.println("dist between " + a + " and " + b);
		return Math.sqrt((a.x + b.x) * (a.x + b.x) + (a.y + b.y) * (a.y + b.y));
	}
}
