package dev.screwbox.playground;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.Validate;
import dev.screwbox.playground.gamescene.teams.TeamInfoComponent;

import java.util.ArrayList;
import java.util.List;

public class TeamInfos {

    private final List<Entity> teamInfos = new ArrayList<>();

    public void add(final Entity teamInfoEntity) {
        teamInfos.add(teamInfoEntity);
    }

    public int teamId(int index) {
        validateIndex(index);
        return teamInfos.get(index).forceId();
    }

    public Color teamColor(int index) {
        validateIndex(index);
        return teamInfos.get(index).get(TeamInfoComponent.class).color;
    }

    private void validateIndex(int index) {
        Validate.range(index, 0, teamInfos.size() - 1, "no team for index %s registered".formatted(index));
    }
}
