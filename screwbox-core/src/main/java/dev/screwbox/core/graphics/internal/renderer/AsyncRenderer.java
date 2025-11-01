package dev.screwbox.core.graphics.internal.renderer;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.Renderer;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.options.SpriteFillOptions;
import dev.screwbox.core.graphics.options.SystemTextDrawOptions;
import dev.screwbox.core.graphics.options.TextDrawOptions;
import dev.screwbox.core.utils.Latch;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

import static java.lang.Thread.currentThread;
import static java.util.Objects.nonNull;

public class AsyncRenderer implements Renderer {

    private final Latch<List<RenderingTask>> renderTasks = Latch.of(new ArrayList<>(), new ArrayList<>());
    private final Renderer next;
    private final ExecutorService executor;
    private final Engine engine;
    private Duration renderingDuration = Duration.none();
    private int renderTaskCount = 0;

    private Future<?> currentRendering = null;

    private record RenderingTask(Order.SystemOrder order, int drawOrder, double orthographicOrder, Runnable task) {

        public double priority() {
            if(drawOrder > order.drawOrder()) {
                return drawOrder * 1_000 + orthographicOrder / 1000.0;
            }
            return order.drawOrder() + drawOrder * 1_000 + orthographicOrder / 1000.0;
        }
        // order of creation
        // orhtographic ordering info
        // orgin system order
        // relative to other system info
        // !! Order within system??
    }

    public AsyncRenderer(final Renderer next, final ExecutorService executor, Engine engine) {
        this.next = next;
        this.executor = executor;
        this.engine = engine;
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
        addTask(0, () -> next.fillWith(color, clip));
    }

    @Override
    public void rotate(final Angle rotation, final ScreenBounds clip, final Color backgroundColor) {
        addTask(0, () -> next.rotate(rotation, clip, backgroundColor));
    }

    @Override
    public void fillWith(final Sprite sprite, final SpriteFillOptions options, final ScreenBounds clip) {
        addTask(0, () -> next.fillWith(sprite, options, clip));
    }

    @Override
    public void drawText(final Offset offset, final String text, final SystemTextDrawOptions options, final ScreenBounds clip) {
        addTask(0, () -> next.drawText(offset, text, options, clip));
    }

    @Override
    public void drawRectangle(final Offset offset, final Size size, final RectangleDrawOptions options, final ScreenBounds clip) {
        addTask(0, () -> next.drawRectangle(offset, size, options, clip));
    }

    @Override
    public void drawLine(final Offset from, final Offset to, final LineDrawOptions options, final ScreenBounds clip) {
        addTask(0, () -> next.drawLine(from, to, options, clip));
    }

    @Override
    public void drawOval(final Offset offset, final int radiusX, final int radiusY, final OvalDrawOptions options, final ScreenBounds clip) {
        addTask(0, () -> next.drawOval(offset, radiusX, radiusY, options, clip));
    }

    @Override
    public void drawSprite(final Supplier<Sprite> sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        double orthographicOrder = options.isSortOrthographic() ? options.scale() * sprite.get().height() + origin.y() : 0;//TODO sprite.get is ugly
        addTask(options.drawOrder(), orthographicOrder, () -> next.drawSprite(sprite, origin, options, clip));
    }

    @Override
    public void drawSprite(final Sprite sprite, final Offset origin, final SpriteDrawOptions options, final ScreenBounds clip) {
        double orthographicOrder = options.isSortOrthographic() ? options.scale() * sprite.height() + origin.y() : 0;
        addTask(options.drawOrder(), orthographicOrder, () -> next.drawSprite(sprite, origin, options, clip));
    }

    @Override
    public void drawText(final Offset offset, final String text, final TextDrawOptions options, final ScreenBounds clip) {
        addTask(0, () -> next.drawText(offset, text, options, clip));
    }

    @Override
    public void drawPolygon(final List<Offset> nodes, final PolygonDrawOptions options, final ScreenBounds clip) {
        addTask(0, () -> next.drawPolygon(nodes, options, clip));
    }

    private void addTask(int drawOrder, final Runnable runnable) {
        addTask(drawOrder, 0, runnable);
    }

    private void addTask(int drawOrder, double orthographicOrder, final Runnable runnable) {
        renderTasks.active().add(new RenderingTask(engine.environment().currentOrder(), drawOrder, orthographicOrder, runnable));
    }

    private FutureTask<Void> finishRenderTasks() {
        return new FutureTask<>(() -> {
            final Time startOfRendering = Time.now();
            try {
                renderTasks.inactive().sort(Comparator.comparing(RenderingTask::priority));
                for (final var renderingTask : renderTasks.inactive()) {
                    renderingTask.task.run();
                }
            } catch (final Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(null, e);
            }
            renderTaskCount = renderTasks.inactive().size();
            renderingDuration = Duration.since(startOfRendering);
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

    public Duration renderDuration() {
        return renderingDuration;
    }

    public int renderTaskCount() {
        return renderTaskCount;
    }
}
