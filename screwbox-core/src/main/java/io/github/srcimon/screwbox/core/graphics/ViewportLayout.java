package io.github.srcimon.screwbox.core.graphics;

public interface ViewportLayout {

    ScreenBounds calculateBounds(int viewportId, int viewportCount, ScreenBounds bounds);

}
