package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Shader;

public record ShaderOptions(Shader shader, Time offset, Duration duration, Ease ease) {
}
