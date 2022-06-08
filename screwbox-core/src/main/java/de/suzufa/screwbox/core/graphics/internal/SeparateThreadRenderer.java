package de.suzufa.screwbox.core.graphics.internal;

import static java.lang.Thread.currentThread;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.utils.Latch;

public class SeparateThreadRenderer implements Renderer {

    private final Latch<List<Runnable>> renderTasks = Latch.of(new ArrayList<>(), new ArrayList<>());
    private final Renderer next;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> currentRendering = null;

    public SeparateThreadRenderer(final Renderer next) {
        this.next = next;
    }

    @Override
    public void updateScreen(final boolean antialiased) {
        waitForCurrentRenderingToEnd();
        next.updateScreen(antialiased);

        renderTasks.swap();
        currentRendering = executor.submit(finishRenderTasks());
    }

    private FutureTask<Void> finishRenderTasks() {
        return new FutureTask<>(() -> {
            for (final var task : renderTasks.backup()) {
                task.run();
            }
            renderTasks.backup().clear();
        }, null);
    }

    @Override
    public Sprite takeScreenshot() {
        return next.takeScreenshot();
    }

    @Override
    public void drawTextCentered(Offset position, String text, Font font, Color color) {
        renderTasks.primary().add(() -> next.drawTextCentered(position, text, font, color));
    }

    @Override
    public void fillWith(final Color color) {
        renderTasks.primary().add(() -> next.fillWith(color));
    }

    @Override
    public void drawRectangle(final WindowBounds bounds, final Color color) {
        renderTasks.primary().add(() -> next.drawRectangle(bounds, color));
    }

    @Override
    public void drawCircle(final Offset offset, final int diameter, final Color color) {
        renderTasks.primary().add(() -> next.drawCircle(offset, diameter, color));
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final double scale, final Percentage opacity,
            final Rotation rotation) {
        renderTasks.primary().add(() -> next.drawSprite(sprite, origin, scale, opacity, rotation));

    }

    @Override
    public void drawText(final Offset offset, final String text, final Font font, final Color color) {
        renderTasks.primary().add(() -> next.drawText(offset, text, font, color));

    }

    @Override
    public void drawLine(final Offset from, final Offset to, final Color color) {
        renderTasks.primary().add(() -> next.drawLine(from, to, color));

    }

    @Override
    public void drawPolygon(final List<Offset> points, final Color color) {
        renderTasks.primary().add(() -> next.drawPolygon(points, color));
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

    public void close() {
        waitForCurrentRenderingToEnd();
        executor.shutdown();
    }

}
