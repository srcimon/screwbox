package io.github.srcimon.screwbox.core.scenes.internal;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.scenes.Scene;
import io.github.srcimon.screwbox.core.scenes.SceneTransition;

//TODO: rework all transitions to SheduledTransitions
public record SheduledTransition(Class<? extends Scene> sceneClass, Time activationTime) {


}
