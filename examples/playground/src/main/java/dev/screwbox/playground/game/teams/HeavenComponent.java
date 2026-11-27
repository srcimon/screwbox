package dev.screwbox.playground.game.teams;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.util.ArrayList;
import java.util.List;

public class HeavenComponent implements Component {

    public List<Entity> deadPlayers = new ArrayList<>();
}
