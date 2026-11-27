package dev.screwbox.playground.game.blueprints;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.importing.AdvancedBlueprint;
import dev.screwbox.core.environment.importing.IdPool;
import dev.screwbox.playground.game.gamemodes.ctf.CaptureZoneComponent;
import dev.screwbox.tiled.GameObject;

public class CaptureZone implements AdvancedBlueprint<GameObject> {

    @Override
    public Entity assembleFrom(GameObject source, IdPool idPool) {

        return new Entity(source.id())
            .bounds(Bounds.atPosition(source.position(), 32, 32))
            .add(new CaptureZoneComponent(), zone -> zone.teamId = source.properties().getInt("team-id"));
    }
}
