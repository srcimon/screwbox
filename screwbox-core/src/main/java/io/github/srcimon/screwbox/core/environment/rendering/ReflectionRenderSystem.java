package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.drawoptions.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.ReflectionImage;
import io.github.srcimon.screwbox.core.utils.Pixelperfect;

import java.util.function.UnaryOperator;

import static java.lang.Math.ceil;

@Order(Order.SystemOrder.PRESENTATION_PREPARE)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype MIRRORS = Archetype.of(ReflectionComponent.class, TransformComponent.class);
    private static final Archetype RELECTED_ENTITIES = Archetype.of(TransformComponent.class, RenderComponent.class);
    private static final Archetype REFLECTION_RENDERERS = Archetype.of(ReflectionResultComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().removeAll(REFLECTION_RENDERERS);
        final var reflectableEntities = engine.environment().fetchAll(RELECTED_ENTITIES);
        for (final Entity mirror : engine.environment().fetchAll(MIRRORS)) {
            final var expansionToMitigrateDrawingIssues = Math.max(0, -engine.graphics().toCanvas(mirror.bounds()).offset().y());
            final var visibleArea = Pixelperfect.bounds(engine.graphics().world().visibleArea().expandTop(expansionToMitigrateDrawingIssues));
            final var visibleAreaOfMirror = mirror.bounds().intersection(visibleArea);
            visibleAreaOfMirror.ifPresent(reflection -> {
                var reflectionOnScreen = engine.graphics().toCanvas(reflection);
                final Size size = Size.of(
                        ceil(reflectionOnScreen.width() / engine.graphics().camera().zoom()),
                        ceil(reflectionOnScreen.height() / engine.graphics().camera().zoom()));
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
                    for (final var entity : reflectableEntities) {
                        reflectionImage.addEntity(entity);
                    }

                    engine.environment().addEntity("reflection",
                            new TransformComponent(reflection),
                            new RenderComponent(
                                    reflectionImage.create(reflectionConfig.blur),
                                    reflectionConfig.drawOrder,
                                    SpriteDrawOptions.originalSize().opacity(reflectionConfig.opacityModifier)),
                            new ReflectionResultComponent());
                }
            });
        }
    }

}