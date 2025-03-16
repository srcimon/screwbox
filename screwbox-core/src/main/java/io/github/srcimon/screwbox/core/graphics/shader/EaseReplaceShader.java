package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Ease;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;

import java.awt.*;
import java.io.Serial;

/**
 * Replaces the {@link Ease} of the underlying shader by applying {@link Ease} on the provided progress.
 *
 * @since 2.17.0
 */
public class EaseReplaceShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Ease ease;
    private final Shader subShader;

    public EaseReplaceShader(final Ease ease, final Shader subShader) {
        super("change-ease-shader-%s-%s".formatted(ease, subShader.cacheKey()));
        this.ease = ease;
        this.subShader = subShader;
    }

    @Override
    public Image apply(Image source, Percent progress) {
        return subShader.apply(source, ease.applyOn(progress));
    }
}
