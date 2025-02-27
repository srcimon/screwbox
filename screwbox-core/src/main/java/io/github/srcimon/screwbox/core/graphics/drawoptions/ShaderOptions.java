package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Shader;

public record ShaderOptions(Shader shader, Time offset, Duration duration, Ease ease) {

    public static ShaderOptions shader(Shader shader) {
        return new ShaderOptions(shader, Time.unset(), Duration.oneSecond(), Ease.LINEAR_IN);
    }

    public ShaderOptions offset(Time offset) {
        return new ShaderOptions(shader, offset, duration, ease);
    }

    public ShaderOptions duration(Duration duration) {
        return new ShaderOptions(shader, offset, duration, ease);
    }

    public ShaderOptions ease(Ease ease) {
        return new ShaderOptions(shader, offset, duration, ease);
    }
}
