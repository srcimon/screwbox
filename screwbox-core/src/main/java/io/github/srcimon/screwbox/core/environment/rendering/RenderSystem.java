package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(SystemOrder.PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    public record BatchEntry(Sprite sprite, Offset offset, SpriteDrawOptions options, int drawOrder)
            implements Comparable<BatchEntry> {

        @Override
        public int compareTo(final BatchEntry o) {
            return Integer.compare(drawOrder, o.drawOrder);
        }
    }

    private static final Archetype RENDERS = Archetype.of(RenderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<BatchEntry> entries = new ArrayList<>();
        final Graphics graphics = engine.graphics();
        final ScreenBounds screenBounds = graphics.screen().bounds();
        Screen screen = graphics.screen();
        double zoom = graphics.camera().zoom();

        for (final Entity entity : engine.environment().fetchAll(RENDERS)) {
            final RenderComponent render = entity.get(RenderComponent.class);
            final double width = render.sprite.size().width() * render.options.scale();
            final double height = render.sprite.size().height() * render.options.scale();
            final var spriteBounds = Bounds.atPosition(entity.position(), width, height);
            final var entityScreenBounds = graphics.toScreen(spriteBounds, render.parallax);
            if (screenBounds.intersects(entityScreenBounds)) {
                entries.add(new BatchEntry(render.sprite, entityScreenBounds.offset(), render.options.scale(render.options.scale() * zoom), render.drawOrder));
            }
        }

        Collections.sort(entries);
        for (final var entry : entries) {
            screen.drawSprite(entry.sprite, entry.offset, entry.options);
        }
    }
}
