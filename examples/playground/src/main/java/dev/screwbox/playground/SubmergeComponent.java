package dev.screwbox.playground;

import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;

public class SubmergeComponent implements Component {

    public Time lastContact = Time.now();
}
