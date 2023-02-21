package io.github.simonbas.screwbox.core.graphics;

public enum AspectRatio {

    STANDARD(4.0 / 3.0),
    WIDESCREEN(16.0 / 9.0);

    private final double ratio;

    AspectRatio(final double ratio) {
        this.ratio = ratio;
    }

    public boolean matches(final Dimension dimension) {
        return ratio == 1.0 * dimension.width() / dimension.height();
    }
}
