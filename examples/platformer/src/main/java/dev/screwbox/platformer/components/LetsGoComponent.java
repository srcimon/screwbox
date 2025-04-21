package dev.screwbox.platformer.components;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class LetsGoComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double modifier = 0;
    public Percent visibility = Percent.max();
}
