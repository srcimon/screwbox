package dev.screwbox.core;

import dev.screwbox.core.utils.Validate;
import dev.screwbox.core.utils.internal.MacOsSupport;

/**
 * Rendering api that is used for all graphic operations. May highly impact your game performance and stability.
 *
 * @since 3.8.0
 */
public enum RenderingApi {

    /**
     * Unspecified {@link RenderingApi}, recommended on MacOs systems. MacOs should automatically default to Metal
     * rendering. Metal rendering cannot be enforced, so maybe your JDK / System defaults to software rendering.
     * If you are experiencing performance issues, this might be caused by defaulting to software rendering. In that
     * case, try to switch to {@link RenderingApi#OPEN_GL} or update the Java Runtime / JDK that you are using.
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
     * Automatically detects best rendering api for your machine.
     */
    public static RenderingApi autodetect() {
        return MacOsSupport.isMacOs() ? UNSPECIFIED : OPEN_GL;
    }

    void configure() {
        Validate.isNull(() -> System.getProperty("sun.java2d.d3d"), "please use RenderingApi instead of using sun.java2d.d3d system property");
        Validate.isNull(() -> System.getProperty("sun.java2d.opengl"), "please use RenderingApi instead of using sun.java2d.opengl system property");

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
