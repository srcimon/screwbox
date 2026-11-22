package dev.screwbox.playground.gamescene.gamemodes.ctf;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class FlagComponent implements Component {

    public Vector homePosition;
    public boolean isAttached = true;
    public Color team;

}
