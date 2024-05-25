package io.github.srcimon.screwbox.vacuum;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;

public class VacuumOutlawsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Vacuum Outlaws");

        screwBox.start();
    }
}
