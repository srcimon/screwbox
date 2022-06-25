package de.suzufa.screwbox.core.physics;

import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;

public interface Physics {

    RaycastBuilder raycastFrom(Vector position);

    SelectEntityBuilder searchAtPosition(Vector position);

    SelectEntityBuilder searchInRange(Bounds range);

    Physics setPathfindingAlgorithm(PathfindingAlgorithm algorithm);

    Optional<Path> findPath(Grid grid, Vector start, Vector end);
}
