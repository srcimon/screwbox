package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.filter.PostProcessingContext;
import dev.screwbox.core.graphics.filter.PostProcessingFilter;
import dev.screwbox.core.graphics.options.ShockwaveOptions;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.utils.Latch;

import java.awt.*;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultPostProcessing implements PostProcessing, Updatable {

    private record AppliedFilter(PostProcessingFilter filter, boolean isViewportFilter) {

    }

    private record ActiveShockwave(Vector position, Shockwave shockwave) {

    }
    private final Engine engine;
    private final List<AppliedFilter> filters = new ArrayList<>();
    private final List<ActiveShockwave> shockwaves = new ArrayList<>();
    private Latch<BufferTarget> bufferTargets;
    private Size currentSize;

    public DefaultPostProcessing(final Engine engine) {//TODO really whole engine? Me dont like
        this.engine = engine;
    }

    record BufferTarget(VolatileImage image, Graphics2D graphics) {

    }

    @Override
    public PostProcessing triggerShockwave(final Vector position, final ShockwaveOptions options) {
        shockwaves.add(new ActiveShockwave(position, new Shockwave(Offset.origin(), options)));
        return this;
    }

    @Override
    public int shockwaveCount() {
        return shockwaves.size();
    }


    public void applyEffects(final VolatileImage source, final Graphics2D target) {
        if (!isActive()) {
            target.drawImage(source, 0, 0, null);
            return;
        }

        prepareBufferTargets(source);

        final var context = createContext();

        List<AppliedFilter> appliedFilters = new ArrayList<>(filters);
        if (!shockwaves.isEmpty()) {
            appliedFilters.addFirst(new AppliedFilter(new ShockwaveFilter(shockwaves.stream().map(s -> new ShockwaveFilter.ShockwaveState(engine.graphics().toCanvas(s.position), s.shockwave.radius, s.shockwave.waveWidth, s.shockwave.options.maxRadius(), s.shockwave.intensity)).toList(), 4), true));//TODO FIX Viewport mapping
        }
        int remainingEffectCount = appliedFilters.size();
        boolean hasPreviousEffect = false;
        ScreenBounds screenBounds = new ScreenBounds(0, 0, source.getWidth(), source.getHeight());
        for (final var filter : appliedFilters) {
            boolean isLastEffect = remainingEffectCount == 1;
            VolatileImage currentSource = hasPreviousEffect
                ? bufferTargets.active().image()
                : source;

            Graphics2D currentTarget = isLastEffect
                ? target
                : bufferTargets.inactive().graphics;

            if (filter.isViewportFilter) {
                for (final var viewport : engine.graphics().viewports()) {
                    ScreenBounds area = viewport.canvas().bounds();
                    currentTarget.setClip(area.x(), area.y(), area.width(), area.height());
                    filter.filter.apply(currentSource, currentTarget, area, context);
                }
            } else {
                currentTarget.setClip(screenBounds.x(), screenBounds.y(), screenBounds.width(), screenBounds.height());
                filter.filter.apply(currentSource, currentTarget, screenBounds, context);
            }
            remainingEffectCount--;
            hasPreviousEffect = true;
            if (!isLastEffect) {
                bufferTargets.toggle();
            }
        }
    }

    private void prepareBufferTargets(final VolatileImage source) {
        final Size sourceSize = Size.of(source.getWidth(), source.getHeight());
        if ((isNull(bufferTargets) && (filters.size() + (shockwaves.isEmpty() ? 0 : 1) > 1)) || bufferTargetsSizeMismatch(sourceSize) ) {
            if (bufferTargetsSizeMismatch(sourceSize)) {
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
        return new PostProcessingContext(
            engine.graphics().configuration().backgroundColor(),
            engine.loop().time(),
            engine.loop().runningTime(),
            engine.graphics().camera().position(),
            engine.graphics().camera().zoom());
    }

    public boolean isActive() {
        return !filters.isEmpty() || !shockwaves.isEmpty();
    }


    @Override
    public PostProcessing addFilter(final PostProcessingFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null");
        filters.add(new AppliedFilter(filter, false));
        return this;
    }

    @Override
    public PostProcessing addViewportFilter(PostProcessingFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null");
        filters.add(new AppliedFilter(filter, true));
        return this;
    }

    @Override
    public PostProcessing clearFilters() {
        filters.clear();
        return this;
    }

    @Override
    public void update() {
        for (final var wave : shockwaves) {
            wave.shockwave.update(engine.loop().delta());
        }
        shockwaves.removeIf(wave -> wave.shockwave.radius > wave.shockwave.options.maxRadius());
    }
}
