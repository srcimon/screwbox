package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;

import java.util.List;

public record GameConfig(List<TeamConfig> teams, Duration respawnTime, String humanTeamName, String map) {

    public record TeamConfig(String name, Color color, int teamSize) {
    }
}
