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

public record ShaderSetup(Shader shader, Time offset, Duration duration, Ease ease) {


    public static ShaderSetup shader(Shader shader) {
        return new ShaderSetup(shader, Time.unset(), Duration.oneSecond(), Ease.LINEAR_IN);
    }

    public ShaderSetup offset(Time offset) {
        return new ShaderSetup(shader, offset, duration, ease);
    }

    public ShaderSetup duration(Duration duration) {
        return new ShaderSetup(shader, offset, duration, ease);
    }

    public ShaderSetup ease(Ease ease) {
        return new ShaderSetup(shader, offset, duration, ease);
    }

    //TODO FIXUP THIS SETUP / CACHE KEY IS NOT VALID!!!
    //TODO duplicated to actual code? or isnt it?
    public Sprite createPreview(final Frame source, int maxFrames) {
        if (!shader.isAnimated()) {
            return Sprite.fromImage(shader.apply(source.image(), null));
        }
        final double stepSize = 1.0 / maxFrames;
        final Duration stepDuration = Duration.ofNanos(duration.nanos() / maxFrames);
        final var frames = new ArrayList<Frame>();
        for (double i = 0; i < 1.0; i += stepSize) {
            frames.add(new Frame(shader.apply(source.image(), ease.applyOn(Percent.of(i))), stepDuration));
        }
        return new Sprite(frames);
    }
}
