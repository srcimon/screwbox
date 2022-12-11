package de.suzufa.screwbox.examples.platformer.enemies;

import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.SignalComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.TriggerAreaComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.KillZoneComponent;
import de.suzufa.screwbox.examples.platformer.components.MovingPlatformComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerMarkerComponent;
import de.suzufa.screwbox.examples.platformer.components.DeathEventComponent.DeathType;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Tileset;

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
