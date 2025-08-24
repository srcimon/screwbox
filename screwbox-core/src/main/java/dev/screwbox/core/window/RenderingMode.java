package dev.screwbox.core.window;

import dev.screwbox.core.utils.internal.MacOsSupport;

/**
 * Rendering mode that is used for all graphic operations. May highly impact your game performance.
 *
 * @since 3.8.0
 */
public enum RenderingMode {

    /**
     * Unspecified {@link RenderingMode}, recommended on MacOs systems. MacOs should automatically use Metal rendering.
     * Metal rendering cannot be enforced, so maybe your JDK / System switches automatically to software rendering.
     * If you are experiencing performance issues because of that, try to switch to {@link RenderingMode#OPEN_GL} or
     * update the Java Runtime / JDK that you are using.
     */
    UNSPECIFIED(() -> {
    }),

    /**
     * Use Direct3D rendering. Only available on Windows.
     */
    DIRECT_3D(() -> {
        if (MacOsSupport.isMacOs()) {
            throw new IllegalArgumentException("Direct3D rendering is not supported on MacOs");
        }
        System.setProperty("sun.java2d.d3d", "true");
    }),

    /**
     * Use OpenGl rendering. May help with performance, if your machine otherwise switches to software rendering.
     * Not recommended on MacOs systems.
     */
    OPEN_GL(() -> System.setProperty("sun.java2d.opengl", "true"));

    private final Runnable applyMode;

    RenderingMode(final Runnable applyMode) {
        this.applyMode = applyMode;
    }

    /**
     * Automatically detects best rendering mode for your machine.
     */
    public static RenderingMode autodetect() {
        return MacOsSupport.isMacOs() ? UNSPECIFIED : OPEN_GL;
    }

    public void apply() {
        applyMode.run();
    }
}
