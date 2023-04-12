package io.github.srcimon.screwbox.examples.platformer.effects;

import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.SourceImport.Converter;
import io.github.srcimon.screwbox.core.entities.components.ScreenTransitionComponent;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.core.graphics.transitions.*;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.utils.ListUtil.randomFrom;

public class FadeInEffect implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        int duration = object.properties().forceInt("duration-secs");

        var randomFadeIn = randomFrom(
                new HorizontalLinesTransition(20),
                new SwipeTransition(),
                new FadingScreenTransition(),
                new CircleTransition(),
                new MosaikTransition(30, 20));

        return new Entity().add(
                new ScreenTransitionComponent(randomFadeIn, ofSeconds(duration)));
    }
}
