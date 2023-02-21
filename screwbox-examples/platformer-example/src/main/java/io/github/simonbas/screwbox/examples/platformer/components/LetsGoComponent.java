package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Component;

public class LetsGoComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double modifier = 0;
    public Percent visibility = Percent.max();
}
