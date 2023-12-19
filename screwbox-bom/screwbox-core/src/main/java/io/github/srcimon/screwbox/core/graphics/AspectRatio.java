package io.github.srcimon.screwbox.core.graphics;

public enum AspectRatio {

    STANDARD(4.0 / 3.0),
    WIDESCREEN(16.0 / 9.0);

    private final double ratio;

    AspectRatio(final double ratio) {
        this.ratio = ratio;
    }

    public boolean matches(final Size size) {
        return ratio == 1.0 * size.width() / size.height();
    }
}
