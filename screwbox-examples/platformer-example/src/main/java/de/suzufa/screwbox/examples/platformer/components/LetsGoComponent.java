package de.suzufa.screwbox.examples.platformer.components;

import de.suzufa.screwbox.core.Percent;
import de.suzufa.screwbox.core.entities.Component;

public class LetsGoComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double modifier = 0;
    public Percent visibility = Percent.max();
}
