package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.effects.PostProcessingContext;
import dev.screwbox.core.graphics.effects.PostProcessingFilter;
import dev.screwbox.core.utils.Latch;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultPostProcessing implements PostProcessing {

    private final Engine engine;
    private final List<PostProcessingFilter> filters = new ArrayList<>();
    private Latch<BufferTarget> bufferTargets;
    private Size currentSize;

    public DefaultPostProcessing(final Engine engine) {//TODO really whole engine? Me dont like
        this.engine = engine;
    }

    record BufferTarget(VolatileImage image, Graphics2D graphics) {

    }


    public void applyEffects(final VolatileImage source, final Graphics2D target) {
        if (!isActive()) {
            target.drawImage(source, 0, 0, null);
            return;
        }

        prepareBufferTargets(source);

        final var context = createContext();

        int remainingEffectCount = filters.size();
        boolean hasPreviousEffect = false;
        for (final var effect : filters) {
            boolean isLastEffect = remainingEffectCount == 1;
            VolatileImage currentSource = hasPreviousEffect
                ? bufferTargets.active().image()
                : source;

            Graphics2D currentTarget = isLastEffect
                ? target
                : bufferTargets.inactive().graphics;

            effect.apply(currentSource, currentTarget, context);
            remainingEffectCount--;
            hasPreviousEffect = true;
            if (!isLastEffect) {
                bufferTargets.toggle();
            }
        }
    }

    private void prepareBufferTargets(final VolatileImage source) {
        final Size sourceSize = Size.of(source.getWidth(), source.getHeight());
        if ((isNull(bufferTargets) && filters.size() > 1) || bufferTargetsSizeMismatch(sourceSize)) {
            if(bufferTargetsSizeMismatch(sourceSize)) {
                bufferTargets.active().graphics.dispose();
                bufferTargets.inactive().graphics.dispose();
                bufferTargets.active().image.flush();
                bufferTargets.inactive().image.flush();
            }
            final var firstImage = ImageOperations.createVolatileImage(sourceSize);
            final var secondImage = ImageOperations.createVolatileImage(sourceSize);
            bufferTargets = Latch.of(
                new BufferTarget(firstImage, firstImage.createGraphics()),
                new BufferTarget(secondImage, secondImage.createGraphics())
            );
            currentSize = sourceSize;
        }
    }

    private boolean bufferTargetsSizeMismatch(final Size sourceSize) {
        return nonNull(currentSize) && !currentSize.equals(sourceSize);
    }

    private PostProcessingContext createContext() {
        PostProcessingContext context = new PostProcessingContext(
            engine.graphics().configuration().backgroundColor(),
            engine.loop().time(),
            engine.loop().runningTime(),
            engine.graphics().camera().position(),
            engine.graphics().camera().zoom());
        return context;
    }

    public boolean isActive() {
        return !filters.isEmpty();
    }

    @Override
    public PostProcessing addFilter(final PostProcessingFilter effect) {
        Objects.requireNonNull(effect, "Effect must not be null");
        filters.add(effect);
        return this;
    }

    @Override
    public PostProcessing clearFilters() {
        filters.clear();
        return this;
    }
}
