package com.unimelb.swen30006.metromadness.trains;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.unimelb.swen30006.metromadness.passengers.Passenger;
import com.unimelb.swen30006.metromadness.stations.Station;
import com.unimelb.swen30006.metromadness.tracks.Line;

public class BigPassengerTrain extends Train {

	/**
	 * Create a new, empty passenger train with large passenger capacity,
	 * initialized as empty
	 */
	public BigPassengerTrain(Line trainLine, Station start, boolean forward, String name, int trainSize) {
		super(trainLine, start, forward, name, trainSize);
	}

	@Override
	public void render(ShapeRenderer renderer) {
		if (!this.inStation()) {
			Color col = this.forward ? FORWARD_COLOUR : BACKWARD_COLOUR;
			float percentage = this.passengers.size() / 20f;
			renderer.setColor(col.cpy().lerp(Color.LIGHT_GRAY, percentage));
			renderer.circle(this.pos.x, this.pos.y, TRAIN_WIDTH * (1 + percentage));
		}
	}

}
