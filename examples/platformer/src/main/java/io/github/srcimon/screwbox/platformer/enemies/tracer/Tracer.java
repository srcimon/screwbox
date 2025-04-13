package io.github.srcimon.screwbox.platformer.enemies.tracer;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.logic.StateComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import io.github.srcimon.screwbox.platformer.components.KillZoneComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiles.GameObject;

public class Tracer implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        return new Entity(object.id(), "Tracer").add(
                new TransformComponent(Bounds.atPosition(object.position(), 16, 16)),
                new RenderComponent(object.layer().order()),
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new DetectLineOfSightToPlayerComponent(140),
                new KillZoneComponent(DeathEventComponent.DeathType.SPIKES),
                new StateComponent(new TracerInactiveState()));
    }

}
