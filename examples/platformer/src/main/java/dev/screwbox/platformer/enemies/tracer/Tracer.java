package dev.screwbox.platformer.enemies.tracer;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.importing.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.logic.StateComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.platformer.components.DeathEventComponent;
import dev.screwbox.platformer.components.DetectLineOfSightToPlayerComponent;
import dev.screwbox.platformer.components.KillZoneComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiled.GameObject;

public class Tracer implements Blueprint<GameObject> {

    @Override
    public Entity assembleFrom(GameObject object) {
        return new Entity(object.id(), "Tracer").add(
                new TransformComponent(Bounds.atPosition(object.position(), 16, 16)),
                new RenderComponent(object.layer().order()),
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new DetectLineOfSightToPlayerComponent(140),
                new KillZoneComponent(DeathEventComponent.DeathType.SPIKES),
                new StateComponent(new TracerInactiveState()));
    }

}
