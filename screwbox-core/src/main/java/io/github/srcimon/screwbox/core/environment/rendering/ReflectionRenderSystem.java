package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.World;
import io.github.srcimon.screwbox.core.environment.*;

import java.util.List;

@Order(SystemOrder.PRESENTATION_EFFECTS)
public class ReflectionRenderSystem implements EntitySystem {

    private static final Archetype REFLECTING_AREAS = Archetype.of(
            ReflectionComponent.class, TransformComponent.class);

    private static final Archetype RELECTED_ENTITIES = Archetype.of(
            TransformComponent.class, RenderComponent.class);

    private static final class ReflectionArea {

        private final double opacityModifier;
        private final Bounds area;
        private final Bounds reflectedArea;
        final double waveSeed;
        private final boolean useWaveEffect;

        public ReflectionArea(final Bounds area, final ReflectionComponent options, final Time time) {
            this.area = area;
            waveSeed = time.milliseconds() / 500.0;
            useWaveEffect = options.useWaveEffect;
            opacityModifier = useWaveEffect
                    ? (Math.sin(waveSeed) * 0.25 + 0.75) * options.opacityModifier.value()
                    : options.opacityModifier.value();
            reflectedArea = area.moveBy(0, -area.height()).expandTop(useWaveEffect ? 2 : 0);
        }

        public SpriteBatch createRenderBatchFor(final List<Entity> reflectableEntities) {
            final SpriteBatch spriteBatch = new SpriteBatch();
            for (final var reflectableEntity : reflectableEntities) {
                final var reflectableBounds = reflectableEntity.get(TransformComponent.class).bounds;
                if (reflectableBounds.intersects(reflectedArea)) {
                    final RenderComponent render = reflectableEntity.get(RenderComponent.class);
                    final var spriteSize = render.sprite.size();
                    final var spriteOrigin = reflectableBounds.position().add(-spriteSize.width() / 2.0, -spriteSize.height() / 2.0);
                    
                    final var xDelta = useWaveEffect ? Math.sin(waveSeed + spriteOrigin.y() / 16) * 2 : 0;
                    final var yDelta = useWaveEffect ? Math.sin(waveSeed) * 2 : 0;
                    final var effectOrigin = Vector.of(
                            spriteOrigin.x() + xDelta,
                            2 * area.minY() - spriteOrigin.y() - spriteSize.height() + yDelta);
                    
                    spriteBatch.addEntry(
                            render.sprite,
                            effectOrigin,
                            render.scale,
                            render.opacity.multiply(opacityModifier),
                            render.rotation,
                            render.flip.invertVertical(),
                            render.drawOrder);
                }
            }
            return spriteBatch;
        }
    }

    @Override
    public void update(final Engine engine) {
        final World world = engine.graphics().world();
        final Bounds visibleArea = world.visibleArea();
        final List<Entity> reflectableEntities = engine.environment().fetchAll(RELECTED_ENTITIES);
        for (final Entity reflectionEntity : engine.environment().fetchAll(REFLECTING_AREAS)) {
            final var reflectionOnScreen = reflectionEntity.get(TransformComponent.class).bounds
                    .intersection(visibleArea);
            if (reflectionOnScreen.isPresent()) {
                final var area = new ReflectionArea(reflectionOnScreen.get(),
                        reflectionEntity.get(ReflectionComponent.class), engine.loop().lastUpdate());
                final SpriteBatch batch = area.createRenderBatchFor(reflectableEntities);
                world.drawSpriteBatch(batch, reflectionOnScreen.get());
            }
        }
    }

}
