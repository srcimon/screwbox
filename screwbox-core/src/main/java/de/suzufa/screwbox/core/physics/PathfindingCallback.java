package de.suzufa.screwbox.core.physics;

import de.suzufa.screwbox.core.Path;

public interface PathfindingCallback {

    void onPathFound(Path path);

    default void onPathNotFound() {

    }
}
