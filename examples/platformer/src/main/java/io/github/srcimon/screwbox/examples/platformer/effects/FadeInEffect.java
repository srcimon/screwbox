package io.github.srcimon.screwbox.examples.platformer.effects;

import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.SourceImport.Converter;
import io.github.srcimon.screwbox.core.environment.rendering.ScreenTransitionComponent;
import io.github.srcimon.screwbox.tiled.GameObject;
import io.github.srcimon.screwbox.core.graphics.transitions.*;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.utils.ListUtil.randomFrom;

public class FadeInEffect implements Converter<GameObject> {

    @Override
    public Entity convert(GameObject object) {
        int duration = object.properties().getInt("duration-secs");

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
