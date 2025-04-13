package dev.screwbox.platformer.components;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;

public class LetsGoComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double modifier = 0;
    public Percent visibility = Percent.max();
}
