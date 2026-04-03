package dev.screwbox.core.scenes.animations;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sizeable;
import dev.screwbox.core.scenes.Scenes;

/**
 * Context that is provided when applying scene transitions.
 *
 * @param size            size of the screen
 * @param progress        progress of the transition animation
 * @param resolutionScale resolution scale to provide consistent optics with different resolutions
 * @see Scenes
 * @since 3.26.0
 */
public record AnimationContext(Size size, Percent progress, double resolutionScale) implements Sizeable {

}
