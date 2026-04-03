package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sizeable;

/**
 * Context that is provided when applying scene transitions.
 *
 * @param size            size of the screen
 * @param progress        progress of the transition animation
 * @param resolutionScale resolution scale to provide consitent optics with different resolutions
 * @see Scenes
 */
public record AnimationContext(Size size, Percent progress, double resolutionScale) implements Sizeable {

}
