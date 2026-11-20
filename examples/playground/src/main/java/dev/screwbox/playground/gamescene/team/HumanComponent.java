package dev.screwbox.playground.gamescene.team;

import dev.screwbox.core.environment.Component;

public class HumanComponent implements Component {
    public int viewportId;

    public HumanComponent(int viewportId) {
        this.viewportId = viewportId;
    }
}
