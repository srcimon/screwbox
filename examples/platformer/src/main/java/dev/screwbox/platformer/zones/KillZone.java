package dev.screwbox.platformer.zones;

import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.light.AerialLightComponent;
import dev.screwbox.core.environment.light.GlowComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.LensFlare;
import dev.screwbox.platformer.components.DeathEventComponent.DeathType;
import dev.screwbox.platformer.components.KillZoneComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiled.GameObject;

public class KillZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        var deathType = object.properties().tryGetEnum("death-type", DeathType.class).orElse(DeathType.SPIKES);

        Entity add = new Entity().add(
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new KillZoneComponent(deathType),
                new TransformComponent(object.bounds()));
        //TODO remove pseudocode below
        if(deathType.equals(DeathType.LAVA)) {
            add.add(new GlowComponent(40, Color.YELLOW.opacity(0.3)), glow -> {
                glow.isRectangular = true;
                glow.lensFlare = LensFlare.noRays().orb(0.4, 0.2, 0.2);
            });
            add.add(new AerialLightComponent(Color.BLACK.opacity(0.7)));
        }
        return add;
    }

}
