package de.suzufa.screwbox.playground.debo.enemies;

import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.SignalComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.TriggerAreaComponent;
import de.suzufa.screwbox.core.entityengine.components.WaterReflectionComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.playground.debo.components.DeathEventComponent.DeathType;
import de.suzufa.screwbox.playground.debo.components.KillZoneComponent;
import de.suzufa.screwbox.playground.debo.components.MovingPlattformComponent;
import de.suzufa.screwbox.playground.debo.components.PlayerMarkerComponent;
import de.suzufa.screwbox.tiled.GameObject;
import de.suzufa.screwbox.tiled.Tileset;

public class MovingSpikes implements Converter<GameObject> {

    private static final Sprite SPRITE = Tileset.fromJson("tilesets/enemies/moving-spikes.json")
            .findById(0);

    @Override
    public Entity convert(GameObject object) {
        double speed = object.properties().getDouble("speed").orElse(30.0);
        return new Entity().add(
                new WaterReflectionComponent(),
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new KillZoneComponent(DeathType.SPIKES),
                new SpriteComponent(SPRITE, object.layer().order()),
                new TransformComponent(object.bounds()),
                new MovingPlattformComponent(object.properties().forceInt("waypoint"), speed));
    }

}
