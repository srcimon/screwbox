package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.effects.PostProcessingContext;
import dev.screwbox.core.graphics.effects.PostProcessingEffect;
import dev.screwbox.core.utils.Latch;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

public class DefaultPostProcessing implements PostProcessing {

    private final GraphicsConfiguration configuration;
    private final List<PostProcessingEffect> effects = new ArrayList<>();
    private final Time startTime;
    private Latch<TempTarget> tempTargets;

    public DefaultPostProcessing(final GraphicsConfiguration configuration) {
        this.configuration = configuration;
        startTime = Time.now();
    }

    record TempTarget(VolatileImage image, Graphics2D graphics) {

    }


    public void applyEffects(final VolatileImage source, final Graphics2D target) {
        if (isActive()) {

            if (isNull(tempTargets) && effects.size() > 1) {//TODO or size is outdated
                var image1 = ImageOperations.createVolatileImage(Size.of(source.getWidth(), source.getHeight()));
                var image2 = ImageOperations.createVolatileImage(Size.of(source.getWidth(), source.getHeight()));
                tempTargets = Latch.of(
                    new TempTarget(image1, image1.createGraphics()),
                    new TempTarget(image2, image2.createGraphics())
                );
            }

            PostProcessingContext context = new PostProcessingContext(
                configuration.backgroundColor(),
                Time.now(),
                Duration.since(startTime));

            int remainingEffectCount = effects.size();
            boolean hasPreviousEffect = false;
            for (final var effect : effects) {
                boolean isLastEffect = remainingEffectCount == 1;
                VolatileImage currentSource = hasPreviousEffect
                    ? tempTargets.active().image()
                    : source;

                Graphics2D currentTarget = isLastEffect
                    ? target
                    : tempTargets.inactive().graphics;

                effect.apply(currentSource, currentTarget, context);
                remainingEffectCount--;
                hasPreviousEffect = true;
                tempTargets.toggle();
            }
        } else {
            target.drawImage(source, 0, 0, null);
        }
    }

    public boolean isActive() {
        return !effects.isEmpty();
    }

    @Override
    public PostProcessing addEffect(final PostProcessingEffect effect) {
        Objects.requireNonNull(effect, "Effect must not be null");
        effects.add(effect);
        return this;
    }

    @Override
    public PostProcessing clearEffects() {
        effects.clear();
        return this;
    }
}
