package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.effects.PostProcessingEffect;
import dev.screwbox.core.utils.Latch;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

public class DefaultPostProcessing implements PostProcessing {

    final List<PostProcessingEffect> effects = new ArrayList<>();

    private TempTarget tempTarget;//TODO latch;

    record TempTarget(VolatileImage image, Graphics2D graphics) {

    }

    public void applyEffects(final VolatileImage source, final Graphics2D target) {
        if(isActive()) {
            if(isNull(tempTarget)) {//TODO or size is outdated
                var image = ImageOperations.createVolatileImage(Size.of(source.getWidth(), source.getHeight()));
                tempTarget = new TempTarget(image, image.createGraphics());
            }

            int remainingEffectCount = effects.size();
            boolean hasPreviousEffect = false;
            for(final var effect : effects) {
                boolean isLastEffect = remainingEffectCount == 1;
                VolatileImage currentSource = hasPreviousEffect
                    ? tempTarget.image()
                    : source;

                Graphics2D currentTarget = isLastEffect
                    ? target
                    : tempTarget.graphics;

                effect.apply(currentSource, currentTarget);
                remainingEffectCount--;
                hasPreviousEffect = true;

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
