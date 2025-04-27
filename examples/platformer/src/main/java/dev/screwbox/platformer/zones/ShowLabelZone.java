package dev.screwbox.platformer.zones;

import dev.screwbox.core.Ease;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport.Converter;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.logic.TriggerAreaComponent;
import dev.screwbox.core.environment.tweening.TweenComponent;
import dev.screwbox.core.environment.tweening.TweenPositionComponent;
import dev.screwbox.platformer.components.LabelComponent;
import dev.screwbox.platformer.components.PlayerMarkerComponent;
import dev.screwbox.tiled.GameObject;

import static dev.screwbox.core.Duration.ofSeconds;

public class ShowLabelZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        String label = object.properties().getString("label");
        Integer size = object.properties().tryGetInt("size").orElse(16);
        return new Entity().add(
                new TweenComponent(ofSeconds(2), Ease.SINE_IN_OUT, true),
                new TweenPositionComponent(object.position().addY(-5), object.position().addY(5)),
                new LabelComponent(label, size),
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new TransformComponent(object.bounds()));
    }

}
