package io.github.simonbas.screwbox.examples.platformer.effects;

import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.SourceImport.Converter;
import io.github.simonbas.screwbox.core.entities.components.ScreenTransitionComponent;
import io.github.simonbas.screwbox.core.graphics.transitions.*;
import io.github.simonbas.screwbox.tiled.GameObject;

import static io.github.simonbas.screwbox.core.Duration.ofSeconds;
import static io.github.simonbas.screwbox.core.utils.ListUtil.randomFrom;

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
