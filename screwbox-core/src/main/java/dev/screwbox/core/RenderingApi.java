package dev.screwbox.core;

import dev.screwbox.core.utils.internal.MacOsSupport;

/**
 * Rendering API that is used for all graphic operations. May highly impact your game performance and stability.
 *
 * @since 3.8.0
 */
public enum RenderingApi {

    /**
     * Unspecified {@link RenderingApi}. Rendering will switch to system default rendering.
     * If you are experiencing performance issues, this might be caused by defaulting to software rendering. In that
     * case, try to switch to {@link RenderingApi#OPEN_GL} or update the Java Runtime / JDK that you are using.
     */
    UNSPECIFIED,

    /**
     * Use Metal rendering. Only available on MacOs machines.
     * <p>
     * Warning: Currently affected by <a href="https://bugs.java.com/bugdatabase/JDK-8371679">JDK-8371679</a>.
     */
    METAL,

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
        return MacOsSupport.isMacOs() ? METAL : OPEN_GL;
    }

    void configure() {
        System.clearProperty("sun.java2d.d3d");
        System.clearProperty("sun.java2d.opengl");
        System.clearProperty("sun.java2d.metal");

        if (DIRECT_3D.equals(this)) {
            if (MacOsSupport.isMacOs()) {
                throw new IllegalArgumentException("Direct3D rendering is not supported on MacOs");
            }
            System.setProperty("sun.java2d.d3d", "true");
        } else if (OPEN_GL.equals(this)) {
            System.setProperty("sun.java2d.opengl", "true");
        } else if (METAL.equals(this)) {
            if (!MacOsSupport.isMacOs()) {
                throw new IllegalArgumentException("Metal rendering is only supported on MacOs");
            }
            System.setProperty("sun.java2d.metal", "true");
        }
    }
}
