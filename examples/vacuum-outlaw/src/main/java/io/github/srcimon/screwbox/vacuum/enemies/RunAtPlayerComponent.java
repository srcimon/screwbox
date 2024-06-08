package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Sheduler;

public class RunAtPlayerComponent implements Component {

    public Sheduler refreshPathSheduler = Sheduler.everySecond();
}
