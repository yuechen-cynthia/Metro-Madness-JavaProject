package com.unimelb.swen30006.metromadness.passengers;

import java.util.ArrayList;
import java.util.Random;

import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class PassengerGenerator {

	// Random number generator
	static final private Random random = new Random(30006);

	// Passenger id generator
	static private int idGen = 1;

	// The station that passengers are getting on
	public Station s;
	// The line they are travelling on
	public ArrayList<Line> lines;

	// The max volume
	public float maxVolume;

	public PassengerGenerator(Station s, ArrayList<Line> lines, float max) {
		this.s = s;
		this.lines = lines;
		this.maxVolume = max;
	}

	public Passenger[] generatePassengers() {
		int count = random.nextInt(4) + 1;
		Passenger[] passengers = new Passenger[count];
		for (int i = 0; i < count; i++) {
			passengers[i] = generatePassenger(random);
		}
		return passengers;
	}

	public Passenger[] generateNonCargoPassengers() {
		int count = random.nextInt(4) + 1;
		Passenger[] passengers = new Passenger[count];
		for (int i = 0; i < count; i++) {
			passengers[i] = generateNonCargoPassenger(random);
		}
		return passengers;
	}

	/**
	 * The generatePassenger method generates passenger in cargo station, which
	 * means all passenger can take 0~50kkg cargo.
	 */
	public Passenger generatePassenger(Random random) {
		// Pick a random station from the line
		Line l = this.lines.get(random.nextInt(this.lines.size()));
		int current_station = l.stations.indexOf(this.s);
		boolean forward = random.nextBoolean();

		// If we are the end of the line then set our direction forward or
		// backward
		if (current_station == 0) {
			forward = true;
		} else if (current_station == l.stations.size() - 1) {
			forward = false;
		}

		// Find the station
		int index = 0;

		if (forward) {
			index = random.nextInt(l.stations.size() - 1 - current_station) + current_station + 1;
		} else {
			index = current_station - 1 - random.nextInt(current_station);
		}
		Station s = l.stations.get(index);

		return generatePassengerChanged(idGen++, random, s);
	}

	/**
	 * The generatePassenger method generates passenger in active station, which
	 * means all passenger can't take any cargo.
	 */
	public Passenger generateNonCargoPassenger(Random random) {
		// Pick a random station from the line
		Line l = this.lines.get(random.nextInt(this.lines.size()));
		int current_station = l.stations.indexOf(this.s);
		boolean forward = random.nextBoolean();

		// If we are the end of the line then set our direction forward or
		// backward
		if (current_station == 0) {
			forward = true;
		} else if (current_station == l.stations.size() - 1) {
			forward = false;
		}

		// Find the station
		int index = 0;

		if (forward) {
			index = random.nextInt(l.stations.size() - 1 - current_station) + current_station + 1;
		} else {
			index = current_station - 1 - random.nextInt(current_station);
		}
		Station s = l.stations.get(index);

		return new Passenger(idGen++, this.s, s);
	}

	/**
	 * The method can create a new passenger, which is used in order to solve
	 * creator problem in class design.
	 */
	public Passenger generatePassengerChanged(int id, Random random, Station ss) {
		return new Passenger(id, random, this.s, ss);
	}

}
