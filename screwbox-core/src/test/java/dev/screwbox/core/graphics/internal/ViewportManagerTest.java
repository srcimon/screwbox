package dev.screwbox.core.graphics.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.SplitScreenOptions;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.renderer.RenderPipeline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static dev.screwbox.core.Vector.$;
import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class ViewportManagerTest {

    @Mock
    RenderPipeline renderPipeline;

    ViewportManager viewportManager;

    @BeforeEach
    void setUp() {
        DefaultCanvas canvas = new DefaultCanvas(renderPipeline.renderer(), new ScreenBounds(20, 20, 640, 480));
        DefaultCamera camera = new DefaultCamera(canvas);
        camera.setZoom(3);
        camera.setPosition($(20, 90));
        camera.setZoomRestriction(0.1, 10);
        DefaultViewport defaultViewport = new DefaultViewport(canvas, camera);
        viewportManager = new ViewportManager(defaultViewport, renderPipeline);
    }

    @Test
    void enableSplitScreen_oneScreen_setsSplitscreenModeEnabled() {
        viewportManager.enableSplitscreenMode(SplitScreenOptions.viewports(2));

        assertThat(viewportManager.isSplitscreenModeEnabled()).isTrue();
    }

    @Test
    void disableSplitScreen_splitScreenEnabled_setsSplitscreenModeEnabledFalse() {
        viewportManager.enableSplitscreenMode(SplitScreenOptions.viewports(2));

        viewportManager.disableSplitscreenMode();

        assertThat(viewportManager.isSplitscreenModeEnabled()).isFalse();
    }

    @Test
    void disableSplitScreen_splitscreenModeEnabled_appliesCameraChangesOfFirstViewportCameraToDefaultViewport() {
        viewportManager.enableSplitscreenMode(SplitScreenOptions.viewports(2));

        viewportManager.viewport(0).ifPresent(viewport -> viewport.camera()
                .setPosition($(100, 100)).setZoomRestriction(1, 4).setZoom(3.0));

        viewportManager.disableSplitscreenMode();

        assertThat(viewportManager.defaultViewport().camera().position()).isEqualTo($(100, 100));
        assertThat(viewportManager.defaultViewport().camera().zoom()).isEqualTo(3.0);
        assertThat(viewportManager.defaultViewport().camera().minZoom()).isEqualTo(1.0);
        assertThat(viewportManager.defaultViewport().camera().maxZoom()).isEqualTo(4.0);
    }

    @Test
    void calculateHoverViewport_noSplitscreen_isDefaultViewport() {
        Viewport viewport = viewportManager.calculateHoverViewport(Offset.origin());

        assertThat(viewport).isEqualTo(viewportManager.defaultViewport());
    }

    @Test
    void enableSplitscreenMode_alreadyEnabled_replacesSplitScreenMode() {
        viewportManager.enableSplitscreenMode(SplitScreenOptions.viewports(2));
        viewportManager.enableSplitscreenMode(SplitScreenOptions.viewports(4));

        assertThat(viewportManager.isSplitscreenModeEnabled()).isTrue();
        assertThat(viewportManager.viewports()).hasSize(4);
    }

    @Test
    void calculateHoverViewport_offsetAboveLeftSplitscreen_isFirst() {
        viewportManager.enableSplitscreenMode(SplitScreenOptions.viewports(2));

        var viewport = viewportManager.calculateHoverViewport(Offset.at(100, 20));

        assertThat(viewport).isEqualTo(viewportManager.viewport(0).orElseThrow());
    }

    @Test
    void calculateHoverViewport_offsetAboveRightplitscreen_isSecond() {
        viewportManager.enableSplitscreenMode(SplitScreenOptions.viewports(2));

        var viewport = viewportManager.calculateHoverViewport(Offset.at(600, 20));
        assertThat(viewport).isEqualTo(viewportManager.viewport(1).orElseThrow());
    }

    @Test
    void primaryViewport_splitScreenEnabled_isFirstSplitScreen() {
        viewportManager.enableSplitscreenMode(SplitScreenOptions.viewports(2));

        var viewport = viewportManager.primaryViewport();

        assertThat(viewport).isEqualTo(viewportManager.viewport(0).orElseThrow());
    }

    @Test
    void primaryViewport_noSplitScreen_isDefaultViewport() {
        var viewport = viewportManager.primaryViewport();

        assertThat(viewport).isEqualTo(viewportManager.defaultViewport());
    }
}