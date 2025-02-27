package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;

import java.awt.*;

//TODO document
public interface Shader {

    Image applyOn(Image image, Percent progress);
    String key( Percent progress);
}
