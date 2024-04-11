package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.graphics.Sprite;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

public class ParticleDesigner implements Serializable {

    private final List<Sprite> templates;

    public static ParticleDesigner useTemplate(final Sprite template) {
        return useTemplates(List.of(template));
    }

    public static ParticleDesigner useTemplate(final Supplier<Sprite> template) {
        return useTemplate(template.get());
    }

    public static ParticleDesigner useTemplates(final List<Sprite> templates) {
        return new ParticleDesigner(templates);
    }

    private ParticleDesigner(final List<Sprite> templates) {
        this.templates = templates;
    }

    public Entity createEntity(final Vector position) {
        return new Entity();
    }
}
