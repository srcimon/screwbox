package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.graphics.effects.PostProcessingEffect;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultPostProcessing implements PostProcessing {

    final List<PostProcessingEffect> effects = new ArrayList<>();

    public void applyEffects(final VolatileImage source, final Graphics2D target) {
        if(!isActive()) {
            target.drawImage(source, 0, 0, null);
            return;
        }

       // var target = ImageOperations.createVolatileImage(screenSize);// TODO avoid
        effects.getFirst().apply(source, target);
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
