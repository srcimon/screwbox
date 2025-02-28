package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderOptions;

import java.awt.*;
import java.io.Serializable;

/**
 * Shaders are used to create graphic effects on {@link Sprite sprites}.
 * @see io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions#shaderOptions(ShaderOptions)
 *
 * @since 2.15.0
 */
public interface Shader extends Serializable {

    /**
     * Returns a new image from source. Can use specified progress to create an
     * animated effect.
     */
    Image applyOn(Image source, Percent progress);

    /**
     * {@code true} when this shader creates an animated effect.
     */
    boolean isAnimated();

    /**
     * Key that is used to cache the shader result. This key should be unique for any configured {@link Shader}.
     * If the shader uses input parameters. These parameters should also be contained in the cacheKey.
     */
    String cacheKey();

}
