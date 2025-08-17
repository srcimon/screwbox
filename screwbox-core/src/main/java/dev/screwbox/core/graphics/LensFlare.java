package dev.screwbox.core.graphics;

import dev.screwbox.core.Percent;

public record LensFlare(int numberOfFlares, Percent opacity) {

    public static final LensFlare DEFAULT = new LensFlare(4, Percent.of(0.125));
}
