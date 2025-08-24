package dev.screwbox.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RenderingApiTest {

    @BeforeEach
    void setUp() {
        System.clearProperty("sun.java2d.d3d");
        System.clearProperty("sun.java2d.opengl");
        System.clearProperty("os.name");
    }

    @Test
    void autodetect_onMacOs_isUnspecified() {
        System.setProperty("os.name", "Mac OS X");

        var renderingApi = RenderingApi.autodetect();

        assertThat(renderingApi).isEqualTo(RenderingApi.UNSPECIFIED);
    }

    @Test
    void autodetect_onWindows_isOpenGl() {
        System.setProperty("os.name", "Windows");

        var renderingApi = RenderingApi.autodetect();

        assertThat(renderingApi).isEqualTo(RenderingApi.OPEN_GL);
    }

    @Test
    void configure_direct3d_removesSystemPropertiesAndSetsDirect3d() {
        System.setProperty("sun.java2d.d3d", "some value");
        System.setProperty("sun.java2d.opengl", "some value");

        RenderingApi.DIRECT_3D.configure();

        assertThat(System.getProperty("sun.java2d.d3d")).isEqualTo("true");
        assertThat(System.getProperty("sun.java2d.opengl")).isNull();
    }

    @Test
    void configure_openGl_removesSystemPropertiesAndSetsOpenGl() {
        System.setProperty("sun.java2d.d3d", "some value");
        System.setProperty("sun.java2d.opengl", "some value");

        RenderingApi.OPEN_GL.configure();

        assertThat(System.getProperty("sun.java2d.opengl")).isEqualTo("true");
        assertThat(System.getProperty("sun.java2d.d3d")).isNull();
    }

    @Test
    void configure_unspecified_removesSystemProperties() {
        System.setProperty("sun.java2d.d3d", "some value");
        System.setProperty("sun.java2d.opengl", "some value");

        RenderingApi.UNSPECIFIED.configure();

        assertThat(System.getProperty("sun.java2d.opengl")).isNull();
        assertThat(System.getProperty("sun.java2d.d3d")).isNull();
    }

    @Test
    void configure_direct3dOnMacOs_throwsException() {
        System.setProperty("os.name", "Mac OS X");

        assertThatThrownBy(RenderingApi.DIRECT_3D::configure)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Direct3D rendering is not supported on MacOs");
    }

}
