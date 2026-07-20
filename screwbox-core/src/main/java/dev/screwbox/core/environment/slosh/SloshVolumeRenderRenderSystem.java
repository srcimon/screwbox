package dev.screwbox.core.environment.slosh;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;

import static dev.screwbox.core.graphics.options.PolygonDrawOptions.Smoothing.HORIZONTAL;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Will render all slosh volumes also containing {@link SloshVolumeRenderComponent}.
 *
 * @since 2.19.0
 */
@ExecutionOrder(Order.PRESENTATION_EFFECTS)
public class SloshVolumeRenderRenderSystem implements EntitySystem {

    private static final Archetype VOLUMES = Archetype.ofSpacial(SloshVolumeComponent.class, SloshVolumeRenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(VOLUMES)) {
            final var volume = entity.get(SloshVolumeComponent.class);

            final var renderConfig = entity.get(SloshVolumeRenderComponent.class);

            final var options = isNull(renderConfig.secondaryColor)
                ? PolygonDrawOptions.filled(renderConfig.color)
                : PolygonDrawOptions.verticalGradient(renderConfig.color, renderConfig.secondaryColor);

            engine.graphics().world().drawPolygon(volume.outline, options.smoothing(HORIZONTAL).drawOrder(renderConfig.drawOrder));

            if (nonNull(renderConfig.surfaceColor)) {
                engine.graphics().world().drawPolygon(volume.surface, PolygonDrawOptions.outline(renderConfig.surfaceColor)
                    .smoothing(HORIZONTAL)
                    .strokeWidth(renderConfig.surfaceStrokeWidth));
            }
        }
    }

}
