package de.suzufa.screwbox.playground.debo.effects;

import static de.suzufa.screwbox.core.Duration.ofSeconds;
import static de.suzufa.screwbox.core.utils.ListUtil.randomFrom;

import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.SourceImport.Converter;
import de.suzufa.screwbox.core.entityengine.components.ScreenTransitionComponent;
import de.suzufa.screwbox.core.graphics.transitions.CircleTransition;
import de.suzufa.screwbox.core.graphics.transitions.FadingScreenTransition;
import de.suzufa.screwbox.core.graphics.transitions.HorizontalLinesTransition;
import de.suzufa.screwbox.core.graphics.transitions.MosaikTransition;
import de.suzufa.screwbox.core.graphics.transitions.SwipeTransition;
import de.suzufa.screwbox.tiled.GameObject;

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
