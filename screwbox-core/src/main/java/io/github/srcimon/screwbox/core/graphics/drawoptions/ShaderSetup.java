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

public record ShaderSetup(Shader shader, Time offset, Duration duration, Ease ease, int cacheSize) {

    public ShaderSetup {
        if(cacheSize > 1 && !shader.isAnimated()) {
            throw new IllegalArgumentException("non animated shaders should not have cache size above 1");
        }
        Validate.zeroOrPositive(cacheSize, "cache size must be positive");
        Validate.max(cacheSize, 1000,  "cache size cannot exceed 1000 element");
    }
    public static ShaderSetup shader(Shader shader) {
        return new ShaderSetup(shader, Time.unset(), Duration.oneSecond(), Ease.LINEAR_IN, shader.isAnimated() ? 20 : 1);
    }

    public ShaderSetup offset(Time offset) {
        return new ShaderSetup(shader, offset, duration, ease, cacheSize);
    }

    public ShaderSetup duration(Duration duration) {
        return new ShaderSetup(shader, offset, duration, ease, cacheSize);
    }

    public ShaderSetup ease(Ease ease) {
        return new ShaderSetup(shader, offset, duration, ease, cacheSize);
    }

    public ShaderSetup cacheSize(int cacheSize) {
        return new ShaderSetup(shader, offset, duration, ease, cacheSize);
    }

    //TODO duplicated to actual code? or isnt it?
    public Sprite createPreview(final Frame source) {
        if (!shader.isAnimated()) {
            return Sprite.fromImage(shader.applyOn(source.image(), null));
        }
        final double stepSize = 1.0 / cacheSize;
        final Duration stepDuration = Duration.ofNanos(duration.nanos() / cacheSize);
        final var frames = new ArrayList<Frame>();
        for (double i = 0; i < 1.0; i += stepSize) {
            frames.add(new Frame(shader.applyOn(source.image(), ease.applyOn(Percent.of(i))), stepDuration));
        }
        return new Sprite(frames);
    }
}
