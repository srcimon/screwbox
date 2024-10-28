package io.github.srcimon.screwbox.core.graphics;

public interface ViewportLayout {

    ScreenBounds calculateBounds(int index, int count, ScreenBounds bounds);

}
