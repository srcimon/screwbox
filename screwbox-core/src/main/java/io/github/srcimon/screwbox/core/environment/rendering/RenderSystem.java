package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.ScreenBounds;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.Viewport;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.ReflectionImage;
import io.github.srcimon.screwbox.core.graphics.internal.filter.WaterDistortionImageFilter;
import io.github.srcimon.screwbox.core.utils.Pixelperfect;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static io.github.srcimon.screwbox.core.environment.Order.SystemOrder.PRESENTATION_WORLD;
import static io.github.srcimon.screwbox.core.graphics.internal.ImageUtil.applyFilter;
import static java.lang.Math.ceil;

/**
 * Renders {@link Entity entities} having a {@link RenderComponent} and also adds reflections for {@link Entity entities}
 * having a {@link ReflectionComponent}.
 */
@Order(PRESENTATION_WORLD)
public class RenderSystem implements EntitySystem {

    private static final Archetype RENDERS = Archetype.ofSpacial(RenderComponent.class);
    private static final Archetype MIRRORS = Archetype.ofSpacial(ReflectionComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> entities = fetchRenderEntities(engine);
        for (final var viewport : engine.graphics().viewports()) {
            final SpriteBatch spriteBatch = renderEntitiesOnViewport(viewport, entities, render -> !render.renderOverLight);
            addReflectionsToBatch(engine, viewport, spriteBatch);
            viewport.canvas().drawSpriteBatch(spriteBatch);
        }
    }

    protected List<Entity> fetchRenderEntities(Engine engine) {
        return engine.environment().fetchAll(RENDERS);
    }

    protected SpriteBatch renderEntitiesOnViewport(final Viewport viewport, final List<Entity> entities, final Predicate<RenderComponent> renderCondition) {
        final SpriteBatch spriteBatch = new SpriteBatch();
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
                    spriteBatch.add(render.sprite, entityScreenBounds.offset(), render.options.scale(render.options.scale() * zoom), render.drawOrder);
                }
            }
        }
        return spriteBatch;
    }

    private void addReflectionsToBatch(final Engine engine, final Viewport viewport, final SpriteBatch spriteBatch) {
        final List<Entity> renderEntities = fetchRenderEntities(engine);
        final var visibleArea = Pixelperfect.bounds(viewport.visibleArea());
        final var zoom = viewport.camera().zoom();
        for (final Entity mirror : engine.environment().fetchAll(MIRRORS)) {
            final var visibleAreaOfMirror = mirror.bounds().intersection(visibleArea);
            visibleAreaOfMirror.ifPresent(reflection -> {
                var reflectionOnScreen = viewport.toCanvas(reflection);
                final Size size = Size.of(
                        ceil(reflectionOnScreen.width() / zoom),
                        ceil(reflectionOnScreen.height() / zoom));
                if (size.isValid()) {
                    final var reflectionConfig = mirror.get(ReflectionComponent.class);
                    final double seed = engine.loop().time().milliseconds() * engine.loop().speed();
                    final UnaryOperator<Bounds> entityMotion = reflectionConfig.applyWaveDistortionProjection
                            ? bounds -> bounds.moveBy(
                            Math.sin((seed + bounds.position().y() * reflectionConfig.frequencyX * 40) * reflectionConfig.speed) * reflectionConfig.amplitude,
                            Math.sin((seed + bounds.position().x() * reflectionConfig.frequencyX * 20) * reflectionConfig.speed / 2.0) * reflectionConfig.amplitude)
                            : null;
                    final var reflectedBounds = reflection.moveBy(Vector.y(-reflection.height()));
                    final var reflectedAreaOnScreen = viewport.toCanvas(reflectedBounds);
                    final var reflectionImage = new ReflectionImage(viewport, reflectionConfig.drawOrder, size, reflectedAreaOnScreen, entityMotion);
                    for (final var entity : renderEntities) {
                        reflectionImage.addEntity(entity);
                    }
                    final var image = reflectionImage.create();
                    Sprite renderSprite = reflectionConfig.applyWaveDistortionPostFilter
                            ? Sprite.fromImage(applyFilter(image, new WaterDistortionImageFilter(image, createFilterConfig(reflection.origin(), reflectionConfig, seed))))
                            : Sprite.fromImage(image);
                    spriteBatch.add(renderSprite, viewport.toCanvas(reflection.origin()), SpriteDrawOptions.scaled(zoom).opacity(reflectionConfig.opacityModifier), reflectionConfig.drawOrder);
                }
            });
        }
    }

    private WaterDistortionImageFilter.WaterDistortionConfig createFilterConfig(final Vector origin, final ReflectionComponent reflectionConfig, final double seed) {
        return new WaterDistortionImageFilter.WaterDistortionConfig(
                seed * reflectionConfig.speed,
                reflectionConfig.amplitude,
                reflectionConfig.frequencyX,
                reflectionConfig.frequencyY,
                Offset.at(origin.x(), origin.y()));
    }

}
