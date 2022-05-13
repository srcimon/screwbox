package de.suzufa.screwbox.core.graphics.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.core.graphics.WindowBounds;
import de.suzufa.screwbox.core.utils.internal.Swappable;

public class QueueingRenderer implements Renderer {

    private final Swappable<List<Runnable>> renderTasks = Swappable.of(new ArrayList<>(), new ArrayList<>());
    private final Renderer next;
    private final ExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Future<?> currentRendering = null;

    public QueueingRenderer(final Renderer next) {
        this.next = next;
    }

    @Override
    public void updateScreen(final boolean antialiased) {
        if (currentRendering != null) {
            try {
                currentRendering.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        renderTasks.backup().clear();
        renderTasks.swap();
        next.updateScreen(antialiased);
        RunnableFuture<Void> renderAll = new FutureTask<>(new Runnable() {

            @Override
            public void run() {
                for (var task : renderTasks.backup()) {
                    task.run();
                }
            }
        }, null);
        currentRendering = executor.submit(renderAll);
    }

    @Override
    public Sprite takeScreenshot() {
        return next.takeScreenshot();
    }

    @Override
    public int calculateTextWidth(final String text, final Font font) {
        return next.calculateTextWidth(text, font);
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

    @Override
    public void terminate() {
        executor.shutdown();
    }

}
