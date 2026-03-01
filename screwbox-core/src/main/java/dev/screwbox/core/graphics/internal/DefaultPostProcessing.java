package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.PostProcessing;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.options.ShockwaveOptions;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;
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

    private final GraphicsConfiguration configuration;
    private final ViewportManager viewportManager;
    private final List<AppliedFilter> filters = new ArrayList<>();
    private final List<Shockwave> shockwaves = new ArrayList<>();
    private Latch<BufferTarget> bufferTargets;
    private Size currentSize;
    private Time now = Time.now();

    public DefaultPostProcessing(final GraphicsConfiguration configuration, final ViewportManager viewportManager) {
        this.configuration = configuration;
        this.viewportManager = viewportManager;
    }

    record BufferTarget(VolatileImage image, Graphics2D graphics) {

    }

    @Override
    public PostProcessing triggerShockwave(final Vector position, final ShockwaveOptions options) {
        shockwaves.add(new Shockwave(now, position, options));//TODO reduce time.now calls
        return this;
    }

    @Override
    public int shockwaveCount() {
        return shockwaves.size();
    }

    @Override
    public PostProcessing removeFilter(final Class<? extends PostProcessingFilter> filter) {
        filters.removeIf(f -> f.filter.getClass().equals(filter));
        return this;
    }

    @Override
    public int filterCount() {
        return filters.size();
    }

    private record AppliedFilter(Time timeAdded, PostProcessingFilter filter, boolean isViewportFilter) {

    }

    public void applyEffects(final VolatileImage source, final Graphics2D target, PostProcessingFilter overlayFilter) {
        prepareBufferTargets(source);

        List<AppliedFilter> appliedFilters = new ArrayList<>(filters);
        if (!shockwaves.isEmpty()) {
            appliedFilters.addFirst(new AppliedFilter(now, new ShockwavePostFilter(shockwaves, 8), true));
        }
        if (nonNull(overlayFilter)) {
            appliedFilters.addLast(new AppliedFilter(now, overlayFilter, false));
        }

        int remainingEffectCount = appliedFilters.size();
        boolean hasPreviousEffect = false;
        for (final var filter : appliedFilters) {
            boolean isLastEffect = remainingEffectCount == 1;
            VolatileImage currentSource = hasPreviousEffect
                ? bufferTargets.active().image()
                : source;

            Graphics2D currentTarget = isLastEffect
                ? target
                : bufferTargets.inactive().graphics;

            currentTarget.setColor(AwtMapper.toAwtColor(configuration.backgroundColor()));
            currentTarget.fillRect(0, 0, currentSource.getWidth(), currentSource.getHeight());
            if (filter.isViewportFilter) {
                for (final var viewport : viewportManager.viewports()) {
                    ScreenBounds area = viewport.canvas().bounds();
                    currentTarget.setClip(area.x(), area.y(), area.width(), area.height());
                    final var context = createContext(filter, viewport);
                    filter.filter.apply(currentSource, currentTarget, context);
                }
            } else {
                currentTarget.setClip(0, 0, source.getWidth(), source.getHeight());
                final var context = createContext(filter, viewportManager.defaultViewport());
                filter.filter.apply(currentSource, currentTarget, context);
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
        if ((isNull(bufferTargets) || bufferTargetsSizeMismatch(sourceSize))) {
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

    private PostProcessingContext createContext(final AppliedFilter filter, final Viewport viewport) {
        return new PostProcessingContext(
            configuration.backgroundColor(),
            Duration.between(filter.timeAdded, now),
            viewport);
    }

    public boolean isActive() {
        return !filters.isEmpty() || !shockwaves.isEmpty();
    }


    @Override
    public PostProcessing addScreenFilter(final PostProcessingFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null");
        filters.add(new AppliedFilter(now, filter, false));
        return this;
    }

    @Override
    public PostProcessing addViewportFilter(PostProcessingFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null");
        filters.add(new AppliedFilter(now, filter, true));
        return this;
    }

    @Override
    public PostProcessing clearFilters() {
        filters.clear();
        return this;
    }

    @Override
    public void update() {
        now = Time.now();
        for (final var wave : shockwaves) {
            wave.update(now);
        }
        shockwaves.removeIf(wave -> wave.progress.isMax());
    }
}
