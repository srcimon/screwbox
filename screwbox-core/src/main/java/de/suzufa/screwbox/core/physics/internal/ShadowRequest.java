package de.suzufa.screwbox.core.physics.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Segment;
import de.suzufa.screwbox.core.Vector;

public class ShadowRequest {

	private final Vector position;
	private final Bounds bounds;

	public ShadowRequest(final Vector position, final Bounds bounds) {
		this.position = position;
		this.bounds = bounds;
	}

	public List<Vector> shadowPolygonPoints() {
		var ankerRaycasts = getAnkerCasts();
		 return List.of(ankerRaycasts.get(0).to(), ankerRaycasts.get(1).to(),
				pointFurtherAway(ankerRaycasts.get(1)), pointFurtherAway(ankerRaycasts.get(0)));
	}

	private List<Segment> getAnkerCasts() {
		if (isDirectlyOnLeft()) {
			return List.of(Segment.between(position, bounds.origin()), Segment.between(position, bounds.bottomLeft()));
		}
		if (isDirectlyOnRight()) {
			return List.of(Segment.between(position, bounds.topRight()),
					Segment.between(position, bounds.bottomRight()));
		}
		if (isDirectlyBelow()) {
			return List.of(Segment.between(position, bounds.bottomLeft()),
					Segment.between(position, bounds.bottomRight()));
		}
		if (isDirectlyOver()) {
			return List.of(Segment.between(position, bounds.topRight()), Segment.between(position, bounds.origin()));
		}
		List<Segment> raycasts = new ArrayList<>();
		raycasts.add(Segment.between(position, bounds.origin()));
		raycasts.add(Segment.between(position, bounds.bottomLeft()));
		raycasts.add(Segment.between(position, bounds.bottomRight()));
		raycasts.add(Segment.between(position, bounds.topRight()));

		Collections.sort(raycasts);
		return List.of(raycasts.get(1), raycasts.get(2));

	}

	public Vector pointFurtherAway(Segment ankerRaycast) {
		double longer = 400;
		return Vector.of((ankerRaycast.to().x() - ankerRaycast.from().x()) * longer + ankerRaycast.to().x(),
				(ankerRaycast.to().y() - ankerRaycast.from().y()) * longer + ankerRaycast.to().y());
	}

	private boolean isDirectlyOver() {
		return position.y() < bounds.minY() && position.x() <= bounds.maxX() && position.x() >= bounds.minX();
	}

	private boolean isDirectlyBelow() {
		return position.y() > bounds.maxY() && position.x() <= bounds.maxX() && position.x() >= bounds.minX();
	}

	private boolean isDirectlyOnLeft() {
		return position.x() < bounds.minX() && position.y() <= bounds.maxY() && position.y() >= bounds.minY();
	}

	private boolean isDirectlyOnRight() {
		return position.x() > bounds.maxX() && position.y() <= bounds.maxY() && position.y() >= bounds.minY();
	}

}
