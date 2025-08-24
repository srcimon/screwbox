package dev.screwbox.core;

import dev.screwbox.core.utils.internal.MacOsSupport;

/**
 * Rendering mode that is used for all graphic operations. May highly impact your game performance.
 *
 * @since 3.8.0
 */
public enum RenderingMode {

    /**
     * Unspecified {@link RenderingMode}, recommended on MacOs systems. MacOs should automatically default to Metal
     * rendering. Metal rendering cannot be enforced, so maybe your JDK / System defaults to software rendering.
     * If you are experiencing performance issues, this might be caused by defaulting to software rendering. In that
     * case, try to switch to {@link RenderingMode#OPEN_GL} or update the Java Runtime / JDK that you are using.
     */
    UNSPECIFIED,

    /**
     * Use Direct3D rendering. Only available on Windows machines.
     */
    DIRECT_3D,

    /**
     * Use OpenGl rendering. May help with performance, if your machine otherwise switches to software rendering.
     * Not recommended on MacOs systems.
     */
    OPEN_GL;

    /**
     * Automatically detects best rendering mode for your machine.
     */
    public static RenderingMode autodetect() {
        return MacOsSupport.isMacOs() ? UNSPECIFIED : OPEN_GL;
    }

    void applyMode() {
        if (DIRECT_3D.equals(this)) {
            if (MacOsSupport.isMacOs()) {
                throw new IllegalArgumentException("Direct3D rendering is not supported on MacOs");
            }
            System.setProperty("sun.java2d.d3d", "true");
        } else if (OPEN_GL.equals(this)) {
            System.setProperty("sun.java2d.opengl", "true");
        }
    }
}
