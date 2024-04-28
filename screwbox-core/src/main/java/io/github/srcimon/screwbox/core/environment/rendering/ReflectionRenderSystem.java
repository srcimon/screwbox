package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.Size;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;
import io.github.srcimon.screwbox.core.graphics.internal.ReflectionImage;
import io.github.srcimon.screwbox.core.utils.Pixelperfect;

import java.util.function.UnaryOperator;

import static java.lang.Math.ceil;

@Order(SystemOrder.PRESENTATION_PREPARE)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype MIRRORS = Archetype.of(ReflectionComponent.class, TransformComponent.class);
    private static final Archetype RELECTED_ENTITIES = Archetype.of(TransformComponent.class, RenderComponent.class);
    private static final Archetype REFLECTION_RENDERERS = Archetype.of(ReflectionResultComponent.class);

    @Override
    public void update(final Engine engine) {
        engine.environment().removeAll(REFLECTION_RENDERERS);
        final var reflectableEntities = engine.environment().fetchAll(RELECTED_ENTITIES);
        for (final Entity mirror : engine.environment().fetchAll(MIRRORS)) {
            final var visibleArea = Pixelperfect.bounds(engine.graphics().world().visibleArea());
            final var visibleAreaOfMirror = mirror.bounds().intersection(visibleArea);
            visibleAreaOfMirror.ifPresent(reflection -> {
                var reflectionOnScreen = engine.graphics().toScreen(reflection);
                final Size size = Size.of(
                        ceil(reflectionOnScreen.size().width() / engine.graphics().camera().zoom()),
                        ceil(reflectionOnScreen.size().height() / engine.graphics().camera().zoom()));
                if (size.isValid()) {
                    final var reflectionConfig = mirror.get(ReflectionComponent.class);
                    final UnaryOperator<Bounds> entityMotion = createMotion(reflectionConfig.useWaveEffect, engine.loop().lastUpdate());
                    final var reflectedBounds = reflection.moveBy(Vector.y(-reflection.height()));
                    final var reflectedAreaOnSreen = engine.graphics().toScreen(reflectedBounds);
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

    private UnaryOperator<Bounds> createMotion(final boolean useWaveEffect, final Time time) {
        if (!useWaveEffect) {
            return null;
        }
        return bounds -> bounds.moveBy(Math.sin((time.milliseconds() + bounds.position().y() * 100) / 320) * 2, 0);
    }
}