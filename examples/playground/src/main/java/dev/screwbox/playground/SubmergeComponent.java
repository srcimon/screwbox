package dev.screwbox.playground;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;

public class SubmergeComponent implements Component {

    public Percent normal = Percent.half();
    public Percent submerged = Percent.max();
}
