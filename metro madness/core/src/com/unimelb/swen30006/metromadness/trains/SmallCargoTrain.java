package com.unimelb.swen30006.metromadness.trains;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

/**
 * A small capacity cargo train.
 */
public class SmallCargoTrain extends SmallPassengerTrain {

	public int maxCargoCapacity;

	/**
	 * Create a new, empty cargo train with small capacity, initialized as empty
	 */
	public SmallCargoTrain(Line trainLine, Station start, boolean forward, String name, int trainsize, int capacity) {
		super(trainLine, start, forward, name, trainsize);
		this.maxCargoCapacity = capacity;

	}

	/**
	 * The method makes the passenger embark to the station.
	 */
	public void embark(Passenger p) throws Exception {
		// Judge the max size and max capacity.
		if ((this.passengers.size() > this.trainSize)
				&& (currentCapacity() + p.getCargo().getWeight() > this.maxCargoCapacity)) {
			throw new Exception();
		}
		this.passengers.add(p);
	}
}
