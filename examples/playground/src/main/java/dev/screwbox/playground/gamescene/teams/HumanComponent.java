package dev.screwbox.playground.gamescene.teams;

import dev.screwbox.core.environment.Component;

public class HumanComponent implements Component {
    public int viewportId;

    public HumanComponent(int viewportId) {
        this.viewportId = viewportId;
    }
}
