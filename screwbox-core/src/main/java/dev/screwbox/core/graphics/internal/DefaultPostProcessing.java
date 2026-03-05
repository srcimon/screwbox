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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultPostProcessing implements PostProcessing, Updatable {

    private record AppliedFilter(Time timeAdded, PostProcessingFilter filter, boolean isViewportFilter) {

    }

    private final GraphicsConfiguration configuration;
    private final ViewportManager viewportManager;
    private final Function<Size, Image> imageFactory;
    private final List<AppliedFilter> filters = new ArrayList<>();
    private final List<Shockwave> shockwaves = new ArrayList<>();
    private Latch<Image> bufferImages;
    private Size currentSize;
    private Time now = Time.now();

    public DefaultPostProcessing(final GraphicsConfiguration configuration, final ViewportManager viewportManager, final Function<Size, Image> imageFactory) {
        this.configuration = configuration;
        this.viewportManager = viewportManager;
        this.imageFactory = imageFactory;
    }

    @Override
    public PostProcessing triggerShockwave(final Vector position, final ShockwaveOptions options) {
        shockwaves.add(new Shockwave(now, position, options));
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

    public void applyEffects(final Image source, final Graphics2D target, PostProcessingFilter overlayFilter) {
        prepareBufferTargets(source);

        final List<AppliedFilter> appliedFilters = new ArrayList<>(filters);
        if (!shockwaves.isEmpty()) {
            appliedFilters.addFirst(new AppliedFilter(now, new ShockwavePostFilter(shockwaves, 8), true));//TODO configure using graphicsconf
        }
        if (nonNull(overlayFilter)) {
            appliedFilters.addLast(new AppliedFilter(now, overlayFilter, false));
        }

        int remainingEffectCount = appliedFilters.size();
        boolean hasPreviousEffect = false;
        for (final var filter : appliedFilters) {
            final boolean isLastEffect = remainingEffectCount == 1;
            final Image currentSource = hasPreviousEffect
                ? bufferImages.active()
                : source;

            final Graphics2D currentTarget = isLastEffect
                ? target
                : (Graphics2D) bufferImages.inactive().getGraphics();

            currentTarget.setColor(AwtMapper.toAwtColor(configuration.backgroundColor()));
            currentTarget.fillRect(0, 0, currentSize.width(), currentSize.height());
            if (filter.isViewportFilter) {
                for (final var viewport : viewportManager.viewports()) {
                    ScreenBounds area = viewport.canvas().bounds();
                    currentTarget.setClip(area.x(), area.y(), area.width(), area.height());
                    final var context = createContext(filter, viewport);
                    filter.filter.apply(currentSource, currentTarget, context);
                }
            } else {
                currentTarget.setClip(0, 0, currentSize.width(), currentSize.height());
                final var context = createContext(filter, viewportManager.defaultViewport());
                filter.filter.apply(currentSource, currentTarget, context);
            }
            remainingEffectCount--;
            hasPreviousEffect = true;
            if (!isLastEffect) {
                currentTarget.dispose();
                bufferImages.toggle();
            }
        }
    }

    private void prepareBufferTargets(final Image source) {
        final Size sourceSize = Size.of(source.getWidth(null), source.getHeight(null));
        if ((isNull(bufferImages) || bufferTargetsSizeMismatch(sourceSize))) {
            if (bufferTargetsSizeMismatch(sourceSize)) {
                bufferImages.active().flush();
                bufferImages.inactive().flush();
            }
            bufferImages = Latch.of(imageFactory.apply(sourceSize), imageFactory.apply(sourceSize));
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

    @Override
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
        shockwaves.removeIf(Shockwave::isFinished);
    }
}
