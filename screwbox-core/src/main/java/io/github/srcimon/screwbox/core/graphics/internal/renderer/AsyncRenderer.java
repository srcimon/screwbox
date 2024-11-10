package io.github.srcimon.screwbox.core.graphics.internal.renderer;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.drawoptions.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.LineDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteFillOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SystemTextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.drawoptions.TextDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.Renderer;
import io.github.srcimon.screwbox.core.utils.Latch;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import static java.lang.Thread.currentThread;
import static java.util.Objects.nonNull;

public class AsyncRenderer implements Renderer {

    private final Latch<List<Runnable>> renderTasks = Latch.of(new ArrayList<>(), new ArrayList<>());
    private final Renderer next;
    private final ExecutorService executor;
    private Duration renderingDuration = Duration.none();

    private Future<?> currentRendering = null;

    public AsyncRenderer(final Renderer next, final ExecutorService executor) {
        this.next = next;
        this.executor = executor;
    }

    @Override
    public void updateContext(final Supplier<Graphics2D> graphics) {
        waitForCurrentRenderingToEnd();
        next.updateContext(graphics);

        renderTasks.toggle();
        currentRendering = executor.submit(finishRenderTasks());
    }

    @Override
    public void fillWith(final Color color, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.fillWith(color, clip));
    }

    @Override
    public void rotate(final Rotation rotation, final ScreenBounds clip, final Color backgroundColor) {
        renderTasks.active().add(() -> next.rotate(rotation, clip, backgroundColor));
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.fillWith(sprite, options, clip));
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawText(offset, text, options, clip));
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawRectangle(offset, size, options, clip));
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawLine(from, to, options, clip));
    }

    @Override
    public void drawCircle(final Offset offset, final int radius, final CircleDrawOptions options, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawCircle(offset, radius, options, clip));
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawSprite(sprite, origin, options, clip));
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawSprite(sprite, origin, options, clip));
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawText(offset, text, options, clip));
    }

    @Override
    public void drawSpriteBatch(final SpriteBatch spriteBatch, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawSpriteBatch(spriteBatch, clip));
    }

    private FutureTask<Void> finishRenderTasks() {
        return new FutureTask<>(() -> {
            final Time startOfRendering = Time.now();
            try {
                for (final var task : renderTasks.inactive()) {
                    task.run();
                }
            } catch (final Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(null, e);
            }
            renderTasks.inactive().clear();
            renderingDuration = Duration.since(startOfRendering);
        }, null);
    }

    private void waitForCurrentRenderingToEnd() {
        if (nonNull(currentRendering)) {
            try {
                currentRendering.get();
            } catch (InterruptedException | ExecutionException e) {
                currentThread().interrupt();
            }
        }
    }

    public Duration renderDuration() {
        return renderingDuration;
    }
}
