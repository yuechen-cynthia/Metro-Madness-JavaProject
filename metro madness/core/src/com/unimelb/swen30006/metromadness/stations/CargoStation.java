package com.unimelb.swen30006.metromadness.stations;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.trains.Train;

/**
 * The Cargo Station which can stop the cargo train.
 */
public class CargoStation extends ActiveStation {

	// Logger
	private static Logger logger = LogManager.getLogger();

	public PassengerGenerator g;
	public ArrayList<Passenger> waiting;

	/**
	 * Create a new, empty cargo station, initialized as empty
	 */
	public CargoStation(float x, float y, PassengerRouter router, String name, float maxPax) {
		super(x, y, router, name, maxPax);
		this.waiting = new ArrayList<Passenger>();
		this.g = new PassengerGenerator(this, this.lines, maxPax);

	}

	/**
	 * The method of processing the passengers embark the train and generate new
	 * waiting passenger when a train stopping in a station.
	 */
	@Override
	public void enter(Train t) throws Exception {
		if (trains.size() >= PLATFORMS) {
			throw new Exception();
		} else {
			// Add the train
			this.trains.add(t);
			// Add the waiting passengers
			Iterator<Passenger> pIter = this.waiting.iterator();
			while (pIter.hasNext()) {
				Passenger p = pIter.next();
				try {
					logger.info("Passenger " + p.id + " carrying " + p.getCargo().getWeight()
							+ " kg cargo embarking at " + this.name + " heading to " + p.destination.name);
					t.embark(p);
					pIter.remove();
				} catch (Exception e) {
					// Do nothing, already waiting
					break;
				}
			}

			// Do not add new passengers if there are too many already
			if (this.waiting.size() > maxVolume) {
				return;
			}
			// Add the new passenger
			Passenger[] ps = this.g.generatePassengers();
			for (Passenger p : ps) {
				try {
					logger.info("Passenger " + p.id + " carrying " + p.getCargo().getWeight() + " kg embarking at "
							+ this.name + " heading to " + p.destination.name);
					t.embark(p);
				} catch (Exception e) {
					this.waiting.add(p);
				}
			}
		}
	}

}
