package io.github.srcimon.screwbox.platformer.components;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;

public class LetsGoComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double modifier = 0;
    public Percent visibility = Percent.max();
}
