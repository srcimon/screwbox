package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.light.ConeGlowComponent;
import dev.screwbox.core.environment.light.ConeLightComponent;
import dev.screwbox.playground.elastics.ElasticLinkComponent;

public class LinkConeLightSystem implements EntitySystem {

    private static final Archetype LIGHTS = Archetype.of(ConeLightComponent.class, ElasticLinkComponent.class);
    private static final Archetype GLOWS = Archetype.of(ConeGlowComponent.class, ElasticLinkComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var light : engine.environment().fetchAll(LIGHTS)) {
            light.get(ConeLightComponent.class).direction = light.get(ElasticLinkComponent.class).angle.addDegrees(180);
        }
        for (final var glow : engine.environment().fetchAll(GLOWS)) {
            glow.get(ConeGlowComponent.class).direction = glow.get(ElasticLinkComponent.class).angle.addDegrees(180);
        }
    }
}
