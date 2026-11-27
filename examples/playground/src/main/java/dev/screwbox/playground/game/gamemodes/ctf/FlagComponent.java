package dev.screwbox.playground.game.gamemodes.ctf;

import dev.screwbox.core.environment.Component;

public class FlagComponent implements Component {

    public int homeZoneId;
    public Integer carriedById;
    public int teamId;
    public boolean isStolen;
}
