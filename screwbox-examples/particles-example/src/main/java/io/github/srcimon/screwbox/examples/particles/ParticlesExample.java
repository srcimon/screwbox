package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;

public class ParticlesExample {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles Example");
        screwBox.start();
    }
}
