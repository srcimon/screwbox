package io.github.srcimon.screwbox.core.graphics.internal;

import io.github.srcimon.screwbox.core.Angle;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.*;
import io.github.srcimon.screwbox.core.utils.Latch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import static java.lang.Thread.currentThread;
import static java.util.Objects.nonNull;

public class SeparateThreadRenderer implements Renderer {

    private final Latch<List<Runnable>> renderTasks = Latch.of(new ArrayList<>(), new ArrayList<>());
    private final Renderer next;
    private final ExecutorService executor;
    private Future<?> currentRendering = null;

    public SeparateThreadRenderer(final Renderer next, final ExecutorService executor) {
        this.next = next;
        this.executor = executor;
    }

    @Override
    public void updateScreen(final boolean antialiased) {
        waitForCurrentRenderingToEnd();
        next.updateScreen(antialiased);

        renderTasks.toggle();
        currentRendering = executor.submit(finishRenderTasks());
    }

    private FutureTask<Void> finishRenderTasks() {
        return new FutureTask<>(() -> {
            for (final var task : renderTasks.inactive()) {
                task.run();
            }
            renderTasks.inactive().clear();
        }, null);
    }

    @Override
    public Sprite takeScreenshot() {
        return next.takeScreenshot();
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
    public void fillRectangle(final ScreenBounds bounds, final Color color) {
        renderTasks.active().add(() -> next.fillRectangle(bounds, color));
    }

    @Override
    public void fillCircle(final Offset offset, final int diameter, final Color color) {
        renderTasks.active().add(() -> next.fillCircle(offset, diameter, color));
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percent opacity,
                           final Angle rotation, final Flip flip, final ScreenBounds clipArea) {
        renderTasks.active().add(() -> next.drawSprite(sprite, origin, scale, opacity, rotation, flip, clipArea));

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
    public void drawSprite(Supplier<Sprite> sprite, Offset origin, double scale, Percent opacity, Angle rotation,
                           Flip flip, ScreenBounds clipArea) {
        renderTasks.active().add(() -> next.drawSprite(sprite, origin, scale, opacity, rotation, flip, clipArea));
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

    @Override
    public void drawCircle(Offset offset, int diameter, Color color) {
        renderTasks.active().add(() -> next.drawCircle(offset, diameter, color));
    }

}
