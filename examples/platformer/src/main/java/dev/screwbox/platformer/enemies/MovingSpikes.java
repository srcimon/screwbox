package dev.screwbox.platformer.enemies;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Blueprint;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.DeathEventComponent.DeathType;
import dev.screwbox.platformer.components.KillZoneComponent;
import dev.screwbox.platformer.components.MovingPlatformComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiled.GameObject;
import dev.screwbox.tiled.Tileset;

public class MovingSpikes implements Blueprint<GameObject> {

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("tilesets/enemies/moving-spikes.json");

    @Override
    public Entity assembleFrom(GameObject object) {
        double speed = object.properties().tryGetDouble("speed").orElse(30.0);
        return new Entity("Moving Spikes").add(
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new KillZoneComponent(DeathType.SPIKES),
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new TransformComponent(object.bounds()),
                new MovingPlatformComponent(object.properties().getInt("waypoint"), speed));
    }

}
