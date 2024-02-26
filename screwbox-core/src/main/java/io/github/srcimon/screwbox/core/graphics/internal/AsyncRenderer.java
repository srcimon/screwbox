package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Font;
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
    private Future<?> currentRendering = null;

    public AsyncRenderer(final Renderer next, final ExecutorService executor) {
        this.next = next;
        this.executor = executor;
    }

    @Override
    public void updateScreen(final Supplier<Graphics2D> graphicsSupplier) {
        waitForCurrentRenderingToEnd();
        next.updateScreen(graphicsSupplier);

        renderTasks.toggle();
        currentRendering = executor.submit(finishRenderTasks());
    }

    @Override
    public void drawTextCentered(final Offset position, final String text, final Font font, final Color color) {
        renderTasks.active().add(() -> next.drawTextCentered(position, text, font, color));
    }

    @Override
    public void fillWith(final Color color) {
        renderTasks.active().add(() -> next.fillWith(color));
    }

    @Override
    public void drawFadingCircle(Offset offset, int diameter, Color color) {
        renderTasks.active().add(() -> next.drawFadingCircle(offset, diameter, color));

    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options) {
        renderTasks.active().add(() -> next.drawRectangle(offset, size, options));
    }

    @Override
    public void fillCircle(final Offset offset, final int diameter, final Color color) {
        renderTasks.active().add(() -> next.fillCircle(offset, diameter, color));
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percent opacity,
                           final Rotation rotation, final Flip flip, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawSprite(sprite, origin, scale, opacity, rotation, flip, clip));

    }

    @Override
    public void drawText(final Offset offset, final String text, final Font font, final Color color) {
        renderTasks.active().add(() -> next.drawText(offset, text, font, color));

    }

    @Override
    public void drawLine(final Offset from, final Offset to, final Color color) {
        renderTasks.active().add(() -> next.drawLine(from, to, color));
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final double scale, final Percent opacity, final Rotation rotation,
                           final Flip flip, final ScreenBounds clip) {
        renderTasks.active().add(() -> next.drawSprite(sprite, origin, scale, opacity, rotation, flip, clip));
    }

    @Override
    public void drawCircle(final Offset offset, final int diameter, final Color color, final int strokeWidth) {
        renderTasks.active().add(() -> next.drawCircle(offset, diameter, color, strokeWidth));
    }

    private FutureTask<Void> finishRenderTasks() {
        return new FutureTask<>(() -> {
            for (final var task : renderTasks.inactive()) {
                try {
                    task.run();
                } catch (final Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(null, e);
                }
            }
            renderTasks.inactive().clear();
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
}
