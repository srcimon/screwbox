package io.github.srcimon.screwbox.examples.platformer.enemies;

import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.entities.Archetype;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.core.entities.components.SignalComponent;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.components.TriggerAreaComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent.DeathType;
import io.github.srcimon.screwbox.examples.platformer.components.KillZoneComponent;
import io.github.srcimon.screwbox.examples.platformer.components.MovingPlatformComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.tiled.Tileset;

public class MovingSpikes implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("tilesets/enemies/moving-spikes.json");

    @Override
    public Entity convert(GameObject object) {
        double speed = object.properties().getDouble("speed").orElse(30.0);
        return new Entity().add(
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new KillZoneComponent(DeathType.SPIKES),
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new TransformComponent(object.bounds()),
                new MovingPlatformComponent(object.properties().forceInt("waypoint"), speed));
    }

}
