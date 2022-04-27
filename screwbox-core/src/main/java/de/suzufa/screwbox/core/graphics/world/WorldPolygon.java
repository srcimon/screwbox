package de.suzufa.screwbox.core.graphics.world;

import java.util.List;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;

public record WorldPolygon(List<Vector> points, Color color, Percentage opacity) {

	public static WorldPolygon polygon(final List<Vector> points, final Color color, final Percentage opacity) {
		return new WorldPolygon(points, color, opacity);
	}
}
