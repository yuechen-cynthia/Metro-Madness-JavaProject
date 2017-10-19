package com.unimelb.swen30006.metromadness.stations;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.passengers.PassengerGenerator;
import com.unimelb.swen30006.metromadness.routers.PassengerRouter;
import com.unimelb.swen30006.metromadness.tracks.Line;
import com.unimelb.swen30006.metromadness.trains.Train;

public class ActiveStation extends Station {
	// Logger
	private static Logger logger = LogManager.getLogger();

	public PassengerGenerator g;
	public ArrayList<Passenger> waiting;
	public float maxVolume;

	public ActiveStation(float x, float y, PassengerRouter router, String name, float maxPax) {
		super(x, y, router, name);
		this.waiting = new ArrayList<Passenger>();
		this.g = new PassengerGenerator(this, this.lines, maxPax);
		this.maxVolume = maxPax;
	}

	/**
	 * The enter method processes the passengers embark the train and generate
	 * new waiting passenger when a train stopping in a "Active station".
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
			// Only the passengers without cargo can be generated in active
			// station.
			Passenger[] ps = this.g.generateNonCargoPassengers();
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

	/**
	 * The enter method processes the passengers embark the train and generate
	 * new waiting passenger when a train stopping in a "Cargo station".
	 */
	public void enterCargoStation(Train t) throws Exception {

		if (trains.size() >= PLATFORMS) {
			throw new Exception();
		} else {
			// Add the train
			this.trains.add(t);
			// Add the waiting passengers
			Iterator<Passenger> pIter = this.waiting.iterator();
			while (pIter.hasNext()) {
				Passenger p = pIter.next();
				if (p.getCargo().getWeight() == 0) {
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
			}

			// Do not add new passengers if there are too many already
			if (this.waiting.size() > maxVolume) {
				return;
			}
			// Add the new passenger
			// Passengers in cargo station can take 0~50kg cargo.
			Passenger[] ps = this.g.generatePassengers();
			for (Passenger p : ps) {
				if (p.getCargo().getWeight() == 0) {
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

	public void render(ShapeRenderer renderer) {
		float radius = RADIUS;
		for (int i = 0; (i < this.lines.size() && i < MAX_LINES); i++) {
			Line l = this.lines.get(i);
			renderer.setColor(l.lineColour);
			renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
			radius = radius - 1;
		}

		// Calculate the percentage
		float t = this.trains.size() / (float) PLATFORMS;
		Color c = Color.WHITE.cpy().lerp(Color.DARK_GRAY, t);
		if (this.waiting.size() > 0) {
			c = Color.RED;
		}

		renderer.setColor(c);
		renderer.circle(this.position.x, this.position.y, radius, NUM_CIRCLE_STATMENTS);
	}

}
