package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;

import java.awt.*;
import java.io.Serializable;

//TODO add shaders to front page features (Render engine)
//TODO document
public interface Shader extends Serializable {

    Image applyOn(Image image, Percent progress);

    boolean isAnimated();

    default String cacheKey() {
        return getClass().getSimpleName();
    }

}
