package dev.screwbox.core.graphics;

import java.util.ArrayList;
import java.util.List;

//TODO document and test
public class LensFlare {

    public record Orb(double distance, double size, double opacity) {

    }

    private List<Orb> orbs = new ArrayList<>();

    public List<Orb> orbs() {
        return orbs;
    }

    public LensFlare addOrb(final double distance, final double size, final double opacity) {
        orbs.add(new Orb(distance, size, opacity));//TODO return new instance
        return this;
    }
}
