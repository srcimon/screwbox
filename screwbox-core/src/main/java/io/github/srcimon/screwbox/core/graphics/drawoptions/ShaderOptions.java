package io.github.srcimon.screwbox.core.graphics.drawoptions;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.Objects;

public record ShaderOptions(Shader shader, Time offset, Duration duration, Ease ease, int cacheSize) {

    public ShaderOptions {
        if(cacheSize > 1 && !shader.isAnimated()) {
            throw new IllegalArgumentException("non animated shaders should not have cache size above 1");
        }
        Validate.zeroOrPositive(cacheSize, "cache size must be positive");
        Validate.max(cacheSize, 1000,  "cache size cannot exceed 1000 element");
    }
    public static ShaderOptions shader(Shader shader) {
        return new ShaderOptions(shader, Time.unset(), Duration.oneSecond(), Ease.LINEAR_IN, shader.isAnimated() ? 20 : 1);
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

    public Sprite createPreview(final Frame source) {
        if (!shader.isAnimated()) {
            return Sprite.fromImage(shader.applyOn(source.image(), null));
        }
        final double stepSize = 1.0 / cacheSize;
        final Duration stepDuration = Duration.ofNanos(duration.nanos() / cacheSize);
        final var frames = new ArrayList<Frame>();
        for (double i = 0; i < 1.0; i += stepSize) {
            frames.add(new Frame(shader.applyOn(source.image(), Percent.of(i)), stepDuration));
        }
        return new Sprite(frames);
    }
}
