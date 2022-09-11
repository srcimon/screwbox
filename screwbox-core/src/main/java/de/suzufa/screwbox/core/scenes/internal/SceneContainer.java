package de.suzufa.screwbox.core.scenes.internal;

import de.suzufa.screwbox.core.entities.internal.DefaultEntities;
import de.suzufa.screwbox.core.scenes.Scene;

public record SceneContainer(Scene scene, DefaultEntities entityEngine) {

}
