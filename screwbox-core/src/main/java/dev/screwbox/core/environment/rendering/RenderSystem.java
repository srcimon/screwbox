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
    private static final Archetype MIRRORS = Archetype.ofSpacial(ReflectionComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> entities = fetchRenderEntities(engine);
        for (final var viewport : engine.graphics().viewports()) {
            renderEntitiesOnViewport(viewport, entities, render -> !render.renderInForeground);
            addReflectionsToBatch(engine, viewport);
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

    private void addReflectionsToBatch(final Engine engine, final Viewport viewport) {
        final List<Entity> renderEntities = fetchRenderEntities(engine);
        final var visibleArea = Pixelperfect.bounds(viewport.visibleArea());
        final var zoom = viewport.camera().zoom();
        for (final Entity mirror : engine.environment().fetchAll(MIRRORS)) {
            final var visibleAreaOfMirror = mirror.bounds().intersection(visibleArea);
            visibleAreaOfMirror.ifPresent(intersection -> {
                // keep height to prevent graphic issue
                final var reflection = Bounds.atOrigin(intersection.minX(), mirror.bounds().minY(), intersection.width(), mirror.bounds().height());
                final var reflectionOnScreen = viewport.toCanvas(reflection);
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
                    final var overlayShader = engine.graphics().configuration().overlayShader();
                    for (final var entity : renderEntities) {
                        reflectionImage.addEntity(entity, overlayShader);
                    }
                    final Sprite reflectionSprite = createReflectionSprite(reflection, reflectionImage, reflectionConfig, seed);
                    viewport.canvas().drawSprite(reflectionSprite, viewport.toCanvas(reflection.origin()), SpriteDrawOptions.scaled(zoom).opacity(reflectionConfig.opacityModifier).ignoreOverlayShader().drawOrder(reflectionConfig.drawOrder));
                }
            });
        }
    }

    private Sprite createReflectionSprite(final Bounds reflection, final ReflectionImage reflectionImage, final ReflectionComponent reflectionConfig, double seed) {
        final var image = reflectionImage.create();
        return reflectionConfig.applyWaveDistortionPostFilter
                ? Sprite.fromImage(applyFilter(image, new DistortionImageFilter(image, createFilterConfig(reflection.origin(), reflectionConfig, seed))))
                : Sprite.fromImage(image);
    }

    private DistortionImageFilter.DistortionConfig createFilterConfig(final Vector origin, final ReflectionComponent reflectionConfig, final double seed) {
        return new DistortionImageFilter.DistortionConfig(
                seed * reflectionConfig.speed,
                reflectionConfig.amplitude,
                reflectionConfig.frequencyX,
                reflectionConfig.frequencyY,
                Offset.at(origin.x(), origin.y()));
    }

}
