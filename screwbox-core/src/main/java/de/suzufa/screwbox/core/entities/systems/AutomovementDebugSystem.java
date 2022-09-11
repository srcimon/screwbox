package de.suzufa.screwbox.core.entities.systems;

import static java.util.Objects.nonNull;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.AutomovementComponent;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;
import de.suzufa.screwbox.core.graphics.World;

public class AutomovementDebugSystem implements EntitySystem {

    private static final Font FONT = new Font("Arial", 11);
    private static final Archetype PATH_CONTAINING = Archetype.of(AutomovementComponent.class);

    @Override
    public void update(Engine engine) {
        World world = engine.graphics().world();
        for (Entity entity : engine.entities().fetchAll(PATH_CONTAINING)) {
            Path path = entity.get(AutomovementComponent.class).path;
            if (nonNull(path)) {
                for (var segment : path.segments()) {
                    world.drawLine(segment, Color.YELLOW);
                }
                int nr = 0;
                for (var node : path.nodes()) {
                    nr++;
                    world.drawTextCentered(node.addY(-5), "#" + nr, FONT, Color.WHITE)
                            .drawCircle(node, 3, Color.YELLOW);
                }
            }
        }

    }

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.PRESENTATION_OVERLAY;
    }
}
