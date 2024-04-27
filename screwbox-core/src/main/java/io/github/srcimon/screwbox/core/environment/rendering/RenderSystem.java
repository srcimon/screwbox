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
import io.github.srcimon.screwbox.core.graphics.Screen;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.internal.SpriteBatchEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(SystemOrder.PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private static final Archetype RENDERS = Archetype.of(RenderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<SpriteBatchEntry> entries = new ArrayList<>();
        final Graphics graphics = engine.graphics();
        final ScreenBounds visibleBounds = graphics.screen().bounds();
        double zoom = graphics.camera().zoom();

        for (final Entity entity : engine.environment().fetchAll(RENDERS)) {
            final RenderComponent render = entity.get(RenderComponent.class);
            final double width = render.sprite.size().width() * render.options.scale();
            final double height = render.sprite.size().height() * render.options.scale();
            final var spriteBounds = Bounds.atPosition(entity.position(), width, height);
            final var entityScreenBounds = graphics.toScreenUsingParallax(spriteBounds, render.parallaxX, render.parallaxY);
            if (visibleBounds.intersects(entityScreenBounds)) {
                entries.add(new SpriteBatchEntry(render.sprite, entityScreenBounds.offset(), render.options.scale(render.options.scale() * zoom), render.drawOrder));
            }
        }

        Collections.sort(entries);
        final Screen screen = graphics.screen();
        for (final var entry : entries) {
            screen.drawSprite(entry.sprite(), entry.offset(), entry.options());
        }
    }
}
