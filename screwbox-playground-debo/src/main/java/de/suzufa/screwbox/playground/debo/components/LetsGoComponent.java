package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entities.Component;

public class LetsGoComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double modifier = 0;
    public Percentage visibility = Percentage.max();
}
