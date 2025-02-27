package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Shader;

public record ShaderOptions(Shader shader, Time offset, Duration duration, Ease ease, int cacheSize) {

    public static ShaderOptions shader(Shader shader) {
        return new ShaderOptions(shader, Time.unset(), Duration.oneSecond(), Ease.LINEAR_IN, 20);
    }

    public ShaderOptions offset(Time offset) {
        return new ShaderOptions(shader, offset, duration, ease, cacheSize);
    }

    public ShaderOptions duration(Duration duration) {
        return new ShaderOptions(shader, offset, duration, ease, cacheSize);
    }

    public ShaderOptions ease(Ease ease) {
        return new ShaderOptions(shader, offset, duration, ease, cacheSize);
    }

    public ShaderOptions cacheSize(int cacheSize) {
        return new ShaderOptions(shader, offset, duration, ease, cacheSize);
    }
}
