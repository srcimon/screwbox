package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.drawoptions.ShaderSetup;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;

import java.awt.*;
import java.io.Serializable;

/**
 * Shaders are used to create graphic effects on {@link Sprite sprites}.
 *
 * @see SpriteDrawOptions#shaderSetup(ShaderSetup)
 * @since 2.15.0
 */
public abstract class Shader implements Serializable {

    private final String cacheKey;
    private final boolean isAnimated;

    protected Shader(final String cacheKey) {
        this(cacheKey, true);
    }

    protected Shader(final String cacheKey, final boolean isAnimated) {
        this.cacheKey = cacheKey;
        this.isAnimated = isAnimated;
    }

    /**
     * {@code true} when this shader creates an animated effect.
     */
    public final boolean isAnimated() {
        return isAnimated;
    }

    /**
     * Key that is used to cache the shader result. This key should be unique for any configured {@link Shader}.
     * If the shader uses input parameters. These parameters should also be contained in the cacheKey.
     */
    public final String cacheKey() {
        return cacheKey;
    }

    /**
     * Returns a new image from source. Can use specified progress to create an
     * animated effect.
     */
    public abstract Image apply(Image source, Percent progress);


}
