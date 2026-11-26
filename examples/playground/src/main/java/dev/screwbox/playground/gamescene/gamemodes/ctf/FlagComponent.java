package dev.screwbox.playground.gamescene.gamemodes.ctf;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

public class FlagComponent implements Component {

    public int homeZoneId;
    public Integer carriedById;
    public Color team;
    public boolean isStolen;
}
