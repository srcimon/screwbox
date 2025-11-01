package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBatch;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.internal.ReflectionImage;
import dev.screwbox.core.graphics.internal.filter.DistortionImageFilter;
import dev.screwbox.core.utils.Pixelperfect;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static dev.screwbox.core.environment.Order.SystemOrder.PRESENTATION_WORLD;
import static dev.screwbox.core.graphics.internal.ImageOperations.applyFilter;
import static java.lang.Math.ceil;

/**
 * Renders {@link Entity entities} having a {@link RenderComponent} and also adds reflections for {@link Entity entities}
 * having a {@link ReflectionComponent}.
 */
@Order(PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private static final Archetype RENDERS = Archetype.ofSpacial(RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> entities = fetchRenderEntities(engine);
        for (final var viewport : engine.graphics().viewports()) {
            renderEntitiesOnViewport(viewport, entities, render -> !render.renderInForeground);
        }
    }

    protected List<Entity> fetchRenderEntities(Engine engine) {
        return engine.environment().fetchAll(RENDERS);
    }

    protected void renderEntitiesOnViewport(final Viewport viewport, final List<Entity> entities, final Predicate<RenderComponent> renderCondition) {
        final double zoom = viewport.camera().zoom();
        final ScreenBounds visibleBounds = new ScreenBounds(Offset.origin(), viewport.canvas().size());
        for (final Entity entity : entities) {
            final RenderComponent render = entity.get(RenderComponent.class);
            if (renderCondition.test(render)) {
                final double width = render.sprite.width() * render.options.scale();
                final double height = render.sprite.height() * render.options.scale();
                final var spriteBounds = Bounds.atPosition(entity.position(), width, height);

                final var entityScreenBounds = viewport.toCanvas(spriteBounds, render.parallaxX, render.parallaxY);
                if (visibleBounds.intersects(entityScreenBounds)) {
                    viewport.canvas().drawSprite(render.sprite, entityScreenBounds.offset(), render.options.scale(render.options.scale() * zoom).drawOrder(render.drawOrder));
//                    spriteBatch.add(render.sprite, entityScreenBounds.offset(), render.options.scale(render.options.scale() * zoom).drawOrder(render.drawOrder), render.drawOrder);//TODO remove drawOrder from render
                }
            }
        }
    }

}
