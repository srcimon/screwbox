package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;

import java.io.Serial;

/**
 * Adds a reflection effect to the {@link Entity}. Refects only {@link Entity entities} having a {@link RenderComponent}
 * which are above the refection area having a draw order below the specified value. Gets processed by
 * {@link RenderSystem}.
 */
public class ReflectionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The opacity of the resulting reflection.
     */
    public final Percent opacityModifier;

    /**
     * The draw order within the resulting {@link SpriteBatch}.
     */
    public int drawOrder;

    /**
     * Projects reflected {@link Sprite sprites} using a wave distortion to create a water effect. Less nice but much
     * more performant than {@link #applyWaveDistortionPostfilter}.
     * Uses {@link #amplitude}, {@link #speed} and {@link #frequenzy} to configure effect.
     *
     * @see #applyWaveDistortionPostfilter
     */
    public boolean applyWaveDistortionProjection = false;

    /**
     * Applies a wave effect distoriton filter to create a water erffect. A little nicer than
     * {@link #applyWaveDistortionProjection} but at the cost of performance.
     * Uses {@link #amplitude}, {@link #speed} and {@link #frequenzy} to configure effect.
     *
     * @see #applyWaveDistortionProjection
     * @since 2.10.0
     */
    public boolean applyWaveDistortionPostfilter = false;

    /**
     * The speed of the water effect created when {@link #applyWaveDistortionPostfilter}
     * or {@link #applyWaveDistortionProjection} is used.
     */
    public double speed = 0.005;

    /**
     * The amplitude of the water effect created when {@link #applyWaveDistortionPostfilter}
     * or {@link #applyWaveDistortionProjection} is used.
     */
    public double amplitude = 2.0;

    /**
     * The frequency of the water effect created when {@link #applyWaveDistortionPostfilter}
     * or {@link #applyWaveDistortionProjection} is used.
     */
    public double frequenzy = 0.5;

    public ReflectionComponent(final Percent opacityModifier, final int drawOrder) {
        this.opacityModifier = opacityModifier;
        this.drawOrder = drawOrder;
    }
}
