package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Graphics;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.ReflectionImage;
import io.github.srcimon.screwbox.core.utils.Pixelperfect;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_WORLD;
import static java.lang.Math.ceil;

/**
 * Renders {@link Entity entities} having a {@link RenderComponent} and also adds refections for {@link Entity entities}
 * having a {@link ReflectionComponent}.
 */
@Order(PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private static final Archetype RENDERS = Archetype.of(RenderComponent.class, TransformComponent.class);
    private static final Archetype MIRRORS = Archetype.of(ReflectionComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final SpriteBatch spriteBatch = createRenderBatch(engine, render -> !render.renderOverLight);
        addReflectionsToSpriteBatch(engine, spriteBatch);
        engine.graphics().canvas().drawSpriteBatch(spriteBatch);
    }

    protected SpriteBatch createRenderBatch(final Engine engine, Predicate<RenderComponent> renderCondition) {
        final List<Entity> renderEntities = engine.environment().fetchAll(RENDERS);
        final SpriteBatch spriteBatch = new SpriteBatch();
        final Graphics graphics = engine.graphics();
        double zoom = graphics.camera().zoom();
        final ScreenBounds visibleBounds = graphics.canvas().bounds();
        for (final Entity entity : renderEntities) {
            final RenderComponent render = entity.get(RenderComponent.class);
            if (renderCondition.test(render)) {
                final double width = render.sprite.width() * render.options.scale();
                final double height = render.sprite.height() * render.options.scale();
                final var spriteBounds = Bounds.atPosition(entity.position(), width, height);

                final var entityScreenBounds = graphics.toCanvas(spriteBounds, render.parallaxX, render.parallaxY);
                if (visibleBounds.intersects(entityScreenBounds)) {
                    spriteBatch.add(render.sprite, entityScreenBounds.offset(), render.options.scale(render.options.scale() * zoom), render.drawOrder);
                }
            }
        }
        return spriteBatch;
    }

    private void addReflectionsToSpriteBatch(final Engine engine, final SpriteBatch spriteBatch) {
        final List<Entity> renderEntities = engine.environment().fetchAll(RENDERS);
        final var visibleArea = Pixelperfect.bounds(engine.graphics().visibleArea());
        final var zoom = engine.graphics().camera().zoom();
        for (final Entity mirror : engine.environment().fetchAll(MIRRORS)) {
            final var visibleAreaOfMirror = mirror.bounds().intersection(visibleArea);
            visibleAreaOfMirror.ifPresent(reflection -> {
                var reflectionOnScreen = engine.graphics().toCanvas(reflection);
                final Size size = Size.of(
                        ceil(reflectionOnScreen.width() / zoom),
                        ceil(reflectionOnScreen.height() / zoom));
                if (size.isValid()) {
                    final var reflectionConfig = mirror.get(ReflectionComponent.class);
                    final long seed = engine.loop().lastUpdate().milliseconds();
                    final UnaryOperator<Bounds> entityMotion = reflectionConfig.useWaveEffect
                            ? bounds -> bounds.moveBy(
                            Math.sin((seed + bounds.position().y() * 100) / 320) * 2,
                            Math.sin((seed + bounds.position().x() * 50) / 500) * 2)
                            : null;
                    final var reflectedBounds = reflection.moveBy(Vector.y(-reflection.height()));
                    final var reflectedAreaOnSreen = engine.graphics().toCanvas(reflectedBounds);
                    final var reflectionImage = new ReflectionImage(engine.graphics(), reflectionConfig.drawOrder, size, reflectedAreaOnSreen, entityMotion);
                    for (final var entity : renderEntities) {
                        reflectionImage.addEntity(entity);
                    }

                    spriteBatch.add(reflectionImage.create(reflectionConfig.blur), engine.graphics().toCanvas(reflection.origin()), SpriteDrawOptions.scaled(zoom).opacity(reflectionConfig.opacityModifier), reflectionConfig.drawOrder);
                }
            });
        }
    }
}
