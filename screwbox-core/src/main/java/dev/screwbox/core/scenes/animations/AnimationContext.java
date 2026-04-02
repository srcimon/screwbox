package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sizeable;

//TODO document
public record AnimationContext(Size size, Percent progress, double resolutionScale) implements Sizeable {

}
