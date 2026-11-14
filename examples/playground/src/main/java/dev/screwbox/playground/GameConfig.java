package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.graphics.Color;

import java.util.Set;

public record GameConfig(Set<Color> teams, int teamSize, Duration respawnTime, Color humanTeam) {
}
