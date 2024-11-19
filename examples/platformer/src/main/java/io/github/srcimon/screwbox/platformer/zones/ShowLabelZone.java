package io.github.srcimon.screwbox.platformer.zones;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.SignalComponent;
import io.github.srcimon.screwbox.core.environment.logic.TriggerAreaComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenPositionComponent;
import io.github.srcimon.screwbox.platformer.components.LabelComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerMarkerComponent;
import io.github.srcimon.screwbox.tiled.GameObject;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

public class ShowLabelZone implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        String label = object.properties().getString("label");
        Integer size = object.properties().tryGetInt("size").orElse(16);
        return new Entity().add(
                new TweenComponent(ofSeconds(2), Ease.SINE_IN_OUT, true),
                new TweenPositionComponent(object.position().addY(-5), object.position().addY(5)),
                new SignalComponent(),
                new LabelComponent(label, size),
                new TriggerAreaComponent(Archetype.ofSpacial(PlayerMarkerComponent.class)),
                new TransformComponent(object.bounds()));
    }

}
