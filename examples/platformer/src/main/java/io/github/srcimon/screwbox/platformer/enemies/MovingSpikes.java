package io.github.srcimon.screwbox.platformer.enemies;

import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent.DeathType;
import io.github.srcimon.screwbox.platformer.components.KillZoneComponent;
import io.github.srcimon.screwbox.platformer.components.MovingPlatformComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiles.GameObject;
import dev.screwbox.tiles.Tileset;

public class MovingSpikes implements Converter<GameObject> {

    private static final Asset<Sprite> SPRITE = Tileset.spriteAssetFromJson("tilesets/enemies/moving-spikes.json");

    @Override
    public Entity convert(GameObject object) {
        double speed = object.properties().tryGetDouble("speed").orElse(30.0);
        return new Entity("Moving Spikes").add(
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new KillZoneComponent(DeathType.SPIKES),
                new RenderComponent(SPRITE.get(), object.layer().order()),
                new TransformComponent(object.bounds()),
                new MovingPlatformComponent(object.properties().getInt("waypoint"), speed));
    }

}
