package de.suzufa.screwbox.playground.debo.enemies;

import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.SourceImport.Converter;
import de.suzufa.screwbox.core.entities.components.SignalComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.entities.components.TriggerAreaComponent;
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
                new SignalComponent(),
                new TriggerAreaComponent(Archetype.of(PlayerMarkerComponent.class, TransformComponent.class)),
                new KillZoneComponent(DeathType.SPIKES),
                new SpriteComponent(SPRITE, object.layer().order()),
                new TransformComponent(object.bounds()),
                new MovingPlattformComponent(object.properties().forceInt("waypoint"), speed));
    }

}
