package com.unimelb.swen30006.metromadness.tracks;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.trains.Train;

public class Line {

	// The colour of this line
	public Color lineColour;
	public Color trackColour;

	// The name of this line
	public String name;
	// The stations on this line
	public ArrayList<Station> stations;
	// The tracks on this line between stations
	public ArrayList<Track> tracks;

	// Create a line
	public Line(Color stationColour, Color lineColour, String name) {
		// Set the line colour
		this.lineColour = stationColour;
		this.trackColour = lineColour;
		this.name = name;

		// Create the data structures
		this.stations = new ArrayList<Station>();
		this.tracks = new ArrayList<Track>();
	}

	public void addStation(Station s, Boolean two_way) {
		// We need to build the track if this is adding to existing stations
		if (this.stations.size() > 0) {
			// Get the last station
			Station last = this.stations.get(this.stations.size() - 1);

			// Generate a new track
			Track t;
			if (two_way) {
				t = new DualTrack(last.position, s.position, this.trackColour);
			} else {
				t = new Track(last.position, s.position, this.trackColour);
			}
			this.tracks.add(t);
		}

		// Add the station
		s.registerLine(this);
		this.stations.add(s);
	}

	@Override
	public String toString() {
		return "Line [lineColour=" + lineColour + ", trackColour=" + trackColour + ", name=" + name + "]";
	}

	public boolean endOfLine(Station s) throws Exception {
		if (this.stations.contains(s)) {
			int index = this.stations.indexOf(s);
			return (index == 0 || index == this.stations.size() - 1);
		} else {
			throw new Exception();
		}
	}

	public Track nextTrack(Station currentStation, boolean forward) throws Exception {
		if (this.stations.contains(currentStation)) {
			// Determine the track index
			int curIndex = this.stations.lastIndexOf(currentStation);
			// Increment to retrieve
			if (!forward) {
				curIndex -= 1;
			}

			// Check index is within range
			if ((curIndex < 0) || (curIndex > this.tracks.size() - 1)) {
				throw new Exception();
			} else {
				return this.tracks.get(curIndex);
			}

		} else {
			throw new Exception();
		}
	}

	public Station nextStation(Station s, boolean forward) throws Exception {
		if (this.stations.contains(s)) {
			int curIndex = this.stations.lastIndexOf(s);
			if (forward) {
				curIndex += 1;
			} else {
				curIndex -= 1;
			}

			// Check index is within range
			if ((curIndex < 0) || (curIndex > this.stations.size() - 1)) {
				throw new Exception();
			} else {
				return this.stations.get(curIndex);
			}
		} else {
			throw new Exception();
		}
	}

	public void render(ShapeRenderer renderer) {
		// Set the color to our line
		renderer.setColor(trackColour);

		// Draw all the track sections
		for (Track t : this.tracks) {
			t.render(renderer);
		}
	}

	/**
	 * The method makes a train enter station.
	 */
	public void enterStation(Train t) throws Exception {
		t.station.enter(t);
	}

	/**
	 * Judge if a train can enter the station.
	 */
	public boolean canEnterStation(Train t) throws Exception {
		return t.station.canEnter(t.trainLine);
	}

	/**
	 * Judge if a passenger can leave the train by comparing the current station
	 * and destination.
	 */
	public boolean shouldLeave(Train t, Passenger p) {
		return t.station.shouldLeave(p);
	}

	/**
	 * Return the current station of a train.
	 */
	public Station currentStation(Train t) {
		return t.station;
	}

	/**
	 * Return the current station position of a train.
	 */
	public Point2D.Float getPos(Train t) {
		return t.station.position;
	}

	/**
	 * Return the current station name of a train.
	 */
	public String getName(Train t) {
		return t.station.name;
	}

	/**
	 * Return the departure timer of current station for a train.
	 */
	public float getDepartureTime(Train t) {
		return t.station.getDepartureTime();
	}

	/**
	 * Judge if a train can enter the track.
	 */
	public boolean canEnterTrack(Train t) {
		return t.track.canEnter(t.forward);
	}

	/**
	 * The method makes a train enter a track.
	 */
	public void enterTrack(Train t) {
		t.track.enter(t);
	}

	/**
	 * The method makes a train depart from the station.
	 */
	public void depart(Train t) throws Exception {
		t.station.depart(t);
	}

	/**
	 * The method makes a train leave a track.
	 */
	public void leaveTrack(Train t) {
		t.track.leave(t);
	}

}
