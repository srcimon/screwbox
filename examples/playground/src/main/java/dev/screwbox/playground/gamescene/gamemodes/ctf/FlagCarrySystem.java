package dev.screwbox.playground.gamescene.gamemodes.ctf;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.importing.AdvancedBlueprint;
import org.w3c.dom.Entity;

public class FlagCarrySystem implements EntitySystem {

    private static final Archetype FLAGS = Archetype.ofSpacial(FlagComponent.class);

    @Override
    public void update(Engine engine) {
//TODO implement
    }
}
